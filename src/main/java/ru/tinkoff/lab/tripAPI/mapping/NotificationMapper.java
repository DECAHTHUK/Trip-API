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
            @Result(property = "userId", column = "boss_id"),
            @Result(property = "request.id", column = "request_id"),
            @Result(property = "request.requestStatus", column = "status"),
            @Result(property = "request.startDate", column = "start_date"),
            @Result(property = "request.endDate", column = "end_date"),
            @Result(property = "request.comment", column = "comment"),
            @Result(property = "request.description", column = "description"),
            @Result(property = "request.ticketsUrl", column = "tickets"),
            @Result(property = "request.workerEmail", column = "email"),
            @Result(property = "request.workerFirstName", column = "first_name"),
            @Result(property = "request.workerSecondName", column = "second_name"),
            @Result(property = "request.destination.office.id", column = "office_id"),
            @Result(property = "request.destination.office.address", column = "office_address"),
            @Result(property = "request.destination.office.description", column = "office_description"),
            @Result(property = "watched", column = "watched")
    })
    @Select("""
            SELECT n.id as notification_id, n.watched, n.user_id as boss_id,
            r.id as request_id, r.status, r.description, r.comment, r.start_date, r.end_date, r.tickets,
            d.id as dest_id, d.description as destination_description, d.seat_place,
            u.email, u.first_name, u.second_name,
            o.id as office_id, o.address as office_address, o.description as office_description
            FROM notification n
            JOIN request r on n.request_id = r.id
            JOIN users u on r.worker_id = u.id
            JOIN destination d on r.destination_id = d.id
            JOIN office o on d.office_id = o.id
            WHERE n.id = '${notificationId}';
            """)
    Notification selectNotification(@Param("notificationId") UUID notificationId);

    @Results(value = {
            @Result(property = "id", column = "notification_id"),
            @Result(property = "userId", column = "boss_id"),
            @Result(property = "request.id", column = "request_id"),
            @Result(property = "request.requestStatus", column = "status"),
            @Result(property = "request.startDate", column = "start_date"),
            @Result(property = "request.endDate", column = "end_date"),
            @Result(property = "request.comment", column = "comment"),
            @Result(property = "request.description", column = "description"),
            @Result(property = "request.ticketsUrl", column = "tickets"),
            @Result(property = "request.workerEmail", column = "email"),
            @Result(property = "request.workerFirstName", column = "first_name"),
            @Result(property = "request.workerSecondName", column = "second_name"),
            @Result(property = "request.destination.office.id", column = "office_id"),
            @Result(property = "request.destination.office.address", column = "office_address"),
            @Result(property = "request.destination.office.description", column = "office_description"),
            @Result(property = "watched", column = "watched")
    })
    @Select("""
            SELECT n.id as notification_id, n.watched, n.user_id as boss_id,
            r.id as request_id, r.status, r.description, r.comment, r.start_date, r.end_date, r.tickets,
            d.id as dest_id, d.description as destination_description, d.seat_place,
            u.email, u.first_name, u.second_name,
            o.id as office_id, o.address as office_address, o.description as office_description
            FROM notification n
            JOIN request r on n.request_id = r.id
            JOIN users u on r.worker_id = u.id
            JOIN destination d on r.destination_id = d.id
            JOIN office o on d.office_id = o.id
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
