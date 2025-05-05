
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
public class FlightCrewFlightAssignmentCreateService extends AbstractGuiService<FlightCrew, FlightAssignment> {

	@Autowired
	FlightCrewFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		String employeeCode;
		FlightCrew member;
		Leg leg;
		boolean status;
	
		//When the create service is called via a button, the request has no id.
		//When it's called via a submit, the request has id = 0.
		if(super.getRequest().hasData("id")) { 
			
			//Checks if the correct member is accessing
			boolean correctMember;
			
			employeeCode = super.getRequest().getData("flightCrewMember",String.class);
			member = this.repository.findFlightCrewByCode(employeeCode);
			correctMember = member == null ? null : super.getRequest().getPrincipal().getActiveRealm().getId() == member.getId();
			
			//Checks if the leg is in the future and published
			boolean correctLeg;
			int legId;
			legId = super.getRequest().getData("leg", int.class);
			leg = this.repository.findLegById(legId);
			correctLeg = leg == null ? null : MomentHelper.isFuture(leg.getScheduledDeparture()) && !leg.isDraftMode();
			
			
			status = correctMember && correctLeg;
			
			super.getResponse().setAuthorised(status);

		} else { 
			super.getResponse().setAuthorised(true);
		}
		
		
	}

	@Override
	public void load() {
		FlightAssignment assignment;
		int id;
		FlightCrew member;
		
		id = super.getRequest().getPrincipal().getActiveRealm().getId();
		member = this.repository.findCrewById(id);
		

		assignment = new FlightAssignment();
		assignment.setDuty(null);
		assignment.setLastUpdate(MomentHelper.getCurrentMoment());
		assignment.setLeg(null);
		assignment.setRemarks(null);
		assignment.setStatus(null);
		assignment.setDraftMode(true);
		assignment.setFlightCrewMember(member);

		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		int legId;
		Leg leg;
		
		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);
		
		assignment.setLeg(leg);
		super.bindObject(assignment, "duty", "status", "remarks");
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		boolean confirmation;
		
		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
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
		SelectChoices legChoices;
		SelectChoices statusChoices;
		SelectChoices dutyChoices;

		legs = this.repository.findFutureAndPublishedLegs(MomentHelper.getCurrentMoment());
		legChoices = SelectChoices.from(legs, "flightNumber", assignment.getLeg());
		statusChoices = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());
		dutyChoices = SelectChoices.from(Duty.class, assignment.getDuty());

		dataset = super.unbindObject(assignment, "status", "remarks");
		dataset.put("confirmation", false);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("status", statusChoices);
		dataset.put("duty", dutyChoices);
		dataset.put("flightCrewMember", assignment.getFlightCrewMember().getEmployeeCode());
		dataset.put("lastUpdate", assignment.getLastUpdate());
		
		super.getResponse().addData(dataset);
		

	}
}
