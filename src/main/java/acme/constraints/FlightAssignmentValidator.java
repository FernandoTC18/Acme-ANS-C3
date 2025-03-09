
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.MomentHelper;
import acme.entities.flightAssignment.FlightAssignment;

public class FlightAssignmentValidator extends AbstractValidator<ValidFlightAssignment, FlightAssignment> {

	@Override
	protected void initialise(final ValidFlightAssignment annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final FlightAssignment flightAssignment, final ConstraintValidatorContext context) {

		assert context != null;

		boolean res;

		if (flightAssignment == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {

			boolean correctDate = !flightAssignment.getLastUpdate().before(MomentHelper.getCurrentMoment());

			super.state(context, correctDate, "lastupdate", "acme.validation.flightassignment.lastupdate.message");
		}

		res = !super.hasErrors(context);

		return res;

	}

}
