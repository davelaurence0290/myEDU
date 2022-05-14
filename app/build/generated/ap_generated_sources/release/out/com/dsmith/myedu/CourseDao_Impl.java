package com.dsmith.myedu;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class CourseDao_Impl implements CourseDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfCourse;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfCourse;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfCourse;

  public CourseDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCourse = new EntityInsertionAdapter<Course>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `courses`(`courseId`,`title`,`description`,`startDate`,`endDate`,`status`,`instructorId`,`termId`,`alarmed`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Course value) {
        stmt.bindLong(1, value.getCourseId());
        if (value.getTitle() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTitle());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getDescription());
        }
        final Long _tmp;
        _tmp = Converter.toDateString(value.getStartDate());
        if (_tmp == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, _tmp);
        }
        final Long _tmp_1;
        _tmp_1 = Converter.toDateString(value.getEndDate());
        if (_tmp_1 == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindLong(5, _tmp_1);
        }
        final String _tmp_2;
        _tmp_2 = Converter.courseStatusfromString(value.getStatus());
        if (_tmp_2 == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, _tmp_2);
        }
        stmt.bindLong(7, value.getInstructorId());
        stmt.bindLong(8, value.getTermId());
        final int _tmp_3;
        _tmp_3 = value.isAlarmed() ? 1 : 0;
        stmt.bindLong(9, _tmp_3);
      }
    };
    this.__deletionAdapterOfCourse = new EntityDeletionOrUpdateAdapter<Course>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `courses` WHERE `courseId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Course value) {
        stmt.bindLong(1, value.getCourseId());
      }
    };
    this.__updateAdapterOfCourse = new EntityDeletionOrUpdateAdapter<Course>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `courses` SET `courseId` = ?,`title` = ?,`description` = ?,`startDate` = ?,`endDate` = ?,`status` = ?,`instructorId` = ?,`termId` = ?,`alarmed` = ? WHERE `courseId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Course value) {
        stmt.bindLong(1, value.getCourseId());
        if (value.getTitle() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTitle());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getDescription());
        }
        final Long _tmp;
        _tmp = Converter.toDateString(value.getStartDate());
        if (_tmp == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, _tmp);
        }
        final Long _tmp_1;
        _tmp_1 = Converter.toDateString(value.getEndDate());
        if (_tmp_1 == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindLong(5, _tmp_1);
        }
        final String _tmp_2;
        _tmp_2 = Converter.courseStatusfromString(value.getStatus());
        if (_tmp_2 == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, _tmp_2);
        }
        stmt.bindLong(7, value.getInstructorId());
        stmt.bindLong(8, value.getTermId());
        final int _tmp_3;
        _tmp_3 = value.isAlarmed() ? 1 : 0;
        stmt.bindLong(9, _tmp_3);
        stmt.bindLong(10, value.getCourseId());
      }
    };
  }

  @Override
  public long insertCourse(final Course course) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      long _result = __insertionAdapterOfCourse.insertAndReturnId(course);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteCourse(final Course course) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfCourse.handle(course);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateCourse(final Course course) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfCourse.handle(course);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Course> getTermCourses(final long id) {
    final String _sql = "SELECT * FROM courses WHERE termId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfCourseId = CursorUtil.getColumnIndexOrThrow(_cursor, "courseId");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
      final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfInstructorId = CursorUtil.getColumnIndexOrThrow(_cursor, "instructorId");
      final int _cursorIndexOfTermId = CursorUtil.getColumnIndexOrThrow(_cursor, "termId");
      final int _cursorIndexOfAlarmed = CursorUtil.getColumnIndexOrThrow(_cursor, "alarmed");
      final List<Course> _result = new ArrayList<Course>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Course _item;
        final int _tmpCourseId;
        _tmpCourseId = _cursor.getInt(_cursorIndexOfCourseId);
        final String _tmpTitle;
        _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        final LocalDate _tmpStartDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfStartDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfStartDate);
        }
        _tmpStartDate = Converter.toDate(_tmp);
        final LocalDate _tmpEndDate;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfEndDate)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfEndDate);
        }
        _tmpEndDate = Converter.toDate(_tmp_1);
        final Course.Status _tmpStatus;
        final String _tmp_2;
        _tmp_2 = _cursor.getString(_cursorIndexOfStatus);
        _tmpStatus = Converter.toCourseStatus(_tmp_2);
        final int _tmpInstructorId;
        _tmpInstructorId = _cursor.getInt(_cursorIndexOfInstructorId);
        final int _tmpTermId;
        _tmpTermId = _cursor.getInt(_cursorIndexOfTermId);
        final boolean _tmpAlarmed;
        final int _tmp_3;
        _tmp_3 = _cursor.getInt(_cursorIndexOfAlarmed);
        _tmpAlarmed = _tmp_3 != 0;
        _item = new Course(_tmpCourseId,_tmpTitle,_tmpDescription,_tmpStartDate,_tmpEndDate,_tmpStatus,_tmpInstructorId,_tmpTermId,_tmpAlarmed);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Course> getInstructorCourses(final long id) {
    final String _sql = "SELECT * FROM courses WHERE instructorId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfCourseId = CursorUtil.getColumnIndexOrThrow(_cursor, "courseId");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
      final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfInstructorId = CursorUtil.getColumnIndexOrThrow(_cursor, "instructorId");
      final int _cursorIndexOfTermId = CursorUtil.getColumnIndexOrThrow(_cursor, "termId");
      final int _cursorIndexOfAlarmed = CursorUtil.getColumnIndexOrThrow(_cursor, "alarmed");
      final List<Course> _result = new ArrayList<Course>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Course _item;
        final int _tmpCourseId;
        _tmpCourseId = _cursor.getInt(_cursorIndexOfCourseId);
        final String _tmpTitle;
        _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        final LocalDate _tmpStartDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfStartDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfStartDate);
        }
        _tmpStartDate = Converter.toDate(_tmp);
        final LocalDate _tmpEndDate;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfEndDate)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfEndDate);
        }
        _tmpEndDate = Converter.toDate(_tmp_1);
        final Course.Status _tmpStatus;
        final String _tmp_2;
        _tmp_2 = _cursor.getString(_cursorIndexOfStatus);
        _tmpStatus = Converter.toCourseStatus(_tmp_2);
        final int _tmpInstructorId;
        _tmpInstructorId = _cursor.getInt(_cursorIndexOfInstructorId);
        final int _tmpTermId;
        _tmpTermId = _cursor.getInt(_cursorIndexOfTermId);
        final boolean _tmpAlarmed;
        final int _tmp_3;
        _tmp_3 = _cursor.getInt(_cursorIndexOfAlarmed);
        _tmpAlarmed = _tmp_3 != 0;
        _item = new Course(_tmpCourseId,_tmpTitle,_tmpDescription,_tmpStartDate,_tmpEndDate,_tmpStatus,_tmpInstructorId,_tmpTermId,_tmpAlarmed);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Course> getCourses() {
    final String _sql = "SELECT * FROM courses ORDER BY startDate";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfCourseId = CursorUtil.getColumnIndexOrThrow(_cursor, "courseId");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
      final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfInstructorId = CursorUtil.getColumnIndexOrThrow(_cursor, "instructorId");
      final int _cursorIndexOfTermId = CursorUtil.getColumnIndexOrThrow(_cursor, "termId");
      final int _cursorIndexOfAlarmed = CursorUtil.getColumnIndexOrThrow(_cursor, "alarmed");
      final List<Course> _result = new ArrayList<Course>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Course _item;
        final int _tmpCourseId;
        _tmpCourseId = _cursor.getInt(_cursorIndexOfCourseId);
        final String _tmpTitle;
        _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        final LocalDate _tmpStartDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfStartDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfStartDate);
        }
        _tmpStartDate = Converter.toDate(_tmp);
        final LocalDate _tmpEndDate;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfEndDate)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfEndDate);
        }
        _tmpEndDate = Converter.toDate(_tmp_1);
        final Course.Status _tmpStatus;
        final String _tmp_2;
        _tmp_2 = _cursor.getString(_cursorIndexOfStatus);
        _tmpStatus = Converter.toCourseStatus(_tmp_2);
        final int _tmpInstructorId;
        _tmpInstructorId = _cursor.getInt(_cursorIndexOfInstructorId);
        final int _tmpTermId;
        _tmpTermId = _cursor.getInt(_cursorIndexOfTermId);
        final boolean _tmpAlarmed;
        final int _tmp_3;
        _tmp_3 = _cursor.getInt(_cursorIndexOfAlarmed);
        _tmpAlarmed = _tmp_3 != 0;
        _item = new Course(_tmpCourseId,_tmpTitle,_tmpDescription,_tmpStartDate,_tmpEndDate,_tmpStatus,_tmpInstructorId,_tmpTermId,_tmpAlarmed);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Course getCourse(final long id) {
    final String _sql = "SELECT * FROM courses WHERE courseId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfCourseId = CursorUtil.getColumnIndexOrThrow(_cursor, "courseId");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
      final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfInstructorId = CursorUtil.getColumnIndexOrThrow(_cursor, "instructorId");
      final int _cursorIndexOfTermId = CursorUtil.getColumnIndexOrThrow(_cursor, "termId");
      final int _cursorIndexOfAlarmed = CursorUtil.getColumnIndexOrThrow(_cursor, "alarmed");
      final Course _result;
      if(_cursor.moveToFirst()) {
        final int _tmpCourseId;
        _tmpCourseId = _cursor.getInt(_cursorIndexOfCourseId);
        final String _tmpTitle;
        _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        final LocalDate _tmpStartDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfStartDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfStartDate);
        }
        _tmpStartDate = Converter.toDate(_tmp);
        final LocalDate _tmpEndDate;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfEndDate)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfEndDate);
        }
        _tmpEndDate = Converter.toDate(_tmp_1);
        final Course.Status _tmpStatus;
        final String _tmp_2;
        _tmp_2 = _cursor.getString(_cursorIndexOfStatus);
        _tmpStatus = Converter.toCourseStatus(_tmp_2);
        final int _tmpInstructorId;
        _tmpInstructorId = _cursor.getInt(_cursorIndexOfInstructorId);
        final int _tmpTermId;
        _tmpTermId = _cursor.getInt(_cursorIndexOfTermId);
        final boolean _tmpAlarmed;
        final int _tmp_3;
        _tmp_3 = _cursor.getInt(_cursorIndexOfAlarmed);
        _tmpAlarmed = _tmp_3 != 0;
        _result = new Course(_tmpCourseId,_tmpTitle,_tmpDescription,_tmpStartDate,_tmpEndDate,_tmpStatus,_tmpInstructorId,_tmpTermId,_tmpAlarmed);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
