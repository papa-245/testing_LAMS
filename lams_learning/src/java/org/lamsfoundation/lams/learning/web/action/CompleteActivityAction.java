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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */	
package org.lamsfoundation.lams.learning.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learning.service.LearnerServiceException;
import org.lamsfoundation.lams.learning.web.bean.SessionBean;
import org.lamsfoundation.lams.learning.web.form.ActivityForm;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;

/**
 * @author daveg
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/CompleteActivity" name="activityForm"
 *                validate="false" scope="request"
 * 
 */
public class CompleteActivityAction extends ActivityAction {
    
    protected static String className = "CompleteActivity";
	
	/**
	 * Sets the current activity as complete and uses the progress engine to find
	 * the next activity (may be null). Note that the activity being completed may be
	 * part of a parallel activity.
	 * Forwards onto the required display action (displayToolActivity,
	 * displayParallelActivity, etc.).
	 */
	public ActionForward execute(
			ActionMapping mapping,
			ActionForm actionForm,
			HttpServletRequest request,
			HttpServletResponse response) {
		ActivityForm form = (ActivityForm)actionForm;
		ActivityMapping actionMappings = getActivityMapping();
		
		SessionBean sessionBean = getSessionBean(request);
		if (sessionBean == null) {
			// forward to the no session error page
			return mapping.findForward(ActivityMapping.NO_SESSION_ERROR);
		}
		
		// check token
		if (!this.isTokenValid(request, true)) {
			// didn't come here from options page
		    log.info(className+": No valid token in request");
			return mapping.findForward(ActivityMapping.DOUBLE_SUBMIT_ERROR);
		}
		
		// Get learner
		User learner = sessionBean.getLearner();
		Lesson lesson = sessionBean.getLesson();
		
		LearnerProgress progress = getLearnerProgress(request);
		Activity activity = LearningWebUtil.getActivityFromRequest(request, getLearnerService());
		
		if (activity == null) {
		    log.error(className+": No activity in request or session");
			return mapping.findForward(ActivityMapping.ERROR);
		}

		ILearnerService learnerService = getLearnerService();
		
		// Set activity as complete
		try {
			progress = learnerService.calculateProgress(activity, learner, lesson);
		}
		catch (LearnerServiceException e) {
			return mapping.findForward("error");
		}
		LearningWebUtil.putActivityInRequest(request, progress.getNextActivity(), learnerService);

		// Save progress in session for Flash request
		sessionBean.setLearnerProgress(progress);
		setSessionBean(sessionBean, request);

		ActionForward forward = actionMappings.getProgressForward(progress,true,request, learnerService);
		
		return forward;
	}

}
