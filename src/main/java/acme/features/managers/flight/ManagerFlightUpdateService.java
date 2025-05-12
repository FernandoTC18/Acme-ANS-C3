
package acme.features.managers.flight;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.realms.Manager;

@GuiService
public class ManagerFlightUpdateService extends AbstractGuiService<Manager, Flight> {
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
	public void bind(final Flight flight) {
		String selfTransferValueSelected;
		Boolean selfTransferValue;

		selfTransferValueSelected = super.getRequest().getData("selfTransferRequired", String.class);
		selfTransferValue = Boolean.valueOf(selfTransferValueSelected);
		super.bindObject(flight, "tag", "cost", "description");
		flight.setSelfTransferRequired(selfTransferValue);

	}
	@Override
	public void validate(final Flight flight) {

	}

	@Override
	public void perform(final Flight flight) {
		this.repository.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {

		Dataset dataset;

		dataset = super.unbindObject(flight, "tag", "selfTransferRequired", "cost", "description", "draftMode", "scheduledDeparture", "scheduledArrival", "originCity", "arrivalCity", "layoversNumber");

		super.getResponse().addData(dataset);
	}
}
