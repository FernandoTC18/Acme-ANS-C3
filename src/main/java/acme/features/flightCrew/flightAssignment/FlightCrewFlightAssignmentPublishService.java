
package acme.features.flightCrew.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.AssignmentStatus;
import acme.entities.flightAssignment.Duty;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.realms.FlightCrew;

@GuiService
public class FlightCrewFlightAssignmentPublishService extends AbstractGuiService<FlightCrew, FlightAssignment> {

	@Autowired
	FlightCrewFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int assignmentId;
		FlightCrew member;
		FlightAssignment assignment;
		
		assignmentId = super.getRequest().getData("id", int.class);
		assignment = this.repository.findAssignmentbyId(assignmentId);
		member = assignment == null ? null : assignment.getFlightCrewMember();
		status = super.getRequest().getPrincipal().hasRealm(member) && assignment != null && assignment.getDraftMode();
		
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment assignment;
		int id;

		id = super.getRequest().getData("id", int.class);
		assignment = this.repository.findAssignmentbyId(id);
		System.out.println(assignment);

		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		super.bindObject(assignment, "duty", "lastUpdate", "status", "remarks", "leg", "flightCrewMember");
		System.out.println(assignment);
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		int id;
		FlightAssignment flightAssignment;
		
		id = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findAssignmentbyId(id);
		
		super.state(MomentHelper.isAfter(flightAssignment.getLeg().getScheduledArrival(), MomentHelper.getCurrentMoment()), "draftMode", "acme-validation-assignment-published");
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		assignment.setDraftMode(false);
		this.repository.save(assignment);

	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		assert assignment != null;
		Dataset dataset;
		Collection<Leg> legs;
		Collection<FlightCrew> members;
		SelectChoices legChoices;
		SelectChoices statusChoices;
		SelectChoices dutyChoices;
		SelectChoices memberChoices;

		legs = this.repository.findAllLegs();
		members = this.repository.findAllMembers();
		legChoices = SelectChoices.from(legs, "flightNumber", assignment.getLeg());
		statusChoices = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());
		dutyChoices = SelectChoices.from(Duty.class, assignment.getDuty());
		memberChoices = SelectChoices.from(members, "employeeCode", assignment.getFlightCrewMember());

		dataset = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks", "leg", "flightCrewMember", "draftMode");
		
		dataset.put("readonly", !assignment.getDraftMode());
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("status", statusChoices);
		dataset.put("duty", dutyChoices);
		dataset.put("flightCrewMember", memberChoices);

		super.getResponse().addData(dataset);

	}

}
