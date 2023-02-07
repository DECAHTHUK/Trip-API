package ru.tinkoff.lab.tripAPI.mapping;

import org.apache.ibatis.annotations.*;
import ru.tinkoff.lab.tripAPI.business.Id;
import ru.tinkoff.lab.tripAPI.business.Office;

@Mapper
public interface OfficeMapper {

    @Result(property = "id", column = "id")
    @Select("INSERT INTO office " +
            "(address, description) " +
            "VALUES(#{address}, #{description}) RETURNING id;")
    Id insertOffice(Office office);

    @Select("SELECT id, address, description " +
            "FROM office WHERE id = uuid(#{officeId});")
    Office selectOffice(String officeId);

    @Update("UPDATE office " +
            "SET address=#{address}, description=#{description} " +
            "WHERE id = uuid(#{id});")
    void updateOffice(Office office);

    @Delete("DELETE FROM office " +
            "WHERE id = uuid(#{officeId});")
    void deleteOffice(String officeId);
}