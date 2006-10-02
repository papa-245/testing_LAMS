/****************************************************************
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
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc;

import org.apache.commons.lang.builder.ToStringBuilder;
import java.util.List; 


/**
 * <p> DTO that holds users attempt history data for jsp purposes
 * </p>
 * 
 * @author Ozgur Demirtas
 */
public class McQuestionContentDTO implements Comparable
{
    private String question;
    private String displayOrder;
    private String feedback;
    private String weight;
    private String mark;
    
    private String caCount;
    private List listCandidateAnswersDTO;
    
	public String toString() {
       return new ToStringBuilder(this)
            .append("question:", question)
            .append("feedback:", feedback)
            .append("weight:", weight)
            .append("caCount:", caCount)
            .append("displayOrder:", displayOrder)
            .append("mark:", mark)
            .append("listCandidateAnswersDTO:", listCandidateAnswersDTO)
            .toString();
    }
	
	public int compareTo(Object o)
    {
		McMonitoredUserDTO mcMonitoredUserDTO = (McMonitoredUserDTO) o;
     
        if (mcMonitoredUserDTO == null)
        	return 1;
		else
			return 0;
    }
    /**
     * @return Returns the displayOrder.
     */
    public String getDisplayOrder() {
        return displayOrder;
    }
    /**
     * @param displayOrder The displayOrder to set.
     */
    public void setDisplayOrder(String displayOrder) {
        this.displayOrder = displayOrder;
    }
    /**
     * @return Returns the feedback.
     */
    public String getFeedback() {
        return feedback;
    }
    /**
     * @param feedback The feedback to set.
     */
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
    /**
     * @return Returns the question.
     */
    public String getQuestion() {
        return question;
    }
    /**
     * @param question The question to set.
     */
    public void setQuestion(String question) {
        this.question = question;
    }
    
    /**
     * @return Returns the weight.
     */
    public String getWeight() {
        return weight;
    }
    /**
     * @param weight The weight to set.
     */
    public void setWeight(String weight) {
        this.weight = weight;
    }
    
    /**
     * @return Returns the mark.
     */
    public String getMark() {
        return mark;
    }
    /**
     * @param mark The mark to set.
     */
    public void setMark(String mark) {
        this.mark = mark;
    }
    /**
     * @return Returns the listCandidateAnswersDTO.
     */
    public List getListCandidateAnswersDTO() {
        return listCandidateAnswersDTO;
    }
    /**
     * @param listCandidateAnswersDTO The listCandidateAnswersDTO to set.
     */
    public void setListCandidateAnswersDTO(List listCandidateAnswersDTO) {
        this.listCandidateAnswersDTO = listCandidateAnswersDTO;
    }
    
    /**
     * @return Returns the caCount.
     */
    public String getCaCount() {
        return caCount;
    }
    /**
     * @param caCount The caCount to set.
     */
    public void setCaCount(String caCount) {
        this.caCount = caCount;
    }
  }
