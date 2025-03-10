
package acme.constraints;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.flight.Flight;

@Validator
public class FlightValidator extends AbstractValidator<ValidFlight, Flight> {

	// Internal state ---------------------------------------------------------

	// ConstraintValidator interface ------------------------------------------

	@Override
	protected void initialise(final ValidFlight annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Flight flight, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (flight == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {

			{
				boolean correctMinScheduleDeparture;
				Date min;

				min = MomentHelper.getCurrentMoment();

				correctMinScheduleDeparture = !flight.getScheduledDeparture().before(min);
				super.state(context, correctMinScheduleDeparture, "scheduledeparture", "acme.validation.flight.correctFlightNumber.message");

			}
			{
				boolean correctMinScheduleArrival;
				Date min;

				min = MomentHelper.deltaFromMoment(flight.getScheduledDeparture(), 1, ChronoUnit.MINUTES);

				correctMinScheduleArrival = !flight.getScheduledDeparture().before(min);
				super.state(context, correctMinScheduleArrival, "schedulearrival", "acme.validation.flight.correctFlightNumber.message");
			}
		}
		result = !super.hasErrors(context);

		return result;
	}
}
