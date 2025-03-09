
package acme.entities.operatesAt;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.entities.airline.Airline;
import acme.entities.airport.Airport;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OperatesAt extends AbstractEntity {

	//Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	//Relationships

	@Mandatory
	@Valid
	@Automapped
	@ManyToOne(optional = false)
	private Airline				airline;

	@Mandatory
	@Valid
	@Automapped
	@ManyToOne(optional = false)
	private Airport				airport;

}
