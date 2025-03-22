
package acme.features.authenticated.booking;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;

public interface AuthenticatedBookingRepository extends AbstractRepository {

	@Query("select b from Booking b where b.locatorCode = :locatorCode")
	Booking findBookingByLocatorCode(@Param("locatorCode") String locatorCode);
}
