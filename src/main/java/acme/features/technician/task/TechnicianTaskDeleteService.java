
package acme.features.technician.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.involves.Involves;
import acme.entities.task.Task;
import acme.entities.task.TaskType;
import acme.features.technician.involves.TechnicianInvolvesRepository;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskDeleteService extends AbstractGuiService<Technician, Task> {

	@Autowired
	private TechnicianTaskRepository		repository;

	@Autowired
	private TechnicianInvolvesRepository	involvesRepository;


	@Override
	public void authorise() {
		boolean status;
		int id;
		Task task;
		Technician technician;

		id = super.getRequest().getData("id", int.class);
		task = this.repository.findTaskById(id);
		technician = task == null ? null : task.getTechnician();
		status = task != null && task.isDraftMode() && super.getRequest().getPrincipal().hasRealm(technician);

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
	public void bind(final Task task) {
		super.bindObject(task, "type", "description", "priority", "estimatedDuration", "technician");

	}

	@Override
	public void validate(final Task task) {
		boolean status;

		status = task.isDraftMode();

		super.state(status, "*", "acme.validation.deletePublishedTask.message");
	}

	@Override
	public void perform(final Task task) {

		List<Involves> invs;

		invs = this.involvesRepository.findInvolvesByTaskId(task.getId());

		for (Involves i : invs)
			this.involvesRepository.delete(i);

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
