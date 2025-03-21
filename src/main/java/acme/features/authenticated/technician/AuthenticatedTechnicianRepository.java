
package acme.features.authenticated.technician;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.realms.Technician;

@Repository
public interface AuthenticatedTechnicianRepository extends AbstractRepository {

	@Query("select a from Technician a where a.licenseNumber = :licenseNumber")
	Technician findTechnicianByLicenseNumber(@Param("licenseNumber") String licenseNumber);
}
