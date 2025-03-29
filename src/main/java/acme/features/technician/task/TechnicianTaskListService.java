
package acme.features.technician.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.task.Task;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskListService extends AbstractGuiService<Technician, Task> {

	@Autowired
	TechnicianTaskRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		List<Task> tasks;
		int id;

		id = super.getRequest().getData("id", int.class);

		tasks = this.repository.findTasksByMaintenanceRecord(id);

		super.getBuffer().addData(tasks);

	}

	@Override
	public void unbind(final Task tasks) {
		Dataset dataset;

		dataset = super.unbindObject(tasks, "type", "description", "priority", "estimatedDuration", "technician");

		super.getResponse().addData(dataset);
	}

}
