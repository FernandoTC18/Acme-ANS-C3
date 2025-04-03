package acme.features.flightCrew.activityLog;



import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activitylog.ActivityLog;
import acme.realms.FlightCrew;

@GuiService
public class FlightCrewActivityLogShowService extends AbstractGuiService<FlightCrew,ActivityLog> {
	
	@Autowired
	private FlightCrewActivityLogRepository repository;
	
	@Override
	public void authorise() {
		boolean status;
		
		status = super.getRequest().getPrincipal().hasRealmOfType(FlightCrew.class);
		
		super.getResponse().setAuthorised(status);
	}
	
	@Override
	public void load() {
		ActivityLog log;
		int id;


		id = super.getRequest().getData("id",int.class);
		log = this.repository.getLogById(id);
		
		super.getBuffer().addData(log);
		
		
	}
	
	@Override
	public void unbind(final ActivityLog log) {
		assert log != null;
		Dataset dataset;	
        

		dataset = super.unbindObject(log, "registrationMoment", "typeOfIncident", "description", "severityLevel", "flightAssignment", "draftMode");

		super.getResponse().addData(dataset);
	}

}
