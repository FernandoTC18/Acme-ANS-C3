
package acme.features.authenticated.assistanceAgent;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Authenticated;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.realms.AssistanceAgent;

@GuiController
public class AuthenticatedAssistanceAgentController extends AbstractGuiController<Authenticated, AssistanceAgent> {

	@Autowired
	private AuthenticatedAssistanceAgentCreateService	createService;

	@Autowired
	private AuthenticatedAssistanceAgentUpdateService	updateService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
	}
}
