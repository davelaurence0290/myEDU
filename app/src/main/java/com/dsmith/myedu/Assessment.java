package com.dsmith.myedu;

import java.time.LocalDate;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "assessments",
        foreignKeys =
                @ForeignKey(entity = Course.class,
                        parentColumns = "courseId",
                        childColumns = "courseId"
                ))
@TypeConverters(Converter.class)
public class Assessment {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name="assessmentId")
    private int assessmentId;

    @ColumnInfo(name="name")
    private String name;

    @ColumnInfo(name="type")
    private AssessmentType type;

    @ColumnInfo(name="startDate")
    private LocalDate startDate;

    @ColumnInfo(name="endDate")
    private LocalDate endDate;

    @ColumnInfo(name="courseId")
    private int courseId;

    @ColumnInfo(name="alarmed")
    private boolean alarmed;

    @Ignore
    public Assessment(String name, AssessmentType type, LocalDate startDate, LocalDate endDate, int courseId){

        setName(name);
        checkDates(startDate,endDate);
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.courseId = courseId;
        this.alarmed = false;

        this.assessmentId = (int)MainActivity.scheduleDatabase.assessmentDao().insertAssessment(this);
    }

    //only called to populate list of existing entries in DB.
    public Assessment(int assessmentId, String name, AssessmentType type, LocalDate startDate, LocalDate endDate, int courseId, boolean alarmed){
        this.assessmentId = assessmentId;
        this.name = name;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.courseId = courseId;
        this.alarmed = alarmed;
    }

    public int getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(int id) {
        this.assessmentId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(!name.equals("")) this.name = name.toUpperCase();
        else throw new IllegalArgumentException("Name must not be empty.");
    }

    public AssessmentType getType() {
        return type;
    }

    public void setType(AssessmentType type) {
        this.type = type;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        checkDates(startDate,this.endDate);
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        checkDates(this.startDate,endDate);
        this.endDate = endDate;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public boolean isAlarmed() {
        return this.alarmed;
    }

    public void setAlarmed(boolean alarmed) {
        this.alarmed = alarmed;
        MainActivity.scheduleDatabase.assessmentDao().updateAssessment(this);
    }
    private void checkDates(LocalDate startDate, LocalDate endDate){
        if (!startDate.isBefore(endDate))  throw new IllegalArgumentException("Start Date must be before End Date");
    }

    @Override
    public String toString(){
        return MainActivity.userSchedule.getCourse(this.getCourseId()).getTitle() + "\n" +
                this.name + ": " + this.getType();
    }

    public String getDetailString(){
        return this.type + "\n\n" +
                "Start Date: " + this.startDate + "\n" +
                "End Date: " + this.endDate;
    }

    public static AssessmentType getTypeFromString(String type){
        switch(type){
            case "PERFORMANCE":
                return AssessmentType.PERFORMANCE;
            case "OBJECTIVE":
                return AssessmentType.OBJECTIVE;
        }
        return null;
    }

    public enum AssessmentType {PERFORMANCE, OBJECTIVE}
}
