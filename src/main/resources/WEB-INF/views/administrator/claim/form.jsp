<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<jstl:choose>
		<jstl:when test="${_command == 'show'}">
			<acme:input-textbox code="administrator.claim.form.label.passengerEmail" path="passengerEmail" readonly="true"/>
			<acme:input-textbox code="administrator.claim.form.label.description" path="description" readonly="true"/>
			<acme:input-select code="administrator.claim.form.label.type" path="type"  choices='${types}' readonly="true"/>
			<acme:input-select code="administrator.claim.form.label.leg" path="leg" choices='${legs}' readonly="true"/>
			<acme:input-textbox code="administrator.claim.form.label.indicator" path="indicator" readonly="true"/>
			<acme:input-moment code="administrator.claim.form.label.registrationMoment" path="registrationMoment" readonly="true"/>
			<acme:button code="administrator.claim.form.button.tracking-log.list" action="/administrator/tracking-log/list?claimId=${id}"/>
		</jstl:when>
	</jstl:choose>
</acme:form>