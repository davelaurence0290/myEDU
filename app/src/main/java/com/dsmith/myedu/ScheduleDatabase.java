package com.dsmith.myedu;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities={Term.class, Instructor.class, Course.class, Assessment.class, Note.class}, version = 2)
public abstract class ScheduleDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "schedule.db";

    private static ScheduleDatabase mScheduleDatabase;

    //Singleton
    public static ScheduleDatabase getInstance(Context context){
        if (mScheduleDatabase == null){
            mScheduleDatabase = Room.databaseBuilder(context, ScheduleDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .addMigrations(MIGRATION_1_2)
                    .build();
        }
        return mScheduleDatabase;
    }

    public abstract TermDao termDao();
    public abstract InstructorDao instructorDao();
    public abstract CourseDao courseDao();
    public abstract AssessmentDao assessmentDao();
    public abstract NoteDao noteDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE courses "
                    + " ADD COLUMN alarmed INTEGER NOT NULL DEFAULT(0)");
            database.execSQL("ALTER TABLE assessments "
                    + " ADD COLUMN alarmed INTEGER NOT NULL DEFAULT(0)");
        }
    };

}
