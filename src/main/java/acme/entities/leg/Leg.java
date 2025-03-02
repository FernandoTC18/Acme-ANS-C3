
package acme.entities.leg;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Leg extends AbstractEntity {

	public static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(min = 7, max = 7)
	@Column(unique = true)
	private String				flightNumber;

	@Mandatory
	@ValidMoment()
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledDeparture;

	@Mandatory
	@ValidMoment()
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledArrival;

	@Mandatory
	@ValidNumber(min = 0)
	@Automapped
	Integer						duration;

	@Mandatory
	@Valid
	@Automapped
	LegStatus					status;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	Airport						departureAirport;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	Airport						arrivalAirport;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	Aircraft					plane;
}
