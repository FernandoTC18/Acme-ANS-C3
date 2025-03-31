package acme.features.flightCrew.leg;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.leg.Leg;

@Repository
public interface FlightCrewLegRepository extends AbstractRepository {
	
	
	@Query("select fa.leg FROM FlightAssignment fa WHERE fa.id = :flightAssignmentId")
	Collection<Leg> getLegsByAssignmentId(int flightAssignmentId);

}
