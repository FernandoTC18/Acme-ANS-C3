
package acme.features.technician.maintenanceRecord;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.maintenanceRecord.MaintenanceRecordStatus;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordUpdateService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		boolean status;
		boolean validAircraft = true;
		int id;
		MaintenanceRecord maintenanceRecord;
		Technician technician;

		if (super.getRequest().hasData("id")) {
			id = super.getRequest().getData("id", int.class);
			maintenanceRecord = this.repository.findMaintenanceRecordById(id);

			// Para comprobar que el estado del registro de mantenimiento existe
			if (super.getRequest().hasData("status")) {
				@SuppressWarnings("unused")
				MaintenanceRecordStatus maintenanceRecordStatus = super.getRequest().getData("status", MaintenanceRecordStatus.class);
			}

			technician = maintenanceRecord == null ? null : maintenanceRecord.getTechnician();

			if (super.getRequest().hasData("aircraft") && super.getRequest().getData("aircraft", int.class) != 0) {
				int aircraftId = super.getRequest().getData("aircraft", int.class);
				Aircraft a = this.repository.findAircraftById(aircraftId);

				validAircraft = a != null;
			}

			status = maintenanceRecord != null && validAircraft && maintenanceRecord.isDraftMode() && super.getRequest().getPrincipal().hasRealm(technician);
		} else
			status = false;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MaintenanceRecord maintenanceRecord;
		int id;

		id = super.getRequest().getData("id", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(id);

		super.getBuffer().addData(maintenanceRecord);
	}

	@Override
	public void bind(final MaintenanceRecord maintenanceRecord) {
		super.bindObject(maintenanceRecord, "moment", "status", "inspectionDueDate", "estimatedCost", "notes", "aircraft");
	}

	@Override
	public void validate(final MaintenanceRecord maintenanceRecord) {

		boolean notPublished = maintenanceRecord.isDraftMode();

		boolean futureInspection = true;
		boolean pastMoment = true;

		Date inspection = maintenanceRecord.getInspectionDueDate();

		Date moment = maintenanceRecord.getMoment();

		if (inspection != null && moment != null) {
			futureInspection = inspection.after(MomentHelper.getCurrentMoment());
			pastMoment = moment.before(MomentHelper.getCurrentMoment()) || moment.equals(MomentHelper.getCurrentMoment());

			super.state(futureInspection, "inspectionDueDate", "acme.validation.maintenanceRecord.futureInspection.message");
			super.state(pastMoment, "moment", "acme.validation.maintenanceRecord.pastMoment.message");
		}

		super.state(notPublished, "*", "acme.validation.updatePublishedMaintenanceRecord.message");
	}

	@Override
	public void perform(final MaintenanceRecord maintenanceRecord) {
		this.repository.save(maintenanceRecord);
	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		Dataset dataset;
		SelectChoices maintenanceRecordStatus;
		SelectChoices aircraftChoices;

		List<Aircraft> aircrafts = this.repository.findAllAircrafts();

		aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", maintenanceRecord.getAircraft());

		maintenanceRecordStatus = SelectChoices.from(MaintenanceRecordStatus.class, maintenanceRecord.getStatus());

		dataset = super.unbindObject(maintenanceRecord, "moment", "status", "inspectionDueDate", "estimatedCost", "notes");
		//dataset.put("confirmation", false);
		//dataset.put("readonly", false);
		dataset.put("status", maintenanceRecordStatus);
		dataset.put("aircraft", aircraftChoices);

		super.getResponse().addData(dataset);
	}

}
