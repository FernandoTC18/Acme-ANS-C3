
package acme.features.technician.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.task.Task;
import acme.entities.task.TaskType;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskDeleteService extends AbstractGuiService<Technician, Task> {

	@Autowired
	private TechnicianTaskRepository repository;


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
	public void bind(final Task task) {
		super.bindObject(task, "type", "description", "priority", "estimatedDuration", "technician");

	}

	@Override
	public void validate(final Task task) {
		Assert.isTrue(task.getDraftMode(), "You can´t delete an published task");
	}

	@Override
	public void perform(final Task task) {
		this.repository.delete(task);
	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset;
		SelectChoices taskTypes;

		taskTypes = SelectChoices.from(TaskType.class, task.getType());

		dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDuration", "technician", "draftMode");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("type", taskTypes);

		super.getResponse().addData(dataset);
	}

}
