
package acme.features.customer.booking;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.entities.flight.Flight;
import acme.realms.Customer;

@GuiService
public class CustomerBookingUpdateService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		boolean correctFlight = true;
		int bookingId;
		Booking booking;
		Customer customer;

		bookingId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(bookingId);
		customer = booking == null ? null : booking.getCustomer();

		if (bookingId != 0 && super.getRequest().hasData("id"))
			if (super.getRequest().hasData("flight")) {
				int flightId = super.getRequest().getData("flight", int.class);
				if (flightId != 0) {
					Flight flight = this.repository.findFlightById(flightId);
					correctFlight = flight != null && flight.getScheduledDeparture().after(MomentHelper.getCurrentMoment()) && !flight.isDraftMode();
				}
			}

		status = super.getRequest().getPrincipal().hasRealm(customer) && booking != null && booking.getDraftMode() && correctFlight;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Booking booking;
		int id;

		id = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(id);

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		int flightId;
		Flight flight;

		flightId = super.getRequest().getData("flight", int.class);
		flight = this.repository.findFlightById(flightId);

		super.bindObject(booking, "locatorCode", "travelClass", "lastCardNibble");
		booking.setFlight(flight);
	}

	@Override
	public void validate(final Booking booking) {
		;
	}

	@Override
	public void perform(final Booking booking) {
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		List<Flight> available;

		SelectChoices classes;
		SelectChoices flights;

		available = this.repository.findAllFlights().stream().filter(a -> a.getScheduledDeparture().after(MomentHelper.getCurrentMoment())).toList();
		classes = SelectChoices.from(TravelClass.class, booking.getTravelClass());

		flights = SelectChoices.from(available, "flightPath", booking.getFlight());

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "draftMode", "lastCardNibble", "flight");
		dataset.put("travelClass", classes);
		dataset.put("flight", flights.getSelected().getKey());
		dataset.put("flights", flights);
		dataset.put("readonly", !booking.getDraftMode());

		super.getResponse().addData(dataset);

	}
}
