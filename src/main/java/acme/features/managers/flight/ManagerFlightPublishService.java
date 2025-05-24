
package acme.features.managers.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.realms.Manager;

@GuiService
public class ManagerFlightPublishService extends AbstractGuiService<Manager, Flight> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerFlightRepository repository;


	// AbstractGuiService interface -------------------------------------------
	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Flight flight;
		Manager manager;

		masterId = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(masterId);
		manager = flight == null ? null : flight.getManager();
		status = flight != null && flight.isDraftMode() && super.getRequest().getPrincipal().hasRealm(manager);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Flight flight = this.repository.findFlightById(id);

		super.getBuffer().addData(flight);
	}
	@Override
	public void bind(final Flight flight) {
		super.bindObject(flight, "tag", "cost", "description", "selfTransferRequired");

	}

	@Override
	public void validate(final Flight flight) {
		{
			Boolean oneLegMin;
			Collection<Leg> legs;

			legs = this.repository.findLegsByFlightId(flight.getId());
			oneLegMin = legs != null && !legs.isEmpty();
			super.state(oneLegMin, "*", "acme.validation.flight.noLegs.message");
		}
		{

			boolean allLegsPublished;
			Collection<Leg> legs;
			int noPublishedFlights = 0;
			legs = this.repository.findLegsByFlightId(flight.getId());
			if (legs != null)

				for (Leg l : legs)
					if (l.isDraftMode())
						noPublishedFlights += 1;
			allLegsPublished = noPublishedFlights == 0;

			super.state(allLegsPublished, "*", "acme.validation.flight.noPublishedLegs.message");
		}
	}

	@Override
	public void perform(final Flight flight) {
		flight.setDraftMode(false);
		this.repository.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {

		Dataset dataset;

		dataset = super.unbindObject(flight, "tag", "selfTransferRequired", "cost", "description", "draftMode", "scheduledDeparture", "scheduledArrival", "originCity", "arrivalCity", "layoversNumber");

		super.getResponse().addData(dataset);

	}
}
