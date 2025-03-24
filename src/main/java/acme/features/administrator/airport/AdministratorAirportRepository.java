
package acme.features.administrator.airport;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airport.Airport;

@Repository
public interface AdministratorAirportRepository extends AbstractRepository {

	@Query("select a from Airport a where a.iataCode = :iataCode")
	Airport findAirportByIataCode(@Param("iataCode") String iataCode);

	@Query("select a from Airport a where a.id = :id")
	Airport findAirportById(int id);

	@Query("select a from Airport a")
	List<Airport> findAllAirports();

}
