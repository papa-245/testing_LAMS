<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>

<%-- param has higher level for request attribute --%>
<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:choose>
	<c:when test="${sessionMap.mode == 'learner' || sessionMap.mode == 'author'}">
		<!-- ordinary learner or preview -->
		<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE)%></c:set>
	</c:when>
	<c:otherwise>
		<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)%></c:set>
	</c:otherwise>
</c:choose>

<lams:html>
<lams:head>
	<title>
		<fmt:message key="label.learning.title" />
	</title>
	<%@ include file="/common/header.jsp"%>

	<c:set var="mode" value="${sessionMap.mode}" />
	<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
	<c:set var="imageGallery" value="${sessionMap.imageGallery}" />
	<c:set var="finishedLock" value="${sessionMap.finishedLock}" />
	<c:set var="mediumImageDimensions" value="${sessionMap.mediumImageDimensions}" />
	<c:set var="thumbnailImageDimensions" value="${empty sessionMap.thumbnailImageDimensions ? 100 : sessionMap.thumbnailImageDimensions}" />

	<link rel="stylesheet" type="text/css" href="<lams:WebAppURL/>/includes/css/fotorama.css"/>
	<link href="<lams:WebAppURL/>includes/css/imageGallery.css" rel="stylesheet" type="text/css">
	
    <lams:css suffix="jquery.jRating"/>
	<link rel="stylesheet" type="text/css" href="${lams}css/jquery.tablesorter.theme-blue.css">
	<link rel="stylesheet" type="text/css" href="${lams}css/jquery.tablesorter.pager.css">
    
    <script type="text/javascript">
		<%-- used for  imageGalleryitem.js --%>
		var saveMultipleImagesUrl = "<c:url value='/learning/saveMultipleImages.do'/>";
		var UPLOAD_FILE_LARGE_MAX_SIZE = "${UPLOAD_FILE_MAX_SIZE}";
		var LABEL_ITEM_BLANK = '<fmt:message key="error.resource.item.file.blank"/>';
		var LABEL_MAX_FILE_SIZE = '<fmt:message key="errors.maxfilesize"><param>{0}</param></fmt:message>';
		var LABEL_NOT_ALLOWED_FORMAT = '<fmt:message key="error.resource.image.not.alowed.format"/>';
	</script>
	<script type="text/javascript" src="<lams:WebAppURL/>includes/javascript/imageGalleryitem.js"></script>
	<script type="text/javascript" src="<lams:WebAppURL/>includes/javascript/uploadImageLearning.js"></script>
    <script type="text/javascript" src="${lams}includes/javascript/upload.js"></script>
 	<script type="text/javascript" src="${lams}includes/javascript/jquery.form.js"></script>
 	<script type="text/javascript" src="<lams:WebAppURL/>includes/javascript/fotorama.js"></script>
	<script type="text/javascript">
	
		$(document).ready(function(){ 
			$('.fotorama')
				.on('fotorama:show ',
					function (e, fotorama, extra) {
					    loadImageData(fotorama.activeFrame.id);
					}
				)
				.fotorama({
					//TODO history   : true,
					width: ${mediumImageDimensions + 60},
					height: Math.round(${mediumImageDimensions + 60}*9/16),
					maxwidth: '100%',
					//ratio: 16/9,
					allowfullscreen: true,
					keyboard: true,
					nav: 'thumbs',
					fit: 'scaledown',
					//thumbfit: 'contain',
					loop: true,
					thumbwidth: 80,//${thumbnailImageDimensions},
					thumbheight: 80,//${thumbnailImageDimensions},
				    data: [
						<c:forEach var="image" items="${sessionMap.imageGalleryList}" varStatus="status">
							<c:set var="description"><div><span class="caption-description">${image.descriptionEscaped}</span></c:set>
							<c:if test="${!image.createByAuthor && (image.createBy != null)}">
								<c:set var="portrait"><div class="roffset5"><lams:Portrait userId="${image.createBy.userId}"/></div></c:set>
								<c:set var="portrait">${fn:replace(portrait, "'", "\\'")}</c:set> <%-- escape the url --%>
								<c:set var="imageAddedBy"><div class="caption-description"><fmt:message key="label.learning.added.by" />&nbsp;<c:out value="${image.createBy.firstName} ${image.createBy.lastName}" escapeXml="true"/></div></c:set>
								<c:set var="description"><table><tr><td>${portrait}</td><td>${description}${imageAddedBy}</td></tr></table></c:set>
							</c:if>					
						
							{
								img: "<lams:WebAppURL />download/?uuid="+${image.mediumFileUuid}+"&preferDownload="+false,
								//thumb: ""<lams:WebAppURL />download/?uuid="+${image.thumbnailFileUuid}+"&preferDownload="+false,
								full: "<lams:WebAppURL />download/?uuid="+${image.originalFileUuid}+"&preferDownload="+false,
								id: '${image.uid}', // Custom anchor is used with the hash:true option.
								caption: '<div class="caption-heading">${image.titleEscaped}</div>'
									+ '${description}'
									+'</div>',
								//html: $('selector'), // ...or '<div>123</div>'. Custom HTML inside the frame.
								//fit: 'cover' // Override the global fit option.
								//any: 'Any data relative to the frame you want to store'
							},
						</c:forEach>
					]
				}
			);
	
		});

		function checkNew(){
			document.location.href = "<c:url value="/learning/start.do"/>?mode=${mode}&toolSessionID=${toolSessionID}";
 		    return false;
		}
		function finishSession(){
			$('#finishButton').attr('disabled', true);
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}"/>';
			return false;
		}
		function continueReflect() {
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}
		
		function deleteImage(imageUid) {
			var	deletionConfirmed = confirm("<fmt:message key="warning.msg.authoring.do.you.want.to.delete"></fmt:message>");
			
			if (deletionConfirmed) {
				document.location.href = "<c:url value="/learning/deleteImage.do"/>?sessionMapID=${sessionMapID}&imageUid=" + imageUid;
			}
			return false;
		}

		function loadImageData(imageUid) {
			$("#image-info").load(
				"<c:url value="/learning/loadImageData.do"/>",
				{
					imageUid: imageUid, 
					sessionMapID: "${sessionMapID}"
				}
			);
		}
				
    </script>
</lams:head>
<body class="stripes">
	 <lams:Page type="learner" title="${imageGallery.title}">

		<%--Advanced settings and notices-----------------------------------%>

		<div class="card card-plain">
			<c:out value="${imageGallery.instructions}" escapeXml="false"/>
		</div>
		
		<c:if test="${sessionMap.lockOnFinish and mode != 'teacher'}">
			<lams:Alert type="danger" id="lock-on-finish" close="false">
				<c:choose>
					<c:when test="${sessionMap.userFinished}">
						<fmt:message key="message.activityLocked" />
					</c:when>
					<c:otherwise>
						<fmt:message key="message.warnLockOnFinish" />
					</c:otherwise>
				</c:choose>
			</lams:Alert>
		</c:if>
		
		<c:if test="${imageGallery.allowRank && (imageGallery.minimumRates ne 0 || imageGallery.maximumRates ne 0)}">
		
			<lams:Alert type="info" id="rating-limits" close="false">
				<c:choose>
					<c:when test="${imageGallery.minimumRates ne 0 and imageGallery.maximumRates ne 0}">
						<fmt:message key="label.rate.limits.reminder">
							<fmt:param value="${imageGallery.minimumRates}"/>
							<fmt:param value="${imageGallery.maximumRates}"/>
						</fmt:message>
					</c:when>
					
					<c:when test="${imageGallery.minimumRates ne 0 and imageGallery.maximumRates eq 0}">
						<fmt:message key="label.rate.limits.reminder.min">
							<fmt:param value="${imageGallery.minimumRates}"/>
						</fmt:message>
					</c:when>
					
					<c:when test="${imageGallery.minimumRates eq 0 and imageGallery.maximumRates ne 0}">
						<fmt:message key="label.rate.limits.reminder.max">
							<fmt:param value="${imageGallery.maximumRates}"/>
						</fmt:message>
					</c:when>
				</c:choose>
				<br>
						
				<fmt:message key="label.rate.limits.topic.reminder">
					<fmt:param value="<span id='count-rated-items'>${sessionMap.countRatedItems}</span>"/>
				</fmt:message>
			</lams:Alert>
			
		</c:if>
		
		<lams:errors/>
		
		<%--Main image---------------------------------------------------%>
		
		<div class="fotorama-container">
			<div class="fotorama"></div>
		</div>
			
		<%--Comments & Ranking/Voting area----------------------------------------------%>	
	 	
		<div id="image-info">
			<%@ include file="/pages/learning/parts/commentsarea.jsp"%>
		</div>
 
		<%--Reflection--------------------------------------------------%>

		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn}">
			<div class="card card-plain voffset10">
				<div class="card-header card-title">
					<fmt:message key="title.reflection" />
				</div>
						
		 		<div class="card-body">
		 			<div class="reflectionInstructions">
						<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true"/>
					</div>
				
					<div class="card card-plain">
						<c:choose>
							<c:when test="${empty sessionMap.reflectEntry}">
								<p>
									<em> 
										<fmt:message key="message.no.reflection.available" />
									</em>
								</p>
							</c:when>
							<c:otherwise>
								<p>
									<lams:out escapeHtml="true" value="${sessionMap.reflectEntry}" />
								</p>
							</c:otherwise>
						</c:choose>
					</div>

					<c:if test="${mode != 'teacher'}">
						<button name="FinishButton" onclick="return continueReflect()" class="btn btn-default pull-left">
							<fmt:message key="label.edit" />
						</button>
					</c:if>
				</div>
			</div>
		</c:if>
		
		<%--Bottom buttons-------------------------------------------%>

		<c:if test="${mode != 'teacher'}">
			<div id="learner-submit" class="voffset10"
				<c:if test="${imageGallery.minimumRates ne 0 && empty sessionMap.currentImage}">style="display:none;"</c:if>
			>
				<c:choose>
					<c:when	test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
						<button name="FinishButton" onclick="return continueReflect()" class="btn btn-primary pull-right" >
							<fmt:message key="label.continue" />
						</button>
					</c:when>
					<c:otherwise>
						<a href="#nogo" name="FinishButton" id="finishButton"	onclick="return finishSession()" class="btn btn-primary pull-right" >
							<span class="na">
								<c:choose>
				 					<c:when test="${sessionMap.isLastActivity}">
				 						<fmt:message key="label.submit" />
				 					</c:when>
				 					<c:otherwise>
				 		 				<fmt:message key="label.finished" />
				 					</c:otherwise>
				 				</c:choose>
							</span>
						</a>
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>	

	</lams:Page>
	<%--closes lams:Page--%>

	<div id="footer"></div>
	<%--closes footer--%>

</body>
</lams:html>
