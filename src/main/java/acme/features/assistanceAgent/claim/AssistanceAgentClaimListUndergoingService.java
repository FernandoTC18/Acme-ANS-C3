
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimStatus;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimListUndergoingService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Claim> claims;
		int assistanceAgent;

		assistanceAgent = super.getRequest().getPrincipal().getActiveRealm().getId();
		claims = this.repository.findPendingClaimsById(assistanceAgent, ClaimStatus.PENDING);

		super.getBuffer().addData(claims);
	}

	@Override
	public void unbind(final Claim claims) {
		Dataset dataset;

		dataset = super.unbindObject(claims, "registrationMoment", "passengerEmail", "description", "indicator", "type", "leg", "draftMode");

		super.getResponse().addData(dataset);
	}

}
