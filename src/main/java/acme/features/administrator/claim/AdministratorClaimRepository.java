
package acme.features.administrator.claim;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;
import acme.entities.leg.Leg;

@Repository
public interface AdministratorClaimRepository extends AbstractRepository {

	@Query("select c from Claim c where c.id = :id")
	Claim findClaimById(int id);

	@Query("select c from Claim c where c.draftMode = false")
	Collection<Claim> findPublishedClaims();

	@Query("select l from Leg l where l.scheduledArrival <= :date and l.draftMode = false ")
	Collection<Leg> findAllPublishedPastLegs(Date date);
}
