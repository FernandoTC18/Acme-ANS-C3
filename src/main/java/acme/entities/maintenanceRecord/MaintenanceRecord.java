
package acme.entities.maintenanceRecord;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Moment;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.entities.aircraft.Aircraft;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MaintenanceRecord extends AbstractEntity {

	private static final long		serialVersionUID	= 1L;

	@Mandatory
	@Automapped
	@ValidMoment
	private Moment					moment;

	@Mandatory
	@Valid
	@Automapped
	private MaintenanceRecordStatus	status;

	@Mandatory
	@ValidMoment(past = false)
	@Automapped
	private Date					inspectionDueDate;

	@Mandatory
	@Automapped
	@ValidMoney
	private Money					estimatedCost;

	@Optional
	@Automapped
	@ValidString(min = 0, max = 255)
	private String					notes;

	@Mandatory
	@Valid
	@Automapped
	@ManyToOne
	private Aircraft				aircraft;
}
