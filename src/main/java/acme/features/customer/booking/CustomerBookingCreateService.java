
package acme.features.customer.booking;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.entities.flight.Flight;
import acme.realms.Customer;

@GuiService
public class CustomerBookingCreateService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Booking booking;

		booking = new Booking();
		booking.setLocatorCode("");
		booking.setTravelClass(null);
		booking.setPrice(null);
		booking.setFlight(null);
		booking.setLastCardNibble("");
		booking.setDraftMode(true);

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		int flightId;
		int customerId;
		Flight flight;
		Customer customer;

		flightId = super.getRequest().getData("flight", int.class);
		flight = this.repository.findFlightById(flightId);

		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		customer = this.repository.findCustomerById(customerId);

		super.bindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "lastCardNibble");
		booking.setFlight(flight);
		booking.setCustomer(customer);
	}

	@Override
	public void validate(final Booking booking) {
		int flightId;
		Flight flight;
		Boolean validFlight = true;

		flightId = super.getRequest().getData("flight", int.class);
		flight = this.repository.findFlightById(flightId);
		if (flight == null)
			validFlight = false;
		super.state(validFlight, "flight", "acme.validation.booking.invalid-Flight-assigned.message");
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

		available = this.repository.findAllFlights();
		classes = SelectChoices.from(TravelClass.class, booking.getTravelClass());

		flights = SelectChoices.from(available, "tag", booking.getFlight());

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "draftMode", "lastCardNibble", "flight");
		dataset.put("travelClass", classes);
		dataset.put("flight", flights.getSelected().getKey());
		dataset.put("flights", flights);
		dataset.put("readonly", !booking.getDraftMode());

		super.getResponse().addData(dataset);
	}
}
