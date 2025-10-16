
package acme.features.authenticated.assistanceAgent;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airline.Airline;

@Repository
public interface AirlineRepository extends AbstractRepository {

	@Query("select count(a) from Airline a where a.iataCode = :code")
	long countByAirlineCode(@Param("code") String code);

	@Query("select a from Airline a where a.id = :id")
	Airline findById(@Param("id") int id);

	@Query("select a from Airline a")
	List<Airline> findAllAirlines();

}
