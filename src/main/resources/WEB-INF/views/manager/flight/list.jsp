<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="manager.flight.list.label.tag" path="tag" width="30%"/>
	<acme:list-column code="manager.flight.list.label.selfTransferRequired" path="selfTransferRequired" width="10%"/>
	<acme:list-column code="manager.flight.list.label.cost" path="cost" width="30%"/>
	<acme:list-column code="manager.flight.list.label.description" path="description" width="30%"/>
	<acme:list-payload path="/payload"/>
</acme:list>

<acme:button code="manager.flight.form.button.create" action="/manager/flight/create"/>
	