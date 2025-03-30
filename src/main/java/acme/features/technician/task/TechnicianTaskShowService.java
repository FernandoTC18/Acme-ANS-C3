
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
		super.getResponse().setAuthorised(true);
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
		assert task != null;
		Dataset dataset;
		SelectChoices taskTypes;

		taskTypes = SelectChoices.from(TaskType.class, task.getType());

		dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDuration", "technician");

		super.getResponse().addData(dataset);
		dataset.put("type", taskTypes);
	}

}
