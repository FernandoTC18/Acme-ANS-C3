<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<jstl:if test="${!readonly}">
		<jstl:choose>
			<jstl:when test="${_command == 'show'}">
				<acme:input-textbox code="administrator.tracking-log.form.label.step" path="step" readonly="true"/>
				<acme:input-double code="administrator.tracking-log.form.label.resolutionPercentage" path="resolutionPercentage" readonly="true"/>
				<acme:input-select code="administrator.tracking-log.form.label.indicator" path="indicator"  choices='${indicators}' readonly="true"/>
				<acme:input-textbox code="administrator.tracking-log.form.label.resolution" path="resolution" readonly="true"/>
				<acme:input-moment code="administrator.tracking-log.form.label.lastUpdateMoment" path="lastUpdateMoment" readonly="true"/>
				<acme:input-moment code="administrator.tracking-log.form.label.orderDate" path="orderDate" readonly="true"/>
				<acme:input-moment code="administrator.tracking-log.form.label.draftMode" path="draftMode" readonly="true"/>
			</jstl:when>
		</jstl:choose>
	</jstl:if>
</acme:form>