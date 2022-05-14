package com.dsmith.myedu;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CourseDao {

    @Query("SELECT * FROM courses WHERE termId = :id")
    public List<Course> getTermCourses(long id);

    @Query("SELECT * FROM courses WHERE instructorId = :id")
    public List<Course> getInstructorCourses(long id);

    @Query("SELECT * FROM courses ORDER BY startDate")
    public List<Course> getCourses();

    @Query("SELECT * FROM courses WHERE courseId = :id")
    public Course getCourse(long id);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public long insertCourse(Course course);

    @Update
    public void updateCourse(Course course);

    @Delete
    public void deleteCourse(Course course);
}
