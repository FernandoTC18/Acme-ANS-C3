
package acme.entities.flightAssignment;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.entities.leg.Leg;
import acme.realms.FlightCrew;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FlightAssignment extends AbstractEntity {

	//Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	//Attributes -------------------------------------------------------------

	@Mandatory
	@Valid
	@Automapped
	Duty						duty;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	Date						lastUpdate;

	@Mandatory
	@Valid
	@Automapped
	AssignmentStatus			status;

	@Optional
	@ValidString(min = 0, max = 255)
	@Automapped
	String						remarks;

	//Relationships -------------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	Leg							leg;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	FlightCrew					flightCrewMember;

}
