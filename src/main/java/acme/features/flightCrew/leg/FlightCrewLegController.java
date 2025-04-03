package acme.features.flightCrew.leg;


import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.leg.Leg;
import acme.realms.FlightCrew;

@GuiController
public class FlightCrewLegController extends AbstractGuiController<FlightCrew, Leg> {
	
	
	@Autowired
	private FlightCrewLegListService listService;
	
	@Autowired
	private FlightCrewLegShowService showService;
	
	
	@PostConstruct
	public void initialise() {
		
		super.addBasicCommand("list", listService);
		super.addBasicCommand("show", this.showService);
	}

}
