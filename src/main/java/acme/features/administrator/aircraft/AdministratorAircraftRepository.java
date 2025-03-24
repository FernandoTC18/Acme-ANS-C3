
package acme.features.administrator.aircraft;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircraft.Aircraft;

@Repository
public interface AdministratorAircraftRepository extends AbstractRepository {

	@Query("select a from Aircraft a where a.registrationNumber = :registrationNumber")
	Aircraft findAircraftByRegistrationNumber(@Param("registrationNumber") String registrationNumber);

	@Query("select a from Aircraft a where a.id = :id")
	Aircraft findAircraftById(@Param("id") int id);

	@Query("select a from Aircraft a")
	List<Aircraft> findAllAircrafts();

}
