
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.entities.aircraft.Aircraft;
import acme.features.authenticated.aircraft.AuthenticatedAircraftRepository;

public class AircraftValidator extends AbstractValidator<ValidAircraft, Aircraft> {

	@Autowired
	private AuthenticatedAircraftRepository repository;


	@Override
	protected void initialise(final ValidAircraft annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Aircraft aircraft, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (aircraft == null)
			super.state(context, false, null, "*", "javax.validation.constraints.NotNull.message");
		else {
			Aircraft existingAircraft;
			boolean uniqueRegistrationNumber;

			existingAircraft = this.repository.findAircraftByRegistrationNumber(aircraft.getRegistrationNumber());
			uniqueRegistrationNumber = existingAircraft == null || aircraft.getRegistrationNumber().isBlank() || existingAircraft.equals(aircraft);

			super.state(context, uniqueRegistrationNumber, "registrationNumber", "acme.validation.aircraft.used-registrationNumber.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
