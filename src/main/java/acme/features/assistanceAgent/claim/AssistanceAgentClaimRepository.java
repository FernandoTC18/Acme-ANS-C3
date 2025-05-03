
package acme.features.assistanceAgent.claim;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimStatus;
import acme.entities.leg.Leg;
import acme.realms.AssistanceAgent;

@Repository
public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("select c from Claim c where c.assistanceAgent.id = :id and c.indicator != :status")
	Collection<Claim> findCompletedClaimsById(int id, ClaimStatus status);

	@Query("select c from Claim c where c.assistanceAgent.id = :id and c.indicator = :status")
	Collection<Claim> findPendingClaimsById(int id, ClaimStatus status);

	@Query("select l.scheduledArrival from Leg l where l.id = :id")
	Date findArrivalTimeLegById(int id);

	@Query("select l from Leg l")
	Collection<Leg> findAllLegs();

	@Query("select l from Leg l where l.id = :id")
	Leg findLegById(int id);

	@Query("select a from AssistanceAgent a where a.id = :id")
	AssistanceAgent findAssistanceAgentById(int id);

	@Query("select c from Claim c where c.id = :id")
	Claim findClaimById(int id);
}
