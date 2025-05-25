
package acme.features.flightCrew.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.realms.FlightCrew;

@GuiService
public class FlightCrewLegListService extends AbstractGuiService<FlightCrew, Leg> {

	@Autowired
	private FlightCrewLegRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int assignmentId;
		FlightCrew member;
		FlightAssignment assignment;

		assignmentId = super.getRequest().getData("id", int.class);
		assignment = this.repository.findAssignmentbyId(assignmentId);
		member = assignment == null ? null : assignment.getFlightCrewMember();
		status = member != null && super.getRequest().getPrincipal().hasRealm(member);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Leg> legs;
		int assignmentId;

		assignmentId = super.getRequest().getData("id", int.class);
		legs = this.repository.getLegsByAssignmentId(assignmentId);

		super.getBuffer().addData(legs);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;
		int assignmentId;

		assignmentId = super.getRequest().getData("id", int.class);
		dataset = super.unbindObject(leg, "flightNumber", "scheduledArrival", "scheduledDeparture", "status");

		super.getResponse().addGlobal("assignmentId", assignmentId);
		super.getResponse().addData(dataset);
	}

}
