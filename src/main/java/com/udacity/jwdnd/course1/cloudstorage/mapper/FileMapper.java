package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM Files")
    List<File> getFiles();

    @Select("SELECT * FROM Files WHERE filename=#{filename}")
    File getFileByName(String filename);

    @Select("SELECT * FROM Files WHERE fileId=#{fileId}")
    File getFileById(Integer fileId);

    @Insert("INSERT INTO Files(filename,contentType,fileSize,userId,fileData) VALUES " +
            "(#{filename},#{contentType},#{fileSize},#{userId},#{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(File file);

    @Update("UPDATE Files SET filename=#{filename},contentType=#{contentType},fileSize=#{fileSize},userId=#{userId}, fileData=#{fileData}" +
            " WHERE fileId=#{fileId}")
    int update(File file);

    @Delete("DELETE Files WHERE fileId=#{fileId}")
    int delete(Integer fileId);
}
