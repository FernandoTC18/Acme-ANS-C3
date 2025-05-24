
package acme.entities.booking;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidBooking;
import acme.entities.flight.Flight;
import acme.features.customer.passenger.CustomerPassengerRepository;
import acme.realms.Customer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "locatorCode")
})
@ValidBooking
public class Booking extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(pattern = "^[A-Z0-9]{6,8}$", message = "{acme.validation.incorrectly-formatted-locatorCode.message}")
	@Column(unique = true)
	private String				locatorCode;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				purchaseMoment;

	@Mandatory
	@Valid
	@Automapped
	private TravelClass			travelClass;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				draftMode;

	@Optional
	@ValidString(pattern = "^\\d{4}$", message = "{acme.validation.incorrectly-formatted-lastCardNibble.message}")
	@Automapped
	private String				lastCardNibble;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Customer			customer;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight				flight;


	@Transient
	public Money getPrice() {
		Money price = new Money();

		CustomerPassengerRepository customerPassengerRepository = SpringHelper.getBean(CustomerPassengerRepository.class);

		if (this.getFlight() == null) {
			price.setAmount(0.0);
			price.setCurrency("EUR");
		} else {
			Integer passengersOfBooking = customerPassengerRepository.findPassengersByBookingId(this.getId()).size();
			price.setAmount(this.getFlight().getCost().getAmount() * passengersOfBooking);
			price.setCurrency(this.getFlight().getCost().getCurrency());
		}

		return price;
	}

}
