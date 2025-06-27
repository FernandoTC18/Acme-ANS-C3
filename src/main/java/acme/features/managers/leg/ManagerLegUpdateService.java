
package acme.features.managers.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.airline.Airline;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.entities.leg.LegStatus;
import acme.realms.Manager;

@GuiService
public class ManagerLegUpdateService extends AbstractGuiService<Manager, Leg> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerLegRepository repository;


	// AbstractGuiService interface -------------------------------------------
	@Override
	public void authorise() {
		boolean status = true;

		boolean correctDepartureAirport = true;
		boolean correctArrivalAirport = true;
		boolean correctPlane = true;
		boolean correctAirline = true;

		int legId;
		Leg leg;
		Flight flight;
		Manager manager;

		legId = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(legId);

		flight = leg == null ? null : leg.getFlight();
		manager = flight == null ? null : flight.getManager();

		if (super.getRequest().getMethod().equals("GET"))
			status = leg != null && flight.isDraftMode() && super.getRequest().getPrincipal().hasRealm(manager);
		else {

			int departureAirportId = super.getRequest().getData("departureAirport", int.class);
			if (departureAirportId != 0) {
				Airport airport = this.repository.findAirportById(departureAirportId);
				correctDepartureAirport = airport != null;
			}

			int arrivalAirportId = super.getRequest().getData("arrivalAirport", int.class);
			if (arrivalAirportId != 0) {
				Airport airport = this.repository.findAirportById(arrivalAirportId);
				correctArrivalAirport = airport != null;
			}

			int planeId = super.getRequest().getData("plane", int.class);
			if (planeId != 0) {
				Aircraft aircraft = this.repository.findAircraftById(planeId);
				correctPlane = aircraft != null;
			}

			int airlineId = super.getRequest().getData("airline", int.class);
			if (airlineId != 0) {
				Airline airline = this.repository.findAirlineById(airlineId);
				correctAirline = airline != null;
			}
			status = leg != null && leg.isDraftMode() && super.getRequest().getPrincipal().hasRealm(manager) && correctDepartureAirport && correctArrivalAirport && correctPlane && correctAirline;
		}
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
		int airlineId;

		Airport departureAirport;
		Airport arrivalAirport;
		Aircraft plane;
		Airline airline;

		departureAirportId = super.getRequest().getData("departureAirport", int.class);
		departureAirport = this.repository.findAirportById(departureAirportId);

		arrivalAirportId = super.getRequest().getData("arrivalAirport", int.class);
		arrivalAirport = this.repository.findAirportById(arrivalAirportId);

		planeId = super.getRequest().getData("plane", int.class);
		plane = this.repository.findAircraftById(planeId);

		airlineId = super.getRequest().getData("airline", int.class);
		airline = this.repository.findAirlineById(airlineId);

		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status");
		leg.setArrivalAirport(arrivalAirport);
		leg.setDepartureAirport(departureAirport);
		leg.setPlane(plane);
		leg.setAirline(airline);
	}

	@Override
	public void validate(final Leg leg) {

	}

	@Override
	public void perform(final Leg leg) {
		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;
		Collection<Airport> airports;
		Collection<Aircraft> planes;
		Collection<Airline> airlines;
		SelectChoices choicesDepartureAirport;
		SelectChoices choicesArrivalAirport;
		SelectChoices choicesPlane;
		SelectChoices choicesStatus;
		SelectChoices choicesAirline;

		airports = this.repository.findAllAirports();
		planes = this.repository.findAllPlanes();
		airlines = this.repository.findAllAirlines();

		choicesDepartureAirport = SelectChoices.from(airports, "iataCode", leg.getDepartureAirport());
		choicesArrivalAirport = SelectChoices.from(airports, "iataCode", leg.getArrivalAirport());
		choicesPlane = SelectChoices.from(planes, "registrationNumber", leg.getPlane());
		choicesStatus = SelectChoices.from(LegStatus.class, leg.getStatus());
		choicesAirline = SelectChoices.from(airlines, "iataCode", leg.getAirline());

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "duration", "status", "draftMode");

		dataset.put("departureAirport", choicesDepartureAirport.getSelected().getKey());
		dataset.put("arrivalAirport", choicesArrivalAirport.getSelected().getKey());
		dataset.put("plane", choicesPlane.getSelected().getKey());
		dataset.put("airline", choicesAirline.getSelected().getKey());

		dataset.put("departureAirports", choicesDepartureAirport);
		dataset.put("arrivalAirports", choicesArrivalAirport);
		dataset.put("planes", choicesPlane);
		dataset.put("statusOptions", choicesStatus);
		dataset.put("airlines", choicesAirline);

		dataset.put("masterId", leg.getFlight().getId());

		super.getResponse().addData(dataset);

	}
}
