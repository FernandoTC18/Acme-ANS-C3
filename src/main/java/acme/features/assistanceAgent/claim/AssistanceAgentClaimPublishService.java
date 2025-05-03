
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
public class AssistanceAgentClaimPublishService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int id;
		Claim claim;
		AssistanceAgent assistanceAgent;

		id = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(id);
		assistanceAgent = claim == null ? null : claim.getAssistanceAgent();
		status = claim != null && claim.getDraftMode() && super.getRequest().getPrincipal().hasRealm(assistanceAgent);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;
		int id;

		AssistanceAgent assistanceAgent;

		assistanceAgent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();

		id = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(id);
		claim.setAssistanceAgent(assistanceAgent);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {

		boolean draftMode;

		draftMode = false;

		super.bindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "indicator");

		claim.setDraftMode(draftMode);
	}

	@Override
	public void validate(final Claim claim) {
	}

	@Override
	public void perform(final Claim claim) {
		claim.setDraftMode(false);
		this.repository.save(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		assert claim != null;
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
		dataset.put("indicator", statusChoices);

		super.getResponse().addData(dataset);
	}

}
