<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="administrator.claim.list.label.registrationMoment" path="registrationMoment" width="20%"/>
	<acme:list-column code="administrator.claim.list.label.passengerEmail" path="passengerEmail" width="20%"/>
	<acme:list-column code="administrator.claim.list.label.type" path="type" width="20%"/>
	<acme:list-column code="administrator.claim.list.label.indicator" path="indicator" width="20%"/>
	<acme:list-column code="administrator.claim.list.label.leg" path="leg" width="10%"/>
	<acme:list-column code="administrator.claim.list.label.draftMode" path="draftMode" width="10%"/>
	<acme:list-payload path="/payload"/>
</acme:list>