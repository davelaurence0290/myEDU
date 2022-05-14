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
public final class AssessmentDao_Impl implements AssessmentDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfAssessment;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfAssessment;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfAssessment;

  public AssessmentDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAssessment = new EntityInsertionAdapter<Assessment>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `assessments`(`assessmentId`,`name`,`type`,`startDate`,`endDate`,`courseId`,`alarmed`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Assessment value) {
        stmt.bindLong(1, value.getAssessmentId());
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        final String _tmp;
        _tmp = Converter.AssessmentTypefromString(value.getType());
        if (_tmp == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, _tmp);
        }
        final Long _tmp_1;
        _tmp_1 = Converter.toDateString(value.getStartDate());
        if (_tmp_1 == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, _tmp_1);
        }
        final Long _tmp_2;
        _tmp_2 = Converter.toDateString(value.getEndDate());
        if (_tmp_2 == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindLong(5, _tmp_2);
        }
        stmt.bindLong(6, value.getCourseId());
        final int _tmp_3;
        _tmp_3 = value.isAlarmed() ? 1 : 0;
        stmt.bindLong(7, _tmp_3);
      }
    };
    this.__deletionAdapterOfAssessment = new EntityDeletionOrUpdateAdapter<Assessment>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `assessments` WHERE `assessmentId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Assessment value) {
        stmt.bindLong(1, value.getAssessmentId());
      }
    };
    this.__updateAdapterOfAssessment = new EntityDeletionOrUpdateAdapter<Assessment>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `assessments` SET `assessmentId` = ?,`name` = ?,`type` = ?,`startDate` = ?,`endDate` = ?,`courseId` = ?,`alarmed` = ? WHERE `assessmentId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Assessment value) {
        stmt.bindLong(1, value.getAssessmentId());
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        final String _tmp;
        _tmp = Converter.AssessmentTypefromString(value.getType());
        if (_tmp == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, _tmp);
        }
        final Long _tmp_1;
        _tmp_1 = Converter.toDateString(value.getStartDate());
        if (_tmp_1 == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, _tmp_1);
        }
        final Long _tmp_2;
        _tmp_2 = Converter.toDateString(value.getEndDate());
        if (_tmp_2 == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindLong(5, _tmp_2);
        }
        stmt.bindLong(6, value.getCourseId());
        final int _tmp_3;
        _tmp_3 = value.isAlarmed() ? 1 : 0;
        stmt.bindLong(7, _tmp_3);
        stmt.bindLong(8, value.getAssessmentId());
      }
    };
  }

  @Override
  public long insertAssessment(final Assessment assessment) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      long _result = __insertionAdapterOfAssessment.insertAndReturnId(assessment);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAssessment(final Assessment assessment) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfAssessment.handle(assessment);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateAssessment(final Assessment assessment) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfAssessment.handle(assessment);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Assessment> getCourseAssessments(final long id) {
    final String _sql = "SELECT * FROM assessments WHERE courseId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfAssessmentId = CursorUtil.getColumnIndexOrThrow(_cursor, "assessmentId");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
      final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
      final int _cursorIndexOfCourseId = CursorUtil.getColumnIndexOrThrow(_cursor, "courseId");
      final int _cursorIndexOfAlarmed = CursorUtil.getColumnIndexOrThrow(_cursor, "alarmed");
      final List<Assessment> _result = new ArrayList<Assessment>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Assessment _item;
        final int _tmpAssessmentId;
        _tmpAssessmentId = _cursor.getInt(_cursorIndexOfAssessmentId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final Assessment.AssessmentType _tmpType;
        final String _tmp;
        _tmp = _cursor.getString(_cursorIndexOfType);
        _tmpType = Converter.toAssessmentType(_tmp);
        final LocalDate _tmpStartDate;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfStartDate)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfStartDate);
        }
        _tmpStartDate = Converter.toDate(_tmp_1);
        final LocalDate _tmpEndDate;
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfEndDate)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfEndDate);
        }
        _tmpEndDate = Converter.toDate(_tmp_2);
        final int _tmpCourseId;
        _tmpCourseId = _cursor.getInt(_cursorIndexOfCourseId);
        final boolean _tmpAlarmed;
        final int _tmp_3;
        _tmp_3 = _cursor.getInt(_cursorIndexOfAlarmed);
        _tmpAlarmed = _tmp_3 != 0;
        _item = new Assessment(_tmpAssessmentId,_tmpName,_tmpType,_tmpStartDate,_tmpEndDate,_tmpCourseId,_tmpAlarmed);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Assessment> getAssessments() {
    final String _sql = "SELECT * FROM assessments ORDER BY startDate";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfAssessmentId = CursorUtil.getColumnIndexOrThrow(_cursor, "assessmentId");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
      final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
      final int _cursorIndexOfCourseId = CursorUtil.getColumnIndexOrThrow(_cursor, "courseId");
      final int _cursorIndexOfAlarmed = CursorUtil.getColumnIndexOrThrow(_cursor, "alarmed");
      final List<Assessment> _result = new ArrayList<Assessment>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Assessment _item;
        final int _tmpAssessmentId;
        _tmpAssessmentId = _cursor.getInt(_cursorIndexOfAssessmentId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final Assessment.AssessmentType _tmpType;
        final String _tmp;
        _tmp = _cursor.getString(_cursorIndexOfType);
        _tmpType = Converter.toAssessmentType(_tmp);
        final LocalDate _tmpStartDate;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfStartDate)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfStartDate);
        }
        _tmpStartDate = Converter.toDate(_tmp_1);
        final LocalDate _tmpEndDate;
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfEndDate)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfEndDate);
        }
        _tmpEndDate = Converter.toDate(_tmp_2);
        final int _tmpCourseId;
        _tmpCourseId = _cursor.getInt(_cursorIndexOfCourseId);
        final boolean _tmpAlarmed;
        final int _tmp_3;
        _tmp_3 = _cursor.getInt(_cursorIndexOfAlarmed);
        _tmpAlarmed = _tmp_3 != 0;
        _item = new Assessment(_tmpAssessmentId,_tmpName,_tmpType,_tmpStartDate,_tmpEndDate,_tmpCourseId,_tmpAlarmed);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Assessment getAssessment(final long id) {
    final String _sql = "SELECT * FROM assessments WHERE assessmentId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfAssessmentId = CursorUtil.getColumnIndexOrThrow(_cursor, "assessmentId");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
      final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
      final int _cursorIndexOfCourseId = CursorUtil.getColumnIndexOrThrow(_cursor, "courseId");
      final int _cursorIndexOfAlarmed = CursorUtil.getColumnIndexOrThrow(_cursor, "alarmed");
      final Assessment _result;
      if(_cursor.moveToFirst()) {
        final int _tmpAssessmentId;
        _tmpAssessmentId = _cursor.getInt(_cursorIndexOfAssessmentId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final Assessment.AssessmentType _tmpType;
        final String _tmp;
        _tmp = _cursor.getString(_cursorIndexOfType);
        _tmpType = Converter.toAssessmentType(_tmp);
        final LocalDate _tmpStartDate;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfStartDate)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfStartDate);
        }
        _tmpStartDate = Converter.toDate(_tmp_1);
        final LocalDate _tmpEndDate;
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfEndDate)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfEndDate);
        }
        _tmpEndDate = Converter.toDate(_tmp_2);
        final int _tmpCourseId;
        _tmpCourseId = _cursor.getInt(_cursorIndexOfCourseId);
        final boolean _tmpAlarmed;
        final int _tmp_3;
        _tmp_3 = _cursor.getInt(_cursorIndexOfAlarmed);
        _tmpAlarmed = _tmp_3 != 0;
        _result = new Assessment(_tmpAssessmentId,_tmpName,_tmpType,_tmpStartDate,_tmpEndDate,_tmpCourseId,_tmpAlarmed);
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
