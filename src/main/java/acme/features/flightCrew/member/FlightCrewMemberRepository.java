
package acme.features.flightCrew.member;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrew;

@Repository
public interface FlightCrewMemberRepository extends AbstractRepository {

	@Query("select f from FlightCrew f where f.employeeCode = :employeeCode")
	FlightCrew findFlightCrewByCode(String employeeCode);

	@Query("select fa.flightCrewMember from FlightAssignment fa where fa.id = :assignmentId")
	Collection<FlightCrew> findCrewMembersByAssignmentId(int assignmentId);
	
	@Query("select f from FlightCrew f where f.id = :id")
	FlightCrew getMemberById(int id);
	
	@Query("select fa from FlightAssignment fa where fa.id = :id")
	FlightAssignment findAssignmentbyId(int id);
	
	@Query("select fa from FlightAssignment fa where fa.flightCrewMember.id = :memberId")
	Collection<FlightAssignment> getAssignmentsByMemberId(int memberId);

}
