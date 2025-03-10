
package acme.entities.trackingLog;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface TrackingLogRepository extends AbstractRepository {

	@Query("select tl from TrackingLog tl where tl.claim.id = :claimId and tl.lastUpdateMoment = (select max(t2.lastUpdateMoment) from TrackingLog t2 where t2.claim.id = :claimId)")
	TrackingLog findLatestTrackingLogByClaimId(int claimId);

}
