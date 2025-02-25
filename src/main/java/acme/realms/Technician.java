
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Technician extends AbstractRole {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Column(unique = true)
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$")
	@Automapped
	private String				licenseNumber;

	@Mandatory
	@ValidString(pattern = "^\\+?\\d{6,15}$")
	@Automapped
	private String				phoneNumber;

	@Mandatory
	@ValidString(min = 0, max = 50)
	@Automapped
	private String				specialization;

	@Mandatory
	@Automapped
	private Boolean				anualHealthTest;

	@Mandatory
	@Automapped
	private Integer				yearsOfExperience;

	@Automapped
	@ValidString(min = 0, max = 255)
	private String				certifications;
	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
