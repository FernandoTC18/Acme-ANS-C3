
package acme.entities.forms;

import java.util.Map;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssistanceAgentDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	Double						ratioClaimsResolved;
	Double						ratioClaimsRejected;

	Map<String, Integer>		topMonthsHighestClaims;

	double						averageNumberLogsPerClaims;
	int							minimumNumberLogsPerClaims;
	int							maximumNumberLogsPerClaims;
	double						deviationNumberLogsPerClaims;

	double						averageNumberClaimsAssisted;
	int							minimumNumberClaimsAssisted;
	int							maximumNumberClaimsAssisted;
	double						deviationNumberClaimsAssisted;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
