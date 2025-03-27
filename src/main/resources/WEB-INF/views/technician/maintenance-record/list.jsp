<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="technician.maintenance-record.list.label.moment" path="moment" width="10%"/>
	<acme:list-column code="technician.maintenance-record.list.label.status" path="status" width="10%"/>
	<acme:list-column code="technician.maintenance-record.list.label.inspectionDueDate" path="inspectionDueDate" width="10%"/>
	<acme:list-column code="technician.maintenance-record.list.label.estimatedCost" path="estimatedCost" width="10%"/>
	<acme:list-column code="technician.maintenance-record.list.label.technician" path="technician" width="10%"/>
	<acme:list-payload path="/payload"/>
</acme:list>

<acme:button code="technician.maintenance-record.list.button.create" action="/technician/maintenance-record/create"/>