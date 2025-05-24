
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

		boolean status = true;

		if (super.getRequest().hasData("id") && super.getRequest().getData("task", int.class) != 0) {

			int taskId = super.getRequest().getData("task", int.class);
			Task task = this.taskRepository.findTaskById(taskId);

			String techUsername = super.getRequest().getPrincipal().getUsername();
			Technician tech = this.taskRepository.findTechnicianByUsername(techUsername);
			status = task != null && !task.isDraftMode() && task.getTechnician().equals(tech);

		}

		super.getResponse().setAuthorised(status);
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

		boolean status = true;

		if (involves.getTask() != null) {

			int taskId = involves.getTask().getId();

			List<Involves> invs = this.repository.findInvolvesByTaskId(taskId);

			invs = invs.stream().filter(i -> i.getMaintenanceRecord().getId() == involves.getMaintenanceRecord().getId()).toList();

			status = invs.isEmpty();

		}

		super.state(status, "task", "acme.validation.involves.alreadyInvolvedTask.message");
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

		String techUsername = super.getRequest().getPrincipal().getUsername();

		int techId = this.taskRepository.findTechnicianByUsername(techUsername).getId();

		// Cojo las tareas del tecnico logeado y filtro para quedarme con las que ya están publicadas

		tasks = this.taskRepository.findTasksByTechnicianId(techId);

		tasks = tasks.stream().filter(t -> !t.isDraftMode()).toList();

		choices = SelectChoices.from(tasks, "description", involves.getTask());

		dataset = super.unbindObject(involves, "task");
		dataset.put("task", choices.getSelected().getKey());

		dataset.put("tasks", choices);

		super.getResponse().addData(dataset);
	}

}
