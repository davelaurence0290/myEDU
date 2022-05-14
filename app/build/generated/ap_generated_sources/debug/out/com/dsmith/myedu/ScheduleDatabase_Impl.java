package com.dsmith.myedu;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class ScheduleDatabase_Impl extends ScheduleDatabase {
  private volatile TermDao _termDao;

  private volatile InstructorDao _instructorDao;

  private volatile CourseDao _courseDao;

  private volatile AssessmentDao _assessmentDao;

  private volatile NoteDao _noteDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `terms` (`termId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT, `startDate` INTEGER, `endDate` INTEGER)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `instructors` (`instructorId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `email` TEXT, `phone` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `courses` (`courseId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT, `description` TEXT, `startDate` INTEGER, `endDate` INTEGER, `status` TEXT, `instructorId` INTEGER NOT NULL, `termId` INTEGER NOT NULL, `alarmed` INTEGER NOT NULL, FOREIGN KEY(`termId`) REFERENCES `terms`(`termId`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`instructorId`) REFERENCES `instructors`(`instructorId`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `assessments` (`assessmentId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `type` TEXT, `startDate` INTEGER, `endDate` INTEGER, `courseId` INTEGER NOT NULL, `alarmed` INTEGER NOT NULL, FOREIGN KEY(`courseId`) REFERENCES `courses`(`courseId`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `notes` (`noteId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `text` TEXT, `courseId` INTEGER NOT NULL, FOREIGN KEY(`courseId`) REFERENCES `courses`(`courseId`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '0dbe07013f782bcdf339ce37d93f1839')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `terms`");
        _db.execSQL("DROP TABLE IF EXISTS `instructors`");
        _db.execSQL("DROP TABLE IF EXISTS `courses`");
        _db.execSQL("DROP TABLE IF EXISTS `assessments`");
        _db.execSQL("DROP TABLE IF EXISTS `notes`");
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        _db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsTerms = new HashMap<String, TableInfo.Column>(4);
        _columnsTerms.put("termId", new TableInfo.Column("termId", "INTEGER", true, 1));
        _columnsTerms.put("title", new TableInfo.Column("title", "TEXT", false, 0));
        _columnsTerms.put("startDate", new TableInfo.Column("startDate", "INTEGER", false, 0));
        _columnsTerms.put("endDate", new TableInfo.Column("endDate", "INTEGER", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTerms = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTerms = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTerms = new TableInfo("terms", _columnsTerms, _foreignKeysTerms, _indicesTerms);
        final TableInfo _existingTerms = TableInfo.read(_db, "terms");
        if (! _infoTerms.equals(_existingTerms)) {
          throw new IllegalStateException("Migration didn't properly handle terms(com.dsmith.myedu.Term).\n"
                  + " Expected:\n" + _infoTerms + "\n"
                  + " Found:\n" + _existingTerms);
        }
        final HashMap<String, TableInfo.Column> _columnsInstructors = new HashMap<String, TableInfo.Column>(4);
        _columnsInstructors.put("instructorId", new TableInfo.Column("instructorId", "INTEGER", true, 1));
        _columnsInstructors.put("name", new TableInfo.Column("name", "TEXT", false, 0));
        _columnsInstructors.put("email", new TableInfo.Column("email", "TEXT", false, 0));
        _columnsInstructors.put("phone", new TableInfo.Column("phone", "TEXT", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysInstructors = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesInstructors = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoInstructors = new TableInfo("instructors", _columnsInstructors, _foreignKeysInstructors, _indicesInstructors);
        final TableInfo _existingInstructors = TableInfo.read(_db, "instructors");
        if (! _infoInstructors.equals(_existingInstructors)) {
          throw new IllegalStateException("Migration didn't properly handle instructors(com.dsmith.myedu.Instructor).\n"
                  + " Expected:\n" + _infoInstructors + "\n"
                  + " Found:\n" + _existingInstructors);
        }
        final HashMap<String, TableInfo.Column> _columnsCourses = new HashMap<String, TableInfo.Column>(9);
        _columnsCourses.put("courseId", new TableInfo.Column("courseId", "INTEGER", true, 1));
        _columnsCourses.put("title", new TableInfo.Column("title", "TEXT", false, 0));
        _columnsCourses.put("description", new TableInfo.Column("description", "TEXT", false, 0));
        _columnsCourses.put("startDate", new TableInfo.Column("startDate", "INTEGER", false, 0));
        _columnsCourses.put("endDate", new TableInfo.Column("endDate", "INTEGER", false, 0));
        _columnsCourses.put("status", new TableInfo.Column("status", "TEXT", false, 0));
        _columnsCourses.put("instructorId", new TableInfo.Column("instructorId", "INTEGER", true, 0));
        _columnsCourses.put("termId", new TableInfo.Column("termId", "INTEGER", true, 0));
        _columnsCourses.put("alarmed", new TableInfo.Column("alarmed", "INTEGER", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCourses = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysCourses.add(new TableInfo.ForeignKey("terms", "NO ACTION", "NO ACTION",Arrays.asList("termId"), Arrays.asList("termId")));
        _foreignKeysCourses.add(new TableInfo.ForeignKey("instructors", "NO ACTION", "NO ACTION",Arrays.asList("instructorId"), Arrays.asList("instructorId")));
        final HashSet<TableInfo.Index> _indicesCourses = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCourses = new TableInfo("courses", _columnsCourses, _foreignKeysCourses, _indicesCourses);
        final TableInfo _existingCourses = TableInfo.read(_db, "courses");
        if (! _infoCourses.equals(_existingCourses)) {
          throw new IllegalStateException("Migration didn't properly handle courses(com.dsmith.myedu.Course).\n"
                  + " Expected:\n" + _infoCourses + "\n"
                  + " Found:\n" + _existingCourses);
        }
        final HashMap<String, TableInfo.Column> _columnsAssessments = new HashMap<String, TableInfo.Column>(7);
        _columnsAssessments.put("assessmentId", new TableInfo.Column("assessmentId", "INTEGER", true, 1));
        _columnsAssessments.put("name", new TableInfo.Column("name", "TEXT", false, 0));
        _columnsAssessments.put("type", new TableInfo.Column("type", "TEXT", false, 0));
        _columnsAssessments.put("startDate", new TableInfo.Column("startDate", "INTEGER", false, 0));
        _columnsAssessments.put("endDate", new TableInfo.Column("endDate", "INTEGER", false, 0));
        _columnsAssessments.put("courseId", new TableInfo.Column("courseId", "INTEGER", true, 0));
        _columnsAssessments.put("alarmed", new TableInfo.Column("alarmed", "INTEGER", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAssessments = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysAssessments.add(new TableInfo.ForeignKey("courses", "NO ACTION", "NO ACTION",Arrays.asList("courseId"), Arrays.asList("courseId")));
        final HashSet<TableInfo.Index> _indicesAssessments = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAssessments = new TableInfo("assessments", _columnsAssessments, _foreignKeysAssessments, _indicesAssessments);
        final TableInfo _existingAssessments = TableInfo.read(_db, "assessments");
        if (! _infoAssessments.equals(_existingAssessments)) {
          throw new IllegalStateException("Migration didn't properly handle assessments(com.dsmith.myedu.Assessment).\n"
                  + " Expected:\n" + _infoAssessments + "\n"
                  + " Found:\n" + _existingAssessments);
        }
        final HashMap<String, TableInfo.Column> _columnsNotes = new HashMap<String, TableInfo.Column>(3);
        _columnsNotes.put("noteId", new TableInfo.Column("noteId", "INTEGER", true, 1));
        _columnsNotes.put("text", new TableInfo.Column("text", "TEXT", false, 0));
        _columnsNotes.put("courseId", new TableInfo.Column("courseId", "INTEGER", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysNotes = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysNotes.add(new TableInfo.ForeignKey("courses", "NO ACTION", "NO ACTION",Arrays.asList("courseId"), Arrays.asList("courseId")));
        final HashSet<TableInfo.Index> _indicesNotes = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoNotes = new TableInfo("notes", _columnsNotes, _foreignKeysNotes, _indicesNotes);
        final TableInfo _existingNotes = TableInfo.read(_db, "notes");
        if (! _infoNotes.equals(_existingNotes)) {
          throw new IllegalStateException("Migration didn't properly handle notes(com.dsmith.myedu.Note).\n"
                  + " Expected:\n" + _infoNotes + "\n"
                  + " Found:\n" + _existingNotes);
        }
      }
    }, "0dbe07013f782bcdf339ce37d93f1839", "91b49e1acbc82d0ba1796498f86d78e9");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "terms","instructors","courses","assessments","notes");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `courses`");
      _db.execSQL("DELETE FROM `terms`");
      _db.execSQL("DELETE FROM `instructors`");
      _db.execSQL("DELETE FROM `assessments`");
      _db.execSQL("DELETE FROM `notes`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public TermDao termDao() {
    if (_termDao != null) {
      return _termDao;
    } else {
      synchronized(this) {
        if(_termDao == null) {
          _termDao = new TermDao_Impl(this);
        }
        return _termDao;
      }
    }
  }

  @Override
  public InstructorDao instructorDao() {
    if (_instructorDao != null) {
      return _instructorDao;
    } else {
      synchronized(this) {
        if(_instructorDao == null) {
          _instructorDao = new InstructorDao_Impl(this);
        }
        return _instructorDao;
      }
    }
  }

  @Override
  public CourseDao courseDao() {
    if (_courseDao != null) {
      return _courseDao;
    } else {
      synchronized(this) {
        if(_courseDao == null) {
          _courseDao = new CourseDao_Impl(this);
        }
        return _courseDao;
      }
    }
  }

  @Override
  public AssessmentDao assessmentDao() {
    if (_assessmentDao != null) {
      return _assessmentDao;
    } else {
      synchronized(this) {
        if(_assessmentDao == null) {
          _assessmentDao = new AssessmentDao_Impl(this);
        }
        return _assessmentDao;
      }
    }
  }

  @Override
  public NoteDao noteDao() {
    if (_noteDao != null) {
      return _noteDao;
    } else {
      synchronized(this) {
        if(_noteDao == null) {
          _noteDao = new NoteDao_Impl(this);
        }
        return _noteDao;
      }
    }
  }
}
