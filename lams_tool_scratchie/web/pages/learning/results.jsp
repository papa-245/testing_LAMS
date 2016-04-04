<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<%-- param has higher level for request attribute --%>
<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
<c:set var="scratchie" value="${sessionMap.scratchie}" />
<c:set var="isUserLeader" value="${sessionMap.isUserLeader}" />

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
	
	<link type="text/css" href="${lams}css/jquery-ui-redmond-theme.css" rel="stylesheet">
	<link type="text/css" href="${lams}css/jquery.jqGrid.css" rel="stylesheet" />
	<link rel="stylesheet" href="<lams:LAMSURL />/includes/font-awesome/css/font-awesome.min.css">
	<style media="screen,projection" type="text/css">
		#reflections-div {
			padding: 10px 0 20px;
		}
		.burning-question-dto {
			padding-bottom: 5px; 
		}
		.ui-jqgrid tr.jqgrow td {
		    white-space: normal !important;
		    height:auto;
		    vertical-align:text-top;
		    padding-top:2px;
		}
		.ui-jqgrid tr.jqgrow td {vertical-align:middle !important}
	</style>

 	<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.locale-en.js"></script>
 	<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.js"></script>
	<script type="text/javascript">
		function likeEntry(burningQuestionUid) {

			var isLike = $( '#like-'+burningQuestionUid ).hasClass( 'fa-thumbs-o-up' );

			if (isLike) {
				$.ajax({
				    url: '<c:url value="/learning/like.do"/>',
					data: {
						sessionMapID: "${sessionMapID}",
						burningQuestionUid: burningQuestionUid
					}
				})
			    .done(function (response) {	       		
		    		if ( ! burningQuestionUid ) {
						alert('<fmt:message key="error.cannot.redisplay.please.refresh"/>');
		  			} else if ( response.added ) {
		  				var currentCount = eval($('#count-'+burningQuestionUid).html());
		  				currentCount += 1;
			       		$('#count-'+burningQuestionUid).html(currentCount);
					}
				});
				
				$( '#like-'+burningQuestionUid ).removeClass( 'fa-thumbs-o-up' ).addClass( 'fa-thumbs-up' );
				
			} else {
				$.ajax({
				    url: '<c:url value="/learning/removeLike.do"/>',
					data: {
						sessionMapID: "${sessionMapID}",
						burningQuestionUid: burningQuestionUid
					}
				})
			    .done(function (response) {
		    		if ( ! burningQuestionUid ) {
						alert('<fmt:message key="error.cannot.redisplay.please.refresh"/>');
		  			} else {
		  				var currentCount = eval($('#count-'+burningQuestionUid).html());
		  				currentCount -= 1;
			       		$('#count-'+burningQuestionUid).html(currentCount);
					}
				});
		
				$( '#like-'+burningQuestionUid ).removeClass( 'fa-thumbs-up' ).addClass( 'fa-thumbs-o-up' );
			}

		}
	
		$(document).ready(function(){
			
			<!-- Display burningQuestionItemDtos -->
			<c:forEach var="burningQuestionItemDto" items="${burningQuestionItemDtos}" varStatus="i">
				<c:set var="scratchieItem" value="${burningQuestionItemDto.scratchieItem}"/>
			
				jQuery("#burningQuestions${scratchieItem.uid}").jqGrid({
					datatype: "local",
					rowNum: 10000,
					height: 'auto',
					autowidth: true,
					shrinkToFit: false,
				   	colNames:[
						'#',
						"<fmt:message key='label.monitoring.summary.user.name' />",
						"<fmt:message key='label.burning.questions' />",
						"<fmt:message key='label.like' />",
						"<fmt:message key='label.count' />"
					],
				   	colModel:[
				   		{name:'id', index:'id', width:0, sorttype:"int", hidden: true},
				   		{name:'groupName', index:'groupName', width:200},
				   		{name:'feedback', index:'feedback', width:401},
				   		{name:'like', index:'like', width:60, align: "center"},
				   		{name:'count', index:'count', width:50, align:"right"}
				   	],
				   	caption: "${scratchieItem.title}"
				});
				
			    <c:forEach var="burningQuestionDto" items="${burningQuestionItemDto.burningQuestionDtos}" varStatus="i">			    
			    	jQuery("#burningQuestions${scratchieItem.uid}").addRowData(${i.index + 1}, {
			   			id:"${i.index + 1}",
			   	     	groupName:"${burningQuestionDto.sessionName}",
				   	    feedback:"<lams:out value='${burningQuestionDto.escapedBurningQuestion}' escapeHtml='true' />",
				   	 	<c:choose>
				   			<c:when test="${!isUserLeader && burningQuestionDto.userLikeUid != null}">
				   				like:'<span class="fa fa-thumbs-up fa-2x"></span>',
				   			</c:when>
					   		<c:when test="${!isUserLeader}">
				   				like:'',
				   			</c:when>
							<c:when test="${burningQuestionDto.userLikeUid != null}">
								like:'<span class="fa fa-thumbs-up fa-2x" title="<fmt:message key="label.unlike"/>"' +
										'onclick="javascript:likeEntry(${burningQuestionDto.burningQuestion.uid});" id="like-${burningQuestionDto.burningQuestion.uid}" />',
							</c:when>
							<c:otherwise>
								like:'<span class="fa fa-thumbs-o-up fa-2x" title="<fmt:message key="label.like"/>"' +
										'onclick="javascript:likeEntry(${burningQuestionDto.burningQuestion.uid});" id="like-${burningQuestionDto.burningQuestion.uid}" />',
							</c:otherwise>
						</c:choose>
				   	 	count:'<span id="count-${burningQuestionDto.burningQuestion.uid}">${burningQuestionDto.likeCount}</span>'
			   	   	});
		        </c:forEach>

		        jQuery("#burningQuestions${scratchieItem.uid}").jqGrid('sortGrid','groupName', false, 'asc');
	        </c:forEach>
			
			<!-- Display reflection entries -->
			jQuery("#reflections").jqGrid({
				datatype: "local",
				rowNum: 10000,
				height: 'auto',
				autowidth: true,
				shrinkToFit: false,
			   	colNames:['#',
						"<fmt:message key='label.monitoring.summary.user.name' />",
					    "<fmt:message key='label.learners.feedback' />"
				],
			   	colModel:[
			   		{name:'id', index:'id', width:0, sorttype:"int", hidden: true},
			   		{name:'groupName', index:'groupName', width:140},
			   		{name:'feedback', index:'feedback', width:568}
			   	],
			   	caption: "<fmt:message key='label.other.groups' />"
			});
		    <c:forEach var="reflectDTO" items="${reflections}" varStatus="i">
		    	jQuery("#reflections").addRowData(${i.index + 1}, {
		   			id:"${i.index + 1}",
		   	     	groupName:"${reflectDTO.groupName}",
			   	    feedback:"<lams:out value='${reflectDTO.reflection}' escapeHtml='true' />"
		   	   	});
		    </c:forEach>
		    
			//jqgrid autowidth (http://stackoverflow.com/a/1610197)
			$(window).bind('resize', function() {
				var grid;
			    if (grid = jQuery(".ui-jqgrid-btable:visible")) {
			    	grid.each(function(index) {
			        	var gridId = $(this).attr('id');
			        	var gridParentWidth = jQuery('#gbox_' + gridId).parent().width();
			        	jQuery('#' + gridId).setGridWidth(gridParentWidth, true);
			    	});
			    }
			});
		    
		})
	
		function finishSession() {
			document.getElementById("finishButton").disabled = true;
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}"/>';
			return false;
		}
		function continueReflect() {
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}
		function editBurningQuestions() {
			document.location.href='<c:url value="/learning/showBurningQuestions.do?sessionMapID=${sessionMapID}"/>';
		}
		function refresh() {
			location.reload();
			return false;
		}
    </script>
</lams:head>

<body class="stripes">

	<div id="content">
		<h1>
			<c:out value="${scratchie.title}"/>
			
			<div class="space-bottom-top align-right">
				<html:button property="refreshButton" onclick="return refresh();" styleClass="button">
					<fmt:message key="label.refresh" />
				</html:button>
			</div>
		</h1>
		
		<c:if test="${not empty sessionMap.submissionDeadline}">
			<div class="info">
				<fmt:message key="authoring.info.teacher.set.restriction" >
					<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
				</fmt:message>
			</div>
		</c:if>

		<%@ include file="/common/messages.jsp"%>

		<h3>
			<fmt:message key="label.score" />
		</h3>
		
		<h3>
			<fmt:message key="label.you.ve.got" >
				<fmt:param>${score}%</fmt:param>
			</fmt:message>
		</h3>
		
		<!-- Display burningQuestionItemDtos -->

		<c:if test="${sessionMap.isBurningQuestionsEnabled}">
			<div class="small-space-top">
				<h3><fmt:message key="label.burning.questions" /></h3>
				
				<c:forEach var="burningQuestionItemDto" items="${burningQuestionItemDtos}" varStatus="i">
					<div class="burning-question-dto">
						<table id="burningQuestions${burningQuestionItemDto.scratchieItem.uid}" class="scroll" cellpadding="0" cellspacing="0"></table>
					</div>
				</c:forEach>
				
				<c:if test="${(mode != 'teacher') && isUserLeader}">
					<html:button property="finishButton" onclick="return editBurningQuestions()" styleClass="button">
						<fmt:message key="label.edit" />
					</html:button>
				</c:if>
			</div>
		</c:if>

		<c:if test="${sessionMap.reflectOn}">
			<div class="small-space-top">
				<h3><fmt:message key="monitor.summary.td.notebookInstructions" /></h3>
				<p>
					<strong><lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true"/></strong>
				</p>

				<c:choose>
					<c:when test="${empty sessionMap.reflectEntry}">
						<p>
							<em> <fmt:message key="message.no.reflection.available" />
							</em>
						</p>
					</c:when>
					<c:otherwise>
						<p>
							<fmt:message key="label.your.answer" />
						</p>
						<p>
							<lams:out escapeHtml="true" value="${sessionMap.reflectEntry}" />
						</p>
					</c:otherwise>
				</c:choose>

				<c:if test="${(mode != 'teacher') && isUserLeader}">
					<html:button property="finishButton" onclick="return continueReflect()" styleClass="button">
						<fmt:message key="label.edit" />
					</html:button>
				</c:if>
				
				<c:if test="${fn:length(reflections) > 0}">
					<div id="reflections-div">
						<table id="reflections" class="scroll" cellpadding="0" cellspacing="0"></table>
					</div>
		        </c:if>
			</div>
		</c:if>

		<c:if test="${mode != 'teacher'}">
			<div class="space-bottom-top align-right">
				<html:link href="#nogo" property="finishButton" styleId="finishButton" onclick="return finishSession()" styleClass="button">
					<span class="nextActivity">
						<c:choose>
							<c:when test="${sessionMap.activityPosition.last}">
								<fmt:message key="label.submit" />
							</c:when>
							<c:otherwise>
								<fmt:message key="label.finished" />
							</c:otherwise>
						</c:choose>
					</span>
				</html:link>
			</div>
		</c:if>

	</div>
	<!--closes content-->

	<div id="footer">
	</div>
	<!--closes footer-->

</body>
</lams:html>
