<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list navigable="false">
	<acme:list-column code="flight-crew.member.list.label.employeeCode" path="employeeCode" width="20%"/>	
	<acme:list-column code="flight-crew.member.list.label.phoneNumber" path="phoneNumber" width="20%"/>
	<acme:list-column code="flight-crew.member.list.label.availability" path="availability" width="20%"/>
	<acme:list-payload path="/payload"/>
</acme:list>