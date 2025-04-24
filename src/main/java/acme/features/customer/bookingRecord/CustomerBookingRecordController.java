
package acme.features.customer.bookingRecord;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.bookingRecord.BookingRecord;
import acme.realms.Customer;

@GuiController
public class CustomerBookingRecordController extends AbstractGuiController<Customer, BookingRecord> {

	@Autowired
	private CustomerBookingRecordCreateService createService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.createService);
	}

}
