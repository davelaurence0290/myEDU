package com.dsmith.myedu;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AssessmentDao {

    @Query("SELECT * FROM assessments WHERE courseId = :id")
    public List<Assessment> getCourseAssessments(long id);

    @Query("SELECT * FROM assessments ORDER BY startDate")
    public List<Assessment> getAssessments();

    @Query("SELECT * FROM assessments WHERE assessmentId = :id")
    public Assessment getAssessment(long id);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public long insertAssessment(Assessment assessment);

    @Update
    public void updateAssessment(Assessment assessment);

    @Delete
    public void deleteAssessment(Assessment assessment);
}
