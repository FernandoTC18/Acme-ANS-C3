
package acme.features.technician.maintenanceRecord;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.task.Task;

@Repository
public interface TechnicianMaintenanceRecordRepository extends AbstractRepository {

	@Query("select mr from MaintenanceRecord mr where mr.id = :id")
	MaintenanceRecord findMaintenanceRecordById(int id);

	@Query("select mr from MaintenanceRecord mr where mr.technician.id = :id")
	List<MaintenanceRecord> findMaintenanceRecordsByTechnicianId(int id);

	@Query("select i.task from Involves i where i.maintenanceRecord.id = :id")
	List<Task> findTasksByMaintenanceRecordId(int id);
}
