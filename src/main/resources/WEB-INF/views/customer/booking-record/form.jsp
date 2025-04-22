<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-select code="customer.bookingRecord.form.label.passengers" path="passenger" choices ="${passengers}"/>
	<acme:input-textbox readonly="true" code="customer.bookingRecord.form.label.booking" path="booking"/>


	<jstl:if test="${!readonly}">
		<acme:submit code="customer.bookingRecord.form.button.create" action="/customer/booking-record/create?bookingId=${bookingId}"/>
	</jstl:if>

</acme:form>