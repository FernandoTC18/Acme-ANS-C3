
package acme.features.technician.involves;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.involves.Involves;

@Repository
public interface TechnicianInvolvesRepository extends AbstractRepository {

	@Query("select i from Involves i where i.id = :id")
	Involves findInvolvesById(int id);

	@Query("select i from Involves i where i.task.id = :id")
	List<Involves> findInvolvesByTaskId(int id);

	@Query("select i from Involves i where i.maintenanceRecord.id = :id")
	List<Involves> findInvolvesByMaintenanceRecordId(int id);

	@Query("select i.task from Involves i where i.id = :id")
	List<Involves> findTasksByInvolveId(int id);

	@Query("select i from Involves i where i.task.id = :taskId and i.maintenanceRecord.id = :mRId")
	List<Involves> findMRInvolvesByTaskId(int taskId, int mRId);

}
