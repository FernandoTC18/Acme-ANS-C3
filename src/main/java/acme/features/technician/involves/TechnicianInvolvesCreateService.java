
package acme.features.technician.involves;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.involves.Involves;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.task.Task;
import acme.features.technician.maintenanceRecord.TechnicianMaintenanceRecordRepository;
import acme.features.technician.task.TechnicianTaskRepository;
import acme.realms.Technician;

@GuiService
public class TechnicianInvolvesCreateService extends AbstractGuiService<Technician, Involves> {

	@Autowired
	private TechnicianInvolvesRepository			repository;

	@Autowired
	private TechnicianMaintenanceRecordRepository	mRRepository;

	@Autowired
	private TechnicianTaskRepository				taskRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Involves involves;

		involves = new Involves();
		int maintenanceRecordId = this.getRequest().getData("maintenanceRecordId", int.class);
		super.getResponse().addGlobal("maintenanceRecordId", maintenanceRecordId);

		super.getBuffer().addData(involves);
	}

	@Override
	public void bind(final Involves involves) {
		int maintenanceRecordId;
		MaintenanceRecord maintenanceRecord;

		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		maintenanceRecord = this.mRRepository.findMaintenanceRecordById(maintenanceRecordId);

		super.bindObject(involves, "task");

		involves.setMaintenanceRecord(maintenanceRecord);
	}

	@Override
	public void validate(final Involves involves) {
		int taskId = super.getRequest().getData("task", int.class);

		Task t = this.taskRepository.findTaskById(taskId);

		super.state(t != null, "*", "acme.validation.nonExistingTask.message");
	}

	@Override
	public void perform(final Involves involves) {
		this.repository.save(involves);
	}

	@Override
	public void unbind(final Involves involves) {
		Dataset dataset;
		SelectChoices choices;
		List<Task> tasks;

		tasks = this.taskRepository.findAllTasks();

		choices = SelectChoices.from(tasks, "description", involves.getTask());

		dataset = super.unbindObject(involves, "task");
		dataset.put("task", choices.getSelected().getKey());

		dataset.put("tasks", choices);

		super.getResponse().addData(dataset);
	}

}
