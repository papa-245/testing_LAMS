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

package org.lamsfoundation.lams.admin.web.controller;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.admin.web.form.UserRolesForm;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author jliew
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
@Controller
public class UserRolesController {

    private static Logger log = Logger.getLogger(UserRolesController.class);
    private static IUserManagementService service;
    private static MessageService messageService;
    private static List<Role> rolelist;

    @Autowired
    private WebApplicationContext applicationContext;

    @RequestMapping("/userroles")
    public String execute(@ModelAttribute UserRolesForm userRolesForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	service = AdminServiceProxy.getService(applicationContext.getServletContext());
	messageService = AdminServiceProxy.getMessageService(applicationContext.getServletContext());
	if (rolelist == null) {
	    rolelist = service.findAll(Role.class);
	    Collections.sort(rolelist);
	}

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	
	Integer orgId = WebUtil.readIntParam(request, "orgId", true);
	Integer userId = WebUtil.readIntParam(request, "userId", true);

	// user and org ids passed as attributes by UserSaveAction
	if (orgId == null) {
	    orgId = (Integer) request.getAttribute("orgId");
	}
	
	userRolesForm.setOrgId(orgId);
	userRolesForm.setUserId(userId);
	
	if (orgId == null) {
	    errorMap.add("GLOBAL", messageService.getMessage("error.org.invalid"));
	    request.setAttribute("errorMap", errorMap);
	    return "userrole";
	}
	if (userId == null || userId == 0) {
	    userId = (Integer) request.getAttribute("userId");
	}
	if (userId == null) {
	    errorMap.add("GLOBAL", messageService.getMessage("error.userid.invalid"));
	    request.setAttribute("errorMap", errorMap);
	    return "userrole";
	}
	log.debug("editing roles for userId: " + userId + " and orgId: " + orgId);

	// test requestor's permission
	Organisation org = (Organisation) service.findById(Organisation.class, orgId);
	User user = (User) service.findById(User.class, userId);
	OrganisationType orgType = org.getOrganisationType();
	Integer orgIdOfCourse = (orgType.getOrganisationTypeId().equals(OrganisationType.CLASS_TYPE))
		? org.getParentOrganisation().getOrganisationId()
		: orgId;
	Boolean isSysadmin = request.isUserInRole(Role.SYSADMIN);
	User requestor = service.getUserByLogin(request.getRemoteUser());
	Integer rootOrgId = service.getRootOrganisation().getOrganisationId();
	Boolean requestorHasRole = service.isUserInRole(requestor.getUserId(), orgIdOfCourse, Role.GROUP_MANAGER)
		|| (service.isUserInRole(requestor.getUserId(), orgIdOfCourse, Role.GROUP_ADMIN)
			&& !rootOrgId.equals(orgId))
		|| (service.isUserGlobalGroupAdmin() && !rootOrgId.equals(orgId));

	if (!(requestorHasRole || isSysadmin)) {
	    request.setAttribute("errorName", "UserRolesController");
	    request.setAttribute("errorMessage", messageService.getMessage("error.authorisation"));
	    return "error";
	}

	userRolesForm.setUserId(userId);
	userRolesForm.setOrgId(org.getOrganisationId());
	// screen display vars
	request.setAttribute("rolelist", service.filterRoles(rolelist, isSysadmin, orgType));
	request.setAttribute("login", user.getLogin());
	request.setAttribute("fullName", user.getFullName());
	request.setAttribute("orgName", org.getName());
	Organisation parentOrg = org.getParentOrganisation();
	if (parentOrg != null && !parentOrg.equals(service.getRootOrganisation())) {
	    request.setAttribute("pOrgId", parentOrg.getOrganisationId());
	    request.setAttribute("parentName", parentOrg.getName());
	}

	String[] roles = null;
	UserOrganisation uo = service.getUserOrganisation(userId, orgId);
	if (uo != null) {
	    Iterator iter2 = uo.getUserOrganisationRoles().iterator();
	    roles = new String[uo.getUserOrganisationRoles().size()];
	    int i = 0;
	    while (iter2.hasNext()) {
		UserOrganisationRole uor = (UserOrganisationRole) iter2.next();
		roles[i] = uor.getRole().getRoleId().toString();
		log.debug("got roleid: " + roles[i]);
		i++;
	    }
	} else {
	    errorMap.add("roles", messageService.getMessage("msg.add.to.org", org.getName()));
	}
	userRolesForm.setRoles(roles);
	return "userrole";
    }

}
