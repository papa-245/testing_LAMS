<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	function disableFinishButton() {
		document.getElementById("finishButton").disabled = true;
	}
         function submitForm(methodName){
                var f = document.getElementById('messageForm');
                f.submit();
        }
</script>

<div id="content">
	<h1>
		${wikiDTO.title}
	</h1>

	<html:form action="/learning" method="post" onsubmit="disableFinishButton();" styleId="messageForm">
		<html:hidden property="toolSessionID" styleId="toolSessionID"/>
		<html:hidden property="mode" value="${mode}" />
		
		<p class="small-space-top">
			<lams:out value="${wikiDTO.reflectInstructions}" />
		</p>

		<html:textarea cols="60" rows="8" property="entryText"
			styleClass="text-area"></html:textarea>

		<div class="space-bottom-top align-right">
			<html:hidden property="dispatch" value="submitReflection" />
			<html:link href="#" styleClass="button" styleId="finishButton"  onclick="submitForm('finish');return false">
				<span class="nextActivity"><fmt:message>button.finish</fmt:message></span>
			</html:link>
		</div>
	</html:form>
</div>

