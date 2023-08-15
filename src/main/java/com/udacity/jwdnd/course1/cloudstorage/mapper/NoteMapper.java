package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Select("SELECT * FROM Notes")
    List<Note> getNodes();

    @Insert("INSERT INTO Notes(noteTitle,noteDescription,userId) VALUES (#{noteTitle},#{noteDescription},#{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insert(Note note);

    @Update("UPDATE Notes SET noteTitle=#{noteTitle},noteDescription=#{noteDescription},userId=#{userId}" +
            " WHERE noteId=#{noteId}")
    int update(Note note);

    @Delete("DELETE Notes WHERE noteId=#{noteId}")
    int delete(Integer noteId);
}
