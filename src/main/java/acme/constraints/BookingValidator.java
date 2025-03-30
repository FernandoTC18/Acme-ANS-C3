
package acme.constraints;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.entities.booking.Booking;
import acme.features.customer.booking.CustomerBookingRepository;

public class BookingValidator extends AbstractValidator<ValidBooking, Booking> {

	@Autowired
	private CustomerBookingRepository repository;


	@Override
	protected void initialise(final ValidBooking annotation) {
		assert annotation != null;
	}
	@Override
	public boolean isValid(final Booking booking, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (booking == null)
			super.state(context, false, null, "*", "javax.validation.constraints.NotNull.message");
		else {

			{
				String locatorCode;
				boolean correctLocatorCode;

				locatorCode = booking.getLocatorCode();
				correctLocatorCode = locatorCode != null && Pattern.matches("^[A-Z0-9]{6,8}$", locatorCode);

				super.state(context, correctLocatorCode, "locatorCode", "acme.validation.booking.invalid-locatorCode.message");
			}
			{
				Booking existingBooking;
				boolean uniqueLocatorCode;

				existingBooking = this.repository.findBookingByLocatorCode(booking.getLocatorCode());
				uniqueLocatorCode = existingBooking == null || booking.getLocatorCode().isBlank() || existingBooking.equals(booking);

				super.state(context, uniqueLocatorCode, "locatorCode", "acme.validation.booking.repeat-locatorCode.message");
			}

		}

		result = !super.hasErrors(context);

		return result;
	}

}
