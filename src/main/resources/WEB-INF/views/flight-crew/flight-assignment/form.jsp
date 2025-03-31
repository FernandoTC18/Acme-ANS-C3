<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-select code="flight-crew.flight-assignment.form.label.duty" path="duty" choices='${duty}'/>
	<acme:input-select code="flight-crew.flight-assignment.form.label.legs" path="leg" choices='${legs}'/>
	<acme:input-select code="flight-crew.flight-assignment.form.label.flightCrew" path="flightCrewMember" choices='${flightCrewMember}'/>
	<acme:input-moment code="flight-crew.flight-assignment.form.label.lastUpdate" path="lastUpdate"/>
	<acme:input-select code="flight-crew.flight-assignment.form.label.status" path="status" choices="${status}"/>
	<acme:input-textbox code="flight-crew.flight-assignment.form.label.remarks" path="remarks"/>
	
	<acme:button code="flight-crew.flight-assignment.form.button.legs.list" action="/flight-crew/leg/list?id=${id}"/>
	<acme:button code="flight-crew.flight-assignment.form.button.crew-members.list" action="/flight-crew/flight-crew/list?id=${id}"/>
	
	<jstl:if test="${!readonly}">
			<jstl:choose>
				<jstl:when test="${acme:anyOf(_command, 'show|update|delete')}">
					<acme:submit code="flight-crew.flight-assignment.form.submit.update" action="/flight-crew/flight-assignment/update"/>
					<acme:submit code="flight-crew.flight-assignment.form.submit.delete" action="/flight-crew/flight-assignment/delete"/>
					<acme:submit code="flight-crew.flight-assignment.form.submit.publish" action="/flight-crew/flight-assignment/publish"/>
				</jstl:when>
				<jstl:when test="${_command == 'create'}">
					<acme:submit code="flight-crew.flight-assignment.form.submit.create" action="/flight-crew/flight-assignment/create"/>
				</jstl:when>
			</jstl:choose>
	</jstl:if>
</acme:form>