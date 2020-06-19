<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>
<c:set var="minNumChars"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_MINIMUM_CHARACTERS)%></c:set>
<c:set var="mustHaveUppercase"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_UPPERCASE)%></c:set>
<c:set var="mustHaveLowercase"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_LOWERCASE)%></c:set>
<c:set var="mustHaveNumerics"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_NUMERICS)%></c:set>
<c:set var="mustHaveSymbols"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_SYMBOLS)%></c:set>

<lams:html>
<lams:head>
	<c:if test="${not empty userForm.userId}">
		<c:set var="title"><fmt:message key="admin.user.edit"/></c:set>
	</c:if> 
	<c:if test="${empty userForm.userId}">
		<c:set var="title"><fmt:message key="admin.user.create"/></c:set>
	</c:if>

	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
	
	<script type="text/javascript" src="/lams/includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="/lams/includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="/lams/includes/javascript/jquery.validate.js"></script>
	<script type="text/javascript" src="/lams/includes/javascript/portrait.js"></script>
	<script type="text/javascript">
		var mustHaveUppercase = ${mustHaveUppercase},
		mustHaveNumerics  = ${mustHaveNumerics},
		mustHaveLowercase  = ${mustHaveLowercase},
		mustHaveSymbols   = ${mustHaveSymbols};
	
		$.validator.addMethod("pwcheck", function(value) {
		 return (!mustHaveUppercase || /[A-Z]/.test(value)) && // has uppercase letters 
		(!mustHaveNumerics || /\d/.test(value)) && // has a digit
		(!mustHaveLowercase || /[a-z]/.test(value)) && // has a lower case
		(!mustHaveSymbols || /[`~!@#$%^&*\(\)_\-+={}\[\]\\|:\;\"\'\<\>,.?\/]/.test(value)); //has symbols
		});
	
		$.validator.addMethod("charactersAllowed", function(value) {
			return /^[A-Za-z0-9\d`~!@#$%^&*\(\)_\-+={}\[\]\\|:\;\"\'\<\>,.?\/]*$/
					.test(value)
		});
	
		$.validator.addMethod("notEqualTo", function(value, element, param) {
			return this.optional(element) || value != param;
		}, "Please specify a different (non-default) value");

		$(function() {
			// Setup form validation 
			$("#userForm").validate({
								errorClass : 'text-danger form-text font-italic',
								//  validation rules
								rules : {
									login : {
										required: true,
										maxlength : 50
									},
									password : {
										required: true,
										minlength : <c:out value="${minNumChars}"/>,
										maxlength : 25,
										charactersAllowed : true,
										pwcheck : true
									},
									password2 : {
										equalTo : "#password"
									},
									firstName : {
										required : true
									},
									lastName : {
										required : true
									},
									email : {
										required: true,
										email: true
									},
									country : {
										required: true,
										notEqualTo: "0"
									}
								},
	
								// Specify the validation error messages
								messages : {
									login : {
										required: "<fmt:message key='error.login.required'/>"
									},
									password : {
										required : "<fmt:message key='error.password.empty'/>",
										minlength : "<fmt:message key='label.password.min.length'><fmt:param value='${minNumChars}'/></fmt:message>",
										maxlength : "<fmt:message key='label.password.max.length'/>",
										charactersAllowed : "<fmt:message key='label.password.symbols.allowed'/> ` ~ ! @ # $ % ^ & * ( ) _ - + = { } [ ] \ | : ; \" ' < > , . ? /",
										pwcheck : "<fmt:message key='label.password.restrictions'/>"
									},
									password2: {
										equalTo : "<fmt:message key='error.password.mismatch'/>"
									},
									firstName: {
										required: "<fmt:message key='error.firstname.required'/>"
									},
									lastName: {
										required: "<fmt:message key='error.lastname.required'/>"
									},
									email: {
										required: "<fmt:message key='error.email.required'/>",
										email: "<fmt:message key='error.valid.email.required'/>"
									},
									country: {
										required: "<fmt:message key='error.country.required'/>",
										notEqualTo: "<fmt:message key='error.country.required'/>"
									}
								},
	
								submitHandler : function(form) {
									form.submit();
								}
			});
	
		});
		
		<c:if test="${not empty userForm.userId}">
		$(document).ready(function(){
			var portraitId = '<c:out value="${userForm.initialPortraitId}" />';
			loadPortrait(portraitId);
		});
	
		function loadPortrait(portraitId) {
			$("#portraitPicture").removeClass();
			$("#portraitPicture").css('background-image','');
			addPortrait( $("#portraitPicture"), portraitId, 
					'<c:out value="${userForm.userId}" />', 'large', true, '/lams/' );
			<c:if test="${isSysadmin}">
			if ( portraitId.length > 0 )  {
				$("#portraitButton").css('display','block');
			} else {
				$("#portraitButton").css('display','none');
			}
			</c:if>
		}
		
		<c:if test="${isSysadmin}">
			function deletePortrait() {
				$("#portraitButton").css('display','none');
				
				$.ajax({
					url : '/lams/saveportrait/deletePortrait.do',
					data : { 	
						'userId': '<c:out value="${userForm.userId}" />' ,
					  	"<csrf:tokenname/>": "<csrf:tokenvalue/>"
					},
					type : 'POST',		
					success : function(response) {
						if ( response == 'deleted') {
							loadPortrait('');
						} else {
							alert("<fmt:message key='error.portrait.removal.failed'/>");
						}
					}
				});
			}
		</c:if>
		</c:if>
	</script>
</lams:head>
    
<body class="stripes">
	
	<%-- Build breadcrumb --%>
	<c:set var="breadcrumbItems"><lams:LAMSURL/>admin/sysadminstart.do | <fmt:message key="sysadmin.maintain" /></c:set>
	<c:set var="breadcrumbItems">${breadcrumbItems}, <lams:LAMSURL/>admin/orgmanage.do?org=1 | <fmt:message key="admin.course.manage" /></c:set>
	<c:set var="breadcrumbItems">${breadcrumbItems}, <lams:LAMSURL/>admin/usersearch.do | <fmt:message key="admin.user.management"/></c:set>
	<c:set var="breadcrumbItems">${breadcrumbItems}, . | <fmt:message key="admin.user.edit"/></c:set>	
	
	<lams:Page type="admin" title="${title}" formID="userForm" breadcrumbItems="${breadcrumbItems}">	
							
				<c:if test="${empty userForm.userId}">
					<lams:Alert type="info" id="passwordConditions" close="false">
					<fmt:message key='label.password.must.contain' />:
						<ul class="list-unstyled ml-2" style="line-height: 1.2">
							<li><span class="fa fa-check"  aria-hidden="true"></span> <fmt:message
									key='label.password.min.length'>
									<fmt:param value='${minNumChars}' />
								</fmt:message></li>
		
							<c:if test="${mustHaveUppercase}">
								<li><span class="fa fa-check" aria-hidden="true"></span> <fmt:message
										key='label.password.must.ucase' /></li>
							</c:if>
							<c:if test="${mustHaveLowercase}">
										<li><span class="fa fa-check aria-hidden="true""></span> <fmt:message
												key='label.password.must.lcase' /></li>
									</c:if>
		
							<c:if test="${mustHaveNumerics}">
								<li><span class="fa fa-check" aria-hidden="true"></span> <fmt:message
										key='label.password.must.number' /></li>
							</c:if>
		
		
							<c:if test="${mustHaveSymbols}">
								<li><span class="fa fa-check" aria-hidden="true"></span> <fmt:message
										key='label.password.must.symbol' /></li>
							</c:if>
						</ul>
					</lams:Alert>
				</c:if>
					
					<!--  Main panel. Do not show portrait area for new user. -->
					<c:if test="${not empty userForm.userId}">
				    <div class="row">
						<div class="col-md-5">
				    			<div class="text-center"><div id="portraitPicture" ></div></div>
							<c:if test="${isSysadmin}">
				    			<div id="portraitButton" class="text-center mt-2" style="display:none; margin-bottom: 5px;">
				    			<a href="#" onclick="javascript:deletePortrait();" class="btn btn-outline-secondary btn-sm"><fmt:message key="label.delete.portrait" /></a></div>
				    			</c:if>
							<div class="mt-3 text-center">
							<a href="<lams:LAMSURL/>admin/userChangePass.jsp?userId=${userForm.userId}&login=${userForm.login}" class="btn btn-outline-secondary btn-sm"><fmt:message key="admin.user.changePassword" /></a>
							</div>
				    			
				    	</div>
					<div class="col-md-6">
					
					</c:if>
					<c:if test="${empty userForm.userId}">
				    <div class="row justify-content-center">
					<div class="col-10 col-md-6">
					</c:if>

					<form:form id="userForm" action="../usersave/saveUserDetails.do" modelAttribute="userForm" method="post">
								<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
								<form:hidden path="orgId" />
								<form:hidden path="userId" />						
					
						<div class="form-group">
							<label for="login">
								<fmt:message key="admin.user.login" />:&nbsp;<span class="text-danger">*</span>
							</label>
								<input id="login" name="login" class="form-control form-control-sm" autocomplete="username" type="text" value="${userForm.login}" maxlength="50" required>
							<small id="loginHelpBlock" class="form-text text-muted">
								<lams:errors path="login"/>
							</small>		
						</div>	
					
						<c:if test="${empty userForm.userId}">

							<div class="form-group">
								<label for="password">
									<fmt:message key="admin.user.password" />:&nbsp;<span class="text-danger">*</span>
								</label>
									<input id="password" name="password" class="form-control form-control-sm" type="password" value="${userForm.password}" maxlength="25" autocomplete="new-password" required>	
								<small id="passwordHelpBlock" class="form-text text-muted">
									<lams:errors path="password"/>
								</small>		
							</div>
							
						
							<div class="form-group">
								<label for="password2">
									<fmt:message key="admin.user.password.confirm" />:&nbsp;<span class="text-danger">*</span>
								</label>
								<input id="password2" name="password2" class="form-control form-control-sm" type="password" value="${userForm.password2}" maxlength="25" autocomplete="new-password" required>
							
							</div>
						</c:if>
						
						<div class="form-group">
							<label for="authenticationMethodId">
								<fmt:message key="admin.user.authentication.method" />:
							</label>
							<form:select id="authenticationMethodId" path="authenticationMethodId"
								cssClass="form-control form-control-sm">
								<c:forEach items="${authenticationMethods}" var="method">
									<form:option value="${method.authenticationMethodId}">
										<c:out value="${method.authenticationMethodName}" />
									</form:option>
								</c:forEach>
							</form:select>
						</div>						
					
						<div class="form-group">
							<label for="title">
								<fmt:message key="admin.user.title" />:
							</label>
								<input id="title" name="title" class="form-control form-control-sm" type="text" value="${userForm.title}" size="32" maxlength="32" autocomplete=honorific-prefix">
						</div>	
						
						<div class="form-group">
							<label for="firstName">
								<fmt:message key="admin.user.first_name" />:&nbsp;<span class="text-danger">*</span>
							</label>
								<input id="firstName" name="firstName" required class="form-control form-control-sm" type="text" value="${userForm.firstName}" maxlength="128" autocomplete="given-name">
							<small id="firstNameHelpBlock" class="form-text text-muted">
								<lams:errors path="firstName"/>
							</small>	
						</div>	
									
						<div class="form-group">
							<label for="lastName">
								<fmt:message key="admin.user.last_name" />:&nbsp;<span class="text-danger">*</span>
							</label>
								<input id="lastName" name="lastName" class="form-control form-control-sm" type="text" value="${userForm.lastName}" required maxlength="128" autocomplete="family-name">
							<small id="lastNameHelpBlock" class="form-text text-muted">
								<lams:errors path="lastName"/>
							</small>	
						</div>	

						<div class="form-group">
							<label for="email">
								<fmt:message key="admin.user.email" />:&nbsp;<span class="text-danger">*</span>
							</label>
								<input id="email" name="email" class="form-control form-control-sm" type="text" value="${userForm.email}" maxlength="128" required autocomplete="email">
							<small id="emailHelpBlock" class="form-text text-muted">
								<lams:errors path="email"/>
							</small>	
						</div>	


						<div class="form-group">
							<label for="addressLine1">
								<fmt:message key="admin.user.address_line_1" />:
							</label>
								<input id="addressLine1" name="addressLine1" class="form-control form-control-sm" type="text" value="${userForm.addressLine1}" maxlength="64" autocomplete="address-line1">
						</div>
						
						
						<div class="form-group">
							<label for="addressLine2">
								<fmt:message key="admin.user.address_line_2" />:
							</label>	
								<input id="addressLine2" name="addressLine2" class="form-control form-control-sm" type="text" value="${userForm.addressLine2}" maxlength="64" autocomplete="address-line2">
										
						</div>
						
						<div class="form-group">
							<label for="addressLine3">
								<fmt:message key="admin.user.address_line_3" />:
							</label>	
								<input id="addressLine3" name="addressLine3" class="form-control form-control-sm" type="text" value="${userForm.addressLine3}" maxlength="64" autocomplete="address-line3">
										
						</div>
						
						<div class="form-group">
							<label for="city">
								<fmt:message key="admin.user.city" />:
							</label>	
								<input id="city" name="city" class="form-control form-control-sm" type="text" value="${userForm.city}" maxlength="64" autocomplete="address=line4">

										
						</div>

						<div class="form-group">
							<label for="postcode">
								<fmt:message key="admin.user.postcode" />:
							</label>	
								<input id="postcode" name="postcode" class="form-control form-control-sm" type="text" value="${userForm.postcode}" value="${userForm.postcode}" maxlength="10">
										
						</div>

						<div class="form-group">
							<label for="state">
								<fmt:message key="admin.user.state" />:
							</label>	
							<form:input path="state" 
								maxlength="64" cssClass="form-control form-control-sm"/>
										
						</div>
						
						<div class="form-group">
							<label for="country">
								<fmt:message key="admin.user.country" />:&nbsp;<span class="text-danger">*</span>
							</label>	
							<form:select path="country" cssClass="form-control form-control-sm">
								<form:option value="0"><fmt:message key="label.select.country" /></form:option>
								<c:forEach items="${countryCodes}" var="countryCode">
									<form:option value="${countryCode.key}">
										${countryCode.value}
									</form:option>
								</c:forEach>
							</form:select>
						</div>
						
						<div class="form-group">
							<label for="dayPhone">
								<fmt:message key="admin.user.day_phone" />:
							</label>	
								<input id="dayPhone" name="dayPhone" class="form-control form-control-sm" type="text" value="${userForm.dayPhone}" maxlength="64" autocomplete="tel">
						</div>

						<div class="form-group">
							<label for="eveningPhone">
								<fmt:message key="admin.user.evening_phone" />:
							</label>	
								<input id="eveningPhone" name="eveningPhone" class="form-control form-control-sm" type="text" value="${userForm.eveningPhone}" maxlength="64" autocomplete="tel">
						</div>

						<div class="form-group">
							<label for="mobilePhone">
								<fmt:message key="admin.user.mobile_phone" />:
							</label>	
								<input id="mobilePhone" name="mobilePhone" class="form-control form-control-sm" type="text" value="${userForm.mobilePhone}" maxlength="64" autocomplete="tel">
						</div>
																								
						<div class="form-group">
							<label for="localeId">
								<fmt:message key="admin.organisation.locale" />:
							</label>	
							<form:select path="localeId" cssClass="form-control form-control-sm">
								<c:forEach items="${locales}" var="locale">
									<form:option value="${locale.localeId}">
										<c:out value="${locale.description}" />
									</form:option>
								</c:forEach>
							</form:select>
						</div>
						
						<div class="form-group">
							<label for="timeZone">
								<fmt:message key="admin.user.time.zone" />:
							</label>
							<form:select path="timeZone" cssClass="form-control form-control-sm">
								<c:forEach items="${timezoneDtos}" var="timezoneDto">
									<form:option value="${timezoneDto.timeZoneId}">
										${timezoneDto.timeZoneId} - ${timezoneDto.displayName}
									</form:option>
								</c:forEach>
							</form:select>
						</div>
						
						<div class="form-group">
							<label for="userTheme">
								<fmt:message key="label.theme" />:
							</label>
							<form:select path="userTheme" class="form-control form-control-sm">
								<c:forEach items="${themes}" var="theme">
									<form:option value="${theme.themeId}">${theme.name}</form:option>
								</c:forEach>
							</form:select>
						</div>
						
						<div class="form-group form-check ml-3">
							<form:checkbox cssClass="form-check-input" path="changePassword" value="true" id="changePassword"  />
							<label class="form-check-label"  for="changePassword">
										<fmt:message key="admin.user.change.password" />
							</label>
						</div>
												
						
						<c:if test="${isSysadmin}">
							<div class="form-group form-check ml-3">
							<form:checkbox cssClass="form-check-input" id="twoFactorAuthenticationEnabled" path="twoFactorAuthenticationEnabled" value="true"  />
							<label class="form-check-label"  for="twoFactorAuthenticationEnabled">
								<fmt:message key="label.2FA.property.enable" /> 
							</label>:							
							</div>
						</c:if>	
						
						<c:if test="${not empty createDate}">
							<div class="form-group">
								<label>
										<fmt:message key="admin.user.create.date" />
									</label>: <lams:Date value="${createDate}"/>
							</div> 
						</c:if>

					
					<div class="row">
					<div class="col-12">
					<hr>
						<div class="pull-right mb-3">
							<a href="<lams:LAMSURL/>admin/usersearch.do" class="btn btn-outline-secondary btn-sm"> <fmt:message key="admin.cancel" /> </a>
							<input type="submit" id="saveButton" class="btn btn-primary btn-sm loffset5" value="<fmt:message key="admin.save" />" />
						</div>
					</div>
					</div>
				</form:form>
				
					</div>
					</div>
				<c:if test="${not empty globalRoles || not empty userOrgRoles}">
			
				<div class="row">
					<div class="col-md-6 offset-md-3">
					<h2 class="mt-3"><fmt:message key="admin.user.roles" /></h2>


						<c:if test="${not empty globalRoles}">
							<div class="ml-2">
								<h3><fmt:message key="label.global.roles" /></h3>
									<ul style="text-indent: 2em;">
										<c:forEach var="role" items="${globalRoles.roles}">
											<li><fmt:message>role.<lams:role role="${role}" />
											</fmt:message></li>
										</c:forEach>
									</ul>
							</div>	
						</c:if>

			
			
						<c:if test="${not empty userOrgRoles}">
							<div class="ml-2">
								<h3><fmt:message key="admin.organisation" /></h3>
						
								<table id="tableRoles" class="table table-striped table-bordered">
									<thead class="thead-light">
										<tr>
											<th><fmt:message key="label.member.of" />:</th>
											<th><fmt:message key="label.with.roles" />:</th>
										</tr>
									</thead>
									<tbody>
									<c:forEach var="userOrgRole" items="${userOrgRoles}">
										<tr>
											<td><c:out value="${userOrgRole.orgName}" /></td>
											<td><c:forEach var="role" items="${userOrgRole.roles}">
													<fmt:message>role.<lams:role role="${role}" />
													</fmt:message>&nbsp;
												</c:forEach>
											</td>
										</tr>
										
										<c:if test="${not empty userOrgRole.childDTOs}">
											<c:forEach var="child" items="${userOrgRole.childDTOs}">
												<tr>
													<td><span class="ml-3 font-italic"><c:out
															value="${child.orgName}"/></span></td>
													<td><c:forEach var="role" items="${child.roles}">
															<fmt:message>role.<lams:role role="${role}" />
															</fmt:message>&nbsp;
														</c:forEach>
													</td>
												</tr>
											</c:forEach>
										</c:if>
									</c:forEach>
									</tbody>
								</table>
							</div>
						</c:if>
					</div>
					</div>	
				</c:if>
			

	</lams:Page>
</body>
</lams:html>
