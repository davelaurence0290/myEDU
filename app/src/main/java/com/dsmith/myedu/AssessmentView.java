package com.dsmith.myedu;

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

public class AssessmentView extends AppCompatActivity implements AddAssessmentView.SaveFinishedListener{

    private final String ASSESSMENT_DETAILS_TAG = "CourseDetailsFragment";
    private final String ASSESSMENT_EDIT_TAG = "CourseEditFragment";
    private final String COURSE_ID = "CourseID";


    private int mCourseId;


    private AssessmentAdapter mAssessmentAdapter;
    private RecyclerView mAssessmentListView;
    private AssessmentDetailsView mAssessmentDetailsView;
    private AddAssessmentView mAddAssessmentView;
    private AddNoteView mAddNoteView;

    private Assessment mSelectedAssessment;
    private Note mSelectedNote;
    private int mSelectedAssessmentPosition = RecyclerView.NO_POSITION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_view);

        mCourseId = this.getIntent().getIntExtra(COURSE_ID,0);

        mAssessmentListView = findViewById(R.id.assessmentList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        mAssessmentListView.setLayoutManager(layoutManager);

        //only create new details fragment if creating fresh, not after recreating activity
        if(getSupportFragmentManager().findFragmentByTag(ASSESSMENT_DETAILS_TAG) == null &&
                getSupportFragmentManager().findFragmentByTag(ASSESSMENT_EDIT_TAG) == null) {
            mAssessmentDetailsView = AssessmentDetailsView.newInstance(null);
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.assessmentDetailsLayout, mAssessmentDetailsView, ASSESSMENT_DETAILS_TAG)
                    .commit();
        }
        if(getSupportFragmentManager().findFragmentByTag(ASSESSMENT_DETAILS_TAG) != null){
            mAssessmentDetailsView = (AssessmentDetailsView)getSupportFragmentManager().findFragmentByTag(ASSESSMENT_DETAILS_TAG);
        }

        mAssessmentAdapter = new AssessmentAdapter(loadAssessments());
        mAssessmentListView.setAdapter(mAssessmentAdapter);

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
                addAssessment();
                return true;

            case R.id.action_edit:
                editAssessment();
                return true;

            case R.id.action_delete:
                deleteAssessment();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ArrayList<Assessment> loadAssessments() {
        if (mCourseId == 0)
                return MainActivity.userSchedule.getAllAssessments();
        else{
            return new ArrayList<> (MainActivity.userSchedule.getAllAssessments().stream()
                    .filter(c-> c.getCourseId() == mCourseId)
                    .collect(Collectors.toList())
            );
        }
    }

    public void addAssessmentClick(View view){
        addAssessment();
    }

    public void addAssessment(){
        if (mCourseId != 0){
            //start add assessment fragment
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.remove(mAssessmentDetailsView);
            mAddAssessmentView = AddAssessmentView.newInstance();
            mAddAssessmentView.setCourseId(mCourseId);
            ft.replace(R.id.assessmentDetailsLayout, mAddAssessmentView, ASSESSMENT_EDIT_TAG);
            ft.commit();
            getSupportFragmentManager().popBackStackImmediate();
        }
        else{
            Toast.makeText(getApplicationContext(), "Assessments can only be added into a Course, please edit a course to add assessments.",Toast.LENGTH_LONG).show();
        }
    }

    public void deleteAssessment(){
        if(mAddAssessmentView != null) {
            Toast.makeText(getApplicationContext(), "Delete Failed: please close assessment edit form before deleting.",Toast.LENGTH_LONG).show();
        }
         if(mSelectedAssessment != null){
            Toast.makeText(getApplicationContext(), "Assessment \'" + mSelectedAssessment.getName() + "\' Deleted",Toast.LENGTH_LONG).show();
            MainActivity.userSchedule.deleteAssessment(mSelectedAssessment);
            mAssessmentAdapter.updateItems();
            mAssessmentAdapter.notifyDataSetChanged();
            mSelectedAssessment = null;

            mAssessmentDetailsView.setItemInfo(null);
        }
    }

    public void editAssessment() {
        //start edit assessment fragment
        if (mSelectedAssessment != null) {
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.remove(mAssessmentDetailsView);
            mAddAssessmentView = AddAssessmentView.newInstance(mSelectedAssessment);
            ft.replace(R.id.assessmentDetailsLayout, mAddAssessmentView, ASSESSMENT_EDIT_TAG);
            ft.commit();
            getSupportFragmentManager().popBackStackImmediate();
        }
    }

    public void showAssessmentDetailsFragment(){
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(mAddAssessmentView);
        if(mSelectedAssessment != null) {
            mAssessmentDetailsView = AssessmentDetailsView.newInstance(mSelectedAssessment);
        }
        else{
            mAssessmentDetailsView = AssessmentDetailsView.newInstance(null);
        }
        ft.replace(R.id.assessmentDetailsLayout, mAssessmentDetailsView, ASSESSMENT_DETAILS_TAG);
        ft.commit();
        getSupportFragmentManager().popBackStackImmediate();

    }

    public void onAssessmentCancelButtonClick(View view){
        showAssessmentDetailsFragment();
        if(mSelectedAssessment != null){
            mAssessmentDetailsView.setItemInfo(mSelectedAssessment);
        }
    }

    public void onAssessmentSaveButtonClick(View view){
        mAddAssessmentView.save();
    }

    @Override
    public void onSaveFinished(){
        showAssessmentDetailsFragment();
        mAssessmentAdapter.updateItems();
        mAssessmentAdapter.notifyDataSetChanged();

        if(mSelectedAssessment != null){
            mAssessmentDetailsView.setItemInfo(mSelectedAssessment);
        }
    }

    public void onSetAlertsClick(View view){
        if(mSelectedAssessment != null){
            String msg = "";
            if (mSelectedAssessment.isAlarmed()){
                msg = "Assessment " + mSelectedAssessment.getName() + " already has alarms set.";
            }
            else{
                setAssessmentAlerts(mSelectedAssessment);
                mSelectedAssessment.setAlarmed(true);
                msg = "Set to receive alerts on assessment: " + mSelectedAssessment.getName() + "\n" +
                        "On Start date: " + mSelectedAssessment.getStartDate() + "\n" +
                        "On End date: " + mSelectedAssessment.getEndDate();
            }

            Toast.makeText(getApplicationContext(), msg,Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Select assessment for which to receive alerts.",Toast.LENGTH_LONG).show();
        }
    }

    private void setAssessmentAlerts(Assessment assessment){
        MainActivity.alertReceiver.setAlerts(
                getApplicationContext(),
                assessment.getStartDate(),
                assessment.getEndDate(),
                "Assessment " + assessment.getName() + " starts today, " + assessment.getStartDate(),
                "Assessment " + assessment.getName() + " ends today, " + assessment.getEndDate(),
                AlertBroadcastReceiver.ALERT_ASSESSMENT
        );
    }

    private class AssessmentHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{

        private Assessment mAssessment;
        private TextView mTextView;

        public AssessmentHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.recycler_view_items, parent, false));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mTextView = itemView.findViewById(R.id.itemTextView);
        }

        public void bind(Assessment assessment, int position) {
            mAssessment = assessment;
            mTextView.setText(assessment.getName());

            if( mSelectedAssessmentPosition == position){
                // Make selected subject stand out
                mTextView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.teal_200));
            }
            else{
                mTextView.setBackgroundColor(Color.WHITE);
            }
        }

        @Override
        public void onClick(View view) {
            //pass information to the assessmentDetailsView fragment.
            mSelectedAssessmentPosition = getAdapterPosition();
            mSelectedAssessment = mAssessment;
            mAssessmentDetailsView.setItemInfo(mSelectedAssessment);
            //make sure note selection is cleared.
            mSelectedNote = null;
            // Highlight last selected using rebinds
            mAssessmentAdapter.notifyDataSetChanged();
        }

        @Override
        public boolean onLongClick(View view) {
            //nothing yet
            return true;
        }
    }

    private class AssessmentAdapter extends RecyclerView.Adapter<AssessmentHolder> {

        private ArrayList<Assessment> mAssessmentList;

        public AssessmentAdapter(ArrayList<Assessment> assessments) {
            mAssessmentList = assessments;
        }

        public void addAssessment(Assessment assessment) {
            // Add the new subject at the beginning of the list
            mAssessmentList.add(0, assessment);

            // Notify the adapter that item was added to the beginning of the list
            notifyItemInserted(0);

            // Scroll to the top
            mAssessmentListView.scrollToPosition(0);
        }

        public void removeAssessment(Assessment assessment) {
            // Find subject in the list
            int index = mAssessmentList.indexOf(assessment);
            if (index >= 0) {
                // Remove the subject
                mAssessmentList.remove(index);

                // Notify adapter of subject removal
                notifyItemRemoved(index);
            }
        }

        public void updateItems(){
            mAssessmentList = loadAssessments();
        }

        @Override
        public AssessmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            return new AssessmentHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(AssessmentHolder holder, int position){
            holder.bind(mAssessmentList.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mAssessmentList.size();
        }
    }
}