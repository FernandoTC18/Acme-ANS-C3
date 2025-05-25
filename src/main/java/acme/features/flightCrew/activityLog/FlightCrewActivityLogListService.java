
package acme.features.flightCrew.activityLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activitylog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrew;

@GuiService
public class FlightCrewActivityLogListService extends AbstractGuiService<FlightCrew, ActivityLog> {

	@Autowired
	private FlightCrewActivityLogRepository repository;


	@Override
	public void authorise() {
		int assignmentId;
		FlightAssignment assignment;
		FlightCrew member;
		boolean status;

		boolean correctMember;
		assignmentId = super.getRequest().getData("id", int.class);
		assignment = this.repository.getAssignmentById(assignmentId);
		member = assignment == null ? null : assignment.getFlightCrewMember();
		correctMember = member == null ? false : super.getRequest().getPrincipal().hasRealm(member);

		status = correctMember;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<ActivityLog> logs;
		int assignmentId;

		assignmentId = super.getRequest().getData("id", int.class);
		logs = this.repository.getLogsByAssignmentId(assignmentId);

		super.getResponse().addGlobal("assignmentId", assignmentId);
		super.getBuffer().addData(logs);
	}

	@Override
	public void unbind(final ActivityLog al) {
		Dataset dataset;
		int assignmentId;

		assignmentId = super.getRequest().getData("id", int.class);

		dataset = super.unbindObject(al, "flightAssignment", "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");
		super.getResponse().addGlobal("assignmentId", assignmentId);
		super.getResponse().addData(dataset);
	}

}
