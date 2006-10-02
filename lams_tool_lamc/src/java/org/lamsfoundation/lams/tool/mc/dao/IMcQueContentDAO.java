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

import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;


/**
 * 
 * @author Ozgur Demirtas
 * <p>Interface for the McQueContent DAO, defines methods needed to access/modify mc question content</p>
 *
 */
public interface IMcQueContentDAO
{
	/**
	 *  * <p>Return the persistent instance of a McQueContent  
	 * with the given identifier <code>uid</code>, returns null if not found. </p>
	 * 
	 * @param uid
	 * @return McQueContent
	 */
	public McQueContent getMcQueContentByUID(Long uid);
	
	/**
	 *  * <p>Return the persistent instance of a McQueContent  
	 * with the given identifier <code>mcContentId</code>, returns null if not found. </p>
	 * 
	 * @param mcContentId
	 * @return McQueContent
	 */
	public McQueContent getToolDefaultQuestionContent(final long mcContentId);
	
	
	
	/**
	 *  * <p>Return the persistent instance of a McQueContent  
	 * with the given identifier <code>question</code> and <code>mcContentUid</code>, returns null if not found. </p>
	 * 
	 * @param question
	 * @param mcContentUid
	 * @return McQueContent
	 */	
	public McQueContent getQuestionContentByQuestionText(final String question, final Long mcContentUid);
	
	/**
	 *  * <p>Return the persistent instance of a McQueContent  
	 * with the given identifier <code>displayOrder</code> and <code>mcContentUid</code>, returns null if not found. </p>
	 * 
	 * @param displayOrder
	 * @param mcContentUid
	 * @return McQueContent
	 */	
	public McQueContent getQuestionContentByDisplayOrder(final Long displayOrder, final Long mcContentUid);
	
	/**
	 *  * <p>Return a list of McQueContent  
	 * with the given identifier <code>question</code> and <code>mcContentUid</code>, returns null if not found. </p>
	 * 
	 * @param mcContentUid
	 * @return List
	 */	
	public List getAllQuestionEntries(final long mcContentId);
	
	/**
	 *  * <p>Return a list of McQueContent  
	 * with the given identifier <code>question</code> and <code>mcContentUid</code>, returns null if not found. </p>
	 * 
	 * @param mcContentUid
	 * @return List
	 */	
	public List refreshQuestionContent(final Long mcContentId);

	/**
	 *  * <p>removes McQueContent  
	 * with the given identifier <code>question</code> and <code>mcContentUid</code>, returns null if not found. </p>
	 * 
	 * @param mcContentUid
	 */	
 	public void cleanAllQuestions(final Long mcContentUid);
 	
	/**
	 *  * <p>removes McQueContent  
	 * with the given identifier <code>mcContentUid</code> </p>
	 * 
	 * @param mcContentUid
	 */	
 	public void cleanAllQuestionsSimple(final Long mcContentUid);
	
	/**
	 *  * <p>resets McQueContent  
	 * with the given identifier <code>mcContentUid</code> </p>
	 * 
	 * @param mcContentUid
	 */	 	
 	public void resetAllQuestions(final Long mcContentUid);

	/**
	 *  * <p>removes McQueContent  
	 * with the given identifier <code>mcContentUid</code> </p>
	 * 
	 * @param mcContentUid
	 */	 	
	public void removeQuestionContentByMcUid(final Long mcContentUid);
 	
	/**
	 *  * <p>saves McQueContent  
	 * with the given identifier <code>mcQueContent</code> </p>
	 * 
	 * @param mcQueContent
	 */	
 	public void saveMcQueContent(McQueContent mcQueContent);
    
	/**
	 *  * <p>updates McQueContent  
	 * with the given identifier <code>mcQueContent</code> </p>
	 * 
	 * @param mcQueContent
	 */ 	
	public void updateMcQueContent(McQueContent mcQueContent);
	
	/**
	 *  * <p>saves McQueContent  
	 * with the given identifier <code>mcQueContent</code> </p>
	 * 
	 * @param mcQueContent
	 */	
	public void saveOrUpdateMcQueContent(McQueContent mcQueContent);
	
	/**
	 *  * <p>removes McQueContent  
	 * with the given identifier <code>uid</code> </p>
	 * 
	 * @param uid
	 */		
	public void removeMcQueContentByUID(Long uid);

	/**
	 *  * <p>removes McQueContent  
	 * with the given identifier <code>mcQueContent</code> </p>
	 * 
	 * @param mcQueContent
	 * @return 
	 */		
	public void removeMcQueContent(McQueContent mcQueContent);
	
	/**
	 *  * <p> used to get the next available display order  
	 * with the given identifier <code>mcContentId</code> </p>
	 * 
	 * @param mcQueContent
	 * @return 
	 */	
	public List getNextAvailableDisplayOrder(final long mcContentId);
	
	public McQueContent findMcQuestionContentByUid(Long uid);
	
	public List getAllQuestionEntriesSorted(final long qaContentId);
	
	public List getMcQueContentsByContentId(long mcContentId);
	
}
