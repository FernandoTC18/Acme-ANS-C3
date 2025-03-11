
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.Manager;

@Validator
public class ManagerValidator extends AbstractValidator<ValidManager, Manager> {

	// Internal state ---------------------------------------------------------

	// ConstraintValidator interface ------------------------------------------

	@Override
	protected void initialise(final ValidManager annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Manager manager, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (manager == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			String name;
			String nameIdentifier;
			String initials;
			String surname;
			boolean correctIdentifierNumber;

			name = manager.getIdentity().getName().trim();
			surname = manager.getIdentity().getSurname().trim();

			nameIdentifier = manager.getIdentifierNumber().substring(0, manager.getIdentifierNumber().length() - 6);

			if (nameIdentifier.length() == 3)
				nameIdentifier = nameIdentifier.substring(0, 2);

			initials = name.substring(0, 1) + surname.substring(0, 1);

			correctIdentifierNumber = initials.equals(nameIdentifier);

			super.state(context, correctIdentifierNumber, "identifierNumber", "acme.validation.manager.identifierNumber.message");

		}
		result = !super.hasErrors(context);

		return result;
	}
}
