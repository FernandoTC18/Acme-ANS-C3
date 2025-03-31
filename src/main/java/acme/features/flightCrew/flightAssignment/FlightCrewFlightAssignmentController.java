
package acme.features.flightCrew.flightAssignment;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrew;

@GuiController
public class FlightCrewFlightAssignmentController extends AbstractGuiController<FlightCrew, FlightAssignment> {

	@Autowired
	private FlightCrewFlightAssignmentListCompletedService		completedListService;

	@Autowired
	private FlightCrewFlightAssignmentListUncompletedService	uncompletedListService;

	@Autowired
	private FlightCrewFlightAssignmentUpdateService				updateService;
	
	@Autowired
	private FlightCrewFlightAssignmentCreateService 			createService;
	
	@Autowired
	private FlightCrewFlightAssignmentPublishService 			publishService;
	
	@Autowired
	private FlightCrewFlightAssignmentDeleteService 			deleteService;

	@Autowired
	private FlightCrewFlightAssignmentShowService				showService;


	@PostConstruct
	protected void initialise() {

		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("completed-list", "list", this.completedListService);
		super.addCustomCommand("uncompleted-list", "list", this.uncompletedListService);
		super.addCustomCommand("publish", "update", this.publishService);
	}

}
