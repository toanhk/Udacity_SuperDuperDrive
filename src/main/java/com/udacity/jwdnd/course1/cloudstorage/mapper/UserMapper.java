package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("SELECT userid,username,salt,password,firstname,lastname FROM USERS WHERE username=#{username}")
    User getUser(String username);

    @Insert("INSERT INTO USERS(username,salt,password,firstname,lastname) VALUES (#{username},#{salt},#{password},#{firstname},#{lastname})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int insert(User user);

    @Update("UPDATE USERS SET username=#{username},salt=#{salt},password=#{password},firstname=#{firstname},lastname=#{firstname} WHERE userid=#{userid}")
    int update(User user);

    @Delete("DELETE USERS WHERE username=#{username}")
    int delete(String username);
}
