
package acme.features.authenticated.aircraft;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircraft.Aircraft;

@Repository
public interface AuthenticatedAircraftRepository extends AbstractRepository {

	@Query("select a from Aircraft a where a.registrationNumber = :registrationNumber")
	Aircraft findAircraftByRegistrationNumber(@Param("registrationNumber") String registrationNumber);
}
