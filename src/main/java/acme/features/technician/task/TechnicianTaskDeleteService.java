
package acme.features.technician.task;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.task.Task;
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
		super.bindObject(task);

	}

	@Override
	public void validate(final Task task) {
		boolean status;

		status = task.isDraftMode();

		super.state(status, "*", "acme.validation.deletePublishedTask.message");
	}

	@Override
	public void perform(final Task task) {

		//List<Involves> invs;

		//invs = this.involvesRepository.findInvolvesByTaskId(task.getId());

		// No debería de haber nada en la lista ya que para borrar una task debe estar sin publicar,
		// y no se puede relacionar un maintenance record con una task sin publicar.

		//for (Involves i : invs)
		//this.involvesRepository.delete(i);

		this.repository.delete(task);
	}

	@Override
	public void unbind(final Task task) {
		//Dataset dataset;
		//SelectChoices taskTypes;

		//taskTypes = SelectChoices.from(TaskType.class, task.getType());

		//dataset = super.unbindObject(task);
		//dataset.put("confirmation", false);
		//dataset.put("readonly", false);
		//dataset.put("type", taskTypes);
		//dataset.put("technician", task.getTechnician());

		//super.getResponse().addData(dataset);
	}

}
