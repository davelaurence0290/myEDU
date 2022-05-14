package com.dsmith.myedu;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface InstructorDao {

    @Query("SELECT * FROM instructors ORDER BY name")
    public List<Instructor> getInstructors();

    @Query("SELECT * FROM instructors WHERE instructorId = :id")
    public Instructor getInstructor(long id);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public long insertInstructor(Instructor instructor);

    @Update
    public void updateInstructor(Instructor instructor);

    @Delete
    public void deleteInstructor(Instructor instructor);
}
