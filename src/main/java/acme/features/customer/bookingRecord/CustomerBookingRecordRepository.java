
package acme.features.customer.bookingRecord;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.bookingRecord.BookingRecord;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

public interface CustomerBookingRecordRepository extends AbstractRepository {

	@Query("select b from Booking b where b.id = :bookingId")
	Booking findBookingById(int bookingId);

	@Query("select c from Customer c where c.id = :customerId")
	Customer findCustomerById(int customerId);

	@Query("select p from Passenger p left join BookingRecord br on br.passenger = p and br.booking.id = :bookingId where p.customer.id = :customerId AND br.id is NULL")
	List<Passenger> findAvailablePassengers(int bookingId, int customerId);

	@Query("select p from Passenger p where p.id = :passengerId")
	Passenger findPassengerById(int passengerId);

	@Query("select br.passenger from BookingRecord br where br.booking.id = :bookingId")
	List<Passenger> findPassengersByBookingId(int bookingId);

	@Query("select br from BookingRecord br where br.booking.id = :bookingId AND br.passenger.id = :passengerId")
	BookingRecord findBookingRecordByPassengerAndBooking(int bookingId, int passengerId);

}
