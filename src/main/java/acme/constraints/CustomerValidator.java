
package acme.constraints;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.features.authenticated.customer.AuthenticatedCustomerRepository;
import acme.realms.Customer;

public class CustomerValidator extends AbstractValidator<ValidCustomer, Customer> {

	@Autowired
	private AuthenticatedCustomerRepository repository;


	@Override
	protected void initialise(final ValidCustomer annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Customer customer, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (customer == null)
			super.state(context, false, null, "*", "javax.validation.constraints.NotNull.message");
		else {

			{
				String identifier;
				boolean correctIdentifier;

				identifier = customer.getIdentifier();
				correctIdentifier = identifier != null && Pattern.matches("^[A-Z]{2,3}\\d{6}$", identifier);

				super.state(context, correctIdentifier, "identifier", "acme.validation.customer.invalid-identifier.message");
			}
			{
				Customer existingCustomer;
				boolean uniqueIdentifier;

				existingCustomer = this.repository.findCustomerByIdentifier(customer.getIdentifier());
				uniqueIdentifier = existingCustomer == null || customer.getIdentifier().isBlank() || existingCustomer.equals(customer);

				super.state(context, uniqueIdentifier, "identifier", "acme.validation.customer.invalid-identifier.message");
			}

		}

		result = !super.hasErrors(context);

		return result;
	}

}
