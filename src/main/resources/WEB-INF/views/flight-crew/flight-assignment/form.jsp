<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-select code="flight-crew.flight-assignment.form.label.duty" path="duty" choices='${duty}'/>
	<acme:input-select code="flight-crew.flight-assignment.form.label.legs" path="leg" choices='${legs}'/>
	<acme:input-textbox code="flight-crew.flight-assignment.form.label.flightCrew" path="flightCrewMember" readonly="true"/>
	<acme:input-moment code="flight-crew.flight-assignment.form.label.lastUpdate" path="lastUpdate" readonly="true"/>
	<acme:input-select code="flight-crew.flight-assignment.form.label.status" path="status" choices="${status}"/>
	<acme:input-textbox code="flight-crew.flight-assignment.form.label.remarks" path="remarks"/>
	
	<jstl:if test="${_command != 'create'}">
		<acme:button code="flight-crew.flight-assignment.form.button.legs.list" action="/flight-crew/leg/list?id=${id}"/>
		<acme:button code="flight-crew.flight-assignment.form.button.crew-members.list" action="/flight-crew/flight-crew/list?id=${id}"/>
		<acme:button code="flight-crew.flight-assignment.form.button.logs.list" action="/flight-crew/activity-log/list?id=${id}"/>
	</jstl:if>

	
	<jstl:if test="${!readonly}">
			<jstl:choose>
				<jstl:when test="${_command != 'create'}">
					<acme:submit code="flight-crew.flight-assignment.form.submit.update" action="/flight-crew/flight-assignment/update?id=${id}"/>
					<acme:submit code="flight-crew.flight-assignment.form.submit.delete" action="/flight-crew/flight-assignment/delete?id=${id}"/>
					<acme:submit code="flight-crew.flight-assignment.form.submit.publish" action="/flight-crew/flight-assignment/publish?id=${id}"/>
				</jstl:when>
				<jstl:when test="${_command == 'create'}">
					<acme:submit code="flight-crew.flight-assignment.form.submit.create" action="/flight-crew/flight-assignment/create"/>
				</jstl:when>
			</jstl:choose>
	</jstl:if>
</acme:form>