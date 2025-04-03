<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="technician.maintenance-record.form.label.moment" path="moment"/>
	<acme:input-select code="technician.maintenance-record.form.label.status" path="status" choices="${status}"/>
	<acme:input-textbox code="technician.maintenance-record.form.label.inspectionDueDate" path="inspectionDueDate"/>
	<acme:input-textbox code="technician.maintenance-record.form.label.estimatedCost" path="estimatedCost"/>
	<acme:input-textbox code="technician.maintenance-record.form.label.notes" path="notes"/>
	<acme:input-textbox code="technician.maintenance-record.form.label.aircraft" path="aircraft"/>
	<acme:input-textbox code="technician.maintenance-record.form.label.technician" path="technician"/>
	
	<jstl:if test="${!readonly}">
			<jstl:choose>
				<jstl:when test="${acme:anyOf(_command, 'show|update|tasks|publish') && draftMode}">
					<acme:button code="technician.maintenance-record.form.button.tasks" action="/technician/involves/list?maintenanceRecordId=${id}"/>
					<acme:submit code="technician.maintenance-record.form.button.publish" action="/technician/maintenance-record/publish"/>
					<acme:submit code="technician.maintenance-record.form.button.update" action="/technician/maintenance-record/update"/>
				</jstl:when>
				<jstl:when test="${_command == 'create'}">
					<acme:submit code="technician.maintenance-record.form.button.create" action="/technician/maintenance-record/create"/>
				</jstl:when>
				<jstl:when test="${_command != 'create'}">
					<acme:button code="technician.maintenance-record.form.button.tasks" action="/technician/involves/list?maintenanceRecordId=${id}"/>
				</jstl:when>
			</jstl:choose>
	</jstl:if>
</acme:form>