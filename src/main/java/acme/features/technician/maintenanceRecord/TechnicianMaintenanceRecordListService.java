
package acme.features.technician.maintenanceRecord;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordListService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		List<MaintenanceRecord> maintenanceRecords;

		maintenanceRecords = this.repository.findAllMaintenanceRecords();

		super.getBuffer().addData(maintenanceRecords);

	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecords) {
		Dataset dataset;

		dataset = super.unbindObject(maintenanceRecords, "moment", "status", "inspectionDueDate", "estimatedCost", "notes", "aircraft", "technician", "draftMode");

		super.getResponse().addData(dataset);
	}

}
