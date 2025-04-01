package acme.features.flightCrew.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.entities.leg.LegStatus;
import acme.realms.FlightCrew;

@GuiService
public class FlightCrewLegShowService extends AbstractGuiService<FlightCrew,Leg> {
	
	
	@Autowired
	private FlightCrewLegRepository repository;
	
	@Override
	public void authorise() {
		boolean status;
		int id;
		FlightCrew member;
		FlightAssignment assignment;
		
		String unformattedId = super.getRequest().getData("assignmentId", String.class);
		String [] splits = unformattedId.split("\\?");
		id = Integer.parseInt(splits[0]);
		assignment = this.repository.getAssignmentById(id);
		member = assignment == null ? null : assignment.getFlightCrewMember();
		status = member != null && assignment != null && super.getRequest().getPrincipal().hasRealm(member);
		super.getResponse().setAuthorised(status);
			
	}
	
	@Override
	public void load() {
		Leg leg;
		int id;
		
		String unformattedId = super.getRequest().getData("assignmentId", String.class);
		String [] splits = unformattedId.split("\\?");
		id = Integer.parseInt(splits[1].replace("id=", ""));
		leg = this.repository.getLegById(id);
		
		super.getBuffer().addData(leg);
		
	}
	
	@Override
	public void unbind(final Leg leg) {
		assert leg != null;
		Dataset dataset;
		Airport arrivalAirport;
		Airport departureAirport;
		Aircraft plane;
		SelectChoices statusChoices;

		
		arrivalAirport = this.repository.getArrivalAirportbyLegId(leg.getId());
		departureAirport = this.repository.getDepartureAirportbyLegId(leg.getId());
		plane = this.repository.getPlanebyLegId(leg.getId());
		statusChoices = SelectChoices.from(LegStatus.class, leg.getStatus());
		
		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "draftMode");
		dataset.put("arrivalAirport", arrivalAirport.getIataCode());
		dataset.put("departureAirport", departureAirport.getIataCode());
		dataset.put("plane", plane.getRegistrationNumber());
		dataset.put("statuses", statusChoices);
		dataset.put("readonly", true);
		
		super.getResponse().addData(dataset);
		
	}


}
