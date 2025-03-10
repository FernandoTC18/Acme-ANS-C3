
package acme.entities.flight;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface FlightRepository extends AbstractRepository {

	@Query("select min(l.scheduledDeparture) from Leg l where l.flight.id=:flightId")
	Date computeScheduledDepartureByFlight(int flightId);

	@Query("select max(l.scheduledArrival) from Leg l where l.flight.id=:flightId")
	Date computeScheduledArrivalByFlight(int flightId);

	@Query("select l.departureAirport.city from Leg l where l.flight.id=:flightId order by l.scheduledDeparture asc")
	String computeOriginCityByFlight(int flightId);

	@Query("select l.arrivalAirport.city from Leg l where l.flight.id=:flightId order by l.scheduledArrival desc")
	String computeArrivalCityByFlight(int flightId);

	@Query("select count(l) from Leg l where l.flight.id=:flightId")
	Integer computeLegsNumberByFlight(int flightId);
}
