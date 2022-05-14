package com.dsmith.myedu;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "instructors")
public class Instructor {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name="instructorId")
    private int instructorId;

    @ColumnInfo(name="name")
    private String name;

    @ColumnInfo(name="email")
    private String email;

    @ColumnInfo(name="phone")
    private String phone;

    @Ignore
    public Instructor(String name, String email, String phone){

        setName(name);
        setEmail(email);
        setPhone(phone);

        this.instructorId = (int)MainActivity.scheduleDatabase.instructorDao().insertInstructor(this);
    }

    //only called to populate list of existing entries in DB.
    public Instructor(int instructorId, String name, String email, String phone){
        this.instructorId = instructorId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int id) {
        this.instructorId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(!name.equals("")) this.name = name.toUpperCase();
        else throw new IllegalArgumentException("Name must not be empty.");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if(!email.equals("")) this.email = email;
        else throw new IllegalArgumentException("Email must not be empty.");
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if(!phone.equals("") && ! phone.matches("[A-Za-z]") ) this.phone = phone;
        else throw new IllegalArgumentException("Phone number must not contain alphabet characters.");
    }

    @Override
    public String toString(){
        return this.name;
    }

    public String getDetailString(){
        return this.email + "\n\n" +
                this.phone;
    }

    public List<Course> getInstructorCourses(){
        return MainActivity.scheduleDatabase.courseDao().getInstructorCourses(this.instructorId);
    }
}
