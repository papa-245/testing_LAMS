<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-latest.pack.js"></script>
<script type="text/javascript" src="includes/javascript/swfobject.js"></script>
<script type="text/javascript" src="includes/javascript/mindmap.resize.js"></script>

<script type="text/javascript">
//<![CDATA[
	flashvars = { xml: "${mindmapContentPath}", user: "${currentMindmapUser}", dictionary: "${localizationPath}" }
	
	embedFlashObject(700, 525);
	
	function getFlashMovie(movieName) {
		var isIE = navigator.appName.indexOf("Microsoft") != -1;
		return (isIE) ? window[movieName] : document[movieName];
	}

	$(window).resize(makeNice);
	
	function embedFlashObject(x, y)
	{
		swfobject.embedSWF("${mindmapType}", "flashContent", x, y, "9.0.0", false, flashvars);
	}
	
	function setToolContentID()
	{
		var toolContentID = document.getElementById("toolContentID");
		toolContentID.value = "${toolContentID}";
	}
	
	function setUserId()
	{
		var userId = document.getElementById("userId");
		userId.value = "${userDTO.uid}";
	}
	
	function setMindmapContent()
	{
		var mindmapContent = document.getElementById("mindmapContent");
		if(mindmapContent != null) {
			mindmapContent.value = getFlashMovie('flashContent').getMindmap();
		}

		setUserId();
		setToolContentID();
	}

	function updateContent()
	{
		$.post("${get}", { dispatch: "${dispatch}", mindmapId: "${mindmapId}", userId: "${userId}", 
			content: getFlashMovie('flashContent').getMindmap() } );
	}
//]]>
</script>

<html:form action="/monitoring" method="get">
	<html:hidden property="toolContentID" styleId="toolContentID" value="${toolContentID}" />
	<html:hidden property="contentFolderID" styleId="contentFolderID" value="${contentFolderID}" />
	
	<table>
		<tr>
			<td colspan="2">
				<h2>
					${userDTO.firstName} ${userDTO.lastName}
				</h2>
			</td>
		</tr>
		<tr>
			<td class="field-name">
				<fmt:message key="label.mindmapEntry" />
			</td>
		</tr>
	</table>
	
	<center id="center12">
		<div id="flashContent">
			<fmt:message>message.enableJavaScript</fmt:message>
		</div>
	</center>
	
	<div class="space-bottom-top align-right">
		<html:button styleClass="button" property="backButton" onclick="history.go(-1)">
			<fmt:message>button.back</fmt:message>
		</html:button>

		<c:choose>
			<c:when test="${isMultiUserMode}">
			</c:when>
			<c:otherwise>
				<html:submit styleClass="button" styleId="saveButton" onclick="updateContent()">
					<fmt:message>button.save</fmt:message>
				</html:submit>	
			</c:otherwise>
		</c:choose>
		
	</div>

</html:form>
