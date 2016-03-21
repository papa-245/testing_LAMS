<%@page import="org.springframework.web.context.request.SessionScope"%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>
<%@ page import="org.lamsfoundation.lams.web.session.SessionManager"%>
<%@ page import="org.lamsfoundation.lams.usermanagement.dto.UserDTO"%>

<c:if test="${empty requestScope.login}">
	<c:set var="login" value="${sessionScope.login}" />
	<c:set var="password" value="${sessionScope.password}" />
</c:if>

<!DOCTYPE html>
<lams:html>

<%-- If login param is empty, this is a regular, manual login page.
	 Otherwise it is a just an almost empty redirect page for integrations and LoginAs authentication.
 --%>
<c:choose>
	<c:when test="${empty login}">
		<lams:head>
			<title><fmt:message key="title.login.window" /></title>
			<lams:css style="core" />
			<link rel="icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
			<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
			<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/browser_detect.js"></script>
			<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>
			<script type="text/javascript">
				function submitForm() {
					$('#loginForm').submit();
				}

				function onEnter(event) {
					intKeyCode = event.keyCode;
					if (intKeyCode == 13) {
						submitForm();
					}
				}
				
				function isBrowserCompatible() {
					return Modernizr.atobbtoa && Modernizr.checked && Modernizr.cookies && Modernizr.nthchild && Modernizr.opacity &&
						   Modernizr.svg && Modernizr.todataurlpng && Modernizr.websockets && Modernizr.xhrresponsetypetext;
					// Modernizr.datauri - should be included, it's a async test though
					// Modernizr.time - should be included, fails in Chrome for an unknown reason (reported)
					// Modernizr.xhrresponsetypejson - should be included, fails in IE 11 for an unknown reason (reported)
				}

				$(document).ready(function() {
					if (!isBrowserCompatible()) {
						$('#browserNotCompatible').show();
					}
					$('#j_username').focus();
					$('#news').load('<lams:LAMSURL/>www/news.html');
				});
			</script>
		</lams:head>

		<body class="stripes">
			<div id="login-page">
				<!--main box 'page'-->
				<h1 class="no-tabs-below">&nbsp;</h1>

				<div id="login-header"></div>
				<!--closes header-->

				<div id="login-content">
					<div id="login-left-col">
						<h1>
							<img src="<lams:LAMSURL/>/www/images/lams_login.gif" alt="LAMS - Learning Activity Management System" />
						</h1>
						
						<div id="browserNotCompatible" class="warning" style="display: none">
							<fmt:message key="msg.browser.compat"/>
						</div>

						<!-- Placeholder for customised login page part -->
						<div id="news"></div>
					</div>
					<!--closes left col-->

					<div id="login-right-col">
						<p class="version">
							<fmt:message key="msg.LAMS.version" />
							<%=Configuration.get(ConfigurationKeys.VERSION)%></p>
						<h2>
							<fmt:message key="button.login" />
						</h2>
						<form action="j_security_check" method="POST" name="loginForm" id="loginForm">
							<c:if test="${!empty param.failed}">
								<div class="warning-login">
									<fmt:message key="error.login" />
								</div>
							</c:if>

							<input type="hidden" name="redirectURL" value='<c:out value="${param.redirectURL}" escapeXml="true" />' />

							<p class="first">
								<fmt:message key="label.username" />
								: <input id="j_username" name="j_username" type="text" size="16" style="width: 125px" tabindex="1"
									value="${login}" onkeypress="onEnter(event)" />
							</p>

							<p>
								<fmt:message key="label.password" />
								: <input id="j_password" name="j_password" type="password" size="16" style="width: 125px" autocomplete="off"
									tabindex="2" value="${password}" onkeypress="onEnter(event)" />
							</p>

							<p class="login-button">
								<a id="loginButton" href="javascript:submitForm()" class="button" tabindex="3" />
								<fmt:message key="button.login" />
								</a>
							</p>
						</form>
						<p class="login-button">
							<a href="<lams:LAMSURL/>forgotPassword.jsp"> <fmt:message key="label.forgot.password" /></a>
							<br />
							<a href="<lams:LAMSURL/>/www/help/troubleshoot-<%=Configuration.get(ConfigurationKeys.SERVER_LANGUAGE)%>.pdf">
								<fmt:message key="label.help" />
							</a>
						</p>
					</div>
					<!--closes right col-->

					<div class="clear"></div>
					<!-- forces the CSS to display the columns-->
				</div>
				<!--closes content-->

				<div id="footer">
					<p>
						<fmt:message key="msg.LAMS.version" />
						<%=Configuration.get(ConfigurationKeys.VERSION)%>
						<a href="<lams:LAMSURL/>/www/copyright.jsp" target='copyright' onClick="openCopyRight()"> &copy; <fmt:message
								key="msg.LAMS.copyright.short" />
						</a>
					</p>
				</div>
				<!--closes footer-->
			</div>
			<!--closes page-->
		</body>
	</c:when>

	<%-- This is version for integrations and LoginAs authentication. --%>

	<c:otherwise>
		<lams:head>
			<lams:css />
			<link rel="icon" href="<lams:LAMSURL/>favicon.ico" type="image/x-icon" />
			<link rel="shortcut icon" href="<lams:LAMSURL/>favicon.ico" type="image/x-icon" />
		</lams:head>
		<body class="stripes">
			<!-- A bit of content so the page is not completely blank -->
			<lams:Page type="admin">

				<div class="text-center" style="margin-top: 20px; margin-bottom: 20px;">
					<i class="fa fa-2x fa-refresh fa-spin text-primary"></i>
					<h4>
						<fmt:message key="msg.loading" />
					</h4>
				</div>


				<form style="display: none" method="POST" action="j_security_check">
					<input type="hidden" name="j_username" value="${login}" /> <input type="hidden" name="j_password"
						value="${password}" /> <input type="hidden" name="redirectURL"
						value='<c:out value="${param.redirectURL}" escapeXml="true" />' />
				</form>
				<div id="footer"></div>
			</lams:Page>
			<%
				// invalidate session so a new user can be logged in
				HttpSession hs = SessionManager.getSession();
				if (hs != null) {
					UserDTO userDTO = (UserDTO) hs.getAttribute("user");
					if (userDTO != null) {
						SessionManager.removeSession(userDTO.getLogin(), true);
					}
				}
			%>
			<script type="text/javascript">
				// submit the hidden form
				document.forms[0].submit();
			</script>
		</body>
	</c:otherwise>
</c:choose>

</lams:html>