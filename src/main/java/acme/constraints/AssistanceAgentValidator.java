
package acme.constraints;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.realms.AssistanceAgent;
import acme.realms.AssistanceAgentRepository;

public class AssistanceAgentValidator extends AbstractValidator<ValidAssistanceAgent, AssistanceAgent> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentRepository repository;

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
			{
				boolean correctCodeFormat;

				correctCodeFormat = assistanceAgent.getEmployeeCode() != null && Pattern.matches("^[A-Z]{2,3}\\d{6}$", assistanceAgent.getEmployeeCode());

				super.state(context, correctCodeFormat, "codeFormat", "acme.validation.manager.invalid-employee-code-format.message");

			}
			{
				AssistanceAgent existingAssistanceAgent;
				boolean uniqueAssistanceAgent;

				existingAssistanceAgent = this.repository.findAssistanceAgentByCode(assistanceAgent.getEmployeeCode());
				uniqueAssistanceAgent = existingAssistanceAgent == null || assistanceAgent.getEmployeeCode().isBlank() || existingAssistanceAgent.equals(assistanceAgent);
				super.state(context, uniqueAssistanceAgent, "uniqueAssistanceAgent", "acme.validation.assistanceAgent.unique-assistance-agent.message");

			}
			{
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
		}

		result = !super.hasErrors(context);

		return result;
	}
}
