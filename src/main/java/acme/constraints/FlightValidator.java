
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.flight.Flight;
import acme.entities.flight.FlightRepository;
import acme.entities.leg.Leg;

@Validator
public class FlightValidator extends AbstractValidator<ValidFlight, Flight> {

	// Internal state ---------------------------------------------------------
	@Autowired
	FlightRepository flightRepository;
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
			boolean notOverlapping;
			List<Leg> legsByFlight;
			Leg currentLeg;
			Leg nextLeg;
			legsByFlight = this.flightRepository.computeLegsByFlight(flight.getId());
			int overlappedLegs = 0;
			for (int i = 0; i < legsByFlight.size() - 1; i++) {
				currentLeg = legsByFlight.get(i);
				nextLeg = legsByFlight.get(i + 1);
				if (MomentHelper.isAfter(currentLeg.getScheduledArrival(), nextLeg.getScheduledDeparture()))
					overlappedLegs = overlappedLegs + 1;
			}
			notOverlapping = overlappedLegs == 0;
			super.state(context, notOverlapping, "*", "acme.validation.flight.overlapped.message");
		}
		result = !super.hasErrors(context);

		return result;
	}
}
