
package acme.features.customer.booking;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.flight.Flight;
import acme.realms.Customer;

public interface CustomerBookingRepository extends AbstractRepository {

	@Query("select b from Booking b where b.locatorCode = :locatorCode")
	Booking findBookingByLocatorCode(@Param("locatorCode") String locatorCode);

	@Query("select b from Booking b where b.id = :id")
	Booking findBookingById(int id);

	@Query("select b from Booking b where b.customer.id = :customerId")
	List<Booking> findBookingByCustomerId(int customerId);

	@Query("select f from Flight f where f.id = :flightId")
	Flight findFlightById(int flightId);

	@Query("select f from Flight f")
	List<Flight> findAllFlights();

	@Query("select c from Customer c where c.id = :customerId")
	Customer findCustomerById(int customerId);

}
