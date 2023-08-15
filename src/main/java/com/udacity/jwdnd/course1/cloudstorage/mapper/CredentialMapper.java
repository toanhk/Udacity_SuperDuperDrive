package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {
    @Select("SELECT * FROM Credentials")
    List<Credential> getCredentials();

//    @Select("SELECT * FROM Credentials WHERE userId=#{userId}")
//    List<Credential> getCredentials(Integer userId);
    @Insert("INSERT INTO Credentials(url,username,key,password,userId) VALUES " +
            "(#{url},#{username},#{key},#{password},#{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int insert(Credential credential);

    @Update("UPDATE Credentials SET url=#{url},username=#{username},key=#{key}," +
            "password=#{password}, userId=#{userId}" +
            " WHERE credentialId=#{credentialId}")
    int update(Credential credential);

    @Delete("DELETE Credentials WHERE credentialId=#{credentialId}")
    int delete(Integer credentialId);
}
