<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-textbox code="flight-crew.member.form.label.employeeCode" path="employeeCode"/>
    <acme:input-textbox code="flight-crew.member.form.label.phoneNumber" path="phoneNumber"/>
    <acme:input-textbox code="flight-crew.member.form.label.languageSkills" path="languageSkills"/>
    <acme:input-select code="flight-crew.member.form.label.availability" path="availability" choices="${availability}"/>
    <acme:input-money code="flight-crew.member.form.label.salary" path="salary"/>
    <acme:input-integer code="flight-crew.member.form.label.experienceYears" path="experienceYears"/>
</acme:form>