
package acme.constraints;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.entities.airport.Airport;
import acme.features.authenticated.airport.AuthenticatedAirportRepository;

public class AirportValidator extends AbstractValidator<ValidAirport, Airport> {

	@Autowired
	private AuthenticatedAirportRepository repository;


	@Override
	protected void initialise(final ValidAirport annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Airport airport, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (airport == null)
			super.state(context, false, null, "*", "javax.validation.constraints.NotNull.message");
		else {

			{
				String iataCode;
				boolean correctIataCode;

				iataCode = airport.getIataCode();
				correctIataCode = iataCode != null && Pattern.matches("^[A-Z]{3}$", iataCode);

				super.state(context, correctIataCode, "iataCode", "acme.validation.airport.invalid-iataCode.message");
			}

			{
				Airport existingAirport;
				boolean uniqueIataCode;

				existingAirport = this.repository.findAirportByIataCode(airport.getIataCode());
				uniqueIataCode = existingAirport == null || airport.getIataCode().isBlank() || existingAirport.equals(airport);

				super.state(context, uniqueIataCode, "iataCode", "acme.validation.airport.invalid-iataCode.message");
			}

		}

		result = !super.hasErrors(context);

		return result;
	}

}
