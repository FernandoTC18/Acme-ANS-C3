<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="assistance-agent.tracking-log.form.label.lastUpdateMoment" path="lastUpdateMoment" width="10%"/>
	<acme:list-column code="assistance-agent.tracking-log.form.label.step" path="step" width="10%"/>
	<acme:list-column code="assistance-agent.tracking-log.form.label.resolutionPercentage" path="resolutionPercentage" width="10%"/>
	<acme:list-column code="assistance-agent.tracking-log.form.label.indicator" path="indicator" width="10%"/>
	<acme:list-column code="assistance-agent.tracking-log.form.label.resolution" path="resolution" width="40%"/>
	<acme:list-column code="assistance-agent.tracking-log.form.label.orderDate" path="orderDate" width="10%"/>
	<acme:list-column code="assistance-agent.tracking-log.form.label.draftMode" path="draftMode" width="10%"/>
	<acme:list-payload path="/payload"/>
</acme:list>

<acme:button code="assistance-agent.tracking-log.list.button.create" action="/assistance-agent/tracking-log/create?claimId=${claimId}"/>