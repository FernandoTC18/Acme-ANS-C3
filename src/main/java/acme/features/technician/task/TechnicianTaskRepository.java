
package acme.features.technician.task;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.task.Task;

@Repository
public interface TechnicianTaskRepository extends AbstractRepository {

	@Query("select t from Task t where t.id = :id")
	Task findTaskById(int id);

	@Query("select i.task from Involves i where i.maintenanceRecord.id  =:id")
	List<Task> findTasksByMaintenanceRecord(int id);

	@Query("select t from Task t where t.technician.id = :id")
	List<Task> findTasksByTechnicianId(int id);

	@Query("select t from Task t")
	List<Task> findAllTasks();
}
