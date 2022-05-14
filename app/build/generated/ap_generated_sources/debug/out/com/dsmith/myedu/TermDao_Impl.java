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
public final class TermDao_Impl implements TermDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfTerm;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfTerm;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfTerm;

  public TermDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTerm = new EntityInsertionAdapter<Term>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `terms`(`termId`,`title`,`startDate`,`endDate`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Term value) {
        stmt.bindLong(1, value.getTermId());
        if (value.getTitle() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTitle());
        }
        final Long _tmp;
        _tmp = Converter.toDateString(value.getStartDate());
        if (_tmp == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindLong(3, _tmp);
        }
        final Long _tmp_1;
        _tmp_1 = Converter.toDateString(value.getEndDate());
        if (_tmp_1 == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, _tmp_1);
        }
      }
    };
    this.__deletionAdapterOfTerm = new EntityDeletionOrUpdateAdapter<Term>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `terms` WHERE `termId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Term value) {
        stmt.bindLong(1, value.getTermId());
      }
    };
    this.__updateAdapterOfTerm = new EntityDeletionOrUpdateAdapter<Term>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `terms` SET `termId` = ?,`title` = ?,`startDate` = ?,`endDate` = ? WHERE `termId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Term value) {
        stmt.bindLong(1, value.getTermId());
        if (value.getTitle() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTitle());
        }
        final Long _tmp;
        _tmp = Converter.toDateString(value.getStartDate());
        if (_tmp == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindLong(3, _tmp);
        }
        final Long _tmp_1;
        _tmp_1 = Converter.toDateString(value.getEndDate());
        if (_tmp_1 == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, _tmp_1);
        }
        stmt.bindLong(5, value.getTermId());
      }
    };
  }

  @Override
  public long insertTerm(final Term term) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      long _result = __insertionAdapterOfTerm.insertAndReturnId(term);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteTerm(final Term term) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfTerm.handle(term);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateTerm(final Term term) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfTerm.handle(term);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Term> getTerms() {
    final String _sql = "SELECT * FROM terms ORDER BY startDate";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfTermId = CursorUtil.getColumnIndexOrThrow(_cursor, "termId");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
      final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
      final List<Term> _result = new ArrayList<Term>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Term _item;
        final int _tmpTermId;
        _tmpTermId = _cursor.getInt(_cursorIndexOfTermId);
        final String _tmpTitle;
        _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
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
        _item = new Term(_tmpTermId,_tmpTitle,_tmpStartDate,_tmpEndDate);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Term getTerm(final long id) {
    final String _sql = "SELECT * FROM terms WHERE termId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfTermId = CursorUtil.getColumnIndexOrThrow(_cursor, "termId");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
      final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
      final Term _result;
      if(_cursor.moveToFirst()) {
        final int _tmpTermId;
        _tmpTermId = _cursor.getInt(_cursorIndexOfTermId);
        final String _tmpTitle;
        _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
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
        _result = new Term(_tmpTermId,_tmpTitle,_tmpStartDate,_tmpEndDate);
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
