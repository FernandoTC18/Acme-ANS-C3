
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
public class FlightCrewActivityLogPublishService extends AbstractGuiService<FlightCrew, ActivityLog> {

	@Autowired
	FlightCrewActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int logId;
		ActivityLog log;
		FlightAssignment assignment;
		FlightCrew member;
		boolean draftMode;

		boolean correctMember;
		logId = super.getRequest().getData("id", int.class);
		assignment = this.repository.getAssignmentByLogId(logId);
		member = assignment == null ? null : assignment.getFlightCrewMember();
		correctMember = member != null ? super.getRequest().getPrincipal().hasRealm(member) : false;

		if (correctMember == true) {

			log = this.repository.getLogById(logId);
			draftMode = log.getDraftMode();

			status = draftMode && MomentHelper.isPast(assignment.getLeg().getScheduledDeparture());//I don't add correctMember 'cause in this branch of the if is always true, so it won't affect the result

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
		int logId = super.getRequest().getData("id", int.class);
		ActivityLog acLog = this.repository.getLogById(logId);
		boolean draftMode = acLog.getFlightAssignment().getDraftMode();

		super.state(!draftMode, "*", "acme.validation.unpublishedFlightAssignment.message");
	}

	@Override
	public void perform(final ActivityLog log) {
		log.setDraftMode(false);
		this.repository.save(log);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;

		dataset = super.unbindObject(log, "registrationMoment", "typeOfIncident", "description", "severityLevel", "flightAssignment", "draftMode");

		super.getResponse().addData(dataset);
	}

}
