package ru.tinkoff.lab.tripAPI.mapping;

import org.apache.ibatis.annotations.*;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.Notification;
import ru.tinkoff.lab.tripAPI.business.dto.NotificationDto;

import java.util.List;
import java.util.UUID;

/**
 * Notification CRUD
 */
@Mapper
public interface NotificationMapper {

    @Result(property = "id", column = "id")
    @Select("""
            INSERT INTO notification (request_id, watched, user_id)
            VALUES(uuid(#{requestId}), #{watched}, uuid(#{userId})) RETURNING id;
            """)
    Id insertNotification(NotificationDto notificationDto) throws RuntimeException;

    @Results(value = {
            @Result(property = "id", column = "notification_id"),
            @Result(property = "user.id", column = "boss_id"),
            @Result(property = "user.email", column = "b_email"),
            @Result(property = "user.firstName", column = "b_first_name"),
            @Result(property = "user.secondName", column = "b_second_name"),
            @Result(property = "user.password", column = "b_password"),
            @Result(property = "user.userRole", column = "b_user_role"),
            @Result(property = "request.id", column = "request_id"),
            @Result(property = "request.requestStatus", column = "status"),
            @Result(property = "request.startDate", column = "start_date"),
            @Result(property = "request.endDate", column = "end_date"),
            @Result(property = "request.comment", column = "comment"),
            @Result(property = "request.description", column = "description"),
            @Result(property = "request.transportTo", column = "transport_to"),
            @Result(property = "request.transportFrom", column = "transport_from"),
            @Result(property = "request.ticketsUrl", column = "tickets"),
            @Result(property = "request.trip.id", column = "trip_id"),
            @Result(property = "request.trip.tripStatus", column = "trip_status"),
            @Result(property = "request.trip.accommodation.id", column = "accom_id"),
            @Result(property = "request.trip.accommodation.address", column = "address"),
            @Result(property = "request.trip.accommodation.checkinTime", column = "checkin_time"),
            @Result(property = "request.trip.accommodation.checkoutTime", column = "checkout_time"),
            @Result(property = "request.trip.accommodation.bookingUrl", column = "booking_tickets"),
            @Result(property = "request.trip.destination.id", column = "dest_id"),
            @Result(property = "request.trip.destination.description", column = "destination_description"),
            @Result(property = "request.trip.destination.seatPlace", column = "seat_place"),
            @Result(property = "request.trip.destination.office.id", column = "office_id"),
            @Result(property = "request.trip.destination.office.address", column = "office_address"),
            @Result(property = "request.trip.destination.office.description", column = "office_description"),
            @Result(property = "request.worker.id", column = "worker_id"),
            @Result(property = "request.worker.email", column = "email"),
            @Result(property = "request.worker.password", column = "password"),
            @Result(property = "request.worker.firstName", column = "first_name"),
            @Result(property = "request.worker.secondName", column = "second_name"),
            @Result(property = "request.worker.userRole", column = "user_role"),
            @Result(property = "request.office.id", column = "office_id"),
            @Result(property = "request.office.address", column = "office_address"),
            @Result(property = "request.office.description", column = "office_description"),
            @Result(property = "watched", column = "watched")
    })
    @Select("""
            SELECT n.id as notification_id, n.watched,
            b.id as boss_id, b.email as b_email, b.password as b_password, b.first_name as b_first_name, b.second_name as b_second_name, b.user_role as b_user_role,
            r.id as request_id, r.status, r.description, r.comment, r.start_date, r.end_date, r.transport_to, r.transport_from, r.tickets,
            t.id as trip_id, t.trip_status, a.id as accom_id, a.address, a.checkin_time, a.checkout_time, a.booking_tickets,
            d.id as dest_id, d.description as destination_description, d.seat_place,
            u.id as worker_id, u.email, u.password, u.first_name, u.second_name, u.user_role,
            o.id as office_id, o.address as office_address, o.description as office_description
            FROM notification n
            JOIN request r ON n.request_id = r.id
            JOIN users u ON r.worker_id = u.id
            JOIN office o ON r.office_id = o.id
            JOIN trip t ON r.trip_id = t.id
            JOIN accommodation a ON t.accommodation_id = a.id
            JOIN destination d ON t.destination_id = d.id
            JOIN users b ON n.user_id = b.id
            WHERE n.id = '${notificationId}' AND n.watched=false;
            """)
    Notification selectNotification(@Param("notificationId") UUID notificationId);

    @Results(value = {
            @Result(property = "id", column = "notification_id"),
            @Result(property = "user.id", column = "boss_id"),
            @Result(property = "user.email", column = "b_email"),
            @Result(property = "user.firstName", column = "b_first_name"),
            @Result(property = "user.secondName", column = "b_second_name"),
            @Result(property = "user.password", column = "b_password"),
            @Result(property = "user.userRole", column = "b_user_role"),
            @Result(property = "request.id", column = "request_id"),
            @Result(property = "request.requestStatus", column = "status"),
            @Result(property = "request.startDate", column = "start_date"),
            @Result(property = "request.endDate", column = "end_date"),
            @Result(property = "request.comment", column = "comment"),
            @Result(property = "request.description", column = "description"),
            @Result(property = "request.transportTo", column = "transport_to"),
            @Result(property = "request.transportFrom", column = "transport_from"),
            @Result(property = "request.ticketsUrl", column = "tickets"),
            @Result(property = "request.trip.id", column = "trip_id"),
            @Result(property = "request.trip.tripStatus", column = "trip_status"),
            @Result(property = "request.trip.accommodation.id", column = "accom_id"),
            @Result(property = "request.trip.accommodation.address", column = "address"),
            @Result(property = "request.trip.accommodation.checkinTime", column = "checkin_time"),
            @Result(property = "request.trip.accommodation.checkoutTime", column = "checkout_time"),
            @Result(property = "request.trip.accommodation.bookingUrl", column = "booking_tickets"),
            @Result(property = "request.trip.destination.id", column = "dest_id"),
            @Result(property = "request.trip.destination.description", column = "destination_description"),
            @Result(property = "request.trip.destination.seatPlace", column = "seat_place"),
            @Result(property = "request.trip.destination.office.id", column = "office_id"),
            @Result(property = "request.trip.destination.office.address", column = "office_address"),
            @Result(property = "request.trip.destination.office.description", column = "office_description"),
            @Result(property = "request.worker.id", column = "worker_id"),
            @Result(property = "request.worker.email", column = "email"),
            @Result(property = "request.worker.password", column = "password"),
            @Result(property = "request.worker.firstName", column = "first_name"),
            @Result(property = "request.worker.secondName", column = "second_name"),
            @Result(property = "request.worker.userRole", column = "user_role"),
            @Result(property = "request.office.id", column = "office_id"),
            @Result(property = "request.office.address", column = "office_address"),
            @Result(property = "request.office.description", column = "office_description"),
            @Result(property = "watched", column = "watched")
    })
    @Select("""
            SELECT n.id as notification_id, n.watched,
            b.id as boss_id, b.email as b_email, b.password as b_password, b.first_name as b_first_name, b.second_name as b_second_name, b.user_role as b_user_role,
            r.id as request_id, r.status, r.description, r.comment, r.start_date, r.end_date, r.transport_to, r.transport_from, r.tickets,
            t.id as trip_id, t.trip_status, a.id as accom_id, a.address, a.checkin_time, a.checkout_time, a.booking_tickets,
            d.id as dest_id, d.description as destination_description, d.seat_place,
            u.id as worker_id, u.email, u.password, u.first_name, u.second_name, u.user_role,
            o.id as office_id, o.address as office_address, o.description as office_description
            FROM notification n
            JOIN request r ON n.request_id = r.id
            JOIN users u ON r.worker_id = u.id
            JOIN office o ON r.office_id = o.id
            JOIN trip t ON r.trip_id = t.id
            JOIN accommodation a ON t.accommodation_id = a.id
            JOIN destination d ON t.destination_id = d.id
            JOIN users b ON n.user_id = b.id
            WHERE n.user_id = '${userId}' AND n.watched=false;
            """)
    List<Notification> selectUnwatchedNotifications(@Param("userId") UUID userId);

    @Update("""
            UPDATE notification
            SET request_id=uuid(#{requestId}), watched=#{watched}, user_id=uuid(#{userId})
            WHERE id = uuid(#{id});
            """)
    void updateNotification(NotificationDto notificationDto);

    @Delete("""
            DELETE FROM notification
            WHERE id = '${notificationId}';
            """)
    void deleteNotification(@Param("notificationId") UUID notificationId);
}
