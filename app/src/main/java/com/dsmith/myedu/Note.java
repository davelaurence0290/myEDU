package com.dsmith.myedu;

import java.time.LocalDate;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes",
        foreignKeys =
        @ForeignKey(entity = Course.class,
                parentColumns = "courseId",
                childColumns = "courseId"
        ))
public class Note {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name="noteId")
    private int noteId;

    @ColumnInfo(name="text")
    private String text;

    @ColumnInfo(name="courseId")
    private int courseId;

    @Ignore
    public Note(String text, int courseId){

        setText(text);
        this.courseId = courseId;

        this.noteId = (int)MainActivity.scheduleDatabase.noteDao().insertNote(this);
    }

    //only called to populate list of existing entries in DB.
    public Note(int noteId, String text, int courseId){
        this.noteId = noteId;
        this.text = text;
        this.courseId = courseId;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int id) {
        this.noteId = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if(!text.equals("")) this.text = text;
        else throw new IllegalArgumentException("Text field cannot be empty.");
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString(){
        return this.text;
    }
}
