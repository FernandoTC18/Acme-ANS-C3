
package acme.features.managers.leg;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;

@Repository
public interface ManagerLegRepository extends AbstractRepository {

	@Query("select f from Flight f where f.id = :flightId")
	Flight findFlightById(int flightId);

	@Query("select l from Leg l where l.flight.id = :masterId ORDER BY l.scheduledDeparture")
	Collection<Leg> findLegsByMasterId(int masterId);

	@Query("select l from Leg l where l.id= :legId")
	Leg findLegById(int legId);

	@Query("select a from Airport a")
	Collection<Airport> findAllAirports();

	@Query("select a from Airport a where a.id = :id")
	Airport findAirportById(int id);

	@Query("select air from Aircraft air")
	Collection<Aircraft> findAllPlanes();

	@Query("select a from Aircraft a where a.id = :id")
	Aircraft findAircraftById(int id);

}
