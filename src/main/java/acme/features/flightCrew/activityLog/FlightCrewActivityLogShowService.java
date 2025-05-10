package acme.features.flightCrew.activityLog;



import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activitylog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrew;

@GuiService
public class FlightCrewActivityLogShowService extends AbstractGuiService<FlightCrew,ActivityLog> {
	
	@Autowired
	private FlightCrewActivityLogRepository repository;
	
	@Override
	public void authorise() {
		boolean status;
		int logId;
		ActivityLog log;
		FlightAssignment assignment;
		FlightCrew member;
		
		logId = super.getRequest().getData("id", int.class);
		log = this.repository.getLogById(logId);
		assignment = this.repository.getAssignmentByLogId(logId);
		member = assignment == null ? null : assignment.getFlightCrewMember();
		status = super.getRequest().getPrincipal().hasRealm(member) && log != null;
		
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
		dataset.put("readonly", !log.getDraftMode());

		super.getResponse().addData(dataset);
	}

}
