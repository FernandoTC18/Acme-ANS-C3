
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

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
			String name;
			String surname;
			String employeeCode;
			boolean correctEmployeeCode;

			name = assistanceAgent.getIdentity().getName();
			surname = assistanceAgent.getIdentity().getSurname();
			employeeCode = assistanceAgent.getEmployeeCode();

			correctEmployeeCode = name.charAt(0) == employeeCode.charAt(0) && surname.charAt(0) == employeeCode.charAt(1);
			super.state(context, correctEmployeeCode, "employeeCode", "acme.validation.assistanceAgent.invalid-employee-code.message");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
