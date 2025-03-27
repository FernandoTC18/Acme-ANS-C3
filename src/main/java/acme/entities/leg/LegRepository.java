
package acme.entities.leg;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface LegRepository extends AbstractRepository {

	@Query("select a.iataCode from Airline a where a.iataCode=:iataCodeLeg")
	String computeAirlineIataCode(String iataCodeLeg);

	@Query("select l from Leg l where l.flightNumber=:flNumber")
	Leg computeLegbyFlightNumber(String flNumber);
}
