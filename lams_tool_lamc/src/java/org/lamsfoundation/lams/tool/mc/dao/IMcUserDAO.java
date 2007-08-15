/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
 * ***********************************************************************/
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc.dao;

import java.util.List;

import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueUsr;
import org.lamsfoundation.lams.tool.mc.pojos.McSession;

/**
 * @author Ozgur Demirtas
 * <p>Interface for the McUser DAO, defines methods needed to access/modify user data</p>
 */
public interface IMcUserDAO {
    
    /**
	 * <p>Return the persistent instance of a McQueUsr 
	 * with the given identifier <code>uid</code>, returns null if not found. </p>
	 * 
	 * @param uid an identifier for the McQueUsr.
	 * @return the persistent instance of a McQueUsr or null if not found
	 */
    public McQueUsr getMcUserByUID(Long uid);
   
    /**
	 * <p> Return the persistent instance of a McQueUsr
	 * with the given user id <code>userId</code>,
	 * returns null if not found.</p>
	 * 
	 * @param userId The id of a McQueUsr
	 * @return the persistent instance of a McQueUsr or null if not found.
	 */
    public McQueUsr findMcUserById(Long userId);
    
    public McQueUsr getMcUserBySession(Long userId, Long sessionId);
    
    public void saveMcUser(McQueUsr mcUser);
    
    /**
     * <p>Update the given persistent instance of McQueUsr.</p>
     * 
     * @param nbUser The instance of McQueUsr to persist.
     */
    public void updateMcUser(McQueUsr mcUser);
    
    /**
     * <p>Delete the given instance of McQueUsr</p>
     * 
     * @param nbUser The instance of McQueUsr to delete. 
     */
    public void removeMcUser(McQueUsr mcUser);
    
    /**
     * <p>Delete the given instance of McQueUsr with the
     * given user id <code>userId</code>
     * 
     * @param userId The mc user id.
     */
    public void removeMcUserById(Long userId);
    
    public List getMcUserBySessionOnly(final McSession mcSession);
    
    public int getTotalNumberOfUsers(McContent mcContent);
    
    public int getTotalNumberOfUsers();
    
    public int countUserComplete(McContent mcContent);
   
    /** Get the max, min and average mark (in that order) for a session */
    public Integer[] getMarkStatisticsForSession(Long sessionUid);
}
