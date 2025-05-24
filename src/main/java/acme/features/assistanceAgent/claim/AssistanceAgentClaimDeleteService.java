
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimType;
import acme.entities.leg.Leg;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimDeleteService extends AbstractGuiService<AssistanceAgent, Claim> {

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

		id = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(id);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		int legId;
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		super.bindObject(claim, "passengerEmail", "description", "type");

		claim.setLeg(leg);
	}

	@Override
	public void validate(final Claim claim) {
		// Intentionally left blank: no associated requirements.
	}

	@Override
	public void perform(final Claim claim) {
		this.repository.deleteTrackingLogsByClaimId(claim.getId());
		this.repository.delete(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		Collection<Leg> legs;

		SelectChoices legChoices;
		SelectChoices typeChoices;

		legs = this.repository.findAllLegs();

		legChoices = SelectChoices.from(legs, "flightNumber", claim.getLeg());
		typeChoices = SelectChoices.from(ClaimType.class, claim.getType());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "indicator", "leg", "draftMode");
		dataset.put("type", typeChoices.getSelected().getKey());
		dataset.put("types", typeChoices);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);

		super.getResponse().addData(dataset);
	}

}
