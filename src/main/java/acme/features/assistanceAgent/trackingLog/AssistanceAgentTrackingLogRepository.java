
package acme.features.assistanceAgent.trackingLog;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;
import acme.entities.trackingLog.TrackingLog;

@Repository
public interface AssistanceAgentTrackingLogRepository extends AbstractRepository {

	@Query("select c from Claim c where c.id = :id")
	Claim findClaimById(int id);

	@Query("select tl from TrackingLog tl where tl.id = :id")
	TrackingLog findTrackingLogById(int id);

	@Query("select tl from TrackingLog tl where tl.claim.assistanceAgent.id = :id")
	Collection<TrackingLog> findTrackingLogsByAgentId(int id);

	@Query("select tl from TrackingLog tl where tl.claim.id = :id")
	List<TrackingLog> findTrackingLogsByClaimId(int id);

}
