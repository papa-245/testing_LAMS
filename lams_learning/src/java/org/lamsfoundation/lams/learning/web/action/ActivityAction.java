//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.2/xslt/JavaClass.xsl

package org.lamsfoundation.lams.learning.web.action;

import javax.servlet.http.*;

import java.util.*;

import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.learning.service.DummyLearnerService;
import org.lamsfoundation.lams.learning.web.bean.SessionBean;
import org.lamsfoundation.lams.learning.web.form.ActivityForm;
import org.lamsfoundation.lams.learning.web.util.ActionMappings;
import org.lamsfoundation.lams.learning.web.util.ActionMappingsWithToolWait;

import org.lamsfoundation.lams.usermanagement.*;
import org.lamsfoundation.lams.web.action.Action;
import org.lamsfoundation.lams.lesson.*;
import org.lamsfoundation.lams.learningdesign.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/** 
 * MyEclipse Struts
 * Creation date: 01-12-2005
 * 
 */
public class ActivityAction extends Action {
	
	protected static final String ACTIVITY_REQUEST_ATTRIBUTE = "activity";
	protected static final String LEARNER_PROGRESS_REQUEST_ATTRIBUTE = "learnerprogress";
	
	//protected ActionMappings actionMappings = new ActionMappingsWithToolWait();
	//protected ActionMappings actionMappings;
	
	/**
	 * Gets the session bean from session.
	 * @return SessionBean for this request, null if no session.
	 */
	protected static SessionBean getSessionBean(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		}
		SessionBean sessionBean = (SessionBean)session.getAttribute(SessionBean.NAME);
		return sessionBean;
	}
	
	/**
	 * Sets the session bean for this session.
	 */
	protected static void setSessionBean(SessionBean sessionBean, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}
		session.setAttribute(SessionBean.NAME, sessionBean);
	}
	
	/**
	 * Get the learner service.
	 */
	protected ILearnerService getLearnerService(HttpServletRequest request) {
		DummyLearnerService learnerService = (DummyLearnerService)LearnerServiceProxy.getLearnerService(this.getServlet().getServletContext());
		learnerService.setRequest(request);
		//learnerService.setActionMappings(actionMappings);
		return learnerService;
	}
	
	/**
	 * Get the ActionMappings.
	 */
	protected ActionMappings getActionMappings() {
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServlet().getServletContext());
        return (ActionMappings)wac.getBean("actionMappings");
	}
	
	/** 
	 * Get the current learner progress. The request attributes are checked
	 * first, if not in request then a new LearnerProgress is loaded using
	 * the LearnerService. The LearnerProgress is also stored in the
	 * session so that the Flash requests don't have to reload it.
	 */
	protected LearnerProgress getLearnerProgress(HttpServletRequest request, ActivityForm form) {
		LearnerProgress learnerProgress = (LearnerProgress)request.getAttribute(ActivityAction.LEARNER_PROGRESS_REQUEST_ATTRIBUTE);
		if (learnerProgress == null) {
			SessionBean sessionBean = getSessionBean(request);
			User learner = sessionBean.getLeaner();
			Lesson lesson = sessionBean.getLesson();
			
			ILearnerService learnerService = getLearnerService(request);
			learnerProgress = learnerService.getProgress(learner, lesson);
			
			// Save progress in session for Flash request
			sessionBean.setLearnerProgress(learnerProgress);
			setSessionBean(sessionBean, request);
		}
		return learnerProgress;
	}
	
	/**
	 * Sets the LearnerProgress in session so that the Flash requests don't
	 * have to reload it.
	 */
	protected void setLearnerProgress(HttpServletRequest request, LearnerProgress learnerProgress) {
		request.setAttribute(ActivityAction.LEARNER_PROGRESS_REQUEST_ATTRIBUTE, learnerProgress);

		SessionBean sessionBean = getSessionBean(request);
		// Save progress in session for Flash request
		sessionBean.setLearnerProgress(learnerProgress);
		setSessionBean(sessionBean, request);
	}
	
	/**
	 * Convenience method to get the requested activity. First check the
	 * request attribute to see if it has been loaded already this request.
	 * If not in request then load from the LearnerProgress using the forms
	 * activityId. If no activityId specified then return null.
	 * @param request
	 * @param form
	 * @param learnerProgress, the current LearerProgress
	 * @return Activity in request
	 */
	protected Activity getActivity(HttpServletRequest request, ActivityForm form, LearnerProgress learnerProgress) {
		Activity activity = (Activity)request.getAttribute(ActivityAction.ACTIVITY_REQUEST_ATTRIBUTE);
		if (activity == null) {
			Long activityId = form.getActivityId();
			if (activityId != null) {
				activity = getActivity(activityId.longValue(), learnerProgress);
			}
		}
		return activity;
	}
	
	/**
	 * Sets an Activity in the request attributes so that it can be used by
	 * actions forwarded to without reloading it.
	 * @param request
	 * @param activity
	 */
	protected void setActivity(HttpServletRequest request, Activity activity) {
		request.setAttribute(ActivityAction.ACTIVITY_REQUEST_ATTRIBUTE, activity);
	}
	
	
	/** TODO: replace method
	 * A quick method to get an activity from within a progress. This method is
	 * temporary.
	 */
	private Activity getActivity(long activityId, LearnerProgress progress) {
		Set activities = progress.getLesson().getLearningDesign().getActivities();
		Iterator i = activities.iterator();
		while (i.hasNext()) {
			Activity activity = (Activity)i.next();
			if (activity.getActivityId().longValue() == activityId) {
				return activity;
			}
		}
		return null;
	}

}