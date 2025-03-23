package acme.realms;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;


@Repository
public interface FlightCrewRepository extends AbstractRepository {
	
	@Query("select f from FlightCrew f where f.employeeCode = :employeeCode")
	FlightCrew findFlightCrewByCode(String employeeCode);

}
