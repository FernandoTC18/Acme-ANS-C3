
package acme.features.customer.passenger;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.passenger.Passenger;

public interface CustomerPassengerRepository extends AbstractRepository {

	@Query("select br.passenger from BookingRecord br where br.booking.id = :bookingId")
	List<Passenger> findPassengersByBookingId(int bookingId);
}
