
package acme.features.flightCrew.member;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrew;

@GuiService
public class FlightCrewMemberListService extends AbstractGuiService<FlightCrew, FlightCrew> {

	@Autowired
	private FlightCrewMemberRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int assignmentId;
		FlightCrew member;
		FlightAssignment assignment;
		
		assignmentId = super.getRequest().getData("id", int.class);
		assignment = this.repository.findAssignmentbyId(assignmentId);
		member = assignment == null ? null : assignment.getFlightCrewMember();
		status = super.getRequest().getPrincipal().hasRealm(member) && assignment != null;
		
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<FlightCrew> crewMembers;
		int assignmentId;

		assignmentId = super.getRequest().getData("id", int.class);
		crewMembers = this.repository.findCrewMembersByAssignmentId(assignmentId);

		super.getBuffer().addData(crewMembers);
	}

	@Override
	public void unbind(final FlightCrew member) {
		Dataset dataset;
		int assignmentId;

		assignmentId = super.getRequest().getData("id", int.class);
		
		dataset = super.unbindObject(member, "employeeCode", "phoneNumber", "languageSkills", "availability", "salary", "experienceYears");
		super.getResponse().addGlobal("assignmentId", assignmentId);
		
		

		super.getResponse().addData(dataset);
	}
}
