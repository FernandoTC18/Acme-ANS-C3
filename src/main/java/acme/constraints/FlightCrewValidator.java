
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.realms.FlightCrew;

public class FlightCrewValidator extends AbstractValidator<ValidFlightCrew, FlightCrew> {

	@Override
	protected void initialise(final ValidFlightCrew annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final FlightCrew flightCrew, final ConstraintValidatorContext context) {

		assert context != null;

		boolean res;

		if (flightCrew == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			String name;
			String surname;
			String employeeCode;
			boolean correctEmployeeCode;

			name = flightCrew.getIdentity().getName();
			surname = flightCrew.getIdentity().getSurname();
			employeeCode = flightCrew.getEmployeeCode();

			correctEmployeeCode = name.charAt(0) == employeeCode.charAt(0) && surname.charAt(0) == employeeCode.charAt(1);

			super.state(context, correctEmployeeCode, "employeeCode", "acme.validation.flightCrew.employeeCode.message");

		}

		res = !super.hasErrors(context);

		return false;
	}

}
