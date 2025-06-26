
package acme.features.assistanceAgent.claim;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;
import acme.entities.leg.Leg;

@Repository
public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("select c from Claim c where c.assistanceAgent.id = :agentId")
	Collection<Claim> findClaimsByAgentId(int agentId);

	@Query("select c from Claim c where c.id = :id")
	Claim findClaimById(int id);

	@Query("select l from Leg l where l.sheduledArrival < :date and l.draftMode = false ")
	Collection<Leg> findAllPublishedPastLegs(Date date);

	@Query("select l from Leg l where l.id = :id")
	Leg findLegById(int id);

	@Query("select c.leg from Claim c where c.id = :id")
	Leg findLegByClaimId(int id);

	@Modifying
	@Transactional
	@Query("delete from TrackingLog tl where tl.claim.id = :id")
	void deleteTrackingLogsByClaimId(int id);

}
