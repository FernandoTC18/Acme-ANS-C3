
package acme.features.managers.flight;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;

@Repository
public interface ManagerFlightRepository extends AbstractRepository {

	@Query("select f from Flight f where f.id= :flightId")
	Flight findFlightById(int flightId);

	@Query("select f from Flight f where f.manager.id= :managerId")
	Collection<Flight> findFlightsByManagerId(int managerId);

	@Query("select l from Leg l where l.flight.id= :flightId")
	Collection<Leg> findLegsByFlightId(int flightId);
}
