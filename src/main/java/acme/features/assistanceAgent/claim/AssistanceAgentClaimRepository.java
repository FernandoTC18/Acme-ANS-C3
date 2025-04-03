
package acme.features.assistanceAgent.claim;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;
import acme.entities.leg.Leg;
import acme.realms.AssistanceAgent;

@Repository
public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("select c from Claim c where c.assistanceAgent.id = :id and c.indicator != PENDING")
	Collection<Claim> findCompletedClaimsById(int id);

	@Query("select c from Claim c where c.assistanceAgent.id = :id and c.indicator = PENDING")
	Collection<Claim> findPendingClaimsById(int id);

	@Query("select l.scheduledArrival from Leg l where l.id = :id")
	Date findArrivalTimeLegById(int id);

	@Query("select l from Leg l")
	Collection<Leg> findAllLegs();

	@Query("select a from AssistanceAgent a")
	Collection<AssistanceAgent> findAllAgents();

	@Query("select c from Claim c where c.id = :id")
	Claim findClaimById(int id);
}
