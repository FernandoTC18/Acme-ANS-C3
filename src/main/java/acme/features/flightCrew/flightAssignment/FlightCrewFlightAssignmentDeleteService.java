
package acme.features.flightCrew.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activitylog.ActivityLog;
import acme.entities.flightAssignment.AssignmentStatus;
import acme.entities.flightAssignment.Duty;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.realms.FlightCrew;

@GuiService
public class FlightCrewFlightAssignmentDeleteService extends AbstractGuiService<FlightCrew, FlightAssignment> {

	@Autowired
	FlightCrewFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		int assignmentId;
		FlightAssignment assignment;
		boolean status;

		//Checks if the correct member is accessing and if it is in draft mode
		boolean correctMember;
		boolean draftMode;

		assignmentId = super.getRequest().getData("id", int.class);
		assignment = this.repository.findAssignmentbyId(assignmentId);
		correctMember = assignment != null && assignment.getFlightCrewMember().getId() == super.getRequest().getPrincipal().getActiveRealm().getId();
		draftMode = correctMember && assignment != null ? assignment.getDraftMode() : false;

		status = correctMember && draftMode && MomentHelper.isFuture(assignment.getLeg().getScheduledDeparture());

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		FlightAssignment assignment;
		int id;

		id = super.getRequest().getData("id", int.class);
		assignment = this.repository.findAssignmentbyId(id);

		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		int legId;
		String employeeCode;
		Leg leg;
		FlightCrew member;

		legId = super.getRequest().getData("leg", int.class);
		employeeCode = super.getRequest().getData("flightCrewMember", String.class);

		leg = this.repository.findLegById(legId);
		member = this.repository.findFlightCrewByCode(employeeCode);

		assignment.setLeg(leg);
		assignment.setFlightCrewMember(member);
		super.bindObject(assignment, "duty", "lastUpdate", "status", "remarks");
	}

	@Override
	public void validate(final FlightAssignment assignment) {

	}

	@Override
	public void perform(final FlightAssignment assignment) {
		Collection<ActivityLog> relatedLogs = this.repository.findLogsByAssignmentId(assignment.getId());

		this.repository.deleteAll(relatedLogs);
		this.repository.delete(assignment);

	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;
		Collection<Leg> legs;
		SelectChoices legChoices;
		SelectChoices statusChoices;
		SelectChoices dutyChoices;

		if (MomentHelper.isPast(assignment.getLeg().getScheduledDeparture()))
			legs = this.repository.findAllLegs();
		else
			legs = this.repository.findFutureAndPublishedLegs(MomentHelper.getCurrentMoment());

		legChoices = SelectChoices.from(legs, "flightNumber", assignment.getLeg());
		statusChoices = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());
		dutyChoices = SelectChoices.from(Duty.class, assignment.getDuty());

		dataset = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks", "draftMode");
		dataset.put("lastUpdate", assignment.getLastUpdate());
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("status", statusChoices);
		dataset.put("duty", dutyChoices);
		dataset.put("flightCrewMember", assignment.getFlightCrewMember().getEmployeeCode());

		super.getResponse().addData(dataset);

	}

}
