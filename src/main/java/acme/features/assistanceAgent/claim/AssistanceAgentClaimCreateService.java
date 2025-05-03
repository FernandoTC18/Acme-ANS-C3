
package acme.features.assistanceAgent.claim;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimStatus;
import acme.entities.claim.ClaimType;
import acme.entities.leg.Leg;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimCreateService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;
		Date moment;
		AssistanceAgent assistanceAgent;

		moment = MomentHelper.getCurrentMoment();
		assistanceAgent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();

		claim = new Claim();
		claim.setRegistrationMoment(moment);
		claim.setDraftMode(true);
		claim.setIndicator(ClaimStatus.PENDING);
		claim.setAssistanceAgent(assistanceAgent);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		int legId;
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		super.bindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "indicator");

		claim.setLeg(leg);
	}

	@Override
	public void validate(final Claim claim) {
		Leg leg;
		Date legTime;
		Date moment;
		boolean condition;

		leg = super.getRequest().getData("leg", Leg.class);
		moment = super.getRequest().getData("registrationMoment", Date.class);
		legTime = this.repository.findArrivalTimeLegById(leg.getId());
		condition = moment.after(legTime);

		super.state(condition, "leg", "acme.validation.leg-time.message");

	}

	@Override
	public void perform(final Claim claim) {
		this.repository.save(claim);
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
		dataset.put("types", typeChoices);
		dataset.put("indicators", statusChoices);
		dataset.put("legs", legChoices);
		dataset.put("leg", legChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

}
