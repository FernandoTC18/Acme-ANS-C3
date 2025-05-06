package acme.features.flightCrew.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activitylog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrew;

@GuiService
public class FlightCrewActivityLogPublishService extends AbstractGuiService<FlightCrew,ActivityLog> {
	
	
	@Autowired
	FlightCrewActivityLogRepository repository;
	
	
	@Override
    public void authorise() {
		boolean status;
		int logId;
		ActivityLog log;
		FlightAssignment assignment;
		FlightCrew member;
		
		
		boolean correctMember;
		logId = super.getRequest().getData("id", int.class);
		assignment = this.repository.getAssignmentByLogId(logId);
		member = assignment == null ? null : assignment.getFlightCrewMember();
		correctMember = super.getRequest().getPrincipal().hasRealm(member);
		
		if (correctMember == true) {
			
			boolean draftMode;
			log = this.repository.getLogById(logId);
			draftMode = log.getDraftMode();
			
			
			boolean assignmentIsPublished = assignment != null && assignment.getDraftMode();
			status = draftMode && !assignmentIsPublished; //I don't add correctMember 'cause in this branch of the if is always true, so it won't affect the result
			
			super.getResponse().setAuthorised(status);
			
		} else {
			
			super.getResponse().setAuthorised(false);
			
		}	
	}
	
	@Override
	public void load() {
		ActivityLog log;
		int id;
		
		id = super.getRequest().getData("id", int.class);
		log = this.repository.getLogById(id);
		
		super.getBuffer().addData(log);
		
	}
	

	@Override
	public void bind(final ActivityLog log) {
		super.bindObject(log, "registrationMoment", "typeOfIncident", "description", "severityLevel");
		
	}
	
	@Override
	public void validate(final ActivityLog log) {
		
	}
	
	@Override
	public void perform(final ActivityLog log) {
		log.setDraftMode(false);
		this.repository.save(log);
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
