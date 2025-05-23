
package acme.features.managers.flight;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.realms.Manager;

@GuiService
public class ManagerFlightDeleteService extends AbstractGuiService<Manager, Flight> {
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
		super.bindObject(flight, "tag", "selfTransferRequired", "cost", "description");

	}
	@Override
	public void validate(final Flight flight) {
		List<Leg> legs;
		List<Booking> bookings;

		boolean noPublishedLegs = true;
		boolean noPublishedBookings = true;
		{
			legs = this.repository.findLegsByFlightId(flight.getId());

			for (int i = 0; i < legs.size(); i++)

				noPublishedLegs = noPublishedLegs && legs.get(i).isDraftMode();

			super.state(noPublishedLegs, "*", "acme.validation.flight.noPublishedLegs.message");
		}
		{
			bookings = this.repository.findBookingsByFlightId(flight.getId());

			for (int i = 0; i < bookings.size(); i++)

				noPublishedBookings = noPublishedBookings && bookings.get(i).getDraftMode();

			super.state(noPublishedBookings, "*", "acme.validation.flight.noPublishedBookings.message");
		}
	}

	@Override
	public void perform(final Flight flight) {
		Collection<Leg> legs;
		Collection<Booking> bookings;
		legs = this.repository.findLegsByFlightId(flight.getId());
		bookings = this.repository.findBookingsByFlightId(flight.getId());
		this.repository.deleteAll(legs);
		this.repository.deleteAll(bookings);
		this.repository.delete(flight);

	}

	@Override
	public void unbind(final Flight flight) {

		Dataset dataset;

		dataset = super.unbindObject(flight, "tag", "selfTransferRequired", "cost", "description", "draftMode", "scheduledDeparture", "scheduledArrival", "originCity", "arrivalCity", "layoversNumber");

		super.getResponse().addData(dataset);
	}
}
