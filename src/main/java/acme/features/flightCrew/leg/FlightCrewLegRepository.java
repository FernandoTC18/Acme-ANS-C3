package acme.features.flightCrew.leg;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.realms.FlightCrew;

@Repository
public interface FlightCrewLegRepository extends AbstractRepository {
	
	
	@Query("select fa.leg FROM FlightAssignment fa WHERE fa.id = :flightAssignmentId")
	Collection<Leg> getLegsByAssignmentId(int flightAssignmentId);
	
	@Query("select fa.flightCrewMember from FlightAssignment fa where fa.id = :id")
	FlightCrew getMemberById(int id);
	
	@Query("select fa from FlightAssignment fa where fa.id = :id")
	FlightAssignment getAssignmentById(int id);
	
	@Query("select l.arrivalAirport from Leg l where l.id = :id")
	Airport getArrivalAirportbyLegId(int id);
	
	@Query("select l.departureAirport from Leg l where l.id = :id")
	Airport getDepartureAirportbyLegId(int id);
	
	@Query("select l.plane from Leg l where l.id = :id")
	Aircraft getPlanebyLegId(int id);
	
	@Query("select l from Leg l where l.id = :id")
	Leg getLegById(int id);
	
	

}
