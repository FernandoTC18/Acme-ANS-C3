package acme.constraints;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.service.Service;
import acme.entities.service.ServiceRepository;

@Validator
public class ServiceValidator extends AbstractValidator<ValidService, Service> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private ServiceRepository serviceRepository;
	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidService annotation) {
		assert annotation != null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isValid(final Service service, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (service == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {

			{
				boolean correctPatternPromotionCode;
				if (service.getPromotionCode() == null)
					correctPatternPromotionCode = true;
				else
					correctPatternPromotionCode = Pattern.matches("^[A-Z]{4}-[0-9]{2}$", service.getPromotionCode()) && !service.getPromotionCode().isBlank();

				super.state(context, correctPatternPromotionCode, "promotionCode", "acme.validation.service.correctPattern.message");
			}

			{
				boolean promCodeNotInDb;
				if (service.getPromotionCode() != null) {
					Service servInDb;
					servInDb = this.serviceRepository.computePromCodeInDbNum(service.getPromotionCode());
					promCodeNotInDb = servInDb == null || service.getPromotionCode().isBlank() || servInDb.equals(service);
				} else
					promCodeNotInDb = true;
				super.state(context, promCodeNotInDb, "promotionCode", "acme.validation.service.promCodeNotInDb.message");
			}

			{
				boolean correctPromotionCodeNumber;
				Integer actualYear;
				Integer lastTwoNumbers;
				if (service.getPromotionCode() != null) {
					actualYear = Integer.valueOf(MomentHelper.getCurrentMoment().getYear() % 100);
					lastTwoNumbers = Integer.valueOf(service.getPromotionCode().substring(5, 7));
					correctPromotionCodeNumber = lastTwoNumbers.equals(actualYear);
				} else
					correctPromotionCodeNumber = true;
				super.state(context, correctPromotionCodeNumber, "promotionCode", "acme.validation.service.correctLastTwoNumbers.message");
			}
		}

		result = !super.hasErrors(context);
		return result;
	}

}
