/*
	Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
    USA

    http://www.gnu.org/licenses/gpl.txt
*/
package org.lamsfoundation.lams.tool.service;

import java.util.List;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.tool.ToolContentIDGenerator;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.dao.IToolSessionDAO;
import org.lamsfoundation.lams.usermanagement.User;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * 
 * @author Jacky Fang 2005-2-23
 * 
 */
public class LamsToolService implements ILamsToolService,ApplicationContextAware
{
    //---------------------------------------------------------------------
    // Instance variables
    //---------------------------------------------------------------------
    private ApplicationContext context;
    private IToolSessionDAO toolSessionDAO;
    private ToolContentIDGenerator contentIDGenerator;
    //---------------------------------------------------------------------
    // Inversion of Control Methods - Method injection
    //---------------------------------------------------------------------
    /**
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    public void setApplicationContext(ApplicationContext context) throws BeansException
    {
        this.context = context;
    }
    
    /**
     * @param toolSessionDAO The toolSessionDAO to set.
     */
    public void setToolSessionDAO(IToolSessionDAO toolSessionDAO)
    {
        this.toolSessionDAO = toolSessionDAO;
    }

    /**
     * @param contentIDGenerator The contentIDGenerator to set.
     */
    public void setContentIDGenerator(ToolContentIDGenerator contentIDGenerator)
    {
        this.contentIDGenerator = contentIDGenerator;
    }
    //---------------------------------------------------------------------
    // Service Methods
    //---------------------------------------------------------------------
    /**
     * @see org.lamsfoundation.lams.tool.service.ILamsToolService#getAllPotentialLearners(long)
     */
    public List getAllPotentialLearners(long toolContentID) throws LamsToolServiceException
    {
        return null;
    }

    /**
     * @see org.lamsfoundation.lams.tool.service.ILamsToolService#createToolSession(org.lamsfoundation.lams.usermanagement.User, org.lamsfoundation.lams.learningdesign.Activity)
     */
    public ToolSession createToolSession(User learner, ToolActivity activity,Lesson lesson) throws LamsToolServiceException
    {
        ToolSession toolSession = activity.createToolSessionForActivity(learner,lesson);
        
        toolSessionDAO.saveToolSession(toolSession);
        
        return toolSession;
    }

    /**
     * @see org.lamsfoundation.lams.tool.service.ILamsToolService#getToolSessionByLearner(org.lamsfoundation.lams.usermanagement.User, org.lamsfoundation.lams.learningdesign.Activity)
     */
    public ToolSession getToolSessionByLearner(User learner, Activity activity) throws LamsToolServiceException
    {
        return toolSessionDAO.getToolSessionByLearner(learner,activity);
    }

    /**
     * @see org.lamsfoundation.lams.tool.service.ILamsToolService#getToolSessionById(java.lang.Long)
     */
    public ToolSession getToolSessionById(Long toolSessionId)
    {
        return toolSessionDAO.getToolSession(toolSessionId);
    }
    /**
     * @see org.lamsfoundation.lams.tool.service.ILamsToolService#notifyToolsToCreateSession(java.lang.Long, org.lamsfoundation.lams.learningdesign.ToolActivity)
     */
    public void notifyToolsToCreateSession(Long toolSessionId, ToolActivity activity)
    {
        ToolSessionManager sessionManager = (ToolSessionManager) findToolService(activity);

        sessionManager.createToolSession(toolSessionId,
                                         activity.getToolContentId());
        
    }

    /**
     * Make a copy of all tools content which belongs to this learning design.
     * 
     * @param toolActivity the tool activity defined in the design.
     * @see org.lamsfoundation.lams.tool.service.ILamsToolService#copyToolContent(org.lamsfoundation.lams.learningdesign.ToolActivity)
     */
    public Long copyToolContent(ToolActivity toolActivity)
    {
        Long newToolcontentID = contentIDGenerator.getNextToolContentIDFor(toolActivity.getTool());
        //This is just for testing purpose because surveyService is the only 
        //service is available at the moment. 
        //TODO we need to remove this once all done.
        if (isSurvey(toolActivity))
        {
            ToolContentManager contentManager = (ToolContentManager) findToolService(toolActivity);
            contentManager.copyToolContent(toolActivity.getToolContentId(),
                                           newToolcontentID);
        }
        return newToolcontentID;
    }
    
    /**
     * @see org.lamsfoundation.lams.tool.service.ILamsToolService#updateToolSession(org.lamsfoundation.lams.tool.ToolSession)
     */
    public void updateToolSession(ToolSession toolSession)
    {
        toolSessionDAO.updateToolSession(toolSession);        
    }
    //---------------------------------------------------------------------
    // Helper Methods
    //---------------------------------------------------------------------
   
    /**
     * Find a tool's service registered inside lams. It is implemented using
     * Spring now. We might need to extract this method to a proxy class to
     * find different service such as EJB or Web service. 
     * @param toolActivity the tool activity defined in the design.
     * @return the service object from tool.
     */
    private Object findToolService(ToolActivity toolActivity)
    {
        return context.getBean(toolActivity.getTool().getServiceName());
    }

    /**
     * This is more for testing purpose. 
     * @param toolActivity the tool activity defined in the design.
     * @return
     */
    private boolean isSurvey(ToolActivity toolActivity)
    {
        return toolActivity.getTool().getServiceName().equals("surveyService");
    }


}
