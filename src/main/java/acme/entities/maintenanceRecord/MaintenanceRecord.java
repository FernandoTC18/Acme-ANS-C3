
package acme.entities.maintenanceRecord;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.entities.aircraft.Aircraft;
import acme.realms.Technician;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MaintenanceRecord extends AbstractEntity {

	private static final long		serialVersionUID	= 1L;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date					moment;

	@Mandatory
	@Valid
	@Automapped
	private MaintenanceRecordStatus	status;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date					inspectionDueDate;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money					estimatedCost;

	@Optional
	@ValidString(min = 0, max = 255)
	@Automapped
	private String					notes;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Aircraft				aircraft;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Technician				technician;

	@Mandatory
	@Valid
	@Automapped
	private Boolean					draftMode;
}
