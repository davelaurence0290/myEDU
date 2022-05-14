package com.dsmith.myedu;

import java.time.LocalDate;

import androidx.room.TypeConverter;

public class Converter {

    @TypeConverter
    public static LocalDate toDate(Long dateLong) {
        if (dateLong == null) {
            return null;
        } else {
            return LocalDate.ofEpochDay(dateLong);
        }
    }

    @TypeConverter
    public static Long toDateString(LocalDate date) {
        if (date == null) {
            return null;
        } else {
            return date.toEpochDay();
        }
    }

    @TypeConverter
    public static Course.Status toCourseStatus(String status) {
        if (status == null){
            return null;
        }
        else{
            switch (status){
                case "IN_PROGRESS":
                    return Course.Status.IN_PROGRESS;
                case "COMPLETE":
                    return Course.Status.COMPLETE;
                case "FAILED":
                    return Course.Status.FAILED;
                case "DROPPED":
                    return Course.Status.DROPPED;
                case "PLANNED":
                    return Course.Status.PLANNED;
            }
        }
        return null;
    }

    @TypeConverter
    public static String courseStatusfromString(Course.Status status){
        if (status == null){
            return null;
        }
        else{
            return status.toString();
        }
    }

    @TypeConverter
    public static Assessment.AssessmentType toAssessmentType(String type) {
        if (type == null){
            return null;
        }
        else{
            switch (type){
                case "PERFORMANCE":
                    return Assessment.AssessmentType.PERFORMANCE;
                case "OBJECTIVE":
                    return Assessment.AssessmentType.OBJECTIVE;
            }
        }
        return null;
    }

    @TypeConverter
    public static String AssessmentTypefromString(Assessment.AssessmentType type){
        if (type == null){
            return null;
        }
        else{
            return type.toString();
        }
    }
}