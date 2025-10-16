<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<acme:form>
	<acme:input-textbox code="authenticated.assistance-agent.form.label.employeeCode" path="employeeCode"/>
	<acme:input-textbox code="authenticated.assistance-agent.form.label.spokenLanguages" path="spokenLanguages"/>
	<acme:input-textbox code="authenticated.assistance-agent.form.label.briefBio" path="briefBio"/>
	<acme:input-integer code="authenticated.assistance-agent.form.label.salary" path="salary"/>
	<acme:input-textarea code="authenticated.assistance-agent.form.label.photo" path="photo"/>
	<acme:input-select code="authenticated.assistance-agent.list.label.airline" path="airlineCode" choices="${airlineCodes}"/>
	<jstl:if test="${_command == 'create'}">
		<acme:submit  code="authenticated.assistance-agent.form.button.create" action="/authenticated/assistance-agent/create"/>
	</jstl:if>
</acme:form>
