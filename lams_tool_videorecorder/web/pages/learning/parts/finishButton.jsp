<%@ include file="/common/taglibs.jsp"%>

<html:form action="/learning" method="post">
	<html:hidden property="videoRecorderUserUID" value="${videoRecorderUserDTO.uid}" />
	<html:hidden property="dispatch" value="openNotebook" />

	<c:if
		test="${videoRecorderUserDTO.finishedActivity and videoRecorderDTO.reflectOnActivity}">
		<div class="space-top">
			<h2>
				${videoRecorderDTO.reflectInstructions}
			</h2>

			<p>
				<c:choose>
					<c:when test="${not empty videoRecorderDTO.notebookEntry}">
						<lams:out escapeHtml="true" value="${videoRecorderUserDTO.notebookEntry}" />
					</c:when>

					<c:otherwise>
						<em><fmt:message key="message.no.reflection.available" /> </em>
					</c:otherwise>
				</c:choose>
			</p>

			<html:submit styleClass="button">
				<fmt:message key="button.edit" />
			</html:submit>
		</div>
	</c:if>
</html:form>

<script type="text/javascript">
	function disableFinishButton() {
		var finishButton = document.getElementById("finishButton");
		if (finishButton != null) {
			finishButton.disabled = true;
		}
	}
</script>

<html:form action="/learning" method="post"
	onsubmit="disableFinishButton();">
	<html:hidden property="videoRecorderUserUID" value="${videoRecorderUserDTO.uid}" />
	<html:hidden property="toolSessionID" value="${toolSessionId}" />
	<div class="space-bottom-top align-right">
		<c:choose>
			<c:when
				test="${!videoRecorderUserDTO.finishedActivity and videoRecorderDTO.reflectOnActivity}">
				<html:hidden property="dispatch" value="openNotebook" />

				<html:submit styleClass="button">
					<fmt:message key="button.continue" />
				</html:submit>
			</c:when>
			<c:otherwise>
				<html:hidden property="dispatch" value="finishActivity" />
				<html:submit styleClass="button" styleId="finishButton">
					<fmt:message key="button.finish" />
				</html:submit>
			</c:otherwise>
		</c:choose>
	</div>
</html:form>
