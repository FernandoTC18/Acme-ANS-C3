
package acme.entities.flight;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidFlight;
import acme.realms.Manager;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@ValidFlight
public class Flight extends AbstractEntity {

	public static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				tag;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				selfTransferRequired;

	@Mandatory
	@ValidMoney(min = 0)
	@Automapped
	private Money				cost;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				description;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Manager				manager;


	@Transient
	public Date getScheduledDeparture() {
		Date result;
		FlightRepository repository;

		repository = SpringHelper.getBean(FlightRepository.class);
		result = repository.computeScheduledDepartureByFlight(this.getId());
		return result;
	}

	@Transient
	public Date getScheduledArrival() {
		Date result;
		FlightRepository repository;

		repository = SpringHelper.getBean(FlightRepository.class);
		result = repository.computeScheduledArrivalByFlight(this.getId());
		return result;
	}

	@Transient
	public String getOriginCity() {
		String result;
		FlightRepository repository;

		repository = SpringHelper.getBean(FlightRepository.class);
		result = repository.computeOriginCityByFlight(this.getId());
		return result;
	}

	@Transient
	public String getArrivalCity() {
		String result;
		FlightRepository repository;

		repository = SpringHelper.getBean(FlightRepository.class);
		result = repository.computeArrivalCityByFlight(this.getId());
		return result;
	}

	@Transient
	public Integer getLayoversNumber() {
		Integer result;
		FlightRepository repository;

		repository = SpringHelper.getBean(FlightRepository.class);
		result = repository.computeLegsNumberByFlight(this.getId()) - 1;
		return result;
	}
}
