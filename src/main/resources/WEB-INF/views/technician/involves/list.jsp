<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="technician.involves.list.label.aircraft" path="aircraft" width="10%"/>
	<acme:list-column code="technician.involves.list.label.task" path="task" width="10%"/>
	<acme:list-column code="technician.involves.list.label.priority" path="priority" width="10%"/>
	<acme:list-column code="technician.involves.list.label.estimatedDuration" path="estimatedDuration" width="10%"/>
	<acme:list-column code="technician.involves.list.label.technician" path="technician" width="10%"/>
	<acme:list-payload path="/payload"/>
</acme:list>

<acme:button code="technician.involves.list.button.create" action="/technician/involves/create?maintenanceRecordId=${maintenanceRecordId}"/>