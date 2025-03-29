
package acme.features.managers.flight;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.realms.Manager;

@GuiService
public class ManagerFlightCreateService extends AbstractGuiService<Manager, Flight> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerFlightRepository repository;


	// AbstractGuiService interface -------------------------------------------
	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Flight flight;
		Manager manager;

		manager = (Manager) super.getRequest().getPrincipal().getActiveRealm();

		flight = new Flight();
		flight.setDraftMode(true);
		flight.setManager(manager);

		super.getBuffer().addData(flight);
	}
	@Override
	public void bind(final Flight flight) {

		String selfTransferValueSelected;
		Boolean selfTransferValue;

		selfTransferValueSelected = super.getRequest().getData("selfTransferRequired", String.class);

		selfTransferValue = selfTransferValueSelected != null ? Boolean.valueOf(selfTransferValueSelected) : false;

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

		SelectChoices choices;
		Dataset dataset;

		choices = new SelectChoices();

		boolean currentValue = flight.getSelfTransferRequired() != null ? flight.getSelfTransferRequired() : false;

		choices = new SelectChoices();

		choices.add("true", "Yes", currentValue == true);
		choices.add("false", "No", currentValue == false);

		if (choices.getSelected() == null)
			choices.add("false", "No", true);

		dataset = super.unbindObject(flight, "tag", "cost", "description");
		dataset.put("selfTransferRequired", choices.getSelected().getKey());
		dataset.put("selfTransferOptions", choices);

		super.getResponse().addData(dataset);
	}
}
