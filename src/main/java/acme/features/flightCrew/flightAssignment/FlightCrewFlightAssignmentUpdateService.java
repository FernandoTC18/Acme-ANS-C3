
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
public class FlightCrewFlightAssignmentUpdateService extends AbstractGuiService<FlightCrew, FlightAssignment> {

	@Autowired
	FlightCrewFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		Leg leg;
		int assignmentId;
		FlightAssignment assignment;
		boolean status;

		//Checks if the correct member is accessing	
		boolean correctMember;
		boolean draftMode;
		assignmentId = super.getRequest().getData("id", int.class);
		assignment = this.repository.findAssignmentbyId(assignmentId);
		correctMember = assignment != null && assignment.getFlightCrewMember().getId() == super.getRequest().getPrincipal().getActiveRealm().getId();
		draftMode = assignment.getDraftMode();

		//If it is a hacking request, it can only contain the id in the dataset. This way i assure that a 401 code is returned instead of an AssertionError.
		if (correctMember) {

			if (super.getRequest().getMethod().equals("GET") && draftMode)
				//If its a GET a request, i'll allow it
				super.getResponse().setAuthorised(true);

			else {

				if (draftMode == false) {
					super.getResponse().setAuthorised(false);
					return;
				}

				//Checks if the leg is in the future
				boolean correctLeg = true;
				int legId;

				legId = super.getRequest().getData("leg", int.class);
				if (legId != 0) {
					leg = this.repository.findLegById(legId);
					correctLeg = leg != null && MomentHelper.isFuture(leg.getScheduledDeparture());
				}

				status = correctLeg && draftMode;

				super.getResponse().setAuthorised(status);
			}
		} else
			super.getResponse().setAuthorised(false);
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
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		assignment.setLeg(leg);
		super.bindObject(assignment, "duty", "status", "remarks");

	}

	@Override
	public void validate(final FlightAssignment assignment) {
		int legId = super.getRequest().getData("leg", int.class);
		Leg leg = this.repository.findLegById(legId);
		if (legId != 0) {
			boolean draftMode = leg.isDraftMode();

			super.state(!draftMode, "leg", "acme.validation.unpublishedLeg.message");

		}
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		this.repository.save(assignment);

	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;
		Collection<Leg> legs;

		SelectChoices legChoices;
		SelectChoices statusChoices;
		SelectChoices dutyChoices;

		legs = this.repository.findAllLegs();
		legChoices = SelectChoices.from(legs, "flightNumber", assignment.getLeg());
		statusChoices = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());
		dutyChoices = SelectChoices.from(Duty.class, assignment.getDuty());

		dataset = super.unbindObject(assignment, "duty", "status", "remarks", "draftMode");

		dataset.put("id", assignment.getId());
		dataset.put("lastUpdate", assignment.getLastUpdate());
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("status", statusChoices);
		dataset.put("duty", dutyChoices);
		dataset.put("flightCrewMember", assignment.getFlightCrewMember().getEmployeeCode());

		super.getResponse().addData(dataset);

	}

}
