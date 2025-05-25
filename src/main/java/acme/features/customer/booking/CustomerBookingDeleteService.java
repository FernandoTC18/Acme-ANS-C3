
package acme.features.customer.booking;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.entities.bookingRecord.BookingRecord;
import acme.entities.flight.Flight;
import acme.realms.Customer;

@GuiService
public class CustomerBookingDeleteService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository repository;


	@Override
	public void authorise() {
		boolean status;
		boolean correctFlight = true;
		boolean flightNotPublished = true;
		int bookingId;
		int flightId;
		Booking booking;
		Customer customer;

		bookingId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(bookingId);
		customer = booking == null ? null : booking.getCustomer();

		if (super.getRequest().hasData("id"))
			if (super.getRequest().hasData("flight")) {
				flightId = super.getRequest().getData("flight", int.class);
				if (flightId != 0) {
					Flight flight = this.repository.findFlightById(flightId);
					correctFlight = flight != null && flight.getScheduledDeparture().after(MomentHelper.getCurrentMoment());
					flightNotPublished = flight != null && !flight.isDraftMode();
				}
				super.getRequest().getData("travelClass", TravelClass.class);
			}

		status = booking != null && super.getRequest().getPrincipal().hasRealm(customer) && booking.getDraftMode() && correctFlight && flightNotPublished;

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
		int customerId;
		Flight flight;
		Customer customer;

		flightId = super.getRequest().getData("flight", int.class);
		flight = this.repository.findFlightById(flightId);

		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		customer = this.repository.findCustomerById(customerId);

		super.bindObject(booking, "locatorCode", "travelClass", "lastCardNibble");
		booking.setFlight(flight);
		booking.setCustomer(customer);
		booking.setPurchaseMoment(MomentHelper.getCurrentMoment());
	}

	@Override
	public void validate(final Booking booking) {
		;
	}

	@Override
	public void perform(final Booking booking) {
		List<BookingRecord> bookingRecords;

		bookingRecords = this.repository.findBookingRecordsByBookingId(booking.getId());
		this.repository.deleteAll(bookingRecords);
		this.repository.delete(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		;
	}

}
