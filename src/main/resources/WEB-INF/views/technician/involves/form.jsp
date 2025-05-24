<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<jstl:if test="${acme:anyOf(_command, 'show|delete')}">
		<acme:input-textbox code="technician.involves.form.label.maintenanceRecord" path="maintenanceRecord"/>
		<acme:input-textbox code="technician.involves.form.label.task" path="task"/>
		<acme:submit code="technician.involves.form.button.delete" action="/technician/involves/delete"/>
	</jstl:if>
	<jstl:if test="${!readonly}">
		<jstl:choose>
			<jstl:when test="${_command == 'create'}">
				<acme:input-select code="technician.involves.form.label.task" path="task" choices="${tasks}"/>
				<acme:submit code="technician.involves.form.button.create" action="/technician/involves/create?maintenanceRecordId=${maintenanceRecordId}"/>
			</jstl:when>
		</jstl:choose>
	</jstl:if>
</acme:form>