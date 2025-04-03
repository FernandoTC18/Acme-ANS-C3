
package acme.features.assistanceAgent.trackingLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimStatus;
import acme.entities.trackingLog.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogCreateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		TrackingLog trackingLog;

		trackingLog = new TrackingLog();
		trackingLog.setDraftMode(true);
		super.getBuffer().addData(trackingLog);

	}

	@Override
	public void bind(final TrackingLog claim) {
		super.bindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "indicator", "assistanceAgent", "leg");

	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		Claim claim;
		Claim claimTrackingLog;
		boolean condition;

		claim = super.getRequest().getData("claim", Claim.class);

		claimTrackingLog = this.repository.findClaimById(claim.getId());
		condition = trackingLog.getDraftMode() && !claimTrackingLog.getDraftMode();

		super.state(condition, "claim", "acme.validation.claim-not-published.message");

	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		this.repository.save(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		assert trackingLog != null;
		Dataset dataset;
		SelectChoices typeChoices;

		typeChoices = SelectChoices.from(ClaimStatus.class, trackingLog.getIndicator());

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "step", "resolutionPercentage", "indicator", "resolution", "orderDate", "draftMode", "claim");
		dataset.put("type", typeChoices);

		super.getResponse().addData(dataset);
	}

}
