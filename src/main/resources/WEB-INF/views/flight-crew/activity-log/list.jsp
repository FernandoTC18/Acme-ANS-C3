<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="flight-crew.log.list.label.registrationMoment" path="registrationMoment" width="20%"/>	
	<acme:list-column code="flight-crew.log.list.label.typeOfIncident" path="typeOfIncident" width="20%"/>
	<acme:list-column code="flight-crew.log.list.label.flightAssignment" path="flightAssignment" width="20%"/>
	<acme:list-payload path="/payload"/>
</acme:list>

<jstl:if test="${draftMode}">
	<acme:button code="flight-crew.log.list.button.create" action="/flight-crew/activity-log/create?assignmentId=${assignmentId}"/>
</jstl:if>
