/*
 * Created on 21/04/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.qa;

import java.security.Principal;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.web.QaAuthoringForm;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * 
 * 
 * The session attributes ATTR_USERDATA and TOOL_USER refer to the same User object.
 * TOOL_USER is the one we consistently use across the application to obtain current user data.
 * 
 * Verify the assumption:
 * We make the assumption that the obtained User object will habe a userId property ready in it. 
 * We use the same  userId property as the user table key when we are saving learner responses and associated user data. 
 *   * 
 */
/**
 * @author ozgurd
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * Common utility functions live here.  
 */
public abstract class QaUtils implements QaAppConstants {

	static Logger logger = Logger.getLogger(QaUtils.class.getName());
	
	public static IQaService getToolService(HttpServletRequest request)
	{
		IQaService qaService=(IQaService)request.getSession().getAttribute(TOOL_SERVICE);
	    return qaService;
	}
	
	
	/**
	 * generateId()
	 * return long
	 * IMPORTANT: The way we obtain either content id or tool session id must be modified
	 * so that we only use lams common to get these ids. This functionality is not 
	 * available yet in the lams common as of 21/04/2005.
	 */
	public static long generateId()
	{
		Random generator = new Random();
		long longId=generator.nextLong();
		if (longId < 0) longId=longId * (-1) ;
		return longId;
	}
	
	/**
	 * helps create a mock user object in development time.
	 * static long generateIntegerId()
	 * @return long
	 */
	public static int generateIntegerId()
	{
		Random generator = new Random();
		int intId=generator.nextInt();
		if (intId < 0) intId=intId * (-1) ;
		return intId;
	}
	
	
	
	/**
     * cleanupSession(TreeMap mapQuestionContent, HttpServletRequest request)
     * return void
     * cleans up the session of the content details
     */

    public static void cleanupSession(HttpServletRequest request)
    {
    	//remove session attributes in Authoring mode 
		request.getSession().removeAttribute(DEFAULT_QUESTION_CONTENT);
		request.getSession().removeAttribute(MAP_QUESTION_CONTENT);
		request.getSession().removeAttribute(CHOICE);
		request.getSession().removeAttribute(IS_DEFINE_LATER);
		request.getSession().removeAttribute(DISABLE_TOOL);
		request.getSession().removeAttribute(CHOICE_TYPE_BASIC);
	    request.getSession().removeAttribute(CHOICE_TYPE_ADVANCED);
	    request.getSession().removeAttribute(CHOICE_TYPE_INSTRUCTIONS);
	    request.getSession().removeAttribute(REPORT_TITLE);
	    request.getSession().removeAttribute(INSTRUCTIONS);
	    request.getSession().removeAttribute(TITLE);
	    request.getSession().removeAttribute(CONTENT_LOCKED);
	    
		
		//remove session attributes in Learner mode
		request.getSession().removeAttribute(MAP_ANSWERS);
		request.getSession().removeAttribute(MAP_QUESTION_CONTENT_LEARNER);
		request.getSession().removeAttribute(CURRENT_QUESTION_INDEX);
		request.getSession().removeAttribute(TOTAL_QUESTION_COUNT);
		request.getSession().removeAttribute(CURRENT_ANSWER);
		request.getSession().removeAttribute(USER_FEEDBACK);
		request.getSession().removeAttribute(TOOL_SESSION_ID);
		request.getSession().removeAttribute(QUESTION_LISTING_MODE);
		request.getSession().removeAttribute(QUESTION_LISTING_MODE_SEQUENTIAL);
		request.getSession().removeAttribute(QUESTION_LISTING_MODE_COMBINED);
		request.getSession().removeAttribute(MAP_USER_RESPONSES);
		request.getSession().removeAttribute(MAP_MAIN_REPORT);
		request.getSession().removeAttribute(REPORT_TITLE_LEARNER);
		request.getSession().removeAttribute(END_LEARNING_MESSAGE);
		request.getSession().removeAttribute(IS_TOOL_ACTIVITY_OFFLINE);
		
		//remove session attributes in Monitoring mode
		request.getSession().removeAttribute(MAP_TOOL_SESSIONS);
		request.getSession().removeAttribute(MAP_MONITORING_QUESTIONS);
		
		//remove session attributes used commonly
		request.getSession().removeAttribute(IS_USERNAME_VISIBLE);
		request.getSession().removeAttribute(REPORT_TITLE_MONITOR);
		request.getSession().removeAttribute(IS_ALL_SESSIONS_COMPLETED);
		request.getSession().removeAttribute(CHECK_ALL_SESSIONS_COMPLETED);
		request.getSession().removeAttribute(TOOL_CONTENT_ID);
		request.getSession().removeAttribute(ATTR_USERDATA);
		request.getSession().removeAttribute(TOOL_USER);
		request.getSession().removeAttribute(TOOL_SERVICE);
		request.getSession().removeAttribute(TARGET_MODE);
		
		request.getSession().removeAttribute(CURRENT_TOOL_USER_FULLNAME);
		request.getSession().removeAttribute(CURRENT_TOOL_USER_ATTEMPTTIME);
		request.getSession().removeAttribute(CURRENT_TOOL_USER_ANSWER);
    }

    public static void setDefaultSessionAttributes(HttpServletRequest request, QaContent defaultQaContent, QaAuthoringForm qaAuthoringForm)
	{
		//should never be null anyway as default content MUST exist in the db
		if (defaultQaContent != null)
		{		
			qaAuthoringForm.setTitle(defaultQaContent.getTitle());
			qaAuthoringForm.setInstructions(defaultQaContent.getInstructions());
			qaAuthoringForm.setReportTitle(defaultQaContent.getReportTitle());
			qaAuthoringForm.setEndLearningMessage(defaultQaContent.getEndLearningMessage());
			qaAuthoringForm.setOnlineInstructions(defaultQaContent.getOnlineInstructions());
			qaAuthoringForm.setOfflineInstructions(defaultQaContent.getOfflineInstructions());
			qaAuthoringForm.setMonitoringReportTitle(defaultQaContent.getMonitoringReportTitle());
			
			request.getSession().setAttribute(TITLE,qaAuthoringForm.getTitle());
			request.getSession().setAttribute(INSTRUCTIONS,qaAuthoringForm.getInstructions());
		}
		
	}
    
    
    /**
     * Helper method to retrieve the user data. We always load up from http
     * session first to optimize the performance. If no session cache available,
     * we load it from data source.
     * @param request A standard Servlet HttpServletRequest class.
     * @param surveyService the service facade of qa tool
     * @return the user data value object
     */
	public static User getUserData(HttpServletRequest request,IQaService qaService) throws QaApplicationException
    {
        User userCompleteData = (User) request.getSession().getAttribute(ATTR_USERDATA);
	    logger.debug(logger + " " + "QaUtils" +  "retrieving userCompleteData: " + userCompleteData);
        /**
         * if no session cache available, retrieve it from data source
         */
        if (userCompleteData == null)
        {	
        	/**
             * WebUtil.getUsername(request,DEVELOPMENT_FLAG) returns the current learner's username based on 
             * user principals defined in the container. If no username is defined in the container, we get a RunTimeException.
             */
        	
        	/**
        	 * pass testing flag as false to obtain user principal 
        	 */
        	try
			{
        		String userName=getUsername(request,false);
        		userCompleteData = qaService.getCurrentUserData(userName);
        	}
        	catch(QaApplicationException e)
			{
        		logger.debug(logger + " " + "QaUtils" +  " Exception occured: Tool expects the current user is an authenticated user and he has a security principal defined. Can't continue!: " + e);
        		throw new QaApplicationException("Exception occured: " +
    			"Tool expects the current user is an authenticated user and he has a security principal defined. Can't continue!");	
			}
        	
            logger.debug(logger + " " + "QaUtils" +  "retrieving userCompleteData from service: " + userCompleteData);
            //this can be redundant as we keep the User data in TOOL_USER
            request.getSession().setAttribute(ATTR_USERDATA, userCompleteData);
        }
        return userCompleteData;
    }

	public static int getCurrentUserId(HttpServletRequest request) throws QaApplicationException
    {
		User user=(User) request.getSession().getAttribute(TOOL_USER);
		logger.debug(logger + " " + "QaUtils" +  " Current user is: " + user + " with id: " + user.getUserId());
		return user.getUserId().intValue();
    }
	
	
	/**
	 * Modified to throw QaApplicationException insteadof RuntimeException
	 * String getUsername(HttpServletRequest req,boolean isTesting) throws RuntimeException
	 * is normally lives in package org.lamsfoundation.lams.util. It generates Runtime exception when the user principal 
	 * is not found. We find this not too usefulespeciaaly in teh development time. Below is a local and modified version
	 * of this function. 
	 * 
	 * @return username from principal object
	 */
	public static String getUsername(HttpServletRequest req,boolean isTesting) throws QaApplicationException
	{
	    if(isTesting)
	        return "test";
	    
		Principal principal = req.getUserPrincipal();
		if (principal == null)
		{
			throw new QaApplicationException("Trying to get username but principal object missing. Request is "
					+ req.toString());
		}
			
		String username = principal.getName();
		if (username == null)
		{
			throw new QaApplicationException("Name missing from principal object. Request is "
					+ req.toString()
					+ " Principal object is "
					+ principal.toString());
		}
		return username;
	}
	
	
	/**
	 * This method exists temporarily until we have the user data is passed properly from teh container to the tool
	 * createMockUser()
	 * @return User 
	 */
	public static User createMockUser()
	{
		logger.debug(logger + " " + "QaUtils" +  " request for new new mock user");
		int randomUserId=generateIntegerId();
		User mockUser=new User();
		mockUser.setUserId(new Integer(randomUserId));
		mockUser.setFirstName(MOCK_USER_NAME + randomUserId);
		mockUser.setLastName(MOCK_USER_LASTNAME + randomUserId);
		mockUser.setLogin(MOCK_LOGIN_NAME + randomUserId); //we assume login and username refers to the same property
		logger.debug(logger + " " + "QaUtils" +  " created mockuser: " + mockUser);
		return mockUser;
	}
	
	
	public static User createAuthoringUser(Integer userId)
	{
		User user=new User();
		user.setUserId(userId);
		return user;
	}
	
	public static User createUser(Integer userId)
	{
		User user=new User();
		user.setUserId(userId);
		
		int randomUserId=generateIntegerId();
		user.setFirstName(MOCK_USER_NAME + randomUserId);
		user.setLastName(MOCK_USER_LASTNAME + randomUserId);
		user.setLogin(MOCK_LOGIN_NAME + randomUserId); 
		return user;
	}
	
	public static boolean getDefineLaterStatus()
	{
		return false;
	}
	
	
	/**
	 * existsContent(long toolContentId)
	 * @param long toolContentId
	 * @return boolean
	 * determine whether a specific toolContentId exists in the db
	 */
	public static boolean existsContent(long toolContentId, HttpServletRequest request)
	{
		/**
		 * retrive the service
		 */
		IQaService qaService =QaUtils.getToolService(request);
	    
    	QaContent qaContent=qaService.loadQa(toolContentId);
	    logger.debug(logger + " " + "QaUtils " +  "retrieving qaContent: " + qaContent);
	    if (qaContent == null) 
	    	return false;
	    
		return true;	
	}

	/**
	 * it is expected that the tool session id already exists in the tool sessions table
	 * existsSession(long toolSessionId)
	 * @param toolSessionId
	 * @return boolean
	 */
	public static boolean existsSession(long toolSessionId, HttpServletRequest request)
	{
		/**
		 * get the service
		 */
    	IQaService qaService =QaUtils.getToolService(request);
	    QaSession qaSession=qaService.retrieveQaSessionOrNullById(toolSessionId);
    	
	    logger.debug(logger + " " + " QaUtils " +  "retrieving qaSession: " + qaSession);
	    
	    if (qaSession == null) 
	    	return false;
	    
		return true;	
	}
	
}
