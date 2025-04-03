
package acme.features.flightCrew.member;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.realms.FlightCrew;

@Repository
public interface FlightCrewMemberRepository extends AbstractRepository {

	@Query("select f from FlightCrew f where f.employeeCode = :employeeCode")
	FlightCrew findFlightCrewByCode(String employeeCode);

	@Query("select fa.flightCrewMember from FlightAssignment fa where fa.id = :assignmentId")
	Collection<FlightCrew> findCrewMembersByAssignmentId(int assignmentId);
	
	@Query("select f from FlightCrew f where f.id = :id")
	FlightCrew getMemberById(int id);

}
