
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerShowService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private CustomerPassengerRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Passenger passenger;
		String id;

		id = super.getRequest().getData("bookingId", String.class);
		String[] parts = id.split("\\?id=");
		int passengerId = Integer.parseInt(parts[1]);
		passenger = this.repository.findPassengerById(passengerId);

		super.getBuffer().addData(passenger);

	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;
		String id;
		Booking booking;

		id = super.getRequest().getData("bookingId", String.class);
		String[] parts = id.split("\\?id=");
		int bookingId = Integer.parseInt(parts[0]);
		booking = this.repository.findBookingById(bookingId);

		dataset = super.unbindObject(passenger, "name", "email", "passportNumber", "birth", "specialNeeds");
		dataset.put("readonly", booking.getDraftMode());

		super.getResponse().addData(dataset);
	}

}
