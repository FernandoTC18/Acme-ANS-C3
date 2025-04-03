
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimListPendingService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Claim> claims;
		int assistanceAgent;

		assistanceAgent = super.getRequest().getPrincipal().getActiveRealm().getId();
		claims = this.repository.findPendingClaimsById(assistanceAgent);

		super.getBuffer().addData(claims);
	}

	@Override
	public void unbind(final Claim claims) {
		Dataset dataset;

		dataset = super.unbindObject(claims, "registrationMoment", "passengerEmail", "description", "type", "indicator", "assistanceAgent", "leg");

		super.getResponse().addData(dataset);
	}

}
