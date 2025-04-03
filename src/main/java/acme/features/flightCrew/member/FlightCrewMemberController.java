
package acme.features.flightCrew.member;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.realms.FlightCrew;

@GuiController
public class FlightCrewMemberController extends AbstractGuiController<FlightCrew, FlightCrew> {

	@Autowired
	private FlightCrewMemberListService listService;
	
	@Autowired FlightCrewMemberShowService showService;


	@PostConstruct
	public void initialise() {

		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		
	}
}
