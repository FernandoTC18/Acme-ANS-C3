
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.realms.AssistanceAgent;

public class AssistanceAgentValidator extends AbstractValidator<ValidAssistanceAgent, AssistanceAgent> {

	// Internal state ---------------------------------------------------------

	// ConstraintValidator interface ------------------------------------------

	@Override
	protected void initialise(final ValidAssistanceAgent annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final AssistanceAgent assistanceAgent, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (assistanceAgent == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean correctEmployeeCode;
			DefaultUserIdentity identity = assistanceAgent.getUserAccount().getIdentity();
			String[] employeeCode = assistanceAgent.getEmployeeCode().trim().split("");
			String[] name = identity.getName().trim().split("");
			String[] surname = identity.getSurname().trim().split("");

			correctEmployeeCode = employeeCode[0].equals(name[0]) && employeeCode[1].equals(surname[0]);
			super.state(context, correctEmployeeCode, "employeeCode", "acme.validation.trackingLog.invalid-employee-code.message");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
