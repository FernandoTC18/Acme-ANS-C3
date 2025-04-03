package acme.features.flightCrew.activityLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activitylog.ActivityLog;
import acme.realms.FlightCrew;

@GuiService
public class FlightCrewActivityLogListService extends AbstractGuiService<FlightCrew, ActivityLog> {
	
	@Autowired
	private FlightCrewActivityLogRepository repository;


	@Override
    public void authorise() {
        super.getResponse().setAuthorised(super.getRequest().getPrincipal().hasRealmOfType(FlightCrew.class));
    }

    @Override
    public void load() {
        Collection<ActivityLog> logs;
        int assignmentId;

        assignmentId = super.getRequest().getData("id", int.class);
        logs = this.repository.getLogsByAssignmentId(assignmentId);
        
        super.getResponse().addGlobal("assignmentId", assignmentId);
        super.getBuffer().addData(logs);
    }

    @Override
    public void unbind(final ActivityLog al) {
        Dataset dataset;
        int assignmentId;
        
        assignmentId = super.getRequest().getData("id",int.class);
        
        dataset = super.unbindObject(al, "flightAssignment", "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");
        super.getResponse().addGlobal("assignmentId", assignmentId);
        super.getResponse().addData(dataset);
    }


}
