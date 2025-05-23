
package acme.entities.involves;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.task.Task;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "task_id"), @Index(columnList = "maintenance_record_id")
})
public class Involves extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private MaintenanceRecord	maintenanceRecord;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Task				task;
}
