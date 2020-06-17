package org.lamsfoundation.lams.tool.assessment.web.controller;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.assessment.AssessmentConstants;
import org.lamsfoundation.lams.tool.assessment.model.Assessment;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUser;
import org.lamsfoundation.lams.tool.assessment.service.IAssessmentService;
import org.lamsfoundation.lams.util.hibernate.HibernateSessionManager;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Controls Assessment time limits
 *
 * @author Marcin Cieslak
 */
@ServerEndpoint("/learningWebsocket")
public class LearningWebsocketServer {

    private static class TimeCache {
	private int relativeTimeLimit;
	private final Map<Long, LocalDateTime> timeLimitLaunchedDate = new ConcurrentHashMap<>();
    }

    /**
     * A singleton which updates Learners with their time limit
     */
    private static final Runnable sendWorker = new Runnable() {
	@Override
	public void run() {
	    try {
		// websocket communication bypasses standard HTTP filters, so Hibernate session needs to be initialised manually
		HibernateSessionManager.openSession();

		Iterator<Entry<Long, Set<Session>>> entryIterator = LearningWebsocketServer.websockets.entrySet()
			.iterator();
		// go through activities and update registered learners with time if needed
		while (entryIterator.hasNext()) {
		    Entry<Long, Set<Session>> entry = entryIterator.next();
		    Long toolContentId = entry.getKey();
		    // if all learners left the activity, remove the obsolete mapping
		    Set<Session> websockets = entry.getValue();
		    if (websockets.isEmpty()) {
			entryIterator.remove();
			timeCaches.remove(toolContentId);
			continue;
		    }

		    Assessment assessment = LearningWebsocketServer.getAssessmentService()
			    .getAssessmentByContentId(toolContentId);
		    long assessmentUid = assessment.getUid();
		    TimeCache timeCache = timeCaches.get(toolContentId);
		    if (timeCache == null) {
			timeCache = new TimeCache();
			timeCaches.put(toolContentId, timeCache);
		    }

		    boolean updateAllUsers = false;
		    int existingRelativeTimeLimit = assessment.getTimeLimit() * 60;
		    if (timeCache.relativeTimeLimit != existingRelativeTimeLimit) {
			timeCache.relativeTimeLimit = existingRelativeTimeLimit;
			updateAllUsers = true;
		    }

		    for (Session websocket : entry.getValue()) {
			String login = websocket.getUserPrincipal().getName();
			AssessmentUser user = LearningWebsocketServer.getAssessmentService()
				.getUserByLoginAndContent(login, toolContentId);
			long userId = user.getUserId();
			boolean updateUser = updateAllUsers;

			if (timeCache.relativeTimeLimit > 0) {
			    AssessmentResult result = LearningWebsocketServer.getAssessmentService()
				    .getLastAssessmentResult(assessmentUid, userId);
			    if (result == null) {
				continue;
			    }
			    LocalDateTime existingLaunchDate = result.getTimeLimitLaunchedDate();
			    if (existingLaunchDate == null) {
				existingLaunchDate = assessmentService.launchTimeLimit(assessmentUid, userId);
			    }

			    LocalDateTime launchedDate = timeCache.timeLimitLaunchedDate.get(userId);
			    if (launchedDate == null || !launchedDate.equals(existingLaunchDate)) {
				updateUser = true;
				timeCache.timeLimitLaunchedDate.put(userId, existingLaunchDate);
			    }
			}

			if (updateUser) {
			    Long secondsLeft = LearningWebsocketServer.getSecondsLeft(timeCache, userId);
			    LearningWebsocketServer.sendUpdate(websocket, secondsLeft);
			}
		    }
		}
	    } catch (IllegalStateException e) {
		// do nothing as server is probably shutting down and we could not obtain Hibernate session
	    } catch (Exception e) {
		// error caught, but carry on
		LearningWebsocketServer.log.error("Error in Assessment worker thread", e);
	    } finally {
		HibernateSessionManager.closeSession();
	    }
	}
    };

    // how ofter the thread runs in seconds
    private static final long CHECK_INTERVAL = 3;

    private static final Logger log = Logger.getLogger(LearningWebsocketServer.class);

    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private static final Map<Long, Set<Session>> websockets = new ConcurrentHashMap<>();
    private static final Map<Long, TimeCache> timeCaches = new ConcurrentHashMap<>();

    private static IAssessmentService assessmentService;

    static {
	// run the singleton thread
	executor.scheduleAtFixedRate(sendWorker, 0, CHECK_INTERVAL, TimeUnit.SECONDS);
    }

    /**
     * Registers the Learner for processing.
     */
    @OnOpen
    public void registerUser(Session websocket) throws IOException {
	Long toolContentID = Long
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_TOOL_CONTENT_ID).get(0));
	String login = websocket.getUserPrincipal().getName();
	AssessmentUser user = LearningWebsocketServer.getAssessmentService().getUserByLoginAndContent(login,
		toolContentID);
	if (user == null) {
	    throw new SecurityException("User \"" + login
		    + "\" is not a participant in Assessment activity with tool content ID " + toolContentID);
	}

	Set<Session> toolContentWebsockets = websockets.get(toolContentID);
	if (toolContentWebsockets == null) {
	    toolContentWebsockets = ConcurrentHashMap.newKeySet();
	    websockets.put(toolContentID, toolContentWebsockets);
	}
	toolContentWebsockets.add(websocket);

	TimeCache timeCache = timeCaches.get(toolContentID);
	if (timeCache != null) {
	    timeCache.timeLimitLaunchedDate.remove(user.getUserId());
	}

	if (log.isDebugEnabled()) {
	    log.debug("User " + login + " entered Assessment with toolContentId: " + toolContentID);
	}
    }

    /**
     * When user leaves the activity.
     */
    @OnClose
    public void unregisterUser(Session websocket, CloseReason reason) {
	Long toolContentID = Long
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_TOOL_CONTENT_ID).get(0));
	websockets.get(toolContentID).remove(websocket);

	if (log.isDebugEnabled()) {
	    // If there was something wrong with the connection, put it into logs.
	    log.debug("User " + websocket.getUserPrincipal().getName() + " left Assessment with Tool Content ID: "
		    + toolContentID
		    + (!(reason.getCloseCode().equals(CloseCodes.GOING_AWAY)
			    || reason.getCloseCode().equals(CloseCodes.NORMAL_CLOSURE))
				    ? ". Abnormal close. Code: " + reason.getCloseCode() + ". Reason: "
					    + reason.getReasonPhrase()
				    : ""));
	}
    }

    public static Long getSecondsLeft(long assessmentUid, long userUid) {
	TimeCache timeCache = timeCaches.get(assessmentUid);
	return timeCache == null ? null : LearningWebsocketServer.getSecondsLeft(timeCache, userUid);
    }

    private static Long getSecondsLeft(TimeCache timeCache, long userUid) {
	if (timeCache.relativeTimeLimit == 0) {
	    return null;
	}

	LocalDateTime now = LocalDateTime.now();
	LocalDateTime finish = timeCache.timeLimitLaunchedDate.get(userUid).plusSeconds(timeCache.relativeTimeLimit);
	long secondsLeft = Duration.between(now, finish).toSeconds();

	return Math.max(0, secondsLeft);
    }

    private static void sendUpdate(Session websocket, Long secondsLeft) throws IOException {
	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	if (secondsLeft == null) {
	    responseJSON.put("clearTimer", true);
	} else {
	    responseJSON.put("secondsLeft", secondsLeft);
	}
	String response = responseJSON.toString();

	if (websocket.isOpen()) {
	    websocket.getBasicRemote().sendText(response);
	}
    }

    private static IAssessmentService getAssessmentService() {
	if (assessmentService == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(SessionManager.getServletContext());
	    assessmentService = (IAssessmentService) wac.getBean(AssessmentConstants.ASSESSMENT_SERVICE);
	}
	return assessmentService;
    }
}