package acme.features.flightCrew.activityLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activitylog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;

@Repository
public interface FlightCrewActivityLogRepository extends AbstractRepository {
	
	@Query("select a from ActivityLog a where a.flightAssignment.id = :id")
	Collection<ActivityLog> getLogsByAssignmentId(int id);
	
	
	@Query("select a.flightAssignment from ActivityLog a where a.id = :id")
	FlightAssignment getAssignmentByLogId(int id);
	
	@Query("select a from ActivityLog a where a.id = :id")
	ActivityLog getLogById(int id);
	
	@Query("select fa from FlightAssignment fa")
	Collection<FlightAssignment> getAllAssignments();
	
	@Query("select fa from FlightAssignment fa where fa.id = :id")
	FlightAssignment getAsssignmentById(int id);
		

}
