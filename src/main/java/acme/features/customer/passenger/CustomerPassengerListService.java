
package acme.features.customer.passenger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerListService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private CustomerPassengerRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		List<Passenger> passengers;
		int customerId;

		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		passengers = this.repository.findPassengersByCustomerId(customerId);

		super.getBuffer().addData(passengers);

	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;

		dataset = super.unbindObject(passenger, "name", "email", "passportNumber", "birth", "specialNeeds");

		super.getResponse().addData(dataset);
	}
}
