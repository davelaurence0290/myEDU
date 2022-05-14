package com.dsmith.myedu;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM notes WHERE courseId = :id")
    public List<Note> getCourseNotes(long id);

    @Query("SELECT * FROM notes ORDER BY noteId")
    public List<Note> getNotes();

    @Query("SELECT * FROM notes WHERE noteId = :id")
    public Note getNote(long id);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public long insertNote(Note note);

    @Update
    public void updateNote(Note note);

    @Delete
    public void deleteNote(Note note);
}
