
package acme.features.technician.task;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.task.Task;
import acme.entities.task.TaskType;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskShowService extends AbstractGuiService<Technician, Task> {

	@Autowired
	TechnicianTaskRepository repository;


	@Override
	public void authorise() {
		boolean status = false;
		int id;
		Task task;
		Technician technician;

		id = super.getRequest().getData("id", int.class);
		task = this.repository.findTaskById(id);
		technician = task == null ? null : task.getTechnician();

		if (task != null && task.isDraftMode()) {
			status = super.getRequest().getPrincipal().hasRealm(technician);
		} else if (task != null && !task.isDraftMode()) {
			status = true;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Task task;
		int id;

		id = super.getRequest().getData("id", int.class);
		task = this.repository.findTaskById(id);

		super.getBuffer().addData(task);

	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset;
		SelectChoices taskTypes;

		taskTypes = SelectChoices.from(TaskType.class, task.getType());

		dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDuration");
		dataset.put("type", taskTypes);

		dataset.put("readonly", !task.isDraftMode());

		// dataset.put("technician", task.getTechnician().getUserAccount().getUsername());

		super.getResponse().addData(dataset);
	}

}
