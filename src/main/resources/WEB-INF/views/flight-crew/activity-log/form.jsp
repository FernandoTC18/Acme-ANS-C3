<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-moment code="flight-crew.log.form.label.registrationMoment" path="registrationMoment"/>
	<acme:input-textbox code="flight-crew.log.form.label.typeOfIncident" path="typeOfIncident"/>
	<acme:input-textbox code="flight-crew.log.form.label.description" path="description"/>
	<acme:input-integer code="flight-crew.log.form.label.severityLevel" path="severityLevel"/>
	
	<jstl:if test="${!readonly}">
			<jstl:choose>
				<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish')}">
					<acme:submit code="flight-crew.log.form.submit.update" action="/flight-crew/activity-log/update"/>
					<acme:submit code="flight-crew.log.form.submit.delete" action="/flight-crew/activity-log/delete"/>
					<acme:submit code="flight-crew.log.form.submit.publish" action="/flight-crew/activity-log/publish"/>
				</jstl:when>
				<jstl:when test="${_command == 'create'}">
					<acme:submit code="flight-crew.log.form.submit.create" action="/flight-crew/activity-log/create?assignmentId=${assignmentId}"/>
				</jstl:when>
			</jstl:choose>
	</jstl:if>
</acme:form>