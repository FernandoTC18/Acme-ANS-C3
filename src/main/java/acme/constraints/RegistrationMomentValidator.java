
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.ConversionHelper;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.PropertyHelper;
import acme.client.helpers.StringHelper;
import acme.internals.helpers.HibernateHelper;

@Validator
public class RegistrationMomentValidator extends AbstractValidator<ValidRegistrationMoment, Date> {

	// Internal state ---------------------------------------------------------

	private Date	lowerLimit;
	private Date	upperLimit;

	// Initialiser ------------------------------------------------------------


	@Override
	public void initialise(final ValidRegistrationMoment annotation) {
		assert annotation != null;

		if (StringHelper.isBlank(annotation.min()))
			this.lowerLimit = PropertyHelper.getRequiredProperty("acme.data.moment.mininum", Date.class);
		else
			this.lowerLimit = ConversionHelper.convert(annotation.min(), Date.class);

	}

	// AbstractValidator interface --------------------------------------------

	@Override
	public boolean isValid(final Date value, final ConstraintValidatorContext context) {
		// HINT: value can be null
		assert context != null;

		boolean result;
		String lowerLocalisedLimit, upperLocalisedLimit;

		if (value == null)
			result = true;
		else {
			this.upperLimit = MomentHelper.getCurrentMoment();
			result = MomentHelper.isInRange(value, this.lowerLimit, this.upperLimit);
			if (!result) {
				lowerLocalisedLimit = ConversionHelper.convert(this.lowerLimit, String.class);
				upperLocalisedLimit = ConversionHelper.convert(this.upperLimit, String.class);
				HibernateHelper.replaceParameter(context, "placeholder", "acme.validation.range.message", lowerLocalisedLimit, upperLocalisedLimit);
			}
		}

		return result;
	}

}
