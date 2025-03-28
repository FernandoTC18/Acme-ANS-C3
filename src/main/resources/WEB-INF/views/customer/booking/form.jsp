<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="customer.booking.form.label.locatorCode" path="locatorCode" readonly = "true"/>
	<acme:input-moment code="customer.booking.form.label.purchaseMoment" path="purchaseMoment" readonly = "true"/>
	<acme:input-select code="customer.booking.form.label.travelClass" path="travelClass" choices ="${travelClass}"/>
	<acme:input-textbox code="customer.booking.form.label.price" path="price"/>
	<acme:input-select code="customer.booking.form.label.flight" path="flight" choices ="${flights}"/>
	<acme:input-textbox code="customer.booking.form.label.lastCardNibble" path="lastCardNibble"/>
	
	<jstl:if test="${!readonly}">
			<jstl:choose>
				<jstl:when test="${acme:anyOf(_command, 'show|update|publish')}">
					<acme:button code="customer.booking.form.button.passengers" action="/customer/passenger/list?bookingId=${id}"/>		
					<acme:submit code="customer.booking.form.button.update" action="/customer/booking/update"/>
					<acme:submit code="customer.booking.form.button.publish" action="/customer/booking/publish"/>
				</jstl:when>
				<jstl:when test="${_command == 'create'}">
					<acme:submit code="customer.booking.form.button.create" action="/customer/booking/create"/>
				</jstl:when>
			</jstl:choose>
	</jstl:if>
	
</acme:form>