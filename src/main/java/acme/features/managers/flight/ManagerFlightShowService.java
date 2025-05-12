
package acme.features.managers.flight;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.realms.Manager;

@GuiService
public class ManagerFlightShowService extends AbstractGuiService<Manager, Flight> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerFlightRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Flight flight;
		Manager manager;

		masterId = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(masterId);
		manager = flight == null ? null : flight.getManager();
		status = flight != null && flight.isDraftMode() && super.getRequest().getPrincipal().hasRealm(manager);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Flight flight = this.repository.findFlightById(id);

		super.getBuffer().addData(flight);
	}

	@Override
	public void unbind(final Flight flight) {

		SelectChoices choices;
		Dataset dataset;

		choices = new SelectChoices();
		choices.add("true", "Yes", flight.getSelfTransferRequired());
		choices.add("false", "No", !flight.getSelfTransferRequired());

		dataset = super.unbindObject(flight, "tag", "cost", "description", "draftMode", "scheduledDeparture", "scheduledArrival", "originCity", "arrivalCity", "layoversNumber");

		dataset.put("selfTransferRequired", flight.getSelfTransferRequired() ? "Yes" : "No");
		dataset.put("selfTransferOptions", choices);

		super.getResponse().addData(dataset);

	}

}
