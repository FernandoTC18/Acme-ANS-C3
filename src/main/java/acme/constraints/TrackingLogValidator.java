
package acme.constraints;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.entities.claim.ClaimStatus;
import acme.entities.trackingLog.TrackingLog;
import acme.entities.trackingLog.TrackingLogRepository;

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
				ClaimStatus indicator = trackingLog.getIndicator();

				correctIndicator = resolutionPercentage == 100.0 && (indicator == ClaimStatus.ACCEPTED || indicator == ClaimStatus.REJECTED) || resolutionPercentage < 100.0 && indicator == ClaimStatus.PENDING;
				super.state(context, correctIndicator, "indicator", "acme.validation.trackingLog.incorrect-indicator.message");
			}
			{
				List<TrackingLog> logs;
				boolean correctPercentage;

				logs = this.repository.findTrackingLogsByClaimId(trackingLog.getClaim().getId());

				List<TrackingLog> allLogs = new ArrayList<>(logs);
				if (!allLogs.contains(trackingLog))
					allLogs.add(trackingLog);

				allLogs.sort(Comparator.comparing(TrackingLog::getLastUpdateMoment));

				double maxPercentage = 0.0;
				correctPercentage = true;

				for (TrackingLog log : allLogs)

					if (log.getResolutionPercentage() <= maxPercentage) {

						if (log.equals(trackingLog)) {
							correctPercentage = false;
							break;
						}
					} else

						maxPercentage = log.getResolutionPercentage();

				super.state(context, correctPercentage, "resolutionPercentage", "acme.validation.trackingLog.invalid-resolution-percentage.message");

			}
			{
				boolean correctResolution;

				if (!trackingLog.getIndicator().equals(ClaimStatus.PENDING)) {
					correctResolution = trackingLog.getResolution() != null && !trackingLog.getResolution().trim().isEmpty();
					super.state(context, correctResolution, "resolution", "acme.validation.trackingLog.mandatory-resolution.message");
				}
			}

		}

		result = !super.hasErrors(context);

		return result;
	}

}
