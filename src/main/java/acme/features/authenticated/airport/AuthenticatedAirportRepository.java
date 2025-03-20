
package acme.features.authenticated.airport;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airport.Airport;

@Repository
public interface AuthenticatedAirportRepository extends AbstractRepository {

	@Query("select a from Airport a where a.iataCode = :iataCode")
	Airport findAirportByIataCode(@Param("iataCode") String iataCode);
}
