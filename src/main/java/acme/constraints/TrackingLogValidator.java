
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.entities.trackingLog.TrackingLog;
import acme.entities.trackingLog.TrackingLogRepository;
import acme.entities.trackingLog.TrackingLogStatus;

public class TrackingLogValidator extends AbstractValidator<ValidTrackingLog, TrackingLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TrackingLogRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidTrackingLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final TrackingLog trackingLog, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (trackingLog == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			{
				boolean correctIndicator;
				Double resolutionPercentage = trackingLog.getResolutionPercentage();
				TrackingLogStatus indicator = trackingLog.getIndicator();

				correctIndicator = resolutionPercentage < 100.0 && indicator == TrackingLogStatus.PENDING || resolutionPercentage == 100.0 && (indicator == TrackingLogStatus.ACCEPTED || indicator == TrackingLogStatus.REJECTED);
				super.state(context, correctIndicator, "indicator", "acme.validation.trackingLog.incorrect-indicator.message");
			}
			{
				TrackingLog latestTrackingLog;
				boolean correctPercentage;

				latestTrackingLog = this.repository.findLatestTrackingLogByClaimId(trackingLog.getClaim().getId());

				if (latestTrackingLog != null) {
					correctPercentage = trackingLog.getResolutionPercentage() > latestTrackingLog.getResolutionPercentage();
					super.state(context, correctPercentage, "resolutionPercentage", "acme.validation.trackingLog.invalid-resolution-percentage.message");
				}
			}
			{
				boolean correctResolution;

				if (!trackingLog.getIndicator().equals(TrackingLogStatus.PENDING)) {
					correctResolution = trackingLog.getResolution() != null && !trackingLog.getResolution().trim().isEmpty();
					super.state(context, correctResolution, "resolution", "acme.validation.trackingLog.mandatory-resolution.message");
				}
			}

		}

		result = !super.hasErrors(context);

		return result;
	}

}
