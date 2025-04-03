
package acme.features.flightCrew.flightAssignment;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignment.Duty;
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
	
	@Query("select fa.leg from FlightAssignment fa where fa.leg.scheduledArrival >:date and fa.flightCrewMember.id =:id ")
	Collection<Leg> getLegsByMemberId(Timestamp date, int id);
	
	@Query("select l from Leg l")
	Collection<Leg> findAllLegs();
	
	@Query("select m from FlightCrew m")
	Collection<FlightCrew> findAllMembers();
	
	@Query("select count(fa.flightCrewMember) from FlightAssignment fa where fa.flightCrewMember.id = :id and fa.duty = :duty")
	Long countMembersByDuty(int id, Optional<Duty> duty);
	
	@Query("select f from FlightCrew f where f.id = :id")
	FlightCrew findCrewById(int id);
	
	
	

}
