
package acme.features.managers.leg;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.claim.Claim;
import acme.entities.flight.Flight;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.entities.leg.LegStatus;
import acme.realms.Manager;

@GuiService
public class ManagerLegDeleteService extends AbstractGuiService<Manager, Leg> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerLegRepository repository;


	// AbstractGuiService interface -------------------------------------------
	@Override
	public void authorise() {
		boolean status;
		int legId;
		Leg leg;
		Flight flight;

		legId = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(legId);
		flight = leg.getFlight();
		status = flight != null && leg.isDraftMode() && super.getRequest().getPrincipal().hasRealm(flight.getManager());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Leg leg = this.repository.findLegById(id);

		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		int departureAirportId;
		int arrivalAirportId;
		int planeId;

		Airport departureAirport;
		Airport arrivalAirport;
		Aircraft plane;

		departureAirportId = super.getRequest().getData("departureAirport", int.class);
		departureAirport = this.repository.findAirportById(departureAirportId);

		arrivalAirportId = super.getRequest().getData("arrivalAirport", int.class);
		arrivalAirport = this.repository.findAirportById(arrivalAirportId);

		planeId = super.getRequest().getData("plane", int.class);
		plane = this.repository.findAircraftById(planeId);

		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status");
		leg.setArrivalAirport(arrivalAirport);
		leg.setDepartureAirport(departureAirport);
		leg.setPlane(plane);
	}

	@Override
	public void validate(final Leg leg) {
		List<Claim> claims;
		List<FlightAssignment> flightAssignments;

		boolean noPublishedClaims = true;
		boolean noPublishedFlightAssignments = true;
		{
			claims = this.repository.findClaimsByLegId(leg.getId());

			for (int i = 0; i < claims.size(); i++)

				noPublishedClaims = noPublishedClaims && claims.get(i).getDraftMode();

			super.state(noPublishedClaims, "*", "acme.validation.leg.noPublishedClaims.message");
		}
		{
			flightAssignments = this.repository.findFlightAssignmentsByLegId(leg.getId());

			for (int i = 0; i < flightAssignments.size(); i++)

				noPublishedFlightAssignments = noPublishedFlightAssignments && flightAssignments.get(i).getDraftMode();

			super.state(noPublishedFlightAssignments, "*", "acme.validation.leg.noPublishedFlightAssignments.message");
		}
	}

	@Override
	public void perform(final Leg leg) {
		Collection<Claim> claims;
		Collection<FlightAssignment> flightAssignments;
		claims = this.repository.findClaimsByLegId(leg.getId());
		flightAssignments = this.repository.findFlightAssignmentsByLegId(leg.getId());
		this.repository.deleteAll(claims);
		this.repository.deleteAll(flightAssignments);
		this.repository.delete(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;
		Collection<Airport> airports;
		Collection<Aircraft> planes;
		SelectChoices choicesDepartureAirport;
		SelectChoices choicesArrivalAirport;
		SelectChoices choicesPlane;
		SelectChoices choicesStatus;

		airports = this.repository.findAllAirports();
		planes = this.repository.findAllPlanes();

		choicesDepartureAirport = SelectChoices.from(airports, "iataCode", leg.getDepartureAirport());
		choicesArrivalAirport = SelectChoices.from(airports, "iataCode", leg.getArrivalAirport());
		choicesPlane = SelectChoices.from(planes, "registrationNumber", leg.getPlane());
		choicesStatus = SelectChoices.from(LegStatus.class, leg.getStatus());

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "duration", "status", "draftMode");

		dataset.put("departureAirport", choicesDepartureAirport.getSelected().getKey());
		dataset.put("arrivalAirport", choicesArrivalAirport.getSelected().getKey());
		dataset.put("plane", choicesPlane.getSelected().getKey());

		dataset.put("departureAirports", choicesDepartureAirport);
		dataset.put("arrivalAirports", choicesArrivalAirport);
		dataset.put("planes", choicesPlane);
		dataset.put("statusOptions", choicesStatus);

		dataset.put("masterId", leg.getFlight().getId());

		super.getResponse().addData(dataset);

	}

}
