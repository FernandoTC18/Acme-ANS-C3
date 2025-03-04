
package acme.entities.task;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.realms.Technician;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Task extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Valid
	@Mandatory
	@Automapped
	private TaskType			type;

	@ValidString(min = 0, max = 255)
	@Mandatory
	@Automapped
	private String				description;

	@ValidNumber(min = 0, max = 10)
	@Mandatory
	@Automapped
	private Integer				priority;

	@ValidNumber(min = 0)
	@Mandatory
	@Automapped
	private Integer				estimatedDuration;

	@Mandatory
	@ManyToOne
	@Automapped
	@Valid
	private MaintenanceRecord	maintenanceRecord;

	@Mandatory
	@ManyToOne
	@Automapped
	@Valid
	private Technician			technician;

	@Mandatory
	@ManyToOne
	@Automapped
	@Valid
	private Aircraft			aircraft;

}
