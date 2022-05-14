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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class InstructorView extends AppCompatActivity implements AddInstructorView.SaveFinishedListener {

    private final String INSTRUCTOR_DETAILS_TAG = "InstructorDetailsFragment";
    private final String INSTRUCTOR_EDIT_TAG = "InstructorEditFragment";

    private InstructorView.InstructorAdapter mInstructorAdapter;
    private RecyclerView mInstructorListView;
    private InstructorDetailsView mInstructorDetailsView;
    private AddInstructorView mAddInstructorView;

    private Instructor mSelectedInstructor;
    private int mSelectedInstructorPosition = RecyclerView.NO_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_view);


        mInstructorListView = findViewById(R.id.instructorList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        mInstructorListView.setLayoutManager(layoutManager);

        //only create new details fragment if creating fresh, not after recreating activity
        if(getSupportFragmentManager().findFragmentByTag(INSTRUCTOR_DETAILS_TAG) == null &&
                getSupportFragmentManager().findFragmentByTag(INSTRUCTOR_EDIT_TAG) == null) {
            mInstructorDetailsView = InstructorDetailsView.newInstance(null);
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.instructorDetailsLayout, mInstructorDetailsView, INSTRUCTOR_DETAILS_TAG)
                    .commit();
        }
        if(getSupportFragmentManager().findFragmentByTag(INSTRUCTOR_DETAILS_TAG) != null){
            mInstructorDetailsView = (InstructorDetailsView)getSupportFragmentManager().findFragmentByTag(INSTRUCTOR_DETAILS_TAG);
        }

        mInstructorAdapter = new InstructorView.InstructorAdapter(loadInstructors());
        mInstructorListView.setAdapter(mInstructorAdapter);

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
                addInstructor();
                return true;

            case R.id.action_edit:
                editInstructor();
                return true;

            case R.id.action_delete:
                deleteInstructor();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ArrayList<Instructor> loadInstructors() {
        return MainActivity.userSchedule.getAllInstructors();
    }

    public void addInstructorClick(View view){
        addInstructor();
    }

    public void addInstructor(){
        //start add instructor fragment
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(mInstructorDetailsView);
        mAddInstructorView = AddInstructorView.newInstance();
        ft.replace(R.id.instructorDetailsLayout, mAddInstructorView, INSTRUCTOR_EDIT_TAG);
        ft.commit();
        getSupportFragmentManager().popBackStackImmediate();
    }

    public void deleteInstructor(){
        if(mAddInstructorView != null) {
            Toast.makeText(getApplicationContext(), "Delete Failed: please close instructor edit form before deleting.",Toast.LENGTH_LONG).show();
        }
        else if(mSelectedInstructor.getInstructorCourses().size() > 0) {
            Toast.makeText(getApplicationContext(), "Delete Failed: please remove " + mSelectedInstructor.getName() + " from all Courses that list them as the instructor",Toast.LENGTH_LONG).show();
        }
        else if(mSelectedInstructor != null){
            Toast.makeText(getApplicationContext(), "Instructor \'" + mSelectedInstructor.getName() + "\' Deleted",Toast.LENGTH_LONG).show();
            MainActivity.userSchedule.deleteInstructor(mSelectedInstructor);
            mInstructorAdapter.notifyDataSetChanged();
            mSelectedInstructor = null;

            mInstructorDetailsView.setItemInfo(null);
        }
    }

    private void editInstructor(){
        //start edit instructor fragment
        if (mSelectedInstructor != null){
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.remove(mInstructorDetailsView);
            mAddInstructorView = AddInstructorView.newInstance(mSelectedInstructor);
            ft.replace(R.id.instructorDetailsLayout, mAddInstructorView, INSTRUCTOR_EDIT_TAG);
            ft.commit();
            getSupportFragmentManager().popBackStackImmediate();
        }
    }

    public void showInstructorDetailsFragment(){
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(mAddInstructorView);
        if(mSelectedInstructor != null) {
            mInstructorDetailsView = InstructorDetailsView.newInstance(mSelectedInstructor);
        }
        else{
            mInstructorDetailsView = InstructorDetailsView.newInstance(null);
        }
        ft.replace(R.id.instructorDetailsLayout, mInstructorDetailsView, INSTRUCTOR_DETAILS_TAG);
        ft.commit();
        getSupportFragmentManager().popBackStackImmediate();

    }

    public void onInstructorCancelButtonClick(View view){
        showInstructorDetailsFragment();
        if(mSelectedInstructor != null){
            mInstructorDetailsView.setItemInfo(mSelectedInstructor);
        }
    }

    public void onInstructorSaveButtonClick(View view){
        mAddInstructorView.save();
    }

    @Override
    public void onSaveFinished(){
        showInstructorDetailsFragment();
        mInstructorAdapter.notifyDataSetChanged();

        if(mSelectedInstructor != null){
            mInstructorDetailsView.setItemInfo(mSelectedInstructor);
        }
    }

    private class InstructorHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{

        private Instructor mInstructor;
        private TextView mTextView;

        public InstructorHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.recycler_view_items, parent, false));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mTextView = itemView.findViewById(R.id.itemTextView);
        }

        public void bind(Instructor instructor, int position) {
            mInstructor = instructor;
            mTextView.setText(instructor.getName());

            if( mSelectedInstructorPosition == position){
                // Make selected subject stand out
                mTextView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.teal_200));
            }
            else{
                mTextView.setBackgroundColor(Color.WHITE);
            }
        }

        @Override
        public void onClick(View view) {
            //pass information to the instructorDetailsView fragment.
            mSelectedInstructorPosition = getAdapterPosition();
            mSelectedInstructor = mInstructor;
            mInstructorDetailsView.setItemInfo(mSelectedInstructor);

            // Highlight last selected using rebinds
            mInstructorAdapter.notifyDataSetChanged();
        }

        @Override
        public boolean onLongClick(View view) {
            //nothing yet
            return true;
        }
    }

    private class InstructorAdapter extends RecyclerView.Adapter<InstructorView.InstructorHolder> {

        private ArrayList<Instructor> mInstructorList;

        public InstructorAdapter(ArrayList<Instructor> instructors) {
            mInstructorList = instructors;
        }

        public void addInstructor(Instructor instructor) {
            // Add the new subject at the beginning of the list
            mInstructorList.add(0, instructor);

            // Notify the adapter that item was added to the beginning of the list
            notifyItemInserted(0);

            // Scroll to the top
            mInstructorListView.scrollToPosition(0);
        }

        public void removeInstructor(Instructor instructor) {
            // Find subject in the list
            int index = mInstructorList.indexOf(instructor);
            if (index >= 0) {
                // Remove the subject
                mInstructorList.remove(index);

                // Notify adapter of subject removal
                notifyItemRemoved(index);
            }

        }

        @Override
        public InstructorView.InstructorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            return new InstructorView.InstructorHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(InstructorView.InstructorHolder holder, int position){
            holder.bind(mInstructorList.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mInstructorList.size();
        }
    }
}