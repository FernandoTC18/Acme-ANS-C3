
package acme.features.flightCrew.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrew;

@GuiService
public class FlightCrewFlightAssignmentListCompletedService extends AbstractGuiService<FlightCrew, FlightAssignment> {

	@Autowired
	private FlightCrewFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(FlightCrew.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<FlightAssignment> flightAssignment;
		int memberId;

		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		flightAssignment = this.repository.getCompletedAssignmentsByMemberId(memberId, MomentHelper.getCurrentMoment());

		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;

		dataset = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks");

		super.getResponse().addData(dataset);
	}

}
