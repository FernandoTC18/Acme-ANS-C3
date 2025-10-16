
package acme.features.authenticated.assistanceAgent;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;
import acme.realms.AssistanceAgent;

@GuiService
public class AuthenticatedAssistanceAgentUpdateService extends AbstractGuiService<Authenticated, AssistanceAgent> {

	@Autowired
	private AuthenticatedAssistanceAgentRepository	repository;

	@Autowired
	private AirlineRepository						airlineRepository;


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		AssistanceAgent agent;
		int userAccountId;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		agent = this.repository.findAgentByUserId(userAccountId);

		super.getBuffer().addData(agent);

	}

	@Override
	public void bind(final AssistanceAgent agent) {
		int airlineCode = super.getRequest().getData("airlineCode", int.class);
		Airline airline = this.airlineRepository.findById(airlineCode);

		super.bindObject(agent, "employeeCode", "spokenLanguages", "briefBio", "salary", "photo");

		agent.setAirline(airline);
	}

	@Override
	public void validate(final AssistanceAgent agent) {
		if (agent.getAirline() == null)
			super.state(false, "airlineCode", "acme.validation.airline.not.null");
	}

	@Override
	public void perform(final AssistanceAgent agent) {
		this.repository.save(agent);

	}

	@Override
	public void unbind(final AssistanceAgent agent) {
		Dataset dataset;

		SelectChoices airlineChoices;
		dataset = super.unbindObject(agent, "employeeCode", "spokenLanguages", "moment", "briefBio", "salary", "photo");

		Collection<Airline> airlines = this.airlineRepository.findAllAirlines();
		airlineChoices = SelectChoices.from(airlines, "iataCode", agent.getAirline());

		dataset.put("airlineCode", airlineChoices.getSelected().getKey());
		dataset.put("airlineCodes", airlineChoices);
		super.getResponse().addData(dataset);

	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}
