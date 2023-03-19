package ru.tinkoff.lab.tripAPI.mapping;

import org.apache.ibatis.annotations.*;
import ru.tinkoff.lab.tripAPI.business.*;
import ru.tinkoff.lab.tripAPI.business.dto.DestinationDto;
import ru.tinkoff.lab.tripAPI.business.dto.TripDto;

import java.util.List;
import java.util.UUID;

@Mapper
public interface AccommodationDestinationTripMapper {

    /**
     * Accommodation CRUD
     */

    @Result(property = "id", column = "id")
    @Select("""
            INSERT INTO accommodation
            (address, booking_tickets)
            VALUES (#{address}, #{bookingUrl}) RETURNING id;
            """)
    Id insertAccommodation(Accommodation accommodation) throws RuntimeException;

    @Result(property = "bookingUrl", column = "booking_tickets")
    @Select("""
            SELECT id, address, booking_tickets
            FROM accommodation WHERE id = '${accommodationId}';
            """)
    Accommodation selectAccommodation(@Param("accommodationId") UUID accommodationId);

    @Update("""
            UPDATE accommodation
            SET address=#{address}, booking_tickets=#{bookingUrl}
            WHERE id = uuid(#{id});
            """)
    void updateAccommodation(Accommodation accommodation);

    @Delete("""
            DELETE FROM accommodation
            WHERE id = '${accommodationId}';
            """)
    void deleteAccommodation(@Param("accommodationId") UUID accommodationId);


    /**
     * Destination CRUD
     */
    @Result(property = "id", column = "id")
    @Select("""
            INSERT INTO destination
            (description, office_id, seat_place)
            VALUES(#{description}, uuid(#{officeId}), #{seatPlace}) RETURNING id;
            """)
    Id insertDestination(DestinationDto destinationDto) throws RuntimeException;

    @Results(value = {
            @Result(property = "office.id", column = "office_id"),
            @Result(property = "office.address", column = "office_address"),
            @Result(property = "office.description", column = "office_description"),
            @Result(property = "seatPlace", column = "seat_place")
    })
    @Select("""
            SELECT d.id, d.description, d.seat_place,
            o.id as office_id, o.address as office_address, o.description as office_description
            FROM destination d
            JOIN office o on d.office_id = o.id
            WHERE d.id = '${destinationId}';
            """)
    Destination selectDestination(@Param("destinationId") UUID destinationId);

    @Update("""
            UPDATE destination
            SET description=#{description}, office_id=uuid(#{officeId}), seat_place=#{seatPlace}
            WHERE id = uuid(#{id});
            """)
    void updateDestination(DestinationDto destinationDto);

    @Delete("""
            DELETE FROM destination
            WHERE id = '${destinationId}';
            """)
    void deleteDestination(@Param("destinationId") UUID destinationId);

    /**
     * Trip CRUD
     */
    @Result(property = "id", column = "id")
    @Select("""
            INSERT INTO trip
            (trip_status, accommodation_id, destination_id, request_id)
            VALUES (#{tripStatus}, uuid(#{accommodationId}), uuid(#{destinationId}), uuid(#{requestId})) RETURNING id;
            """)
    Id insertTrip(TripDto tripDto) throws RuntimeException;

    @Results(value = {
            @Result(property = "tripStatus", column = "trip_status"),
            @Result(property = "requestId", column = "request_id"),
            @Result(property = "accommodation.id", column = "accom_id"),
            @Result(property = "accommodation.address", column = "address"),
            @Result(property = "accommodation.bookingUrl", column = "booking_tickets"),
            @Result(property = "destination.id", column = "dest_id"),
            @Result(property = "destination.description", column = "destination_description"),
            @Result(property = "destination.seatPlace", column = "seat_place"),
            @Result(property = "destination.office.id", column = "office_id"),
            @Result(property = "destination.office.address", column = "office_address"),
            @Result(property = "destination.office.description", column = "office_description")
    })

    @Select("""
            SELECT t.id, t.trip_status, t.request_id,
            	a.id as accom_id, a.address, a.booking_tickets,
            	d.id as dest_id, d.description as destination_description, d.seat_place,
            	o.id as office_id, o.address as office_address, o.description as office_description
            FROM trip t
            JOIN accommodation a on a.id = t.accommodation_id
            JOIN destination d on t.destination_id = d.id
            JOIN office o on d.office_id = o.id
            WHERE t.id = '${tripId}';
            """)
    Trip selectTrip(@Param("tripId") UUID tripId);

    @Results(value = {
            @Result(property = "id", column = "trip_id"),
            @Result(property = "tripStatus", column = "trip_status"),
            @Result(property = "requestId", column = "request_id"),
            @Result(property = "accommodation.id", column = "accom_id"),
            @Result(property = "accommodation.address", column = "address"),
            @Result(property = "accommodation.bookingUrl", column = "booking_tickets"),
            @Result(property = "destination.id", column = "dest_id"),
            @Result(property = "destination.description", column = "destination_description"),
            @Result(property = "destination.seatPlace", column = "seat_place"),
            @Result(property = "destination.office.id", column = "office_id"),
            @Result(property = "destination.office.address", column = "office_address"),
            @Result(property = "destination.office.description", column = "office_description")
    })
    @Select("""
            SELECT t.id as trip_id, t.trip_status, t.request_id, a.id as accom_id, a.address, a.booking_tickets,
            d.id as dest_id, d.description as destination_description, d.seat_place,
            o.id as office_id, o.address as office_address, o.description as office_description
            FROM trip t
            JOIN request r on t.request_id = r.id
            JOIN accommodation a on t.accommodation_id = a.id
            JOIN destination d on t.destination_id = d.id
            JOIN office o on d.office_id = o.id
            WHERE r.worker_id = '${userId}'
            ORDER BY r.start_date OFFSET #{offset} ROWS FETCH NEXT #{rows} ROWS ONLY;
            """)
    List<Trip> selectSomeTrips(@Param("userId") UUID userId, int offset, int rows);


    @Update("""
            UPDATE trip
            SET trip_status=#{tripStatus}, accommodation_id=uuid(#{accommodationId}),
            destination_id=uuid(#{destinationId}), request_id=uuid(#{requestId})
            WHERE id = uuid(#{id});
            """)
    void updateTrip(TripDto tripDto);

    @Update("""
            UPDATE trip
            SET trip_status='CANCELED'
            WHERE id = '${tripId}';
            """)
    void cancelTrip(@Param("tripId") UUID tripId);

    @Update("""
            UPDATE trip
            SET trip_status='COMPLETED'
            WHERE id = '${tripId}';
            """)
    void completeTrip(@Param("tripId") UUID tripId);

    @Delete("""
            DELETE FROM trip
            WHERE id = '${tripId}';
            """)
    void deleteTrip(@Param("tripId") UUID tripId);

}
