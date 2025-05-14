
package acme.features.flightCrew.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activitylog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrew;

@GuiService
public class FlightCrewActivityLogCreateService extends AbstractGuiService<FlightCrew, ActivityLog> {

	@Autowired
	FlightCrewActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int assignmentId;
		FlightAssignment assignment;
		FlightCrew member;

		if (super.getRequest().hasData("id")) {

			assignmentId = super.getRequest().getData("assignmentId", int.class);
			assignment = this.repository.getAssignmentById(assignmentId);
			member = assignment == null ? null : assignment.getFlightCrewMember();
			status = super.getRequest().getPrincipal().hasRealm(member) && assignment != null;

			super.getResponse().setAuthorised(status);

		} else
			super.getResponse().setAuthorised(true);

	}

	@Override
	public void load() {
		ActivityLog log;
		int assignmentId;
		FlightAssignment assignment;

		assignmentId = super.getRequest().getData("assignmentId", int.class);
		assignment = this.repository.getAssignmentById(assignmentId);

		log = new ActivityLog();
		log.setDescription("");
		log.setRegistrationMoment(null);
		log.setSeverityLevel(null);
		log.setTypeOfIncident("");
		log.setDraftMode(true);
		log.setFlightAssignment(assignment);

		super.getBuffer().addData(log);
		super.getResponse().addGlobal("assignmentId", assignmentId);

	}

	@Override
	public void bind(final ActivityLog log) {
		super.bindObject(log, "registrationMoment", "typeOfIncident", "description", "severityLevel");
	}

	@Override
	public void validate(final ActivityLog log) {
		;
	}

	@Override
	public void perform(final ActivityLog log) {
		log.setDraftMode(true);
		this.repository.save(log);
	}

	@Override
	public void unbind(final ActivityLog log) {
		assert log != null;
		Dataset dataset;
		int assignmentId;

		assignmentId = super.getRequest().getData("assignmentId", int.class);
		dataset = super.unbindObject(log, "registrationMoment", "typeOfIncident", "description", "severityLevel");
		super.getResponse().addGlobal("assignmentId", assignmentId);
		super.getResponse().addData(dataset);
	}

}
