<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="administrator.airline.form.label.name" path="name"/>
	<acme:input-textbox code="administrator.airline.form.label.iataCode" path="iataCode"/>
	<acme:input-textbox code="administrator.airline.form.label.type" path="type"/>
	<acme:input-textbox code="administrator.airline.form.label.foundation" path="foundation"/>
</acme:form>