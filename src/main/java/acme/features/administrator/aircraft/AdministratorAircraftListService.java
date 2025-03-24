
package acme.features.administrator.aircraft;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;

@GuiService
public class AdministratorAircraftListService extends AbstractGuiService<Administrator, Aircraft> {

	@Autowired
	AdministratorAircraftRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		List<Aircraft> aircrafts;

		aircrafts = this.repository.findAllAircrafts();

		super.getBuffer().addData(aircrafts);

	}

	@Override
	public void unbind(final Aircraft aircraft) {
		Dataset dataset;

		dataset = super.unbindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "status", "details");

		super.getResponse().addData(dataset);
	}

}
