package ru.tinkoff.lab.tripAPI.mapping;

import org.apache.ibatis.annotations.*;
import ru.tinkoff.lab.tripAPI.business.*;

@Mapper
public interface AccommodationDestinationTripMapper {

    //Accommodation CRUD
    @Result(property = "id", column = "id")
    @Select("INSERT INTO accommodation " +
            "(address, checkin_time, checkout_time, booking_tickets) " +
            "VALUES (#{address}, #{checkin_time}, #{checkout_time}, #{booking_tickets}) RETURNING id;")
    Id insertAccommodation(Accommodation accommodation);

    @Select("SELECT id, address, checkin_time, checkout_time, booking_tickets " +
            "FROM accommodation WHERE id = uuid(#{accommodationId});")
    Accommodation selectAccommodation(String accommodationId);

    @Update("UPDATE public.accommodation " +
            "SET address=#{address}, checkin_time=#{checkin_time}, checkout_time=#{checkout_time}, " +
            "booking_tickets=#{booking_tickets} " +
            "WHERE id = uuid(#{id});")
    void updateAccommodation(Accommodation accommodation);

    @Delete("DELETE FROM accommodation " +
            "WHERE id = uuid(#{accommodationId});")
    void deleteAccommodation(String accommodationId);


    // Destination CRUD
    //TODO NEED TO ASK ANDREY IF ADDITIONAL OFFICEID PROPERTY IS OK
    @Select("INSERT INTO destination " +
            "(description, office_id, seat_place) " +
            "VALUES (#{description}, #{officeId}, #{seat_place}) RETURNING id;")
    Id insertDestination(Destination destination, String officeId);

    @Result(property = "office", javaType = Office.class,
            column = "office_id", one = @One(select = "selectOffice"))
    @Select("SELECT id, description, office_id, seat_place " +
            "FROM destination WHERE id = uuid(#{destinationId});")
    Destination selectDestination(String destinationId);

    @Update("UPDATE destination " +
            "SET description=#{description}, office_id=#{officeId}, seat_place=#{seat_place} " +
            "WHERE id = uuid(#{id});")
    void updateDestination(Destination destination, String officeId);

    //TODO NEED TO KNOW IF WE ARE MAKING DELETE CASCADE
    @Delete("DELETE FROM destination " +
            "WHERE id = uuid(#{destinationId});")
    void deleteDestination(String destinationId);

    //Trip CRUD

    @Select("INSERT INTO trip " +
            "(trip_status, accommodation_id, destination_id) " +
            "VALUES (#{trip_status}, uuid(#{accommodationId}), uuid(#{destinationId}));")
    Id insertTrip(Trip trip, String accommodationId, String destinationId);

    @Results(value = {
            @Result(property = "accommodation", javaType = Accommodation.class,
            column = "accommodation_id", one = @One(select = "selectAccommodation")),
            @Result(property = "destination", javaType = Destination.class,
            column = "destination_id", one = @One(select = "selectDestination"))
    })

    @Select("SELECT id, trip_status, accommodation_id, destination_id " +
            "FROM trip WHERE id = uuid(#{tripId});")
    Trip selectTrip(String tripId);


    @Update("UPDATE trip " +
            "SET trip_status=#{trip_status}, accommodation_id=uuid(#{accommodationId}), " +
            "destination_id=uuid(#{destinationId}) " +
            "WHERE id = uuid(#{id});")
    void updateTrip(Trip trip, String accommodationId, String destinationId);

    @Delete("DELETE FROM trip " +
            "WHERE id = uuid(#{tripId});")
    void deleteTrip(String tripId);

    //TODO ASK ABOUT THIS(MYBATIS CANNOT USE METHODS FROM OTHER MAPPERS)
    @Select("SELECT id, address, description " +
            "FROM office WHERE id = uuid(#{officeId});")
    Office selectOffice(String officeId);
}
