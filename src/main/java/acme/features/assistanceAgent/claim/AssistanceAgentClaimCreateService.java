
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
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);

		if (status) {
			String method;
			int legId;
			Leg leg;
			String type;
			method = super.getRequest().getMethod();

			if (method.equals("GET"))
				status = true;
			else {
				legId = super.getRequest().getData("leg", int.class);
				leg = this.repository.findLegById(legId);
				type = super.getRequest().getData("type", String.class);
				status = (legId == 0 || leg != null) && (type.equals("0") || this.isValidEnum(ClaimType.class, type));
			}
		}

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
		claim.setAssistanceAgent(assistanceAgent);

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
		{
			Date legTime;
			Date moment;
			boolean condition;

			if (claim.getLeg() != null) {
				moment = claim.getRegistrationMoment();
				legTime = claim.getLeg().getScheduledArrival();
				condition = !moment.before(legTime);

				super.state(condition, "*", "acme.validation.leg-time.message");
			}
		}
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

		legs = this.repository.findAllLegs();

		legChoices = SelectChoices.from(legs, "flightNumber", claim.getLeg());
		typeChoices = SelectChoices.from(ClaimType.class, claim.getType());

		dataset = super.unbindObject(claim, "passengerEmail", "description", "type", "leg");
		dataset.put("type", typeChoices.getSelected().getKey());
		dataset.put("types", typeChoices);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);

		super.getResponse().addData(dataset);
	}

	// Ancillary methods ------------------------------------------------------

	private <E extends Enum<E>> boolean isValidEnum(final Class<E> enumClass, final String name) {
		if (name == null)
			return false;
		try {
			Enum.valueOf(enumClass, name);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

}
