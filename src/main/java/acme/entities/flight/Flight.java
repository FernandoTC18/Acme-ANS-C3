
package acme.entities.flight;

import java.util.Date;
import java.util.List;

import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.entities.leg.Leg;

public class Flight extends AbstractEntity {

	public static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				tag;

	@Mandatory
	@Automapped
	private Boolean				selfTransferRequired;

	@Mandatory
	@ValidMoney(min = 0)
	@Automapped
	private Money				price;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				description;

	@Mandatory
	@Valid
	@OneToMany()
	private List<Leg>			legs;


	@Transient
	public Date getScheduledDeparture() {
		Leg firstLeg = this.legs.get(0);
		return firstLeg.getScheduledDeparture();
	}

	@Transient
	public Date getScheduledArrival() {
		Leg lastLeg = this.legs.get(this.legs.size() - 1);
		return lastLeg.getScheduledArrival();
	}

	@Transient
	public String getOriginCity() {
		Leg firstLeg = this.legs.get(0);
		return firstLeg.getDepartureAirport().getCity();
	}

	@Transient
	public String getArrivalCity() {
		Leg lastLeg = this.legs.get(this.legs.size() - 1);
		return lastLeg.getArrivalAirport().getCity();
	}

	@Transient
	public Integer getLayoversNumber() {
		return this.legs.size() - 1;
	}
}
