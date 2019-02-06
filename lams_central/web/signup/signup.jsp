<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %> 
<!DOCTYPE html>
<lams:html>
<lams:head>
	<title><fmt:message key="title.lams.signup" /></title>
	
	<lams:css/>
	<link rel="icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<link type="text/css" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" rel="stylesheet" />

	<script type="text/javascript" src="/lams/includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="/lams/includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="/lams/includes/javascript/jquery.validate.js"></script>
	<script type="text/javascript" src="/lams/includes/javascript/bootstrap-material-design.min.js"></script>
	<script type="text/javascript">
		jQuery(document).ready(function(){
			var selectedTab = (${(signupOrganisation != null) && signupOrganisation.loginTabActive}) ? 1 : 0;
			$("#tabs").tabs({
				selected: selectedTab
			});
		});
	</script>
</lams:head>

<body class="stripes">
	<lams:Page type="admin" formID="SignupForm">
		<div class="page-header">
			<p class="text-center">
				<img src="<lams:LAMSURL/>/images/svg/lams_logo_black.svg"
					alt="LAMS - Learning Activity Management System" width="200px"></img>
			</p>
			<c:if test="${not empty signupOrganisation}">
				<h1 align="center">
					<c:out value="${signupOrganisation.organisation.name}" />
					<c:if test="${not empty signupOrganisation.organisation.code}">
					(<c:out value="${signupOrganisation.organisation.code}" />)
				</c:if>
				</h1>
				<c:if test="${not empty signupOrganisation.blurb}">
					<div id="signup-intro" class="card card-plain">
						<c:out value="${signupOrganisation.blurb}" escapeXml="false" />
					</div>
				</c:if>
			</c:if>

            <div class="alert alert-info">
                <ul class="fa-ul">
                    <li><i class="fa fa-info-circle fa-li fa-lg"></i>
                        <fmt:message key="register.if.you.want.to.signup" />
                    </li>
                    <c:if test="${not empty signupOrganisation and signupOrganisation.emailVerify}">
	                     <li>
	                        <fmt:message key="register.email.verify" />
	                    </li>
                    </c:if>
                </ul>
            </div>    

			<c:if test="${not empty error}">
				<lams:Alert type="danger" id="errors" close="false">
					<c:out value="${error}" />
				</lams:Alert>
			</c:if>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="card with-nav-tabs card-plain">
					<div class="card-header" style="height: 51px">
						<ul class="nav nav-tabs">
							<li class="active"><a href="#tab1default" data-toggle="tab"><fmt:message
										key="register.signup.to.lams" /></a></li>
							<li><a href="#tab2default" data-toggle="tab"><fmt:message
										key="register.login" /></a></li>
						</ul>
					</div>
					<div class="card-body">
						<div class="tab-content">
							<div class="tab-pane fade in active" id="tab1default">
								<p>
									<%@ include file="singupTab.jsp"%>
								</p>
							</div>
							<div class="tab-pane fade" id="tab2default">
								<p>
									<%@ include file="loginTab.jsp"%>
								</p>
							</div>

						</div>
					</div>
				</div>
			</div>
		</div>
	</lams:Page>
</body>
</lams:html>
