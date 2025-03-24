package acme.features.administrator.airline;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airline.Airline;


@Repository
public interface AdministratorAirlineRepository extends AbstractRepository {
	
	@Query("select a from Airline a where a.iataCode = :iataCode")
	Airline findAirlineByIataCode(@Param("iataCode") String iataCode);
	
	@Query("select a from Airline a where a.id = :id")
	Airline findAirlineById(@Param("id") int id);
	
	@Query("select a from Airline a")
	List<Airline> findAllAirlines();

}
