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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.planner.dto;

public class PedagogicalPlannerActivityDTO {
    private String pedagogicalPlannerUrl;
    private String toolIconUrl;
    private String title;
    private String type;
    private String editingAdviceUrl;
    private String checkEditingAdviceUrl;
    private Boolean supportsPlanner;
    private Short group;
    private String parentActivityTitle;
    private Boolean lastNestedActivity = false;
    private Boolean defaultBranch = false;
    private Short complexActivityType;

    public static final short TYPE_BRANCHING_ACTIVITY = 1;
    public static final short TYPE_OPTIONAL_ACTIVITY = 2;
    public static final short TYPE_PARALLEL_ACTIVITY = 3;

    public Boolean getSupportsPlanner() {
	return supportsPlanner;
    }

    public void setSupportsPlanner(Boolean supportsPlanner) {
	this.supportsPlanner = supportsPlanner;
    }

    public String getPedagogicalPlannerUrl() {
	return pedagogicalPlannerUrl;
    }

    public void setPedagogicalPlannerUrl(String toolSignature) {
	pedagogicalPlannerUrl = toolSignature;
    }

    public PedagogicalPlannerActivityDTO(String type, String title, Boolean supportsPlanner,
	    String pedagogicalPlannerUrl, String toolIconUrl, String checkEditingAdviceUrl, String editingAdviceUrl) {
	this.supportsPlanner = supportsPlanner;
	this.pedagogicalPlannerUrl = pedagogicalPlannerUrl;
	this.toolIconUrl = toolIconUrl;
	this.checkEditingAdviceUrl = checkEditingAdviceUrl;
	this.editingAdviceUrl = editingAdviceUrl;
	this.title = title;
	this.type = type;

    }

    public String getToolIconUrl() {
	return toolIconUrl;
    }

    public void setToolIconUrl(String toolIconUrl) {
	this.toolIconUrl = toolIconUrl;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getEditingAdviceUrl() {
	return editingAdviceUrl;
    }

    public void setEditingAdviceUrl(String editingAdviceUrl) {
	this.editingAdviceUrl = editingAdviceUrl;
    }

    public String getCheckEditingAdviceUrl() {
	return checkEditingAdviceUrl;
    }

    public void setCheckEditingAdviceUrl(String checkEditingAdviceUrl) {
	this.checkEditingAdviceUrl = checkEditingAdviceUrl;
    }

    public Short getGroup() {
	return group;
    }

    public void setGroup(Short branch) {
	group = branch;
    }

    public String getParentActivityTitle() {
	return parentActivityTitle;
    }

    public void setParentActivityTitle(String branchingActivityTitle) {
	parentActivityTitle = branchingActivityTitle;
    }

    public Boolean getLastNestedActivity() {
	return lastNestedActivity;
    }

    public void setLastNestedActivity(Boolean lastNestedActivity) {
	this.lastNestedActivity = lastNestedActivity;
    }

    public Boolean getDefaultBranch() {
	return defaultBranch;
    }

    public void setDefaultBranch(Boolean defaultBranch) {
	this.defaultBranch = defaultBranch;
    }

    public Short getComplexActivityType() {
	return complexActivityType;
    }

    public void setComplexActivityType(Short complexActivityType) {
	this.complexActivityType = complexActivityType;
    }
}