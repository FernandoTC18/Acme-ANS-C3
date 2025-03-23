package acme.features.authenticated.airline;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airline.Airline;

@Repository
public interface AirlineRepository extends AbstractRepository {
	
	@Query("select a from Airline a where a.iataCode = :iataCode")
	Airline findAirlineByIataCode(@Param("iataCode") String iataCode);

}
