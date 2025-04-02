
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
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerBookingPublishService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int bookingId;
		Booking booking;
		Customer customer;

		bookingId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(bookingId);
		customer = booking == null ? null : booking.getCustomer();
		status = super.getRequest().getPrincipal().hasRealm(customer) || booking != null && !booking.getDraftMode();

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
		super.bindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "lastCardNibble");
	}

	@Override
	public void validate(final Booking booking) {
		{
			Boolean hasNibble;
			hasNibble = !booking.getLastCardNibble().isBlank() || booking.getLastCardNibble() == null;
			super.state(hasNibble, "lastCardNibble", "acme.validation.lastCardNibble.message");
		}
		{
			List<Passenger> passengers;
			Boolean hasPassengers;
			passengers = this.repository.findPassengersByBookingId(booking.getId());
			hasPassengers = !passengers.isEmpty();
			super.state(hasPassengers, "*", "acme.validation.not-has-passengers.message");
		}
		{
			List<Passenger> passengers;
			Boolean passengersArePublished = true;
			passengers = this.repository.findPassengersByBookingId(booking.getId());
			for (Passenger p : passengers)
				if (p.getDraftMode()) {
					passengersArePublished = false;
					break;
				}
			super.state(passengersArePublished, "*", "acme.validation.passengers-not-publish.message");
		}

	}

	@Override
	public void perform(final Booking booking) {
		booking.setDraftMode(false);
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
