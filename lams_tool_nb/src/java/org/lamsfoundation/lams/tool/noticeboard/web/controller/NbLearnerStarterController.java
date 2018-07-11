/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.noticeboard.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardUser;
import org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService;
import org.lamsfoundation.lams.tool.noticeboard.util.NbApplicationException;
import org.lamsfoundation.lams.tool.noticeboard.util.NbWebUtil;
import org.lamsfoundation.lams.tool.noticeboard.web.form.NbLearnerForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;

/**
 * Creation Date: 27-06-05
 *
 * The learner url can be of three modes learner, teacher or author. Depending on
 * what mode was set, it will trigger the corresponding action. If the mode parameter
 * is missing or a key is not found in the keymap, it will call the unspecified method
 * which defaults to the learner action.
 *
 * <p>
 * The difference between author mode (which is basically the preview)
 * is that if the defineLater flag is set, it will not be able to see the noticeboard screen
 * </p>
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
@Controller
@RequestMapping("/starter")
public class NbLearnerStarterController {

    static Logger logger = Logger.getLogger(NbLearnerStarterController.class.getName());

    @Autowired
    @Qualifier("nbService")
    private INoticeboardService nbService;

    @Autowired
    @Qualifier("nbMessageService")
    private MessageService messageService;

    @Autowired
    private WebApplicationContext applicationContext;

    private UserDTO getUserDTO(HttpServletRequest request) {
	// set up the user details
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	if (user == null) {
	    String error = messageService.getMessage(NoticeboardConstants.ERR_MISSING_PARAM, "User");
	    logger.error(error);
	    throw new NbApplicationException(error);
	}
	return user;
    }

    /** Get the user id from the shared session */
    public Long getUserID(HttpServletRequest request) {
	UserDTO user = getUserDTO(request);
	return new Long(user.getUserID().longValue());
    }

    /** Get the user id from the url - needed for the monitoring mode */
    public Long getUserIDFromURLCall(HttpServletRequest request) {
	return WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID, false);
    }

    public String unspecified(@ModelAttribute NbLearnerForm NbLearnerForm, Errors errors, HttpServletRequest request,
	    HttpServletResponse response) {

	return learner(NbLearnerForm, errors, request, response);
    }

    @RequestMapping("/learner")
    public String learner(@ModelAttribute NbLearnerForm NbLearnerForm, Errors errors, HttpServletRequest request,
	    HttpServletResponse response) {

	NoticeboardContent nbContent = null;
	NoticeboardUser nbUser = null;
//	saveMessages(request, null);

	Long toolSessionID = NbWebUtil.convertToLong(NbLearnerForm.getToolSessionID());

	if (toolSessionID == null) {
	    String error = "Unable to continue. The parameters tool session id is missing";
	    logger.error(error);
	    throw new NbApplicationException(error);
	}

	nbContent = nbService.retrieveNoticeboardBySessionID(toolSessionID);
	// nbSession = nbService.retrieveNoticeboardSession(toolSessionId);

	if (nbContent == null) {
	    String error = "An Internal error has occurred. Please exit and retry this sequence";
	    logger.error(error);
	    throw new NbApplicationException(error);
	}

	if (isFlagSet(nbContent, NoticeboardConstants.FLAG_DEFINE_LATER)) {
	    return "defineLater";
	}

	boolean readOnly = false;
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, false);
	Long userID = null;
	if (mode == ToolAccessMode.LEARNER || mode == ToolAccessMode.AUTHOR) {
	    userID = getUserID(request);
	    nbUser = nbService.retrieveNbUserBySession(userID, toolSessionID);

	    if (!nbContent.isContentInUse()) {
		/* Set the ContentInUse flag to true, and defineLater flag to false */
		nbContent.setContentInUse(true);
		nbService.saveNoticeboard(nbContent);
	    }

	    if (nbUser == null) {
		//create a new user with this session id
		nbUser = new NoticeboardUser(userID);
		UserDTO user = getUserDTO(request);
		nbUser.setUsername(user.getLogin());
		nbUser.setFullname(user.getFirstName() + " " + user.getLastName());
		nbService.addUser(toolSessionID, nbUser);
	    }
	} else {
	    // user will not exist if force completed.
	    userID = getUserIDFromURLCall(request);
	    nbUser = nbService.retrieveNbUserBySession(userID, toolSessionID);
	    readOnly = true;
	}

	NbLearnerForm.copyValuesIntoForm(nbContent, readOnly, mode.toString());

	NotebookEntry notebookEntry = nbService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		NoticeboardConstants.TOOL_SIGNATURE, userID.intValue());
	if (notebookEntry != null) {
	    request.setAttribute("reflectEntry", notebookEntry.getEntry());
	}
	request.setAttribute("reflectInstructions", nbContent.getReflectInstructions());
	request.setAttribute("reflectOnActivity", nbContent.getReflectOnActivity());
	request.setAttribute("allowComments", nbContent.isAllowComments());
	request.setAttribute("likeAndDislike", nbContent.isCommentsLikeAndDislike());
	request.setAttribute("anonymous", nbContent.isAllowAnonymous());

	Boolean userFinished = (nbUser != null && NoticeboardUser.COMPLETED.equals(nbUser.getUserStatus()));
	request.setAttribute("userFinished", userFinished);

	LearningWebUtil.putActivityPositionInRequestByToolSessionId(toolSessionID, request,
		applicationContext.getServletContext());

	/*
	 * Checks to see if the runOffline flag is set.
	 * If the particular flag is set, control is forwarded to jsp page
	 * displaying to the user the message according to what flag is set.
	 */
//	if (displayMessageToUser(nbContent, errors)) {
//	    saveMessages(request, message);
//A	    request.setAttribute("messageForm", );	
//A	    return ("message");
//	}

	return "learnerContent";

    }

    @RequestMapping("/teacher")
    public String teacher(@ModelAttribute NbLearnerForm NbLearnerForm, Errors errors, HttpServletRequest request,
	    HttpServletResponse response) throws NbApplicationException {
	return learner(NbLearnerForm, errors, request, response);
    }

    @RequestMapping("/author")
    public String author(@ModelAttribute NbLearnerForm NbLearnerForm, Errors errors, HttpServletRequest request,
	    HttpServletResponse response) throws NbApplicationException {

	return learner(NbLearnerForm, errors, request, response);

    }

    /**
     * <p>
     * Performs a check on the flag indicated by <code>flag</code>
     * In this noticeboard tool, there are 3 possible flags:
     * <li>defineLater</li>
     * <li>contentInUse</li>
     * <br>
     * Returns true if the flag is set, false otherwise
     * <p>
     *
     * @param content
     *            The instance of NoticeboardContent
     * @param flag
     *            The flag to check, can take the following set of values (defineLater, contentInUse)
     * @return Returns true if flag is set, false otherwise
     */
    private boolean isFlagSet(NoticeboardContent content, int flag) throws NbApplicationException {
	switch (flag) {
	    case NoticeboardConstants.FLAG_DEFINE_LATER:
		return (content.isDefineLater());
	    //  break;
	    case NoticeboardConstants.FLAG_CONTENT_IN_USE:
		return (content.isContentInUse());
	    //	break;
	    default:
		throw new NbApplicationException("Invalid flag");
	}

    }

    /**
     * <p>
     * This methods checks the defineLater and runOffline flag.
     * If defineLater flag is set, then a message is added to an ActionMessages
     * object saying that the contents have not been defined yet. If the runOffline
     * flag is set, a message is added to ActionMessages saying that the contents
     * are not being run online.
     * This method will return true if any one of the defineLater or runOffline flag is set.
     * Otherwise false will be returned.
     * </p>
     *
     * @param content
     *            The instance of NoticeboardContent
     * @param message
     *            the instance of ActtionMessages
     * @return true if any of the flags are set, false otherwise
     */
    private boolean displayMessageToUser(NoticeboardContent content, Errors errors) {
	boolean isDefineLaterSet = isFlagSet(content, NoticeboardConstants.FLAG_DEFINE_LATER);
	if (isDefineLaterSet) {
	    if (isDefineLaterSet) {
		errors.reject("message.defineLaterSet");
	    }
	    return true;
	} else {
	    return false;
	}
    }

}