
package acme.features.assistanceAgent.trackingLog;

import java.util.Collection;
import java.util.Date;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
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
		boolean status;
		int id;
		Claim claim;
		AssistanceAgent assistanceAgent;

		id = super.getRequest().getData("claimId", int.class);
		claim = this.repository.findClaimById(id);
		assistanceAgent = claim == null ? null : claim.getAssistanceAgent();
		status = claim != null && super.getRequest().getPrincipal().hasRealm(assistanceAgent);

		if (status) {
			String method;
			String indicator;
			method = super.getRequest().getMethod();

			if (method.equals("GET"))
				status = true;
			else {
				indicator = super.getRequest().getData("indicator", String.class);
				status = indicator.equals("0") || this.isValidEnum(ClaimStatus.class, indicator);
			}
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrackingLog trackingLog;
		Date moment;
		int claimId;
		Claim claim;

		claimId = super.getRequest().getData("claimId", int.class);
		claim = this.repository.findClaimById(claimId);
		moment = MomentHelper.getCurrentMoment();

		trackingLog = new TrackingLog();
		trackingLog.setLastUpdateMoment(moment);
		trackingLog.setIndicator(ClaimStatus.PENDING);
		trackingLog.setOrderDate(moment);
		trackingLog.setDraftMode(true);
		trackingLog.setClaim(claim);

		super.getResponse().addGlobal("claimId", claimId);
		super.getBuffer().addData(trackingLog);

	}

	@Override
	public void bind(final TrackingLog trackingLog) {
		super.bindObject(trackingLog, "step", "resolutionPercentage", "indicator", "resolution");
	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		{
			boolean correctlogs;
			Collection<TrackingLog> logs;
			Predicate<TrackingLog> isPublished;
			long totalCompletedLogs;
			long publishedCompletedLogs;

			logs = this.repository.findFinishedTrackingLogsByClaimId(trackingLog.getClaim().getId(), 100.00);
			isPublished = log -> !log.getDraftMode();

			totalCompletedLogs = logs.stream().count();
			publishedCompletedLogs = logs.stream().filter(isPublished).count();

			if (publishedCompletedLogs == 0) {
				correctlogs = totalCompletedLogs < 1;
				super.state(correctlogs, "*", "acme.validation.trackingLog.invalid-number-completed-logs.message");
			} else {
				correctlogs = totalCompletedLogs < 2;
				super.state(correctlogs, "*", "acme.validation.trackingLog.invalid-number-completed-published-logs.message");
			}
		}
	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		this.repository.save(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;
		SelectChoices indicatorChoices;

		indicatorChoices = SelectChoices.from(ClaimStatus.class, trackingLog.getIndicator());

		dataset = super.unbindObject(trackingLog, "step", "resolutionPercentage", "indicator", "resolution");
		dataset.put("indicator", indicatorChoices.getSelected().getKey());
		dataset.put("indicators", indicatorChoices);

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
