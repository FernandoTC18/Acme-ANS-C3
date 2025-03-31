
package acme.features.flightCrew.flightAssignment;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

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
import acme.realms.FlightCrewAvailability;

@GuiService
public class FlightCrewFlightAssignmentUpdateService extends AbstractGuiService<FlightCrew, FlightAssignment> {

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
		System.out.println(assignment);

		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		super.bindObject(assignment, "duty", "lastUpdate", "status", "remarks", "leg", "flightCrewMember", "draftMode");
		System.out.println(assignment);
	}

	@Override
	public void validate(final FlightAssignment assignment) {

		FlightCrew member;
		Collection<Leg> legs;
		Long pilotNumber;
		Long copilotNumber;

		super.state(assignment.getDuty() == Duty.LEAD_ATTENDANT, "duty", "acme.validation.not-lead-attendant.message");

		member = super.getRequest().getData("flightCrewMember", FlightCrew.class);
		super.state(member.getAvailability() == FlightCrewAvailability.AVAILABLE, "flightCrewMember", "acme.validation.unavailable-crew-member.message");

		Date currentMoment = MomentHelper.getCurrentMoment();
		Timestamp moment = Timestamp.from(currentMoment.toInstant());
		legs = this.repository.getLegsByMemberId(moment, member.getId());
		super.state(legs.isEmpty(), "leg", "acme.validation.assigned-leg.message");

		pilotNumber = this.repository.countMembersByDuty(member.getId(), Optional.of(Duty.PILOT));
		copilotNumber = this.repository.countMembersByDuty(member.getId(), Optional.of(Duty.COPILOT));

		if (assignment.getDuty() == Duty.PILOT)
			super.state(pilotNumber < 1, "flightCrewMember", "acme.validation.number-of-pilot.message");

		if (assignment.getDuty() == Duty.COPILOT)
			super.state(copilotNumber < 1, "flightCrewMember", "acme.validation.number-of-copilot.message");
	}

	@Override
	public void perform(final FlightAssignment assignment) {
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
		if (assignment.getDuty() != Duty.LEAD_ATTENDANT || assignment.getDraftMode() != false)
			dataset.put("readonly", true);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("status", statusChoices);
		dataset.put("duty", dutyChoices);
		dataset.put("flightCrewMember", memberChoices);

		super.getResponse().addData(dataset);

	}

}
