<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<table class="table table-sm table-bordered">

    <tr>
        <th scope="row">
        	<acme:print code="assistance-agent.dashboard.form.label.ratio-of-claims-resolved-succesfully"/>
        </th>
        <td>
			<acme:print value="${ratioClaimsResolved}"/>
		</td>
    </tr>

    <tr>
       	<th scope="row">
        	<acme:print code="assistance-agent.dashboard.form.label.ratio-of-claims-rejected"/>
        </th>
        <td>
			<acme:print value="${ratioClaimsRejected}"/>
		</td>
    </tr>

    <tr>
        <th scope="row">
        	<acme:print code="assistance-agent.dashboard.form.label.top-months-with-claims"/>
        </th>
        <td>
	        <ul>
	            <jstl:forEach var="entry" items="${topMonthsHighestClaims}">
	                <li><acme:print value="${entry.key}"/>: <acme:print value="${entry.value}"/></li>
	            </jstl:forEach>
	        </ul>
        </td>
	</tr>

	<tr>
    	<th scope="row">
    		<acme:print code="assistance-agent.dashboard.form.label.average-logs-per-claim"/>
    	</th>
    	<td>
			<acme:print value="${averageNumberLogsPerClaims}"/>
		</td>
	</tr>

	<tr>
    	<th scope="row">
    		<acme:print code="assistance-agent.dashboard.form.label.minimum-logs-per-claim"/>
    	</th>
		<td>
			<acme:print value="${minimumNumberLogsPerClaims}"/>
		</td>
	</tr>

	<tr>
    	<th scope="row">
    		<acme:print code="assistance-agent.dashboard.form.label.maximum-logs-per-claim"/>
    	</th>
    	<td>
			<acme:print value="${maximumNumberLogsPerClaims}"/>
		</td>
	</tr>

	<tr>
	     <th scope="row">
	    	<acme:print code="assistance-agent.dashboard.form.label.deviation-logs-per-claim"/>
	    </th>
	    <td>
			<acme:print value="${deviationNumberLogsPerClaims}"/>
		</td>
	</tr>

	<tr>
	    <th scope="row">
	    	<acme:print code="assistance-agent.dashboard.form.label.average-claims-assisted"/>
	    </th>
		<td>
			<acme:print value="${averageNumberClaimsAssisted}"/>
		</td>
	</tr>

	<tr>
	    <th scope="row">
	    	<acme:print code="assistance-agent.dashboard.form.label.minimum-claims-assisted"/>
	    </th>
	    <td>
			<acme:print value="${minimumNumberClaimsAssisted}"/>
		</td>
	</tr>
	
	<tr>
	    <th scope="row">
	    	<acme:print code="assistance-agent.dashboard.form.label.maximum-claims-assisted"/>
	    </th>
	    <td>
			<acme:print value="${maximumNumberClaimsAssisted}"/>
		</td>
	</tr>

	<tr>
	    <th scope="row">
	    	<acme:print code="assistance-agent.dashboard.form.label.deviation-claims-assisted"/>
	    </th>
	    <td>
			<acme:print value="${deviationNumberClaimsAssisted}"/>
		</td>
	</tr>

 </table>
