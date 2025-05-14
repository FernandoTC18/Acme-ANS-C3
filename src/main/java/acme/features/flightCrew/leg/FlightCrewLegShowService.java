
package acme.features.flightCrew.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.leg.Leg;
import acme.entities.leg.LegStatus;
import acme.realms.FlightCrew;

@GuiService
public class FlightCrewLegShowService extends AbstractGuiService<FlightCrew, Leg> {

	@Autowired
	private FlightCrewLegRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int id;
		int memberId;
		Leg leg;
		Collection<Leg> legs;

		id = super.getRequest().getData("id", int.class);
		leg = this.repository.getLegById(id);
		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		legs = this.repository.getLegsByMemberId(memberId);

		if (leg == null)
			status = false;
		else
			status = legs.contains(leg) && id == leg.getId();

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Leg leg;
		int id;

		id = super.getRequest().getData("id", int.class);
		leg = this.repository.getLegById(id);

		super.getBuffer().addData(leg);

	}

	@Override
	public void unbind(final Leg leg) {
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
