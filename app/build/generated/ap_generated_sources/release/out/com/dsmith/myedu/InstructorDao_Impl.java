package com.dsmith.myedu;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class InstructorDao_Impl implements InstructorDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfInstructor;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfInstructor;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfInstructor;

  public InstructorDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfInstructor = new EntityInsertionAdapter<Instructor>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `instructors`(`instructorId`,`name`,`email`,`phone`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Instructor value) {
        stmt.bindLong(1, value.getInstructorId());
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        if (value.getEmail() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getEmail());
        }
        if (value.getPhone() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getPhone());
        }
      }
    };
    this.__deletionAdapterOfInstructor = new EntityDeletionOrUpdateAdapter<Instructor>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `instructors` WHERE `instructorId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Instructor value) {
        stmt.bindLong(1, value.getInstructorId());
      }
    };
    this.__updateAdapterOfInstructor = new EntityDeletionOrUpdateAdapter<Instructor>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `instructors` SET `instructorId` = ?,`name` = ?,`email` = ?,`phone` = ? WHERE `instructorId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Instructor value) {
        stmt.bindLong(1, value.getInstructorId());
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        if (value.getEmail() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getEmail());
        }
        if (value.getPhone() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getPhone());
        }
        stmt.bindLong(5, value.getInstructorId());
      }
    };
  }

  @Override
  public long insertInstructor(final Instructor instructor) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      long _result = __insertionAdapterOfInstructor.insertAndReturnId(instructor);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteInstructor(final Instructor instructor) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfInstructor.handle(instructor);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateInstructor(final Instructor instructor) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfInstructor.handle(instructor);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Instructor> getInstructors() {
    final String _sql = "SELECT * FROM instructors ORDER BY name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfInstructorId = CursorUtil.getColumnIndexOrThrow(_cursor, "instructorId");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final List<Instructor> _result = new ArrayList<Instructor>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Instructor _item;
        final int _tmpInstructorId;
        _tmpInstructorId = _cursor.getInt(_cursorIndexOfInstructorId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final String _tmpEmail;
        _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
        final String _tmpPhone;
        _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
        _item = new Instructor(_tmpInstructorId,_tmpName,_tmpEmail,_tmpPhone);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Instructor getInstructor(final long id) {
    final String _sql = "SELECT * FROM instructors WHERE instructorId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfInstructorId = CursorUtil.getColumnIndexOrThrow(_cursor, "instructorId");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final Instructor _result;
      if(_cursor.moveToFirst()) {
        final int _tmpInstructorId;
        _tmpInstructorId = _cursor.getInt(_cursorIndexOfInstructorId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final String _tmpEmail;
        _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
        final String _tmpPhone;
        _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
        _result = new Instructor(_tmpInstructorId,_tmpName,_tmpEmail,_tmpPhone);
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
