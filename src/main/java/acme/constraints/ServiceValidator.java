
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.service.Service;

@Validator
public class ServiceValidator extends AbstractValidator<ValidService, Service> {

	// Internal state ---------------------------------------------------------

	// ConstraintValidator interface ------------------------------------------

	@Override
	protected void initialise(final ValidService annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Service service, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (service == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {

			boolean correctPromotionCode;
			Integer actualYear;
			Integer lastTwoNumbers;
			if (service.getPromotionCode() != null) {
				actualYear = Integer.valueOf(MomentHelper.getCurrentMoment().getYear() % 100);
				lastTwoNumbers = Integer.valueOf(service.getPromotionCode().substring(5, 7));
				correctPromotionCode = lastTwoNumbers.equals(actualYear);
			} else
				correctPromotionCode = true;
			super.state(context, correctPromotionCode, "promotionCode", "acme.validation.service.correctLastTwoNumbers.message");
		}

		result = !super.hasErrors(context);
		return result;
	}
}
