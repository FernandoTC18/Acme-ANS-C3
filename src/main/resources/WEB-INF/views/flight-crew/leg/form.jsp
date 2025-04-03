<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-textbox code="manager.leg.form.label.flightNumber" path="flightNumber"/>
    <acme:input-moment code="manager.leg.form.label.scheduledDeparture" path="scheduledDeparture"/>
    <acme:input-moment code="manager.leg.form.label.scheduledArrival" path="scheduledArrival"/>
    <acme:input-select code="manager.leg.form.label.status" path="status" choices="${statuses}"/>
    <acme:input-textbox code="manager.leg.form.label.departureAirport" path="departureAirport"/>
    <acme:input-textbox code="manager.leg.form.label.arrivalAirport" path="arrivalAirport"/>
    <acme:input-textbox code="manager.leg.form.label.plane" path="plane"/>
</acme:form>