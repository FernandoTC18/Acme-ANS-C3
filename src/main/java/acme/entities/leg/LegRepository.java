
package acme.entities.leg;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface LegRepository extends AbstractRepository {

	@Query("select l from Leg l where l.flight.id=:flightId order by l.order asc")
	List<Leg> computeLegsByFlight(int flightId);

	@Query("select a.iataCode from Airline a where a.iataCode=:iataCodeLeg")
	String computeAirlineIataCode(String iataCodeLeg);
}
