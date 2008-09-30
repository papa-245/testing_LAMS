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
/* $Id$ */

package org.lamsfoundation.lams.tool.dimdim.web.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @struts.form name="adminForm"
 * @author Anthony Sukkar
 */
public class AdminForm extends ActionForm {

    private static final long serialVersionUID = 8367278543453322252L;

    // Fields

    private String dimdimServerURL;

    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

	ActionErrors errors = new ActionErrors();

	if (StringUtils.isBlank(this.dimdimServerURL)) {
	    // TODO add error messages;
	}
	return errors;
    }

    public String getDimdimServerURL() {
	return dimdimServerURL;
    }

    public void setDimdimServerURL(String dimdimServerURL) {
	this.dimdimServerURL = dimdimServerURL;
    }

}
