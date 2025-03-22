
package acme.features.authenticated.customer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import acme.client.repositories.AbstractRepository;
import acme.realms.Customer;

public interface AuthenticatedCustomerRepository extends AbstractRepository {

	@Query("select c from Customer c where c.identifier = :identifier")
	Customer findCustomerByIdentifier(@Param("identifier") String identifier);
}
