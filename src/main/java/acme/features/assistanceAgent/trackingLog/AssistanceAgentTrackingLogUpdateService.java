
package acme.features.assistanceAgent.trackingLog;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

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
		status = trackingLog != null && trackingLog.getDraftMode() && super.getRequest().getPrincipal().hasRealm(assistanceAgent);

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
		int id;

		id = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(id);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void bind(final TrackingLog trackingLog) {
		super.bindObject(trackingLog, "step", "resolutionPercentage", "indicator", "resolution");
	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		{
			if (trackingLog.getResolutionPercentage() != null) {
				Double newPerc;
				OptionalInt optid;
				int idx;
				boolean isGreater;
				boolean isLesser;

				newPerc = trackingLog.getResolutionPercentage();

				List<TrackingLog> sortedLogs = this.loadAndSortLogs(trackingLog);
				optid = this.findIndexOf(trackingLog, sortedLogs);

				if (optid.isPresent()) {
					idx = optid.getAsInt();
					isGreater = this.isGreaterThanPrevious(idx, newPerc, sortedLogs);
					isLesser = this.isLessThanNext(idx, newPerc, sortedLogs);
					super.state(isGreater, "resolutionPercentage", "acme.validation.trackingLog.invalid-resolution-percentage-greater.message");
					super.state(isLesser, "resolutionPercentage", "acme.validation.trackingLog.invalid-resolution-percentage-lesser.message");
				}
			}
		}
	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		Date moment;

		moment = MomentHelper.getCurrentMoment();
		trackingLog.setLastUpdateMoment(moment);
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

	private List<TrackingLog> loadAndSortLogs(final TrackingLog trackingLog) {
		List<TrackingLog> logs = this.repository.findTrackingLogsByClaimId(trackingLog.getClaim().getId());
		List<TrackingLog> all = new ArrayList<>(logs);

		all.sort(Comparator.comparing(TrackingLog::getOrderDate).thenComparing(TrackingLog::getResolutionPercentage));
		return all;
	}

	private OptionalInt findIndexOf(final TrackingLog target, final List<TrackingLog> sorted) {
		return IntStream.range(0, sorted.size()).filter(i -> sorted.get(i).getId() == target.getId()).findFirst();
	}

	private boolean isGreaterThanPrevious(final int idx, final Double newPerc, final List<TrackingLog> logs) {
		if (idx == 0)
			return true;
		Double prev = logs.get(idx - 1).getResolutionPercentage();
		if (prev.equals(Double.valueOf(100.00)))
			return prev.equals(newPerc);
		return newPerc > prev;
	}

	private boolean isLessThanNext(final int idx, final Double newPerc, final List<TrackingLog> logs) {
		if (idx >= logs.size() - 1)
			return true;
		Double next = logs.get(idx + 1).getResolutionPercentage();
		return newPerc < next;
	}

}
