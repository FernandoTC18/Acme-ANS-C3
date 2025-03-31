
package acme.features.flightCrew.member;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.realms.FlightCrew;

@GuiService
public class FlightCrewMemberListService extends AbstractGuiService<FlightCrew, FlightCrew> {

	@Autowired
	private FlightCrewMemberRepository repository;


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(FlightCrew.class);

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

		dataset = super.unbindObject(member, "employeeCode", "phoneNumber", "languageSkills", "availability", "salary", "experienceYears");

		super.getResponse().addData(dataset);
	}
}
