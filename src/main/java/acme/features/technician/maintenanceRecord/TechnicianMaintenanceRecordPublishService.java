
package acme.features.technician.maintenanceRecord;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.maintenanceRecord.MaintenanceRecordStatus;
import acme.entities.task.Task;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordPublishService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {

		int id;

		id = super.getRequest().getData("id", int.class);

		List<Task> tasks;

		tasks = this.repository.findTasksByMaintenanceRecordId(id);

		boolean anyTaskUnpublished = false;

		for (Task t : tasks)
			if (t.getDraftMode() == true) {
				anyTaskUnpublished = true;
				break;
			}

		boolean atLeastOnePublishedTask = false;

		for (Task t : tasks)
			if (t.getDraftMode() == false) {
				atLeastOnePublishedTask = true;
				break;
			}

		boolean status = !anyTaskUnpublished && atLeastOnePublishedTask;

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
		super.bindObject(maintenanceRecord, "moment", "status", "inspectionDueDate", "estimatedCost", "notes", "aircraft", "technician");

	}

	@Override
	public void validate(final MaintenanceRecord maintenanceRecord) {
	}

	@Override
	public void perform(final MaintenanceRecord maintenanceRecord) {
		maintenanceRecord.setDraftMode(false);
		this.repository.save(maintenanceRecord);
	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		Dataset dataset;

		SelectChoices maintenanceRecordStatus;

		maintenanceRecordStatus = SelectChoices.from(MaintenanceRecordStatus.class, maintenanceRecord.getStatus());

		dataset = super.unbindObject(maintenanceRecord, "moment", "status", "inspectionDueDate", "estimatedCost", "notes", "aircraft", "technician", "draftMode");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("status", maintenanceRecordStatus);

		super.getResponse().addData(dataset);
	}

}
