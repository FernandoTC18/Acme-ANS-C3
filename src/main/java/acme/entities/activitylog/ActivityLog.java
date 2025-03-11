
package acme.entities.activitylog;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidActivityLog
public class ActivityLog extends AbstractEntity {

	//Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	//Attributes --------------------------------------------------

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	Date						registrationMoment;

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	String						typeOfIncident;

	@Mandatory
	@ValidString(min = 1, max = 255)
	@Automapped
	String						description;

	@Mandatory
	@ValidNumber(min = 0, max = 10)
	@Automapped
	Integer						severityLevel;

	//Relationships --------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	FlightAssignment			flightAssignment;

}
