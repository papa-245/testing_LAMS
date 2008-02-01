/****************************************************************
 * Copyright (C) 2007 LAMS Foundation (http://lamsfoundation.org)
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

package org.lamsfoundation.integration.webct;


import java.sql.Timestamp;
import java.util.Date;
import java.text.DateFormat;

/**
 * Class for mapping LAMS lessons to the database
 * @author luke foxton
 *
 */
public class LamsLesson {

	private long id;
	private long ptId;				// powerlink instance id
	private long lessonId; 
	private long learningContextId;	// webct learning context id
	private long sequenceId;
	private String title;
	private String description;
	private String ownerId; 		// webct userId
	private String ownerFirstName;	// webct user first name
	private String ownerLastName;	// webct user second name
	private boolean hidden; 		// only visible to owner if true
	private boolean schedule;
	private Timestamp startTimestamp;
	private Timestamp endTimestamp;
	
	
	
	/**
	 * Default constructor
	 */
	public LamsLesson() {}
	
	/**
	 * Minimal constructor
	 */
	public LamsLesson(long lessonId, long learningContextId, String title) {
		this.lessonId = lessonId;
		this.learningContextId = learningContextId;
		this.title = title;
	}
	
	/**
	 * Full constructor
	 */
	public LamsLesson(
					long lessonId, 
					long ptId,
					long learningContextId, 
					long sequenceId, 
					String title,
					String description, 
					String ownerId, 
					String ownerFirstName,
					String ownerLastName, 
					boolean hidden, 
					boolean schedule,
					Timestamp startTimestamp, 
					Timestamp endTimestamp)
	{
		this.lessonId = lessonId;
		this.ptId = ptId;
		this.learningContextId = learningContextId;
		this.sequenceId = sequenceId;
		this.title = title;
		this.description = description;
		this.ownerId = ownerId;
		this.ownerFirstName = ownerFirstName;
		this.ownerLastName = ownerLastName;
		this.hidden = hidden;
		this.schedule = schedule;
		this.startTimestamp = startTimestamp;
		this.endTimestamp = endTimestamp;
	}
	
	
	
	public long getLessonId() {
		return lessonId;
	}
	public void setLessonId(long lessonId) {
		this.lessonId = lessonId;
	}
	public long getSequenceId() {
		return sequenceId;
	}
	public void setSequenceId(long sequenceId) {
		this.sequenceId = sequenceId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerFirstName() {
		return ownerFirstName;
	}
	public void setOwnerFirstName(String ownerFirstName) {
		this.ownerFirstName = ownerFirstName;
	}
	public String getOwnerLastName() {
		return ownerLastName;
	}
	public void setOwnerLastName(String ownerLastName) {
		this.ownerLastName = ownerLastName;
	}
	public boolean getHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	public boolean getSchedule() {
		return schedule;
	}
	public void setSchedule(boolean schedule) {
		this.schedule = schedule;
	}
	public Timestamp getStartTimestamp() {
		return startTimestamp;
	}
	public void setStartTimestamp(Timestamp startTimestamp) {
		this.startTimestamp = startTimestamp;
	}
	public Timestamp getEndTimestamp() {
		return endTimestamp;
	}
	public void setEndTimestamp(Timestamp endTimestamp) {
		this.endTimestamp = endTimestamp;
	}

	public long getLearningContextId() {
		return learningContextId;
	}

	public void setLearningContextId(long learningContextId) {
		this.learningContextId = learningContextId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	public String toString()
	{
	    final String TAB = "    ";
	    
	    String retValue = "";
	    
	    retValue = "LamsLesson ( "
	        + "lessonId = " + this.lessonId + TAB
	        + "ptId = " + this.ptId + TAB
	        + "learningContextId = " + this.learningContextId + TAB
	        + "sequenceId = " + this.sequenceId + TAB
	        + "title = " + this.title + TAB
	        + "description = " + this.description + TAB
	        + "ownerId = " + this.ownerId + TAB
	        + "ownerFirstName = " + this.ownerFirstName + TAB
	        + "ownerLastName = " + this.ownerLastName + TAB
	        + "hidden = " + this.hidden + TAB
	        + "schedule = " + this.schedule + TAB
	        + "startTimestamp = " + this.startTimestamp + TAB
	        + "endTimestamp = " + this.endTimestamp + TAB
	        + " )";
	
	    return retValue;
	}

	public String getStartStr()
	{
		Timestamp t = this.getStartTimestamp();
		if (t != null)
		{
			String ret =  DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date(t.getTime()));
			ret += " " + DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date(t.getTime()));
			return ret;
		}
		else
		{
			return "";
		} 
	}
	
	public String getEndStr()
	{
		Timestamp t = this.getEndTimestamp();
		if (t != null)
		{
			String ret = DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date(t.getTime()));
			ret += " " + DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date(t.getTime()));
			return ret;
		}
		else
		{
			return "";
		}
	}
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPtId() {
		return ptId;
	}

	public void setPtId(long ptId) {
		this.ptId = ptId;
	}
	
}
