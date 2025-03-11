
package acme.constraints;

import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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
				boolean notOverlapping;
				List<Leg> legsByFlight;
				Leg currentLeg;
				Leg previousLeg;
				legsByFlight = this.repository.computeLegsByFlight(leg.getFlight().getId());

				legsByFlight.removeIf(l -> l.getFlightNumber().equals(leg.getFlightNumber()));

				legsByFlight.add(leg);

				legsByFlight.sort(Comparator.comparing(Leg::getSequenceOrder));
				int overlappedLegs = 0;
				for (int i = 1; i < legsByFlight.size(); i++) {

					currentLeg = legsByFlight.get(i);
					previousLeg = legsByFlight.get(i - 1);
					if (!MomentHelper.isAfter(currentLeg.getScheduledDeparture(), previousLeg.getScheduledArrival()))
						overlappedLegs = overlappedLegs + 1;

				}
				notOverlapping = overlappedLegs == 0;
				super.state(context, notOverlapping, "*", "acme.validation.leg.overlapped.message");
			}
			{
				boolean iataInDB;
				String iataCodeDB;

				iataCodeDB = this.repository.computeAirlineIataCode(leg.getFlightNumber().substring(0, 3));

				iataInDB = iataCodeDB != null && iataCodeDB.equals(leg.getFlightNumber().substring(0, 3));
				super.state(context, iataInDB, "flightnumber", "acme.validation.leg.correctFlightNumber.message");
			}
			{
				boolean correctMinScheduleDeparture;
				Date min;

				min = MomentHelper.getCurrentMoment();

				correctMinScheduleDeparture = !MomentHelper.isBefore(leg.getScheduledDeparture(), min);
				super.state(context, correctMinScheduleDeparture, "scheduledDeparture", "acme.validation.leg.correctMinScheduleDeparture.message");

			}
			{
				boolean correctMinScheduleArrival;
				Date min;

				min = MomentHelper.deltaFromMoment(leg.getScheduledDeparture(), 1, ChronoUnit.MINUTES);

				correctMinScheduleArrival = !MomentHelper.isBefore(leg.getScheduledArrival(), min);
				super.state(context, correctMinScheduleArrival, "scheduledArrival", "acme.validation.leg.correctMinScheduleArrival.message");
			}
		}
		result = !super.hasErrors(context);

		return result;
	}
}
