package ru.tinkoff.lab.tripAPI.mapping;

import org.apache.ibatis.annotations.*;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.Request;
import ru.tinkoff.lab.tripAPI.business.dto.RequestDto;

import java.util.UUID;


/**
 * Request operations
 */
@Mapper
public interface RequestMapper {

    @Result(property = "id", column = "id")
    @Select("""
            INSERT INTO request(
            	trip_id, status, description, worker_id, office_id, comment,
            	start_date, end_date, transport_to, transport_from, tickets)
            	VALUES (uuid(#{tripId}), #{requestStatus}, #{description}, uuid(#{workerId}),
            	       uuid(#{officeId}), #{comment}, #{startDate}, #{endDate}, #{transportTo},
            	       #{transportFrom}, #{ticketsUrl})
            	RETURNING id;
            """)
    Id insertRequest(RequestDto requestDto) throws RuntimeException;

    @Results(value = {
            @Result(property = "id", column = "request_id"),
            @Result(property = "requestStatus", column = "status"),
            @Result(property = "startDate", column = "start_date"),
            @Result(property = "endDate", column = "end_date"),
            @Result(property = "transportTo", column = "transport_to"),
            @Result(property = "transportFrom", column = "transport_from"),
            @Result(property = "ticketsUrl", column = "tickets"),
            @Result(property = "trip.id", column = "trip_id"),
            @Result(property = "trip.tripStatus", column = "trip_status"),
            @Result(property = "trip.accommodation.id", column = "accom_id"),
            @Result(property = "trip.accommodation.address", column = "address"),
            @Result(property = "trip.accommodation.checkinTime", column = "checkin_time"),
            @Result(property = "trip.accommodation.checkoutTime", column = "checkout_time"),
            @Result(property = "trip.accommodation.bookingUrl", column = "booking_tickets"),
            @Result(property = "trip.destination.id", column = "dest_id"),
            @Result(property = "trip.destination.description", column = "destination_description"),
            @Result(property = "trip.destination.seatPlace", column = "seat_place"),
            @Result(property = "trip.destination.office.id", column = "office_id"),
            @Result(property = "trip.destination.office.address", column = "office_address"),
            @Result(property = "trip.destination.office.description", column = "office_description"),
            @Result(property = "worker.id", column = "user_id"),
            @Result(property = "worker.email", column = "email"),
            @Result(property = "worker.password", column = "password"),
            @Result(property = "worker.firstName", column = "first_name"),
            @Result(property = "worker.secondName", column = "second_name"),
            @Result(property = "worker.userRole", column = "user_role"),
            @Result(property = "office.id", column = "office_id"),
            @Result(property = "office.address", column = "office_address"),
            @Result(property = "office.description", column = "office_description")

    })
    @Select("""
            SELECT r.id as request_id, r.status, r.description, r.comment, r.start_date, r.end_date, r.transport_to, r.transport_from, r.tickets,
            t.id as trip_id, t.trip_status, a.id as accom_id, a.address, a.checkin_time, a.checkout_time, a.booking_tickets,
            d.id as dest_id, d.description as destination_description, d.seat_place,
            u.id as user_id, u.email, u.password, u.first_name, u.second_name, u.user_role,
            o.id as office_id, o.address as office_address, o.description as office_description
            FROM request r
            JOIN trip t on r.trip_id = t.id
            JOIN users u on r.worker_id = u.id
            JOIN office o on r.office_id = o.id
            JOIN accommodation a on t.accommodation_id = a.id
            JOIN destination d on t.destination_id = d.id
            WHERE r.id = '${requestId}';
            """)
    Request selectRequest(@Param("requestId") UUID requestId);

    @Update("""
            UPDATE request
            SET trip_id=uuid(#{tripId}), status=#{requestStatus}, description=#{description},
            worker_id=uuid(#{workerId}), office_id=uuid(#{officeId}), comment=#{comment},
            start_date=#{startDate}, end_date=#{endDate}, transport_to=#{transportTo},
            transport_from=#{transportFrom}, tickets=#{ticketsUrl}
            WHERE id = uuid(#{id});
            """)
    void updateRequest(RequestDto requestDto);

    @Delete("""
            DELETE FROM request
            WHERE id = '${requestId}';
            """)
    void deleteRequest(@Param("requestId") UUID requestId);
}
