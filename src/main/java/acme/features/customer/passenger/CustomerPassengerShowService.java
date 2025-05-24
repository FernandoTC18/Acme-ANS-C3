
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerShowService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private CustomerPassengerRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int passengerId;
		Passenger passenger;

		passengerId = super.getRequest().getData("id", int.class);
		passenger = this.repository.findPassengerById(passengerId);
		status = passenger != null && super.getRequest().getPrincipal().hasRealm(passenger.getCustomer());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Passenger passenger;
		int id;

		id = super.getRequest().getData("id", int.class);
		passenger = this.repository.findPassengerById(id);

		super.getBuffer().addData(passenger);

	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;
		boolean canBeDeleted;

		canBeDeleted = this.repository.findBookingByPassengerId(passenger.getId()).isEmpty();

		dataset = super.unbindObject(passenger, "name", "email", "passportNumber", "birth", "specialNeeds");
		dataset.put("readonly", !passenger.getDraftMode());
		dataset.put("canBeDeleted", canBeDeleted);
		super.getResponse().addData(dataset);
	}

}
