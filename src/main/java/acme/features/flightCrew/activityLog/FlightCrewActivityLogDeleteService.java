
package acme.features.flightCrew.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activitylog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrew;

@GuiService
public class FlightCrewActivityLogDeleteService extends AbstractGuiService<FlightCrew, ActivityLog> {

	@Autowired
	private FlightCrewActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int logId;
		ActivityLog log;
		FlightAssignment assignment;
		FlightCrew member;

		boolean correctMember;
		logId = super.getRequest().getData("id", int.class);
		assignment = this.repository.getAssignmentByLogId(logId);
		member = assignment == null ? null : assignment.getFlightCrewMember();
		correctMember = member != null ? super.getRequest().getPrincipal().hasRealm(member) : false;

		if (correctMember == true) {

			boolean draftMode;
			log = this.repository.getLogById(logId);
			draftMode = log.getDraftMode();

			status = draftMode && MomentHelper.isPast(assignment.getLeg().getScheduledDeparture());

			super.getResponse().setAuthorised(status);

		} else
			super.getResponse().setAuthorised(false);
	}

	@Override
	public void load() {
		ActivityLog log;
		int id;

		id = super.getRequest().getData("id", int.class);
		log = this.repository.getLogById(id);

		super.getBuffer().addData(log);

	}

	@Override
	public void bind(final ActivityLog log) {
		super.bindObject(log, "registrationMoment", "typeOfIncident", "description", "severityLevel");

	}

	@Override
	public void validate(final ActivityLog log) {
		int id;
		ActivityLog activityLog;

		id = super.getRequest().getData("id", int.class);
		activityLog = this.repository.getLogById(id);

		super.state(activityLog.getDraftMode() == true, "*", "acme.validation.log-published");
	}

	@Override
	public void perform(final ActivityLog log) {
		this.repository.delete(log);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;

		dataset = super.unbindObject(log, "registrationMoment", "typeOfIncident", "description", "severityLevel", "flightAssignment", "draftMode");

		super.getResponse().addData(dataset);
	}

}
