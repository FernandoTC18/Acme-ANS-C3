
package acme.constraints;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.features.flightCrew.flightAssignment.FlightCrewFlightAssignmentRepository;
import acme.realms.FlightCrew;

public class FlightCrewValidator extends AbstractValidator<ValidFlightCrew, FlightCrew> {
	
	
	@Autowired
	private FlightCrewFlightAssignmentRepository flightCrewRepository;

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
		else 
		{
			
			String name;
			String surname;
			String employeeCode;
			boolean correctEmployeeCode;

			name = flightCrew.getIdentity().getName();
			surname = flightCrew.getIdentity().getSurname();
			employeeCode = flightCrew.getEmployeeCode();
			
			
			if (Pattern.matches("^[A-Z]{2,3}\\d{6}$", employeeCode)) {
				correctEmployeeCode = name.charAt(0) == employeeCode.charAt(0) && surname.charAt(0) == employeeCode.charAt(1);

			} else {
				correctEmployeeCode = false;
			}
			
			super.state(context, correctEmployeeCode, "employeeCode", "acme.validation.flightCrew.employeeCode.message");
		}
		{
			FlightCrew existingFlightCrew;
			boolean uniqueEmployeeCode;
			
			existingFlightCrew = flightCrewRepository.findFlightCrewByCode(flightCrew.getEmployeeCode());
			uniqueEmployeeCode = existingFlightCrew == null || existingFlightCrew.getEmployeeCode().isBlank() ||  existingFlightCrew.equals(flightCrew);
			
			super.state(context, uniqueEmployeeCode, "employeeCode", "acme.validation.flightCrew.uniqueEmployeeC.message");
			
			
		}

		res = !super.hasErrors(context);

		return res;
	}

}
