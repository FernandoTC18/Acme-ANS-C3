package acme.constraints;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.entities.airline.Airline;

import acme.features.authenticated.airline.AirlineRepository;

public class AirlineValidator extends AbstractValidator<ValidAirline, Airline>{
	
	
	@Autowired
	private AirlineRepository airlineRepository;
	
	
	@Override
	protected void initialise(final ValidAirline annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Airline airline, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (airline == null)
			super.state(context, false, null, "*", "javax.validation.constraints.NotNull.message");
		else {

			{
				String iataCode;
				boolean correctIataCode;

				iataCode = airline.getIataCode();
				correctIataCode = iataCode != null && Pattern.matches("^[A-Z]{3}$", iataCode);

				super.state(context, correctIataCode, "iataCode", "acme.validation.airline.invalid-iataCode.message");
			}

			{
				Airline existingAirport;
				boolean uniqueIataCode;

				existingAirport = this.airlineRepository.findAirlineByIataCode(airline.getIataCode());
				uniqueIataCode = existingAirport == null || airline.getIataCode().isBlank() || existingAirport.equals(airline);

				super.state(context, uniqueIataCode, "iataCode", "acme.validation.airline.uniqueIataCode.message");
			}

		}

		result = !super.hasErrors(context);

		return result;
	}

}
