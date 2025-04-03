package acme.features.flightCrew.flightAssignment;


import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.AssignmentStatus;
import acme.entities.flightAssignment.Duty;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.realms.FlightCrew;

@GuiService
public class FlightCrewFlightAssignmentDeleteService extends AbstractGuiService<FlightCrew,FlightAssignment> {
	
	@Autowired
	FlightCrewFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		Boolean status = super.getRequest().getPrincipal().hasRealmOfType(FlightCrew.class);
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
		super.bindObject(assignment, "duty", "lastUpdate", "status", "remarks", "leg", "flightCrewMember");
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		int id;
		FlightAssignment flightAssignment;
		
		id = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findAssignmentbyId(id);
		
		super.state(flightAssignment.getDraftMode() == true, "draftMode", "acme-validation-assignment-published");
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		this.repository.delete(assignment);

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

		dataset = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks", "leg", "flightCrewMember");
		dataset.put("readonly", !assignment.getDraftMode());
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("status", statusChoices);
		dataset.put("duty", dutyChoices);
		dataset.put("flightCrewMember", memberChoices);

		super.getResponse().addData(dataset);

	}

}
