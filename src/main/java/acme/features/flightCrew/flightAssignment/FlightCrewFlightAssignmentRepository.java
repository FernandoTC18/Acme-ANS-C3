
package acme.features.flightCrew.flightAssignment;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activitylog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.realms.FlightCrew;

@Repository
public interface FlightCrewFlightAssignmentRepository extends AbstractRepository {

	@Query("select f from FlightCrew f where f.employeeCode = :employeeCode")
	FlightCrew findFlightCrewByCode(String employeeCode);

	@Query("select fa from FlightAssignment fa where fa.id = :id")
	FlightAssignment findAssignmentbyId(int id);

	@Query("select fa from FlightAssignment fa where fa.flightCrewMember.id = :memberId and fa.leg.scheduledArrival < :arrivalDate")
	Collection<FlightAssignment> getCompletedAssignmentsByMemberId(int memberId, Date arrivalDate);

	@Query("select fa from FlightAssignment fa where fa.flightCrewMember.id = :memberId and fa.leg.scheduledArrival >= :arrivalDate")
	Collection<FlightAssignment> getUncompletedAssignmentsByMemberId(int memberId, Date arrivalDate);

	@Query("select l from Leg l")
	Collection<Leg> findAllLegs();

	@Query("select f from FlightCrew f where f.id = :id")
	FlightCrew findCrewById(int id);

	@Query("select l from Leg l where l.id = :id")
	Leg findLegById(int id);

	@Query("select l from Leg l where l.scheduledArrival > :date and l.draftMode = false")
	Collection<Leg> findFutureAndPublishedLegs(Date date);

	@Query("select al from ActivityLog al where al.flightAssignment.id = :id")
	Collection<ActivityLog> findLogsByAssignmentId(int id);

}
