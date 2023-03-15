package ru.tinkoff.lab.tripAPI.mapping;

import org.apache.ibatis.annotations.*;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.Request;
import ru.tinkoff.lab.tripAPI.business.dto.RequestDto;

import java.util.List;
import java.util.UUID;


/**
 * Request operations
 */
@Mapper
public interface RequestMapper {

    @Result(property = "id", column = "id")
    @Select("""
            INSERT INTO request(status, description, worker_id, destination_id, comment,
            	start_date, end_date, tickets)
            	VALUES (#{requestStatus}, #{description}, uuid(#{workerId}),
            	       uuid(#{destinationId}), #{comment}, #{startDate}, #{endDate}, #{ticketsUrl})
            	RETURNING id;
            """)
    Id insertRequest(RequestDto requestDto) throws RuntimeException;

    @Results(value = {
            @Result(property = "id", column = "request_id"),
            @Result(property = "requestStatus", column = "status"),
            @Result(property = "startDate", column = "start_date"),
            @Result(property = "endDate", column = "end_date"),
            @Result(property = "ticketsUrl", column = "tickets"),
            @Result(property = "workerEmail", column = "email"),
            @Result(property = "workerFirstName", column = "first_name"),
            @Result(property = "workerSecondName", column = "second_name"),
            @Result(property = "worker.userRole", column = "user_role"),
            @Result(property = "destination.office.id", column = "office_id"),
            @Result(property = "destination.office.address", column = "office_address"),
            @Result(property = "destination.office.description", column = "office_description")

    })
    @Select("""
            SELECT r.id as request_id, r.status, r.description, r.comment, r.start_date, r.end_date, r.tickets,
            d.id as dest_id, d.description as destination_description, d.seat_place,
            u.email, u.first_name, u.second_name,
            o.id as office_id, o.address as office_address, o.description as office_description
            FROM request r
            JOIN users u on r.worker_id = u.id
            JOIN destination d on r.destination_id = d.id
            JOIN office o on d.office_id = o.id
            WHERE r.id = '${requestId}';
            """)
    Request selectRequest(@Param("requestId") UUID requestId);

    @Update("""
            UPDATE request
            SET status=#{requestStatus}, description=#{description},
            worker_id=uuid(#{workerId}), destination_id=uuid(#{destinationId}), comment=#{comment},
            start_date=#{startDate}, end_date=#{endDate}, tickets=#{ticketsUrl}
            WHERE id = uuid(#{id});
            """)
    void updateRequest(RequestDto requestDto);

    @Delete("""
            DELETE FROM request
            WHERE id = '${requestId}';
            """)
    void deleteRequest(@Param("requestId") UUID requestId);

    @Results(value = {
            @Result(property = "id", column = "request_id"),
            @Result(property = "requestStatus", column = "status"),
            @Result(property = "startDate", column = "start_date"),
            @Result(property = "endDate", column = "end_date"),
            @Result(property = "ticketsUrl", column = "tickets"),
            @Result(property = "workerEmail", column = "email"),
            @Result(property = "workerFirstName", column = "first_name"),
            @Result(property = "workerSecondName", column = "second_name"),
            @Result(property = "worker.userRole", column = "user_role"),
            @Result(property = "destination.office.id", column = "office_id"),
            @Result(property = "destination.office.address", column = "office_address"),
            @Result(property = "destination.office.description", column = "office_description")

    })
    @Select("""
            SELECT r.id as request_id, r.status, r.description, r.comment, r.start_date, r.end_date, r.tickets,
            d.id as dest_id, d.description as destination_description, d.seat_place,
            u.email, u.first_name, u.second_name,
            o.id as office_id, o.address as office_address, o.description as office_description
            FROM request r
            JOIN users u on r.worker_id = u.id
            JOIN destination d on r.destination_id = d.id
            JOIN office o on d.office_id = o.id
            WHERE u.id in (SELECT user_id FROM users_relations WHERE boss_id = '${bossId}')
            AND r.status = 'PENDING'
            ORDER BY r.start_date OFFSET #{offset} ROWS FETCH NEXT #{rows} ROWS ONLY;
            """)
    List<Request> selectIncomingRequests(@Param("bossId") UUID bossId, int offset, int rows);

    //TODO ask if we need requests with all statuses
    @Results(value = {
            @Result(property = "id", column = "request_id"),
            @Result(property = "requestStatus", column = "status"),
            @Result(property = "startDate", column = "start_date"),
            @Result(property = "endDate", column = "end_date"),
            @Result(property = "ticketsUrl", column = "tickets"),
            @Result(property = "workerEmail", column = "email"),
            @Result(property = "workerFirstName", column = "first_name"),
            @Result(property = "workerSecondName", column = "second_name"),
            @Result(property = "worker.userRole", column = "user_role"),
            @Result(property = "destination.office.id", column = "office_id"),
            @Result(property = "destination.office.address", column = "office_address"),
            @Result(property = "destination.office.description", column = "office_description")

    })
    @Select("""
            SELECT r.id as request_id, r.status, r.description, r.comment, r.start_date, r.end_date, r.tickets,
            d.id as dest_id, d.description as destination_description, d.seat_place,
            u.email, u.first_name, u.second_name,
            o.id as office_id, o.address as office_address, o.description as office_description
            FROM request r
            JOIN users u on r.worker_id = u.id
            JOIN destination d on r.destination_id = d.id
            JOIN office o on d.office_id = o.id
            WHERE u.id = '${workerId}'
            ORDER BY r.start_date OFFSET #{offset} ROWS FETCH NEXT #{rows} ROWS ONLY;
            """)
    List<Request> selectOutgoingRequests(@Param("workerId") UUID workerId, int offset, int rows);
}
