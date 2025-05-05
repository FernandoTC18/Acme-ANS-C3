
package acme.entities.leg;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface LegRepository extends AbstractRepository {

	@Query("select a.iataCode from Airline a where a.iataCode=:iataCodeLeg")
	String computeAirlineIataCode(String iataCodeLeg);

	@Query("select l from Leg l where l.flightNumber=:flNumber")
	Leg computeLegByFlightNumber(String flNumber);

	@Query("select l from Leg l where l.plane.registrationNumber=:planeRegNumber and l.id != :legId")
	List<Leg> computeDistinctLegsWithSameAircraft(String planeRegNumber, int legId);
}
