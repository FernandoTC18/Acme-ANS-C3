
package acme.constraints;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.Manager;
import acme.realms.ManagerRepository;

@Validator
public class ManagerValidator extends AbstractValidator<ValidManager, Manager> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private ManagerRepository managerRepository;
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
			{
				boolean correctIdentifierFormat;

				correctIdentifierFormat = manager.getIdentifierNumber() != null && Pattern.matches("^[A-Z]{2,3}\\d{6}$", manager.getIdentifierNumber());

				super.state(context, correctIdentifierFormat, "identifierNumber", "acme.validation.manager.identifierNumberFormat.message");

			}
			{
				boolean idNumberNotInDb;
				Manager managerInDb;
				managerInDb = this.managerRepository.computeManagerByIdNumberInDb(manager.getIdentifierNumber());
				idNumberNotInDb = managerInDb == null || manager.getIdentifierNumber().isBlank() || managerInDb.equals(manager);
				super.state(context, idNumberNotInDb, "promotionCode", "acme.validation.manager.identifierNumberDB.message");
			}

			{
				String name;
				String nameIdentifier;
				String surname;
				boolean correctIdentifierName;

				name = manager.getIdentity().getName().trim();
				surname = manager.getIdentity().getSurname().trim();

				nameIdentifier = manager.getIdentifierNumber();
				if (name != null && surname != null && nameIdentifier != null && (manager.getIdentifierNumber().length() == 8 || manager.getIdentifierNumber().length() == 9)) {
					correctIdentifierName = nameIdentifier.charAt(0) == name.charAt(0) && nameIdentifier.charAt(1) == surname.charAt(0);

					super.state(context, correctIdentifierName, "identifierNumber", "acme.validation.manager.identifierNumber.message");
				}
			}
		}
		result = !super.hasErrors(context);

		return result;
	}
}
