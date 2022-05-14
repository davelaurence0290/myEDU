package com.dsmith.myedu;

import java.time.LocalDate;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TermDao {

    @Query("SELECT * FROM terms ORDER BY startDate")
    public List<Term> getTerms();

    @Query("SELECT * FROM terms WHERE termId = :id")
    public Term getTerm(long id);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public long insertTerm(Term term);

    @Update
    public void updateTerm(Term term);

    @Delete
    public void deleteTerm(Term term);
}
