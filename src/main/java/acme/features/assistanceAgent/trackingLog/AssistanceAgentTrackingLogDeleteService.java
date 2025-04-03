
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
public class AssistanceAgentTrackingLogDeleteService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int id;
		TrackingLog trackingLog;
		Claim claim;
		AssistanceAgent assistanceAgent;

		id = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(id);
		claim = trackingLog == null ? null : trackingLog.getClaim();
		assistanceAgent = claim == null ? null : claim.getAssistanceAgent();
		status = claim != null && !claim.getDraftMode() && super.getRequest().getPrincipal().hasRealm(assistanceAgent) && !trackingLog.getDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrackingLog trackingLog;
		int id;

		id = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(id);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void bind(final TrackingLog trackingLog) {
		super.bindObject(trackingLog, "registrationMoment", "passengerEmail", "description", "type", "indicator", "assistanceAgent", "leg");

	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		boolean status;

		status = trackingLog.getDraftMode();

		super.state(status, "*", "acme.validation.deletePublishedTrackingLoh.message");
	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		this.repository.delete(trackingLog);
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
