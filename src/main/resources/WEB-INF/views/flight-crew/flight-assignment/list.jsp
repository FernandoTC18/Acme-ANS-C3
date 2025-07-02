<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="flight-crew.flight-assignment.list.label.duty" path="duty" width="33%"/>	
	<acme:list-column code="flight-crew.flight-assignment.list.label.status" path="status" width="33%"/>
	<acme:list-column code="flight-crew.flight-assignment.list.label.lastUpdate" path="lastUpdate" width="33%"/>
	<acme:list-payload path="/payload"/>
</acme:list>

<jstl:choose>
	<jstl:when test="${_command != 'completed-list'}">
	<acme:button code="flight-crew.flight-assignment.list.button.create" action="/flight-crew/flight-assignment/create"/>
	</jstl:when>
</jstl:choose>
