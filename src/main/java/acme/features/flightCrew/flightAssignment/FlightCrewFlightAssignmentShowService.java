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
public class FlightCrewFlightAssignmentShowService extends AbstractGuiService<FlightCrew, FlightAssignment> {
	
	@Autowired
	private FlightCrewFlightAssignmentRepository repository;
	
	@Override
	public void authorise() {
		boolean status;
		int assignmentId;
		FlightCrew member;
		FlightAssignment assignment;
		
		assignmentId = super.getRequest().getData("id", int.class);
		assignment = this.repository.findAssignmentbyId(assignmentId);
		member = assignment == null ? null : assignment.getFlightCrewMember();
		status = super.getRequest().getPrincipal().hasRealm(member) && assignment != null;
		
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
	public void unbind(final FlightAssignment assignment) {
		assert assignment != null;
		Dataset dataset;
		Collection<Leg> legs;
		SelectChoices legChoices;
		SelectChoices statusChoices;
		SelectChoices dutyChoices;
		
		if (assignment.getDraftMode()) {
			legs = this.repository.findAllLegs();
		} else {
			legs = this.repository.findFutureAndPublishedLegs(MomentHelper.getCurrentMoment());
		}
		
		
		legChoices = SelectChoices.from(legs,"flightNumber", assignment.getLeg());
		statusChoices = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());
		dutyChoices = SelectChoices.from(Duty.class, assignment.getDuty());
		
		dataset = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks", "draftMode");
		
		dataset.put("readonly", !assignment.getDraftMode());
		dataset.put("lastUpdate", assignment.getLastUpdate());
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("status", statusChoices);
		dataset.put("duty", dutyChoices);
		dataset.put("flightCrewMember", assignment.getFlightCrewMember().getEmployeeCode());
		
		super.getResponse().addData(dataset);
		
	}

}
