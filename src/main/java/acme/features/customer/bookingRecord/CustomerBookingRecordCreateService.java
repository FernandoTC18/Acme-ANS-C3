
package acme.features.customer.bookingRecord;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.bookingRecord.BookingRecord;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerBookingRecordCreateService extends AbstractGuiService<Customer, BookingRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = true;
		boolean correctBooking = true;
		boolean correctPassenger = true;

		Booking booking;
		int bookingId;

		bookingId = super.getRequest().getData("bookingId", int.class);
		booking = this.repository.findBookingById(bookingId);

		if (super.getRequest().hasData("id")) {

			if (super.getRequest().hasData("passenger")) {
				int passengerId = super.getRequest().getData("passenger", int.class);
				if (passengerId != 0) {
					Passenger passenger = this.repository.findPassengerById(passengerId);
					List<Passenger> passengersFromBooking = this.repository.findPassengersByBookingId(bookingId);
					correctPassenger = passenger != null && !passengersFromBooking.contains(passenger) && super.getRequest().getPrincipal().hasRealm(passenger.getCustomer());
				}
			}

			if (super.getRequest().hasData("booking")) {
				String bookingLocator;
				bookingLocator = super.getRequest().getData("booking", String.class);
				Booking bookingFromLocator = this.repository.findBookingByLocatorCode(bookingLocator);
				correctBooking = bookingFromLocator != null && booking.equals(bookingFromLocator);
			}
		}

		status = booking != null && super.getRequest().getPrincipal().hasRealm(booking.getCustomer()) && booking.getDraftMode() && correctPassenger && correctBooking;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		BookingRecord bookingRecord;
		int bookingId;
		Booking booking;

		bookingId = super.getRequest().getData("bookingId", int.class);
		booking = this.repository.findBookingById(bookingId);

		bookingRecord = new BookingRecord();
		bookingRecord.setPassenger(null);
		bookingRecord.setBooking(booking);

		super.getBuffer().addData(bookingRecord);
		super.getResponse().addGlobal("bookingId", bookingId);
	}

	@Override
	public void bind(final BookingRecord bookingRecord) {
		int bookingId;
		Booking booking;

		bookingId = super.getRequest().getData("bookingId", int.class);
		booking = this.repository.findBookingById(bookingId);

		super.bindObject(bookingRecord, "passenger");
		bookingRecord.setBooking(booking);

	}

	@Override
	public void validate(final BookingRecord bookingRecord) {
		;
	}

	@Override
	public void perform(final BookingRecord bookingRecord) {
		this.repository.save(bookingRecord);
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Dataset dataset;
		int bookingId;
		List<Passenger> available;
		SelectChoices passengers;
		Booking booking;

		bookingId = super.getRequest().getData("bookingId", int.class);

		available = this.repository.findAvailablePassengers(bookingId);
		passengers = SelectChoices.from(available, "name", bookingRecord.getPassenger());
		dataset = super.unbindObject(bookingRecord, "booking");
		booking = this.repository.findBookingById(bookingId);

		dataset.put("booking", booking.getLocatorCode());
		dataset.put("passenger", passengers.getSelected().getKey());
		dataset.put("passengers", passengers);

		super.getResponse().addGlobal("bookingId", bookingId);
		super.getResponse().addData(dataset);
	}
}
