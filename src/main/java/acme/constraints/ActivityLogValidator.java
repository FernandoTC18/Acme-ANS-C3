
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.entities.activitylog.ActivityLog;
import acme.entities.activitylog.ActivityLogRepository;

public class ActivityLogValidator extends AbstractValidator<ValidActivityLog, ActivityLog> {

	@Autowired
	private ActivityLogRepository activityLogRepository;


	@Override
	protected void initialise(final ValidActivityLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final ActivityLog activityLog, final ConstraintValidatorContext context) {

		assert context != null;

		boolean res;

		if (activityLog == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean correctDate;
			Date legDate;

			ActivityLog log = this.activityLogRepository.getActivityLogByFlightAssignmentId(activityLog.getLogger().getId());
			legDate = log.getLogger().getLeg().getScheduledArrival();

			Date logDate = activityLog.getRegistrationMoment();

			correctDate = logDate.after(logDate);

			super.state(context, correctDate, "registrationMoment", "");
		}

		res = !super.hasErrors(context);

		return res;

	}
}
