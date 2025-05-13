
package acme.features.technician.involves;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.involves.Involves;
import acme.realms.Technician;

@GuiService
public class TechnicianInvolvesShowService extends AbstractGuiService<Technician, Involves> {

	@Autowired
	TechnicianInvolvesRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int id;
		Technician tech = null;

		Involves involves;

		id = super.getRequest().getData("id", int.class);
		involves = this.repository.findInvolvesById(id);
		if (involves != null)
			tech = involves.getTask().getTechnician();
		status = involves != null && super.getRequest().getPrincipal().hasRealm(tech);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Involves involves;
		int id;

		id = super.getRequest().getData("id", int.class);
		involves = this.repository.findInvolvesById(id);

		super.getBuffer().addData(involves);

	}

	@Override
	public void unbind(final Involves involves) {
		assert involves != null;
		Dataset dataset;

		dataset = super.unbindObject(involves, "maintenanceRecord", "task");
		dataset.put("readonly", true);

		super.getResponse().addData(dataset);
	}

}
