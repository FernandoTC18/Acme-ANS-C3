
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
public class AssistanceAgentClaimShowService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
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
		if (claim.getDraftMode() != false)
			dataset.put("readonly", true);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("type", typeChoices);
		dataset.put("assistanceAgent", agentChoices);

		super.getResponse().addData(dataset);
	}

}
