
package acme.entities.trackingLog;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface TrackingLogRepository extends AbstractRepository {

	@Query("select tl from TrackingLog tl where tl.claim.id = :claimId")
	List<TrackingLog> findTrackingLogsByClaimId(int claimId);

}
