
package acme.constraints;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.aircraft.Aircraft;
import acme.entities.aircraft.AircraftStatus;
import acme.entities.airport.Airport;
import acme.entities.leg.Leg;
import acme.entities.leg.LegRepository;

@Validator
public class LegValidator extends AbstractValidator<ValidLeg, Leg> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private LegRepository repository;
	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidLeg annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (leg == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {

			{
				boolean correctMinScheduleDeparture;
				Date scheduledDeparture;

				scheduledDeparture = leg.getScheduledDeparture();
				if (scheduledDeparture != null) {
					correctMinScheduleDeparture = !MomentHelper.isBefore(leg.getScheduledDeparture(), MomentHelper.getCurrentMoment());
					super.state(context, correctMinScheduleDeparture, "scheduledDeparture", "acme.validation.leg.correctMinScheduleDeparture.message");
				}

			}
			{
				boolean correctFlightNumber;

				correctFlightNumber = leg.getFlightNumber() != null && Pattern.matches("^[A-Z]{3}\\d{4}$", leg.getFlightNumber());

				super.state(context, correctFlightNumber, "flightNumber", "acme.validation.leg.correctFlightNumberPattern.message");

			}
			{
				boolean flNumberNotInDb;
				Leg legInDb;
				legInDb = this.repository.computeLegByFlightNumber(leg.getFlightNumber());
				flNumberNotInDb = legInDb == null || leg.getFlightNumber().isBlank() || legInDb.equals(leg);
				super.state(context, flNumberNotInDb, "flightNumber", "acme.validation.leg.flightNumberNotInDb.message");
			}
			{
				boolean iataInDB;

				if (leg.getFlightNumber() != null && leg.getFlightNumber().length() == 7 && leg.getAirline() != null) {

					iataInDB = leg.getAirline().getIataCode().equals(leg.getFlightNumber().substring(0, 3));
					super.state(context, iataInDB, "flightNumber", "acme.validation.leg.correctFlightNumberIataCode.message");
				}
			}
			{
				boolean correctMinScheduleArrival;
				Date scheduledDeparture;
				Date scheduledArrival;

				scheduledDeparture = leg.getScheduledDeparture();
				scheduledArrival = leg.getScheduledArrival();

				if (scheduledArrival != null && scheduledDeparture != null) {
					correctMinScheduleArrival = !MomentHelper.isBefore(scheduledArrival, MomentHelper.deltaFromMoment(leg.getScheduledDeparture(), 1, ChronoUnit.MINUTES));
					super.state(context, correctMinScheduleArrival, "scheduledArrival", "acme.validation.leg.correctMinScheduleArrival.message");
				}
			}
			{
				boolean distinctAirports;
				Airport departureAirport;
				Airport arrivalAirport;

				departureAirport = leg.getDepartureAirport();
				arrivalAirport = leg.getArrivalAirport();

				if (departureAirport != null && arrivalAirport != null) {
					distinctAirports = !departureAirport.equals(arrivalAirport);
					super.state(context, distinctAirports, "arrivalAirport", "acme.validation.leg.departureArrivalAirportsDistinct.message");
				}
			}
			{
				boolean noAircraftInMaintenance;
				Aircraft aircraft;

				aircraft = leg.getPlane();

				if (aircraft != null) {
					noAircraftInMaintenance = !aircraft.getStatus().equals(AircraftStatus.UNDER_MAINTENANCE);
					super.state(context, noAircraftInMaintenance, "plane", "acme.validation.leg.noAircraftInMaintenance.message");
				}
			}

			{
				boolean availableAircraft;
				Aircraft aircraft;
				aircraft = leg.getPlane();
				if (aircraft != null && leg.getScheduledArrival() != null && leg.getScheduledDeparture() != null) {
					int aircraftMatchesNumber = 0;
					List<Leg> legsWithSamePlane = this.repository.computeDistinctLegsWithSameAircraft(aircraft.getRegistrationNumber(), leg.getId());

					for (Leg l : legsWithSamePlane)
						if (leg.getScheduledDeparture().before(l.getScheduledArrival()) && leg.getScheduledArrival().after(l.getScheduledDeparture()))
							aircraftMatchesNumber += 1;
					availableAircraft = aircraftMatchesNumber == 0;
					super.state(context, availableAircraft, "plane", "acme.validation.leg.noAvailableAircraft.message");
				}
			}

		}
		result = !super.hasErrors(context);

		return result;
	}
}
