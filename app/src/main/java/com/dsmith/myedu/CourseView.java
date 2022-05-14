package com.dsmith.myedu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.stream.Collectors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CourseView extends AppCompatActivity implements AddCourseView.CourseSaveFinishedListener,
        AddNoteView.NoteSaveFinishedListener,
        CourseDetailsView.NoteSelectedListener {

    private final String COURSE_DETAILS_TAG = "CourseDetailsFragment";
    private final String COURSE_EDIT_TAG = "CourseEditFragment";
    private final String NOTE_ADD_TAG = "NoteAddFragment";
    private final String TERM_ID = "TermID";
    private final String COURSE_ID = "CourseID";
    private final String INSTRUCTOR_ID = "InstructorID";

    private int mTermId;

    private CourseAdapter mCourseAdapter;
    private RecyclerView mCourseListView;
    private CourseDetailsView mCourseDetailsView;
    private AddCourseView mAddCourseView;
    private AddNoteView mAddNoteView;

    private Course mSelectedCourse;
    private Note mSelectedNote;
    private int mSelectedCoursePosition = RecyclerView.NO_POSITION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_view);

        //mTermId = this.getIntent().getIntExtra(TERM_ID,0);

        mCourseListView = findViewById(R.id.courseList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        mCourseListView.setLayoutManager(layoutManager);

        //only create new details fragment if creating fresh, not after recreating activity
        if(getSupportFragmentManager().findFragmentByTag(COURSE_DETAILS_TAG) == null &&
                getSupportFragmentManager().findFragmentByTag(COURSE_EDIT_TAG) == null) {
            mCourseDetailsView = CourseDetailsView.newInstance(null);
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.courseDetailsLayout, mCourseDetailsView, COURSE_DETAILS_TAG)
                    .commit();
        }
        if(getSupportFragmentManager().findFragmentByTag(COURSE_DETAILS_TAG) != null){
            mCourseDetailsView = (CourseDetailsView)getSupportFragmentManager().findFragmentByTag(COURSE_DETAILS_TAG);
        }
    }

    public void onStart(){
        super.onStart();
        mTermId = this.getIntent().getIntExtra(TERM_ID,0);
        mCourseAdapter = new CourseAdapter(loadCourses());
        mCourseListView.setAdapter(mCourseAdapter);

        if(mCourseDetailsView != null){
            mCourseDetailsView.updateAdapters();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                addCourse();
                return true;

            case R.id.action_edit:
                editCourse();
                return true;

            case R.id.action_delete:
                deleteCourse();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ArrayList<Course> loadCourses() {
        if (mTermId == 0)
                return MainActivity.userSchedule.getAllCourses();
        else{
            return MainActivity.userSchedule.getCourses(mTermId);
        }
    }

    public void addCourseClick(View view){
        addCourse();
    }

    public void addCourse(){
        if (mTermId != 0){
            //start add term fragment
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.remove(mCourseDetailsView);
            mAddCourseView = AddCourseView.newInstance();
            mAddCourseView.setTermId(mTermId);
            ft.replace(R.id.courseDetailsLayout, mAddCourseView, COURSE_EDIT_TAG);
            ft.commit();
            getSupportFragmentManager().popBackStackImmediate();
        }
        else{
            Toast.makeText(getApplicationContext(), "Courses can only be added into a Term, please edit a term to add courses.",Toast.LENGTH_LONG).show();
        }
    }

    public void deleteCourse(){
        if(mAddCourseView != null) {
            Toast.makeText(getApplicationContext(), "Delete Failed: please close course edit form before deleting.",Toast.LENGTH_LONG).show();
        }
        else if(mSelectedCourse != null){
            Toast.makeText(getApplicationContext(), "Course \'" + mSelectedCourse.getTitle() + "\' Deleted",Toast.LENGTH_LONG).show();
            MainActivity.userSchedule.deleteCourse(mSelectedCourse);
            mCourseAdapter.updateItems();
            mCourseAdapter.notifyDataSetChanged();
            mSelectedCourse = null;

            mCourseDetailsView.setItemInfo(null);
        }
    }

    public void editCourse() {
        //start edit course fragment
        if (mSelectedCourse != null) {
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.remove(mCourseDetailsView);
            mAddCourseView = AddCourseView.newInstance(mSelectedCourse);
            ft.replace(R.id.courseDetailsLayout, mAddCourseView, COURSE_EDIT_TAG);
            ft.commit();
            getSupportFragmentManager().popBackStackImmediate();
        }
    }

    public void editAssessmentsClick(View view){ startEditAssessmentsActivity();}

    private void startEditAssessmentsActivity(){
        //start view/edit course activity
        if(mSelectedCourse != null) {
            Intent intent = new Intent(CourseView.this, AssessmentView.class);

            intent.putExtra(COURSE_ID, mSelectedCourse.getCourseId());
            startActivity(intent);
        }
    }

    public void editInstructorsClick(View view){ startEditInstructorsActivity();}

    private void startEditInstructorsActivity(){
        Intent intent = new Intent(CourseView.this, InstructorView.class);
        startActivity(intent);
    }

    public void showCourseDetailsFragment(){
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(mAddCourseView);
        if(mSelectedCourse != null) {
            mCourseDetailsView = CourseDetailsView.newInstance(mSelectedCourse);
        }
        else{
            mCourseDetailsView = CourseDetailsView.newInstance(null);
        }
        ft.replace(R.id.courseDetailsLayout, mCourseDetailsView, COURSE_DETAILS_TAG);
        ft.commit();
        getSupportFragmentManager().popBackStackImmediate();

    }

    public void onCourseCancelButtonClick(View view){
        showCourseDetailsFragment();
        if(mSelectedCourse != null){
            mCourseDetailsView.setItemInfo(mSelectedCourse);
        }
    }

    public void onCourseSaveButtonClick(View view){
        mAddCourseView.save();
    }

    public void addNoteClick(View view){
        showNoteDialog(null);
    }

    public void editNoteClick(View view){
        if (mSelectedNote != null){
            showNoteDialog(mSelectedNote);
        }
        else{
            Toast.makeText(getApplicationContext(), "Please select note to edit",Toast.LENGTH_LONG).show();
        }
    }

    public void showNoteDialog(Note note){
        //start add note fragment
        if(note != null){
            mAddNoteView = AddNoteView.newInstance(note);
        }
        else{
            mAddNoteView = AddNoteView.newInstance();
        }
        mAddNoteView.setCourseId(mSelectedCourse.getCourseId());
        mAddNoteView.show(getSupportFragmentManager(), NOTE_ADD_TAG);
    }

    public void onNoteCancelButtonClick(View view){
        if(mSelectedCourse != null){
            mCourseDetailsView.setItemInfo(mSelectedCourse);
        }
        mAddNoteView.dismiss();
    }

    public void onNoteSaveButtonClick(View view){
        mAddNoteView.save();
    }

    @Override
    public void onCourseSaveFinished(){
        showCourseDetailsFragment();
        mCourseAdapter.updateItems();
        mCourseAdapter.notifyDataSetChanged();

        if(mSelectedCourse != null){
            mCourseDetailsView.setItemInfo(mSelectedCourse);
        }
    }

    @Override
    public void onNoteSaveFinished(){
        mAddNoteView.dismiss();
        mCourseDetailsView.updateNoteList();

        if(mSelectedCourse != null){
            mCourseDetailsView.setItemInfo(mSelectedCourse);
        }
    }

    @Override
    public void onNoteSelected(Note note){
        mSelectedNote = note;
    }

    public void shareCourseNotesClick(View view){
        if (mSelectedCourse != null){
            ArrayList<Note> notes = MainActivity.userSchedule.getNotes(mSelectedCourse.getCourseId());

            String msgText = mSelectedCourse.getTitle() + "\n" +
                    mSelectedCourse.getDetailString() + "\n\n";

            for (Note note : notes){
                msgText += "--" + note.getText() + "\n\n";
            }

            Intent intent = new Intent(Intent.ACTION_SEND);

            // plain text extras
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Course Notes: " + mSelectedCourse.getTitle());
            intent.putExtra(Intent.EXTRA_TEXT, msgText);

            // If at least one app can handle intent, allow user to choose
            if (intent.resolveActivity(getPackageManager()) != null) {
                Intent chooser = intent.createChooser(intent, "Share Course Notes");
                startActivity(chooser);
            }
            else{
                Toast.makeText(getApplicationContext(), "No application to handle sharing.",Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onSetAlertsClick(View view){
        if(mSelectedCourse != null){
            String msg = "";
            if (mSelectedCourse.isAlarmed()){
                msg = "Course " + mSelectedCourse.getTitle() + " already has alarms set.";
            }
            else{
                setCourseAlerts(mSelectedCourse);
                mSelectedCourse.setAlarmed(true);
                msg = "Set to receive alerts on course: " + mSelectedCourse.getTitle() + "\n" +
                        "On Start date: " + mSelectedCourse.getStartDate() + "\n" +
                        "On End date: " + mSelectedCourse.getEndDate();
            }

            Toast.makeText(getApplicationContext(), msg,Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Select course for which to receive alerts.",Toast.LENGTH_LONG).show();
        }
    }

    private void setCourseAlerts(Course course){
        MainActivity.alertReceiver.setAlerts(
                getApplicationContext(),
                course.getStartDate(),
                course.getEndDate(),
                "Course " + course.getTitle() + " starts today, " + course.getStartDate(),
                "Course " + course.getTitle() + " ends today, " + course.getEndDate(),
                AlertBroadcastReceiver.ALERT_COURSE
        );
    }

    private class CourseHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{

        private Course mCourse;
        private TextView mTextView;

        public CourseHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.recycler_view_items, parent, false));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mTextView = itemView.findViewById(R.id.itemTextView);
        }

        public void bind(Course course, int position) {
            mCourse = course;
            mTextView.setText(course.getTitle());

            if( mSelectedCoursePosition == position){
                // Make selected subject stand out
                mTextView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.teal_200));
            }
            else{
                mTextView.setBackgroundColor(Color.WHITE);
            }
        }

        @Override
        public void onClick(View view) {
            //pass information to the termDetailsView fragment.
            mSelectedCoursePosition = getAdapterPosition();
            mSelectedCourse = mCourse;
            mCourseDetailsView.setItemInfo(mSelectedCourse);
            //make sure note selection is cleared.
            mSelectedNote = null;
            // Highlight last selected using rebinds
            mCourseAdapter.notifyDataSetChanged();
        }

        @Override
        public boolean onLongClick(View view) {
            //nothing yet
            return true;
        }
    }

    private class CourseAdapter extends RecyclerView.Adapter<CourseHolder> {

        private ArrayList<Course> mCourseList;

        public CourseAdapter(ArrayList<Course> courses) {
            mCourseList = courses;
        }

        public void addCourse(Course course) {
            // Add the new subject at the beginning of the list
            mCourseList.add(0, course);

            // Notify the adapter that item was added to the beginning of the list
            notifyItemInserted(0);

            // Scroll to the top
            mCourseListView.scrollToPosition(0);
        }

        public void removeCourse(Course course) {
            // Find subject in the list
            int index = mCourseList.indexOf(course);
            if (index >= 0) {
                // Remove the subject
                mCourseList.remove(index);

                // Notify adapter of subject removal
                notifyItemRemoved(index);
            }
        }

        public void updateItems(){
            mCourseList = loadCourses();
        }

        @Override
        public CourseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            return new CourseHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CourseHolder holder, int position){
            holder.bind(mCourseList.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mCourseList.size();
        }
    }
}