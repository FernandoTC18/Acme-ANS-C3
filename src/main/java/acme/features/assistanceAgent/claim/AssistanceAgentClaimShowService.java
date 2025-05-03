
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimStatus;
import acme.entities.claim.ClaimType;
import acme.entities.leg.Leg;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimShowService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int id;
		Claim claim;
		AssistanceAgent assistanceAgent;

		id = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(id);
		assistanceAgent = claim == null ? null : claim.getAssistanceAgent();
		status = claim != null && super.getRequest().getPrincipal().hasRealm(assistanceAgent);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;
		int id;

		id = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(id);

		super.getBuffer().addData(claim);

	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		Collection<Leg> legs;
		SelectChoices legChoices;
		SelectChoices typeChoices;
		SelectChoices statusChoices;

		legs = this.repository.findAllLegs();

		legChoices = SelectChoices.from(legs, "flightNumber", claim.getLeg());
		typeChoices = SelectChoices.from(ClaimType.class, claim.getType());
		statusChoices = SelectChoices.from(ClaimStatus.class, claim.getIndicator());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "indicator", "assistanceAgent", "leg", "draftMode");
		dataset.put("legs", legChoices);
		dataset.put("types", typeChoices);
		dataset.put("indicators", statusChoices);

		super.getResponse().addData(dataset);
	}

}
