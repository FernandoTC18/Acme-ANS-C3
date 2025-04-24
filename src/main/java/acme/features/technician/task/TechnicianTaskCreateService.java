
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
public class TechnicianTaskCreateService extends AbstractGuiService<Technician, Task> {

	@Autowired
	private TechnicianTaskRepository repository;


	@Override
	public void authorise() {

		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Task task;

		task = new Task();

		task.setDraftMode(true);

		super.getBuffer().addData(task);
	}

	@Override
	public void bind(final Task task) {

		String user = super.getRequest().getPrincipal().getUsername();

		Technician tech = this.repository.findTechnicianByUsername(user);

		super.bindObject(task, "type", "description", "priority", "estimatedDuration");

		task.setTechnician(tech);

	}

	@Override
	public void validate(final Task task) {
	}

	@Override
	public void perform(final Task task) {
		this.repository.save(task);
	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset;
		SelectChoices taskTypes;
		//SelectChoices technicianChoices;

		taskTypes = SelectChoices.from(TaskType.class, task.getType());

		//List<Technician> techs = this.repository.findAllTechnicians();

		//technicianChoices = SelectChoices.from(techs, "userAccount.username", task.getTechnician());

		dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDuration");
		//dataset.put("confirmation", false);
		//dataset.put("readonly", false);
		dataset.put("type", taskTypes);
		//dataset.put("technician", technicianChoices.getSelected().getKey());
		//dataset.put("technicians", technicianChoices);

		super.getResponse().addData(dataset);
	}

}
