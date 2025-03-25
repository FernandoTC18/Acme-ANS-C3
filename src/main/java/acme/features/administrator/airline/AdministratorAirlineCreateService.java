package acme.features.administrator.airline;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;
import acme.entities.airline.AirlineType;

@GuiService
public class AdministratorAirlineCreateService extends AbstractGuiService<Administrator,Airline> {
	
	@Autowired
	private AdministratorAirlineRepository repository;
	
	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}
	
	@Override
	public void load() {
		Airline airline;
		
		airline = new Airline();
		
		super.getBuffer().addData(airline);
	}
	
	@Override
	public void bind(final Airline airline) {
		super.bindObject(airline, "name", "iataCode", "website", "type", "foundation", "email", "phoneNumber");
		
	}
	
	@Override
	public void validate(final Airline airline) {
		boolean confirmation;
		
		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}
	
	@Override
	public void perform(final Airline airline) {
		this.repository.save(airline);
	}
	
	@Override
	public void unbind(final Airline airline) {
		Dataset dataset;
		SelectChoices airlineTypes;
		
		airlineTypes = SelectChoices.from(AirlineType.class, airline.getType());
		
		dataset = super.unbindObject(airline, "name", "iataCode", "website", "type", "foundation", "email", "phoneNumber");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("types", airlineTypes);
		
		super.getResponse().addData(dataset);
	}

}
