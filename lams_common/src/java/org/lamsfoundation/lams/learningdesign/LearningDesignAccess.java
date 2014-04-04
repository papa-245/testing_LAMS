/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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
package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Marks when an user used (opened, saved etc.) a learning design the last time.
 * 
 * @author Marcin Cieslak
 * 
 */
public class LearningDesignAccess implements Serializable {

    Long learningDesignId;
    Integer userId;
    Date accessDate;

    public Long getLearningDesignId() {
	return learningDesignId;
    }

    public void setLearningDesignId(Long learningDesignId) {
	this.learningDesignId = learningDesignId;
    }

    public Integer getUserId() {
	return userId;
    }

    public void setUserId(Integer userId) {
	this.userId = userId;
    }

    public Date getAccessDate() {
	return accessDate;
    }

    public void setAccessDate(Date accessDate) {
	this.accessDate = accessDate;
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(learningDesignId).append(userId).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	LearningDesignAccess other = (LearningDesignAccess) obj;
	if (learningDesignId == null) {
	    if (other.learningDesignId != null) {
		return false;
	    }
	} else if (!learningDesignId.equals(other.learningDesignId)) {
	    return false;
	}
	if (userId == null) {
	    if (other.userId != null) {
		return false;
	    }
	} else if (!userId.equals(other.userId)) {
	    return false;
	}
	return true;
    }
}