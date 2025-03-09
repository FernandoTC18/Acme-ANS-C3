
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
				Leg nextLeg;

				legsByFlight = this.repository.computeLegsByFlight(leg.getFlight().getId());
				legsByFlight.add(leg);
				legsByFlight.sort(Comparator.comparing(Leg::getScheduledDeparture));

				int overlappedLegs = 0;
				for (int i = 0; i < legsByFlight.size() - 1; i++) {

					currentLeg = legsByFlight.get(i);
					nextLeg = legsByFlight.get(i + 1);
					if (currentLeg.getScheduledArrival().after(nextLeg.getScheduledDeparture()))
						overlappedLegs = overlappedLegs + 1;

				}
				notOverlapping = overlappedLegs > 0;
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

				correctMinScheduleDeparture = !leg.getScheduledDeparture().before(min);
				super.state(context, correctMinScheduleDeparture, "scheduledeparture", "acme.validation.leg.correctFlightNumber.message");

			}
			{
				boolean correctMinScheduleArrival;
				Date min;

				min = MomentHelper.deltaFromMoment(leg.getScheduledDeparture(), 1, ChronoUnit.MINUTES);

				correctMinScheduleArrival = !leg.getScheduledDeparture().before(min);
				super.state(context, correctMinScheduleArrival, "schedulearrival", "acme.validation.leg.correctFlightNumber.message");
			}
		}
		result = !super.hasErrors(context);

		return result;
	}
}
