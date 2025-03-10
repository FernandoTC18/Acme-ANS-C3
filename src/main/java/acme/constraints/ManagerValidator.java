
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
			String[] surnames;
			boolean correctIdentifierNumber;

			name = manager.getIdentity().getName();
			surnames = manager.getIdentity().getSurname().trim().split("");

			nameIdentifier = manager.getIdentifierNumber().substring(0, manager.getIdentifierNumber().length() - 6);

			initials = nameIdentifier.length() == 2 ? (name.substring(0, 1) + surnames[0]).toUpperCase() : (name.substring(0, 1) + surnames[0] + surnames[1]).toUpperCase();

			correctIdentifierNumber = initials.equals(nameIdentifier);

			super.state(context, correctIdentifierNumber, "identifiernumber", "acme.validation.manager.identifiernumber.message");

		}
		result = !super.hasErrors(context);

		return result;
	}
}
