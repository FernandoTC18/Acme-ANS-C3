
package acme.constraints;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.leg.Leg;
import acme.entities.leg.LegRepository;

@Validator
public class LegValidator extends AbstractValidator<ValidLeg, Leg> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private LegRepository repository;
	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidLeg annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (leg == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {

			{
				boolean correctMinScheduleDeparture;
				Date min;

				min = MomentHelper.getCurrentMoment();
				if (min != null) {
					correctMinScheduleDeparture = !MomentHelper.isBefore(leg.getScheduledDeparture(), min);
					super.state(context, correctMinScheduleDeparture, "scheduledDeparture", "acme.validation.leg.correctMinScheduleDeparture.message");
				}

			}
			{
				boolean correctFlightNumber;

				correctFlightNumber = leg.getFlightNumber() != null && Pattern.matches("^[A-Z]{3}\\d{4}$", leg.getFlightNumber());

				super.state(context, correctFlightNumber, "flightNumber", "aacme.validation.leg.correctFlightNumberPattern.message");

			}
			{
				boolean flNumberNotInDb;
				Leg legInDb;
				legInDb = this.repository.computeLegbyFlightNumber(leg.getFlightNumber());
				flNumberNotInDb = legInDb == null || leg.getFlightNumber().isBlank() || legInDb.equals(leg);
				super.state(context, flNumberNotInDb, "flightNumber", "acme.validation.manager.identifierNumberDB.message");
			}
			{
				boolean iataInDB;
				String iataCodeDB;

				iataCodeDB = this.repository.computeAirlineIataCode(leg.getFlightNumber().substring(0, 3));

				iataInDB = iataCodeDB != null && iataCodeDB.equals(leg.getFlightNumber().substring(0, 3));
				super.state(context, iataInDB, "flightnumber", "acme.validation.leg.correctFlightNumberPattern.message");
			}
			{
				boolean correctMinScheduleArrival;
				Date min;

				min = MomentHelper.deltaFromMoment(leg.getScheduledDeparture(), 1, ChronoUnit.MINUTES);
				if (min != null) {
					correctMinScheduleArrival = !MomentHelper.isBefore(leg.getScheduledArrival(), min);
					super.state(context, correctMinScheduleArrival, "scheduledArrival", "acme.validation.leg.correctMinScheduleArrival.message");
				}
			}
		}
		result = !super.hasErrors(context);

		return result;
	}
}
