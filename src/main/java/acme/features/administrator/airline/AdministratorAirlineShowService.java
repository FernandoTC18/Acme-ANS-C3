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
public class AdministratorAirlineShowService extends AbstractGuiService<Administrator,Airline> {
	
	@Autowired
	AdministratorAirlineRepository repository;
	
	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true); 
	}
	
	@Override
	public void load() {
		Airline airline;
		int id;
		
		id = super.getRequest().getData("id", int.class);
		airline = this.repository.findAirlineById(id);
		
		super.getBuffer().addData(airline);
		
	}
	
	@Override
	public void unbind(final Airline airline) {
		assert airline != null;
		Dataset dataset;
		SelectChoices airlineTypes;
		
		
		airlineTypes = SelectChoices.from(AirlineType.class, airline.getType());
		dataset = super.unbindObject(airline, "name", "iataCode", "website", "type", "foundation", "email", "phoneNumber");
		
		super.getResponse().addData(dataset);
		dataset.put("types", airlineTypes);
	}
	
	

}
