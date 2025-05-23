<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="assistance-agent.tracking-log.form.label.step" path="step"/>
	<acme:input-double code="assistance-agent.tracking-log.form.label.resolutionPercentage" path="resolutionPercentage"/>
	<acme:input-select code="assistance-agent.tracking-log.form.label.indicator" path="indicator"  choices='${indicators}'/>
	<acme:input-textbox code="assistance-agent.tracking-log.form.label.resolution" path="resolution"/>
	<jstl:if test="${!readonly}">
		<jstl:choose>
			<jstl:when test="${_command == 'show' && draftMode == false}">
				<acme:input-moment code="assistance-agent.tracking-log.form.label.lastUpdateMoment" path="lastUpdateMoment" readonly="true"/>
				<acme:input-moment code="assistance-agent.tracking-log.form.label.orderDate" path="orderDate" readonly="true"/>
			</jstl:when>
			<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
				<acme:input-moment code="assistance-agent.tracking-log.form.label.lastUpdateMoment" path="lastUpdateMoment" readonly="true"/>
				<acme:input-moment code="assistance-agent.tracking-log.form.label.orderDate" path="orderDate" readonly="true"/>
				
				<acme:submit code="assistance-agent.tracking-log.form.button.publish" action="/assistance-agent/tracking-log/publish"/>
				<acme:submit code="assistance-agent.tracking-log.form.button.update" action="/assistance-agent/tracking-log/update"/>
				<acme:submit code="assistance-agent.tracking-log.form.button.delete" action="/assistance-agent/tracking-log/delete"/>
			</jstl:when>
			<jstl:when test="${_command == 'create'}">			
				<acme:submit code="assistance-agent.tracking-log.form.button.create" action="/assistance-agent/tracking-log/create?claimId=${claimId}"/></jstl:when>
		</jstl:choose>
	</jstl:if>
</acme:form>