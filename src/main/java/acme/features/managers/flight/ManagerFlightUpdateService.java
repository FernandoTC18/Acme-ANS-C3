
package acme.features.managers.flight;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoice;
import acme.client.components.views.SelectChoices;
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

		SelectChoices choices;
		Dataset dataset;

		SelectChoice trueChoice = new SelectChoice();
		trueChoice.setKey("true");
		trueChoice.setLabel("Yes");
		trueChoice.setSelected(flight.getSelfTransferRequired() == true);

		SelectChoice falseChoice = new SelectChoice();
		falseChoice.setKey("false");
		falseChoice.setLabel("No");
		falseChoice.setSelected(flight.getSelfTransferRequired() == false);

		choices = SelectChoices.from(new SelectChoice[] {
			trueChoice, falseChoice
		});

		dataset = super.unbindObject(flight, "tag", "cost", "description");
		dataset.put("selfTransfered", choices.getSelected().getKey());
		dataset.put("options", choices);

		super.getResponse().addData(dataset);
	}
}
