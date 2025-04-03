
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
import acme.entities.claim.ClaimType;
import acme.entities.leg.Leg;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimCreateService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Claim claim;

		claim = new Claim();
		claim.setDraftMode(true);
		super.getBuffer().addData(claim);

	}

	@Override
	public void bind(final Claim claim) {
		super.bindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "indicator", "assistanceAgent", "leg");

	}

	@Override
	public void validate(final Claim claim) {
		Leg leg;
		Date legTime;
		boolean condition;

		leg = super.getRequest().getData("leg", Leg.class);

		Date currentMoment = MomentHelper.getCurrentMoment();
		legTime = this.repository.findArrivalTimeLegById(leg.getId());
		condition = currentMoment.before(legTime);

		super.state(condition, "legScheduledArrival", "acme.validation.leg-time.message");

	}

	@Override
	public void perform(final Claim claim) {
		this.repository.save(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		assert claim != null;
		Dataset dataset;
		Collection<Leg> legs;
		Collection<AssistanceAgent> agents;
		SelectChoices legChoices;
		SelectChoices typeChoices;
		SelectChoices agentChoices;

		legs = this.repository.findAllLegs();
		agents = this.repository.findAllAgents();
		legChoices = SelectChoices.from(legs, "flightNumber", claim.getLeg());
		typeChoices = SelectChoices.from(ClaimType.class, claim.getType());
		agentChoices = SelectChoices.from(agents, "employeeCode", claim.getAssistanceAgent());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "indicator", "assistanceAgent", "leg", "draftMode");
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("type", typeChoices);
		dataset.put("assistanceAgent", agentChoices);

		super.getResponse().addData(dataset);
	}

}
