package acme.features.flightCrew.member;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrew;
import acme.realms.FlightCrewAvailability;
import ch.qos.logback.core.status.Status;

@GuiService
public class FlightCrewMemberShowService extends AbstractGuiService<FlightCrew,FlightCrew> {
	
	
	@Autowired
	private FlightCrewMemberRepository repository;
	
	@Override
	public void authorise() {
		boolean status;
		boolean assignmentIsFromMember = false;
		int memberId;
		FlightCrew member;
		Collection<FlightAssignment> assignments;
		
		memberId = super.getRequest().getData("id", int.class);
		assignments = this.repository.getAssignmentsByMemberId(memberId);
		
		for (FlightAssignment a : assignments) {
			member = a.getFlightCrewMember();
			
			if (super.getRequest().getPrincipal().hasRealm(member)) {
				assignmentIsFromMember = true;
			} else {
				assignmentIsFromMember = false;
				break;
			}
			
			
			
		}
		
		status = assignmentIsFromMember;
		
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightCrew member;
		int memberId;

		memberId = super.getRequest().getData("id", int.class);
		member = this.repository.getMemberById(memberId);

		super.getBuffer().addData(member);
	}

	@Override
	public void unbind(final FlightCrew member) {
		Dataset dataset;
		int assignmentId;
		SelectChoices availabilityChoices;
		
		availabilityChoices = SelectChoices.from(FlightCrewAvailability.class, member.getAvailability());
		dataset = super.unbindObject(member, "employeeCode", "phoneNumber", "languageSkills", "availability", "salary", "experienceYears");
		dataset.put("availability", availabilityChoices);
		
		

		super.getResponse().addData(dataset);
	}

}
