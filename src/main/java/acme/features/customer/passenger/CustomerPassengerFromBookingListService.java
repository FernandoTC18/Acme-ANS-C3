
package acme.features.customer.passenger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerFromBookingListService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private CustomerPassengerRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		List<Passenger> passengers;
		int bookingId;

		bookingId = super.getRequest().getData("bookingId", int.class);
		passengers = this.repository.findPassengersByBookingId(bookingId);

		super.getResponse().addGlobal("bookingId", bookingId);
		super.getBuffer().addData(passengers);

	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;
		int bookingId;
		Booking booking;

		bookingId = super.getRequest().getData("bookingId", int.class);
		booking = this.repository.findBookingById(bookingId);

		dataset = super.unbindObject(passenger, "name", "email", "passportNumber", "birth", "specialNeeds");

		super.getResponse().addGlobal("isPublished", !booking.getDraftMode());
		super.getResponse().addGlobal("bookingId", bookingId);
		super.getResponse().addData(dataset);
	}
}
