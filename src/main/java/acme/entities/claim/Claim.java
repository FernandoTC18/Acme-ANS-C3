
package acme.entities.claim;

import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.entities.leg.Leg;
import acme.entities.trackingLog.TrackingLog;
import acme.realms.AssistanceAgent;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Claim extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	@Mandatory
	@ValidEmail
	@Column(unique = true)
	private String				passengerEmail;

	@Mandatory
	@ValidString(min = 1, max = 255)
	@Automapped
	private String				description;

	@Mandatory
	@Valid
	@Automapped
	private ClaimType			type;

	@Mandatory
	@Valid
	@Automapped
	Boolean						draftMode;

	// Derived attributes -----------------------------------------------------


	@Transient
	public ClaimStatus getIndicator() {
		Collection<TrackingLog> logs;
		ClaimRepository repository;

		repository = SpringHelper.getBean(ClaimRepository.class);
		logs = repository.findTrackingLogsByClaimId(this.getId(), Double.valueOf(100.00));
		Optional<TrackingLog> optionalLog = logs.stream().filter(tl -> !tl.getDraftMode()).max(Comparator.comparing(TrackingLog::getOrderDate));

		if (optionalLog.isPresent())
			return optionalLog.get().getIndicator();
		else
			return ClaimStatus.PENDING;
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private AssistanceAgent	assistanceAgent;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Leg				leg;
}
