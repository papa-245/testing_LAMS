<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:if test="${mode == null}"><c:set var="mode" value="${sessionMap.mode}" /></c:if>
<c:set var="isAuthoringRestricted" value="${mode == 'teacher'}" />

<%@ page import="org.lamsfoundation.lams.qb.service.IQbService" %>
<script>
	// Inform author whether the QB question was modified
	var qbQuestionModified = ${empty qbQuestionModified ? 0 : qbQuestionModified},
		qbMessage = null;
	switch (qbQuestionModified) {
		case <%= IQbService.QUESTION_MODIFIED_UPDATE %>: 
			qbMessage = '<fmt:message key="message.qb.modified.update" />';
		case <%= IQbService.QUESTION_MODIFIED_VERSION_BUMP %>: 
			let showMessage = true;
		
			// check if we are in main authoring environment
			if (typeof window.parent.GeneralLib != 'undefined') {
				// check if any other activities require updating
				let activitiesWithQuestion = window.parent.GeneralLib.checkQuestionExistsInToolActivities('${oldQbQuestionUid}');
				if (activitiesWithQuestion.length > 1) {
					showMessage = false;
					// update, if teacher agrees to it
					window.parent.GeneralLib.replaceQuestionInToolActivities('${sessionMap.toolContentID}', activitiesWithQuestion, 
																			 '${oldQbQuestionUid}','${newQbQuestionUid}');
				}
			}

			if (showMessage) {
				qbMessage = '<fmt:message key="message.qb.modified.version" />';
			}

			break;
		case <%= IQbService.QUESTION_MODIFIED_ID_BUMP %>: 
			qbMessage = '<fmt:message key="message.qb.modified.new" />';
			break;
	}
	if (qbMessage) {
		alert(qbMessage);
	}
</script>

<!-- Dropdown menu for choosing a question from question bank -->
<div class="panel panel-default voffset20">
	<div class="panel-heading panel-title">
		<div id="importExport" class="btn-group pull-right">
			<a href="#" id="importQTILink" onClick="javascript:importQTI();return false;" class="btn btn-default btn-xs loffset5">
				<fmt:message key="label.authoring.import.qti" /> 
			</a>
		</div> 
		<fmt:message key="label.questions"/>
	</div>
	<input type="hidden" name="itemCount" id="itemCount" value="${fn:length(sessionMap.itemList)}">
		
	<table class="table table-condensed table-striped">
		<c:forEach var="item" items="${sessionMap.itemList}" varStatus="status">
			<tr>
				<td style="width:5%">
					${status.count})
				</td>
				<td style="padding-top:15px; padding-bottom:15px;">
					<c:out value="${item.qbQuestion.name}" escapeXml="true"/>
				</td>
				
				<td style="width:1%">
					<span class='alert-info btn-xs question-type-alert'>
						<c:choose>
							<c:when test="${item.qbQuestion.type == 1}">
								<fmt:message key="label.type.multiple.choice" />
							</c:when>
							<c:when test="${item.qbQuestion.type == 3}">
								<fmt:message key="label.type.short.answer" />
							</c:when>
						</c:choose>
	       			</span>
				</td>
				<td style="width:1%">
				    <c:set var="maxQuestionVersion" value="1" />
				    <c:choose>
						<c:when test="${fn:length(item.qbQuestion.versionMap) == 1}">
							<button class="btn btn-default btn-xs dropdown-toggle2 question-version-dropdown" disabled="disabled">
							    Version ${item.qbQuestion.version}
							</button>
						</c:when>
			
						<c:otherwise>
							<div class="dropdown question-version-dropdown">
								<button class="btn btn-default btn-xs dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
							    	Version ${item.qbQuestion.version}&nbsp;<span class="caret"></span>
								</button>
								
								<ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
									<c:forEach items="${item.qbQuestion.versionMap}" var="otherVersion">
										 <c:set var="maxQuestionVersion" value="${otherVersion.key}" />
										 
							    		<li <c:if test="${item.qbQuestion.version == otherVersion.key}">class="disabled"</c:if>>
							    			<a href="#nogo" onclick="javascript:changeItemQuestionVersion(${status.index}, ${otherVersion.value});">Version ${otherVersion.key}</a>
							    		</li>
							    	</c:forEach>
								</ul>
	
							</div>			
						</c:otherwise>
					</c:choose>
				</td>
				
				<td style="width: 3%">
					<c:if test="${item.qbQuestion.version < maxQuestionVersion}">
						<i class="fa fa-exclamation newer-version-prompt" title="There is a newer version of this question"></i>
					</c:if>
				</td>
				
				<c:if test="${!isAuthoringRestricted}">
					<td class="arrows" style="width:5%">
						<c:if test="${not status.first}">
							<lams:Arrow state="up" titleKey="label.up" onclick="return upItem(${status.index})"/>
						</c:if>
			
						<c:if test="${not status.last}">
							<lams:Arrow state="down" titleKey="label.down" onclick="return downItem(${status.index})"/>
						</c:if>
					</td>
				</c:if>
					
				<td align="center" style="width:5%">
					<c:set var="editItemUrl" >
						<c:url value='/authoring/editItem.do'/>?sessionMapID=${sessionMapID}&itemIndex=${status.index}&KeepThis=true&TB_iframe=true&modal=true
					</c:set>		
					<a href="${editItemUrl}" class="thickbox"> 
						<i class="fa fa-pencil"	title="<fmt:message key='label.edit' />"/></i>
					</a>
				</td>			
				
				<c:if test="${!isAuthoringRestricted}">
					<td align="center" style="width:5%">
						<i class="fa fa-times"	title="<fmt:message key="label.delete" />" id="delete${status.index}" 
							onclick="removeItem(${status.index})"></i>
					</td>
				</c:if>
			</tr>
		</c:forEach>
	</table>
</div>
