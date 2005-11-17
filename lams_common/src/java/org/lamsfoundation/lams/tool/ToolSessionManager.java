/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2004 
 * Created on 2004-12-6
 ******************************************************************************** */

package org.lamsfoundation.lams.tool;

import java.util.List;

import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.usermanagement.User;



/**
 * The interface that defines the tool's contract regarding session. It must 
 * be implemented by the tool to establish the communication channel between
 * tool and lams core service.
 * 
 * @author Jacky Fang 
 * @since 2004-12-6
 * @version 1.1
 */
public interface ToolSessionManager
{
    /**
     * Create a tool session for a piece of tool content using the tool 
     * session id generated by LAMS. If no content exists with the given 
     * tool content id, then use the default content id.
     * 
     * @param toolSessionId the generated tool session id.
     * @param toolContentId the tool content id specified.
     * @throws ToolException if an error occurs e.g. defaultContent is missing.
     */
    public void createToolSession(Long toolSessionId, Long toolContentId) throws ToolException;
    
    /**
     * Call the controller service to complete and leave the tool session.
     * @param toolSessionId the runtime tool session id.
     * @return the url for next activity.
     * @throws DataMissingException if no tool session matches the toolSessionId 
     * @throws ToolException if any other error occurs
     */
    public String leaveToolSession(Long toolSessionId, Long learnerId) 
    	throws DataMissingException, ToolException;

    /**
     * Export the XML fragment for the session export. Not sure if this is required.
     * @throws DataMissingException if no tool session matches the toolSessionId 
     * @throws ToolException if any other error occurs
     */
    public ToolSessionExportOutputData exportToolSession(Long toolSessionId) 
    	throws DataMissingException, ToolException;

    /**
     * Export the XML fragment for the session export. Not sure if this is required.
     * @throws DataMissingException if no tool session matches the toolSessionId 
     * @throws ToolException if any other error occurs
     */
    public ToolSessionExportOutputData exportToolSession(List toolSessionIds) 
    	throws DataMissingException, ToolException;
    
    /**
     * Remove sesson data according specified the tool session id. 
     * @param toolSessionId the generated tool session id.
     * @throws DataMissingException if no tool session matches the toolSessionId 
     * @throws ToolException if any other error occurs
     */
    public void removeToolSession(Long toolSessionId)
    	throws DataMissingException, ToolException;
    
}
