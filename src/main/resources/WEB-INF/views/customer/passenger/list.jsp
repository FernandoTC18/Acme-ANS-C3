<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="customer.passenger.list.label.email" path="email" width="20%"/>
	<acme:list-column code="customer.passenger.list.label.passportNumber" path="passportNumber" width="40%"/>
	<acme:list-column code="customer.passenger.list.label.birth" path="birth" width="40%"/>
	<acme:list-payload path="/payload"/>
</acme:list>
	




