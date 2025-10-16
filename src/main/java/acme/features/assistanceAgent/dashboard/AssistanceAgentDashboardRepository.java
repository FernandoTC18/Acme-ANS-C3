
package acme.features.assistanceAgent.dashboard;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;
import acme.entities.trackingLog.TrackingLog;

@Repository
public interface AssistanceAgentDashboardRepository extends AbstractRepository {

	@Query("select c from Claim c where c.assistanceAgent.id = :agentId")
	Collection<Claim> findClaimsByAgentId(int agentId);

	@Query("select count(t) from TrackingLog t where t.claim.id = :claimId")
	int countTrackingLogsByClaimId(int claimId);

	@Query("select t from TrackingLog t where t.claim.id = :claimId")
	Collection<TrackingLog> findTrackingLogsByClaimId(int claimId);
}
