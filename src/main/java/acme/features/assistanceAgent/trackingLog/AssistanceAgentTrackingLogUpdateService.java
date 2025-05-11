
package acme.features.assistanceAgent.trackingLog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.ClaimStatus;
import acme.entities.trackingLog.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogUpdateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int trackingLogId;
		TrackingLog trackingLog;
		AssistanceAgent assistanceAgent;

		trackingLogId = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(trackingLogId);
		assistanceAgent = trackingLog == null ? null : trackingLog.getClaim().getAssistanceAgent();
		status = trackingLog != null && super.getRequest().getPrincipal().hasRealm(assistanceAgent);

		if (status) {
			String method;
			String indicator;
			method = super.getRequest().getMethod();

			if (method.equals("GET"))
				status = true;
			else {
				indicator = super.getRequest().getData("indicator", String.class);
				status = this.isValidEnum(ClaimStatus.class, indicator);
			}
		}

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
		Date moment;

		super.bindObject(trackingLog, "step", "resolutionPercentage", "indicator", "resolution");
		moment = MomentHelper.getCurrentMoment();
		trackingLog.setLastUpdateMoment(moment);
	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		{
			boolean correctPercentage;
			Collection<TrackingLog> oldLogs;
			List<TrackingLog> logs;

			oldLogs = this.repository.findTrackingLogsByClaimId(trackingLog.getClaim().getId());
			logs = new ArrayList<>(oldLogs);
			logs.removeIf(log -> log.getId() == trackingLog.getId());
			logs.add(trackingLog);
			logs.sort(Comparator.comparing(TrackingLog::getOrderDate));
			correctPercentage = true;
			for (int i = 0; i < logs.size() - 1; i++) {
				TrackingLog currentLog = logs.get(i);
				TrackingLog nextLog = logs.get(i + 1);
				if (currentLog.getResolutionPercentage() > nextLog.getResolutionPercentage()) {
					correctPercentage = false;
					break;
				}
			}
			super.state(correctPercentage, "resolutionPercentage", "acme.validation.trackingLog.invalid-resolution-percentage.message");
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

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "step", "resolutionPercentage", "indicator", "resolution", "orderDate", "draftMode");
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
