
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
public class ManagerLegShowService extends AbstractGuiService<Manager, Leg> {
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

		if (leg != null) {
			flight = leg.getFlight();
			status = flight != null && flight.isDraftMode() || super.getRequest().getPrincipal().hasRealm(flight.getManager());
		} else
			status = false;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Leg leg = this.repository.findLegById(id);

		super.getBuffer().addData(leg);
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
