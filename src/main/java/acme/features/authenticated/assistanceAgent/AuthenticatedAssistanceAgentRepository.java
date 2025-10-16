
package acme.features.authenticated.assistanceAgent;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.realms.AssistanceAgent;

@Repository
public interface AuthenticatedAssistanceAgentRepository extends AbstractRepository {

	@Query("select u from UserAccount u where u.id = :id")
	UserAccount findUserAccountById(int id);

	@Query("select t from AssistanceAgent t where t.userAccount.id = :id")
	AssistanceAgent findAgentByUserId(int id);

}
