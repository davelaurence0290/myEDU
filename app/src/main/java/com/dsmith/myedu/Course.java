package com.dsmith.myedu;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "courses",
        foreignKeys ={
                @ForeignKey(entity = Term.class,
                parentColumns = "termId",
                childColumns = "termId"
                ),
                @ForeignKey(entity = Instructor.class,
                        parentColumns = "instructorId",
                        childColumns = "instructorId"
                )
        })
@TypeConverters(Converter.class)
public class Course {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name="courseId")
    private int courseId;

    @ColumnInfo(name="title")
    private String title;

    @ColumnInfo(name="description")
    private String description;

    @ColumnInfo(name="startDate")
    private LocalDate startDate;

    @ColumnInfo(name="endDate")
    private LocalDate endDate;

    @ColumnInfo(name="status")
    private Status status;

    @ColumnInfo(name="instructorId")
    private int instructorId;

    @ColumnInfo(name="termId")
    private int termId;

    @ColumnInfo(name="alarmed")
    private boolean alarmed;

    @Ignore
    public Course(String title, String description, LocalDate startDate,
                  LocalDate endDate, Status status, int instructorId, int termId){

        setTitle(title);
        checkDates(startDate,endDate);
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.instructorId = instructorId;
        this.termId = termId;
        this.alarmed = false;

        this.courseId = (int)MainActivity.scheduleDatabase.courseDao().insertCourse(this);
    }

    //only called to populate list of existing entries in DB.
    public Course(int courseId, String title, String description, LocalDate startDate,
                  LocalDate endDate, Status status, int instructorId, int termId, boolean alarmed){
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.instructorId = instructorId;
        this.termId = termId;
        this.alarmed = alarmed;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int id) {
        this.courseId = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if(!title.equals("")) this.title = title.toUpperCase();
        else throw new IllegalArgumentException("Title must not be empty.");
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        checkDates(startDate, this.endDate);
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        checkDates(this.startDate, endDate);
        this.endDate = endDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public boolean isAlarmed() {
        return this.alarmed;
    }

    public void setAlarmed(boolean alarmed) {
        this.alarmed = alarmed;
        MainActivity.scheduleDatabase.courseDao().updateCourse(this);
    }

    private void checkDates(LocalDate startDate, LocalDate endDate){
        if (!startDate.isBefore(endDate))  throw new IllegalArgumentException("Start Date must be before End Date");
    }

    public void checkAssessmentDates(LocalDate startDate, LocalDate endDate){
        if (startDate.isBefore(this.startDate) || endDate.isAfter(this.endDate)){
            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/DD/YYYY");
            throw new IllegalArgumentException(
                    "Assessment dates must be between current course dates:\n" +
                            "Start: " + this.startDate + "\n" +
                            "End: " + this.endDate);
        }
    }

    public String getDetailString(){
        Instructor instr = MainActivity.userSchedule.getInstructor(instructorId);

        return this.description + "\n\n" +
                "Start Date: " + this.startDate + "\n" +
                "End Date: " + this.endDate +  "\n\n" +
                "Status: " + this.status.toString() + "\n\n" +
                "Instructor: " + instr.getName() + "\n" +
                "Email: " + instr.getEmail() + "\n" +
                "Phone: " + instr.getPhone();
    }

    @Override
    public String toString(){
        return this.title + ", " + this.status.toString();
    }

    public enum Status {IN_PROGRESS, COMPLETE, FAILED, DROPPED, PLANNED}

    public static Status getStatusFromString(String status){
        switch(status){
            case "IN_PROGRESS":
                return Status.IN_PROGRESS;
            case "COMPLETE":
                return Status.COMPLETE;
            case "FAILED":
                return Status.FAILED;
            case "DROPPED":
                return Status.DROPPED;
            case "PLANNED":
                return Status.PLANNED;
        }
        return null;
    }
}
