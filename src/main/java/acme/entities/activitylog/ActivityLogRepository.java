
package acme.entities.activitylog;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface ActivityLogRepository extends AbstractRepository {

	@Query("select a from ActivityLog a where a.flightAssignment.id = :flightAssignmentId and a.registrationMoment = (select max(al.registrationMoment) from ActivityLog al where al.flightAssignment.id = :flightAssignmentId)")
	ActivityLog getMostRecentActivityLogByFlightAssignmentId(int flightAssignmentId);

}
