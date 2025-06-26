
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.airline.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "employeeCode", unique = true)
})
public class FlightCrew extends AbstractRole {

	//Serialisation version --------------------------------------------------
	private static final long		serialVersionUID	= 1L;

	//Attributes --------------------------------------------------

	@Mandatory
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$", message = "{acme.validation.incorrectly-formatted-employeeCode.message}")
	@Column(unique = true)
	private String					employeeCode;

	@Mandatory
	@ValidString(pattern = "^\\+?\\d{6,15}$", message = "{acme.validation.incorrectly-formatted-phoneNumber.message}")
	@Automapped
	private String					phoneNumber;

	@Mandatory
	@ValidString(min = 1, max = 255)
	@Automapped
	private String					languageSkills;

	@Mandatory
	@Valid
	@Automapped
	private FlightCrewAvailability	availability;

	@Mandatory
	@ValidMoney()
	@Automapped
	private Money					salary;

	@Optional
	@ValidNumber(min = 0, max = 120)
	@Automapped
	private Integer					experienceYears;

	//Relationships --------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline					airline;

}
