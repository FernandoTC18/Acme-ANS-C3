
package acme.features.assistanceAgent.dashboard;

import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.IntSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimStatus;
import acme.entities.forms.AssistanceAgentDashboard;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentDashboardShowService extends AbstractGuiService<AssistanceAgent, AssistanceAgentDashboard> {

	@Autowired
	private AssistanceAgentDashboardRepository	repository;

	private static ZoneId						zone		= ZoneId.systemDefault();
	private static DateTimeFormatter			monthFmt	= DateTimeFormatter.ofPattern("yyyy-MM");


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();

		Collection<Claim> claims = this.repository.findClaimsByAgentId(agentId);

		final AssistanceAgentDashboard dashboard = new AssistanceAgentDashboard();

		int totalClaims = claims.size();
		long resolved = claims.stream().filter(c -> c.getIndicator() == ClaimStatus.ACCEPTED).count();
		long rejected = claims.stream().filter(c -> c.getIndicator() == ClaimStatus.REJECTED).count();

		dashboard.setRatioClaimsResolved(AssistanceAgentDashboardShowService.safeRatio(resolved, totalClaims));
		dashboard.setRatioClaimsRejected(AssistanceAgentDashboardShowService.safeRatio(rejected, totalClaims));

		Map<String, Integer> monthCounts = claims.stream().collect(Collectors.groupingBy(c -> this.toYearMonth(c).format(AssistanceAgentDashboardShowService.monthFmt), Collectors.summingInt(x -> 1)));

		Map<String, Integer> topMonths = monthCounts.entrySet().stream().sorted(Map.Entry.<String, Integer> comparingByValue().reversed()).limit(3).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));

		dashboard.setTopMonthsHighestClaims(topMonths);

		List<Integer> logsPerClaim = claims.stream().map(c -> this.repository.findTrackingLogsByClaimId(c.getId()).size()).toList();

		Stats logStats = AssistanceAgentDashboardShowService.computeStats(logsPerClaim);
		dashboard.setAverageNumberLogsPerClaims(logStats.avg());
		dashboard.setMinimumNumberLogsPerClaims(logStats.min());
		dashboard.setMaximumNumberLogsPerClaims(logStats.max());
		dashboard.setDeviationNumberLogsPerClaims(logStats.std());

		LocalDate today = LocalDate.now(AssistanceAgentDashboardShowService.zone);
		LocalDate start = today.minusDays(29);

		Map<LocalDate, Long> claimsCountByDay = claims.stream().filter(c -> !this.toLocalDate(c).isBefore(start)).collect(Collectors.groupingBy(this::toLocalDate, Collectors.counting()));

		List<Integer> claimsPerDay = AssistanceAgentDashboardShowService.dateRange(start, today).map(day -> claimsCountByDay.getOrDefault(day, 0L).intValue()).toList();

		Stats assistedStats = AssistanceAgentDashboardShowService.computeStats(claimsPerDay);
		dashboard.setAverageNumberClaimsAssisted(assistedStats.avg());
		dashboard.setMinimumNumberClaimsAssisted(assistedStats.min());
		dashboard.setMaximumNumberClaimsAssisted(assistedStats.max());
		dashboard.setDeviationNumberClaimsAssisted(assistedStats.std());

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final AssistanceAgentDashboard object) {
		Dataset dataset = super.unbindObject(object, "ratioClaimsResolved", "ratioClaimsRejected", "topMonthsHighestClaims", "averageNumberLogsPerClaims", "minimumNumberLogsPerClaims", "maximumNumberLogsPerClaims", "deviationNumberLogsPerClaims",
			"averageNumberClaimsAssisted", "minimumNumberClaimsAssisted", "maximumNumberClaimsAssisted", "deviationNumberClaimsAssisted");

		super.getResponse().addData(dataset);
	}

	// Ancillary methods ------------------------------------------------------

	private static double safeRatio(final long part, final long total) {
		return total == 0 ? 0.0 : (double) part / (double) total;
	}

	private LocalDate toLocalDate(final Claim c) {
		Instant instant = c.getRegistrationMoment().toInstant();
		return instant.atZone(AssistanceAgentDashboardShowService.zone).toLocalDate();
	}

	private YearMonth toYearMonth(final Claim c) {
		return YearMonth.from(this.toLocalDate(c));
	}

	private static Stream<LocalDate> dateRange(final LocalDate startInclusive, final LocalDate endInclusive) {
		long days = java.time.temporal.ChronoUnit.DAYS.between(startInclusive, endInclusive) + 1;
		return java.util.stream.LongStream.range(0, days).mapToObj(startInclusive::plusDays);
	}

	private static Stats computeStats(final List<Integer> values) {
		if (values == null || values.isEmpty())
			return new Stats(0.0, 0, 0, 0.0);

		IntSummaryStatistics summary = values.stream().mapToInt(Integer::intValue).summaryStatistics();
		double avg = summary.getAverage();

		double variance = values.stream().mapToDouble(v -> {
			double diff = v - avg;
			return diff * diff;
		}).average().orElse(0.0);

		double std = Math.sqrt(variance);

		return new Stats(avg, summary.getMin(), summary.getMax(), std);
	}


	private record Stats(double avg, int min, int max, double std) {
	}

}
