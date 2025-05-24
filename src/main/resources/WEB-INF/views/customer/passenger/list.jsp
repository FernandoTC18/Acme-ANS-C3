<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list show="/customer/passenger/show">
	<acme:list-column code="customer.passenger.list.label.name" path="name" width="20%"/>
    <acme:list-column code="customer.passenger.list.label.email" path="email" width="20%"/>
    <acme:list-column code="customer.passenger.list.label.passportNumber" path="passportNumber" width="20%"/>
    <acme:list-column code="customer.passenger.list.label.birth" path="birth" width="20%"/>
    <acme:list-payload path="/payload"/>
</acme:list>

<jstl:if test="${!readonly}">
    <jstl:choose>
        <jstl:when test="${_command == 'listFromBooking'}">	
        	<jstl:if test="${!isPublished}">
            <acme:button code="customer.bookingRecord.list.button.create" action="/customer/booking-record/create?bookingId=${bookingId}"/>
            <acme:button code="customer.bookingRecord.list.button.delete" action="/customer/booking-record/delete?bookingId=${bookingId}"/>
            </jstl:if>
        </jstl:when>
        <jstl:when test="${_command == 'list'}">
            <acme:button code="customer.passenger.list.button.create" action="/customer/passenger/create"/>
        </jstl:when>
    </jstl:choose>
</jstl:if>


