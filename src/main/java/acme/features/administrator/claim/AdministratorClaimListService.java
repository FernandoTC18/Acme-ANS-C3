
package acme.features.administrator.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;

@GuiService
public class AdministratorClaimListService extends AbstractGuiService<Administrator, Claim> {

	@Autowired
	private AdministratorClaimRepository repository;


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Claim> claims;

		claims = this.repository.findPublishedClaims();

		super.getBuffer().addData(claims);
	}

	@Override
	public void unbind(final Claim claims) {
		Dataset dataset;

		dataset = super.unbindObject(claims, "registrationMoment", "passengerEmail", "description", "indicator", "type", "leg", "draftMode");

		super.getResponse().addData(dataset);
	}

}
