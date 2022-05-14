package com.dsmith.myedu;

import android.util.Log;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "terms")
@TypeConverters(Converter.class)
public class Term {

    private static final String TAG = "Term.java";

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name="termId")
    private int termId;

    @ColumnInfo(name="title")
    private String title;

    @ColumnInfo(name="startDate")
    private LocalDate startDate;

    @ColumnInfo(name="endDate")
    private LocalDate endDate;

    @Ignore
    public Term(String title, LocalDate startDate, LocalDate endDate){

        setTitle(title);
        checkDates(startDate,endDate);
        this.startDate = startDate;
        this.endDate = endDate;

        this.termId = (int)MainActivity.scheduleDatabase.termDao().insertTerm(this);
    }

    //only called to populate list of existing entries in DB.
    public Term(int termId, String title, LocalDate startDate, LocalDate endDate){
        this.termId = termId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    public int getTermId() {
        return termId;
    }

    public void setTermId(int id) {
        this.termId = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if(!title.equals("")) this.title = title.toUpperCase();
        else throw new IllegalArgumentException("Title must not be empty.");
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

    public String getDetailString(){
        return "Start Date: " + this.startDate + "\n" +
                "End Date: " + this.endDate;
    }

    @Override
    public String toString(){
        return this.title;
    }


    public List<Course> getTermCourses(){
        return MainActivity.scheduleDatabase.courseDao().getTermCourses(this.termId);
    }

    private void checkDates(LocalDate startDate, LocalDate endDate){
        //Check if start date is before end date
        if (!startDate.isBefore(endDate))  throw new IllegalArgumentException("Start Date must be before End Date");

        //check if dates overlap existing term
        ArrayList<Term> overlaps = new ArrayList<>(MainActivity.userSchedule.getAllTerms().stream()
                .filter(t -> (
                        (t.getStartDate().isAfter(startDate) && t.getStartDate().isBefore(endDate)) ||
                        (t.getEndDate().isAfter(startDate) && t.getEndDate().isBefore(endDate)) ||
                        (t.getStartDate().isBefore(startDate) && t.getEndDate().isAfter(endDate)))
                ).collect(Collectors.toList())
        );
        if (overlaps.size() > 0){
            throw new IllegalArgumentException("Tern dates overlap with existing term: " + overlaps.get(0).getTitle());
        }

    }

    public void checkCourseDates(LocalDate startDate, LocalDate endDate){
        if (startDate.isBefore(this.startDate) || endDate.isAfter(this.endDate)){
            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/DD/YYYY");
            throw new IllegalArgumentException(
                    "Course dates must be between current term dates:\n" +
                            "Start: " + this.startDate + "\n" +
                            "End: " + this.endDate);
        }
    }
}
