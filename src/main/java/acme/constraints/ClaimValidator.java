
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimRepository;

public class ClaimValidator extends AbstractValidator<ValidClaim, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClaimRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidClaim annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Claim claim, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (claim == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			Claim existingClaim;
			boolean uniqueEmail;

			existingClaim = this.repository.findClaimByExistingEmail(claim.getPassengerEmail());
			uniqueEmail = existingClaim == null || claim.getPassengerEmail().isBlank() || existingClaim.equals(claim);

			super.state(context, uniqueEmail, "passengerEmail", "acme.validation.claim.duplicatedPassengerEmail.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
