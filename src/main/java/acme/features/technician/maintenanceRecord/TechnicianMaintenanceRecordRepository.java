
package acme.features.technician.maintenanceRecord;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenanceRecord.MaintenanceRecord;

@Repository
public interface TechnicianMaintenanceRecordRepository extends AbstractRepository {

	@Query("select mr from MaintenanceRecord mr where mr.id = :id")
	MaintenanceRecord findMaintenanceRecordById(@Param("id") int id);

	@Query("select mr from MaintenanceRecord mr where mr.draftMode = false")
	List<MaintenanceRecord> findAllMaintenanceRecords();
}
