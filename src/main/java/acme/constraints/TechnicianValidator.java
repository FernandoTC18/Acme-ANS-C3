
package acme.constraints;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.features.authenticated.technician.AuthenticatedTechnicianRepository;
import acme.realms.Technician;

public class TechnicianValidator extends AbstractValidator<ValidTechnician, Technician> {

	@Autowired
	private AuthenticatedTechnicianRepository repository;


	@Override
	protected void initialise(final ValidTechnician annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Technician technician, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (technician == null)
			super.state(context, false, null, "*", "javax.validation.constraints.NotNull.message");
		else {

			{
				String licenseNumber;
				boolean correctLicenseNumber;

				licenseNumber = technician.getLicenseNumber();
				correctLicenseNumber = licenseNumber != null && Pattern.matches("^[A-Z]{2,3}\\d{6}$", licenseNumber);

				super.state(context, correctLicenseNumber, "licenseNumber", "acme.validation.technician.invalid-licenseNumber.message");
			}

			{
				Technician existingTechnician;
				boolean uniqueLicenseNumber;

				existingTechnician = this.repository.findTechnicianByLicenseNumber(technician.getLicenseNumber());
				uniqueLicenseNumber = existingTechnician == null || technician.getLicenseNumber().isBlank() || existingTechnician.equals(technician);

				super.state(context, uniqueLicenseNumber, "licenseNumber", "acme.validation.technician.used-licenseNumber.message");
			}

		}

		result = !super.hasErrors(context);

		return result;
	}

}
