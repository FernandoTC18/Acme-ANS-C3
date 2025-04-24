
package acme.features.technician.maintenanceRecord;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.maintenanceRecord.MaintenanceRecordStatus;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordCreateService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {

		boolean status = true;

		if (super.getRequest().hasData("id") && super.getRequest().getData("aircraft", int.class) != 0) {
			int aircraftId = super.getRequest().getData("aircraft", int.class);
			Aircraft a = this.repository.findAircraftById(aircraftId);
			status = a != null;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MaintenanceRecord maintenanceRecord;

		maintenanceRecord = new MaintenanceRecord();

		maintenanceRecord.setDraftMode(true);

		super.getBuffer().addData(maintenanceRecord);
	}

	@Override
	public void bind(final MaintenanceRecord maintenanceRecord) {
		String username = super.getRequest().getPrincipal().getUsername();
		Technician tech = this.repository.findTechnicianByUsername(username);
		super.bindObject(maintenanceRecord, "moment", "status", "inspectionDueDate", "estimatedCost", "notes", "aircraft");
		maintenanceRecord.setTechnician(tech);

	}

	@Override
	public void validate(final MaintenanceRecord maintenanceRecord) {
		boolean status;

		Date inspection = maintenanceRecord.getInspectionDueDate();

		Date moment = maintenanceRecord.getMoment();

		status = inspection.after(moment);

		super.state(status, "inspectionDueDate", "acme.validation.maintenanceRecord.nextInspectionPriorMaintenanceMoment.message");
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

		dataset = super.unbindObject(maintenanceRecord, "moment", "inspectionDueDate", "estimatedCost", "notes");
		//dataset.put("confirmation", false);
		//dataset.put("readonly", false);
		dataset.put("status", maintenanceRecordStatus);
		dataset.put("aircraft", aircraftChoices);

		super.getResponse().addData(dataset);
	}

}
