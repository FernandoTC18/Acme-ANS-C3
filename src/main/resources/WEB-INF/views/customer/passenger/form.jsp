<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="customer.passenger.form.label.name" path="name"/>
	<acme:input-email code="customer.passenger.form.label.email" path="email"/>
	<acme:input-textbox code="customer.passenger.form.label.passportNumber" path="passportNumber"/>
	<acme:input-moment code="customer.passenger.form.label.birth" path="birth"/>
	<acme:input-textbox code="customer.passenger.form.label.specialNeeds" path="specialNeeds"/>
	
	<jstl:if test="${!readonly}">
	    <jstl:choose>
	        <jstl:when test="${_command != 'create'}">	
	            <acme:submit code="customer.passenger.form.button.update" action="/customer/booking/update"/>
	        </jstl:when>
	        <jstl:when test="${_command == 'create'}">
	            <acme:submit code="customer.passenger.form.button.create" action="/customer/booking/create"/>
	        </jstl:when>
	    </jstl:choose>
	</jstl:if>

	
</acme:form>