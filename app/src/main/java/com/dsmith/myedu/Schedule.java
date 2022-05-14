package com.dsmith.myedu;

import android.media.MediaCasException;
import android.util.Log;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Schedule {

    private static final String TAG = "Schedule.java";

    private ArrayList<Term> allTerms             = new ArrayList<Term>();
    private ArrayList<Instructor> allInstructors = new ArrayList<Instructor>();
    private ArrayList<Course> allCourses         = new ArrayList<Course>();
    private ArrayList<Assessment> allAssessments = new ArrayList<Assessment>();
    private ArrayList<Note> allNotes             = new ArrayList<Note>();

    private static Schedule userSchedule;

    private Schedule(){
        initData();
    }

    public static Schedule getInstance(){

        if (userSchedule == null){
            userSchedule = new Schedule();
            //userSchedule.initData();
        }
        return userSchedule;
    }

    public void addTerm(String title, LocalDate startDate, LocalDate endDate){
        this.allTerms.add(new Term(title, startDate, endDate));
        this.allTerms.sort((t1, t2) -> t1.getStartDate().compareTo(t2.getStartDate()));
    }

    public void updateTerm(int id, String title, LocalDate startDate, LocalDate endDate){
        Optional<Term> oTerm = getAllTerms().stream().filter(t->t.getTermId() == id)
                .findFirst();
        if(oTerm.isPresent()){
            Term term = oTerm.get();
            term.setTitle(title);
            term.setStartDate(startDate);
            term.setEndDate(endDate);

            MainActivity.scheduleDatabase.termDao().updateTerm(term);
        }
    }

    public void loadTerms(){
        List<Term> dbTerms = MainActivity.scheduleDatabase.termDao().getTerms();
        dbTerms.forEach(term -> this.allTerms.add(term));

        this.allTerms.sort((t1, t2) -> t1.getStartDate().compareTo(t2.getStartDate()));
    }

    public Term getTerm(int id){
        Optional<Term> oTerm = this.allTerms.stream().filter(t-> t.getTermId() == id).findFirst();
        if(oTerm.isPresent()) {
            return oTerm.get();
        }
        return null;
    }

    public Term getCurrentTerm(){
        Optional<Term> oTerm = this.allTerms.stream()
                .filter(t->
                        t.getStartDate().atStartOfDay().isBefore(LocalDateTime.now()) &&
                                t.getEndDate().atStartOfDay().isAfter(LocalDateTime.now())
                )
                .findFirst();
        if(oTerm.isPresent()) {
            return oTerm.get();
        }
        return null;
    }

    public void deleteTerm(Term term){
        ArrayList<Course> termCourses = new ArrayList<>(
                this.allCourses.stream()
                .filter(c -> c.getTermId() == term.getTermId())
                .collect(Collectors.toList())
        );
        for( int ii = termCourses.size()-1; ii >= 0; ii--){
            deleteCourse(termCourses.get(ii));
        }

        MainActivity.scheduleDatabase.termDao().deleteTerm(term);
        this.allTerms.remove(term);
    }

    public void addInstructor(String name, String email, String phone){
        this.allInstructors.add(new Instructor(name, email, phone));
    }

    public void updateInstructor(int id, String name, String email, String phone){
        Optional<Instructor> oInstructor = getAllInstructors().stream().filter(i->i.getInstructorId() == id)
                .findFirst();
        if(oInstructor.isPresent()){
            Instructor instructor = oInstructor.get();
            instructor.setName(name);
            instructor.setEmail(email);
            instructor.setPhone(phone);

            MainActivity.scheduleDatabase.instructorDao().updateInstructor(instructor);
        }
    }
    public void loadInstructors(){
        List<Instructor> dbInstructors = MainActivity.scheduleDatabase.instructorDao().getInstructors();
        dbInstructors.forEach(instructor -> this.allInstructors.add(instructor));

        this.allInstructors.sort((i1, i2) -> i1.getName().compareTo(i2.getName()));
    }

    public Instructor getInstructor(int id){
        Optional<Instructor> oInstructor = this.allInstructors.stream().filter(i-> i.getInstructorId() == id).findFirst();
        if(oInstructor.isPresent()) {
            return oInstructor.get();
        }
        return null;
    }

    public void deleteInstructor(Instructor instructor){
        MainActivity.scheduleDatabase.instructorDao().deleteInstructor(instructor);
        this.allInstructors.remove(instructor);
    }

    public void addCourse(String title, String description, LocalDate startDate,
                          LocalDate endDate, Course.Status status, int instructorId, int termId){
        this.allCourses.add(new Course(title, description, startDate, endDate, status, instructorId, termId));
    }

    public void updateCourse(int id, String title, String description, LocalDate startDate,
                             LocalDate endDate, Course.Status status, int instructorId, int termId){
        Optional<Course> oCourse = getAllCourses().stream().filter(c->c.getCourseId() == id)
                .findFirst();
        if(oCourse.isPresent()){
            Course course = oCourse.get();
            course.setTitle(title);
            course.setDescription(description);
            course.setStartDate(startDate);
            course.setEndDate(endDate);
            course.setStatus(status);
            course.setInstructorId(instructorId);
            course.setTermId(termId);

            MainActivity.scheduleDatabase.courseDao().updateCourse(course);
        }
    }

    public void loadCourses(){
        List<Course> dbCourses = MainActivity.scheduleDatabase.courseDao().getCourses();
        dbCourses.forEach(course -> this.allCourses.add(course));
    }

    public Course getCourse(int id){
        Optional<Course> oCourse = this.allCourses.stream().filter(c-> c.getCourseId() == id).findFirst();
        if(oCourse.isPresent()) {
            return oCourse.get();
        }
        return null;
    }

    public ArrayList<Course> getCourses(int termId){
        return new ArrayList<>(MainActivity.userSchedule.getAllCourses().stream()
                .filter(c -> c.getTermId() == termId)
                .collect(Collectors.toList())
        );
    }

    public void deleteCourse(Course course){
        ArrayList<Assessment> courseAssessments = new ArrayList<>(
                this.allAssessments.stream()
                        .filter(a -> a.getCourseId() == course.getCourseId())
                        .collect(Collectors.toList())
        );
        for( int ii = courseAssessments.size()-1; ii >= 0; ii--){
            deleteAssessment(courseAssessments.get(ii));
        }

        ArrayList<Note> courseNotes = new ArrayList<>(
                this.allNotes.stream()
                        .filter(n -> n.getCourseId() == course.getCourseId())
                        .collect(Collectors.toList())
        );
        for( int ii = courseNotes.size()-1; ii >= 0; ii--){
            deleteNote(courseNotes.get(ii));
        }

        MainActivity.scheduleDatabase.courseDao().deleteCourse(course);
        this.allCourses.remove(course);
    }

    public void addAssessment(String name, Assessment.AssessmentType type, LocalDate startDate, LocalDate endDate, int courseId){
        this.allAssessments.add(new Assessment(name, type, startDate, endDate, courseId));
    }

    public void updateAssessment(int id, String name, Assessment.AssessmentType type, LocalDate startDate, LocalDate endDate, int courseId){
        Optional<Assessment> oAssessment = getAllAssessments().stream().filter(a->a.getAssessmentId() == id)
                .findFirst();
        if(oAssessment.isPresent()){
            Assessment assessment = oAssessment.get();
            assessment.setName(name);
            assessment.setType(type);
            assessment.setStartDate(startDate);
            assessment.setEndDate(endDate);
            assessment.setCourseId(courseId);

            MainActivity.scheduleDatabase.assessmentDao().updateAssessment(assessment);
        }
    }

    public void loadAssessments(){
        List<Assessment> dbAssessments = MainActivity.scheduleDatabase.assessmentDao().getAssessments();
        dbAssessments.forEach(assessment -> this.allAssessments.add(assessment));
    }

    public Assessment getAssessment(int id){
        Optional<Assessment> oAssessment = this.allAssessments.stream().filter(a-> a.getAssessmentId() == id).findFirst();
        if(oAssessment.isPresent()) {
            return oAssessment.get();
        }
        return null;
    }

    public ArrayList<Assessment> getAssessments(int courseId){
        return new ArrayList<>(MainActivity.userSchedule.getAllAssessments().stream()
                .filter(a -> a.getCourseId() == courseId)
                .collect(Collectors.toList())
        );
    }

    public void deleteAssessment(Assessment assessment){
        MainActivity.scheduleDatabase.assessmentDao().deleteAssessment(assessment);
        this.allAssessments.remove(assessment);
    }

    public void addNote(String text, int courseId){
        this.allNotes.add(new Note(text, courseId));
    }

    public void updateNote(int id, String text, int courseId){
        Optional<Note> oNote = getAllNotes().stream().filter(n->n.getNoteId() == id)
                .findFirst();
        if(oNote.isPresent()){
            Note note = oNote.get();
            note.setText(text);
            note.setCourseId(courseId);

            MainActivity.scheduleDatabase.noteDao().updateNote(note);
        }
    }

    public void loadNotes(){
        List<Note> dbNotes = MainActivity.scheduleDatabase.noteDao().getNotes();
        dbNotes.forEach(note -> this.allNotes.add(note));
    }

    public Note getNote(int id){
        Optional<Note> oNote = this.allNotes.stream().filter(n-> n.getNoteId() == id).findFirst();
        if(oNote.isPresent()) {
            return oNote.get();
        }
        return null;
    }

    public ArrayList<Note> getNotes(int courseId){
        return new ArrayList<>(MainActivity.userSchedule.getAllNotes().stream()
                .filter(n -> n.getCourseId() == courseId)
                .collect(Collectors.toList())
        );
    }

    public void deleteNote(Note note){
        MainActivity.scheduleDatabase.noteDao().deleteNote(note);
        this.allNotes.remove(note);
    }

    public ArrayList<Term> getAllTerms() {
        return allTerms;
    }

    public ArrayList<Instructor> getAllInstructors() {
        return allInstructors;
    }

    public ArrayList<Course> getAllCourses() {
        return allCourses;
    }

    public ArrayList<Assessment> getAllAssessments() {
        return allAssessments;
    }

    public ArrayList<Note> getAllNotes() {
        return allNotes;
    }

    public void initData(){

        loadTerms();
        loadInstructors();
        loadCourses();
        loadAssessments();
        loadNotes();
        /*
        allTerms.add(new Term( "Spring 2021",LocalDate.of(2021, 3, 1),LocalDate.of(2021,8,31)));
        allTerms.add(new Term( "Fall 2021",LocalDate.of(2021, 9, 1),LocalDate.of(2021,12,31)));

        allInstructors.add(new Instructor("Dave Fullerton","me@aol.com","555-4567"));
        allInstructors.add(new Instructor("Emily Faust","me@aol.com","555-4567"));
        allInstructors.add(new Instructor("Jane McCormick","me@aol.com","555-4567"));

        allCourses.add(new Course( "Intro to Java",
                "This course will teach students the fundamentals of the Java programming language.",
                LocalDate.of(2021, 3, 1),
                LocalDate.of(2021,4,15),
                Course.Status.IN_PROGRESS,
                1,
                1));
        allCourses.add(new Course( "Project Management",
                "The importance of managing various project resources over the life of the project cannot be overstated. " +
                        "This course will familiarize students with the knowledge necessary to guide projects toward success.",
                LocalDate.of(2021, 4, 16),
                LocalDate.of(2021,5,31),
                Course.Status.PLANNED,
                2,
                1));
        allCourses.add(new Course( "Introduction to Web Development",
                "In a world where web presence is ever increasing for businesses, web site development continues to " +
                        "be one of the fastest growing demands for technical skills. This course will introduce students to the world of " +
                        "web development while also introducing many of the contemporary technologies used in the industry today.",
                LocalDate.of(2021, 9, 1),
                LocalDate.of(2021,10,15),
                Course.Status.PLANNED,
                3,
                2));

        allAssessments.add(new Assessment( "Java Mid Term", Assessment.AssessmentType.OBJECTIVE,
                LocalDate.of(2021, 3, 21),
                LocalDate.of(2021,3,22),
                1));
        allAssessments.add(new Assessment( "Java Final", Assessment.AssessmentType.OBJECTIVE,
                LocalDate.of(2021, 4, 15),
                LocalDate.of(2021,4,15),
                1));
        allAssessments.add(new Assessment( "Proj Mgmt Final", Assessment.AssessmentType.OBJECTIVE,
                LocalDate.of(2021, 5, 31),
                LocalDate.of(2021,5,31),
                2));
        allAssessments.add(new Assessment( "Web Design Proj", Assessment.AssessmentType.PERFORMANCE,
                LocalDate.of(2021, 10, 21),
                LocalDate.of(2021,10,31),
                3));

        allNotes.add(new Note( "Note 1, Course 1",1));
        allNotes.add(new Note( "Note 2, Course 1",1));
        allNotes.add(new Note( "Note 3, Course 1",1));
        allNotes.add(new Note( "Note 1, Course 2",2));
        allNotes.add(new Note( "Note 1, Course 3",3));
        */

    }
}
