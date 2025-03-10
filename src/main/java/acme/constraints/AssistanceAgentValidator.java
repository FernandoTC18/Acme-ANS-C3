
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
			String employeeCode = assistanceAgent.getEmployeeCode();

			correctEmployeeCode = employeeCode.charAt(0) == identity.getName().charAt(0) && employeeCode.charAt(1) == identity.getSurname().charAt(0);
			super.state(context, correctEmployeeCode, "employeeCode", "acme.validation.trackingLog.invalid-employee-code.message");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
