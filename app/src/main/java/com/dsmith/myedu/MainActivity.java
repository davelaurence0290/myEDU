package com.dsmith.myedu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity.java";

    public static Schedule userSchedule;
    public static ScheduleDatabase scheduleDatabase;
    public static AlertBroadcastReceiver alertReceiver;

    private Term mCurrentTerm;
    private ArrayList<Course> mCurrentCourses;
    private ArrayList<Assessment> mCurrentAssessments;

    private Button viewTermsButton;

    private TextView mDateTextView;
    private TextView mCurrentTermView;
    private RecyclerView mCurrentCourseList;
    private RecyclerView mCurrentAssessmentList;
    private ItemAdapter<Course> mCurrentCourseAdapter;
    private ItemAdapter<Assessment> mCurrentAssessmentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize schedule DB and the scheduler object
        scheduleDatabase = ScheduleDatabase.getInstance(getApplicationContext());
        userSchedule = Schedule.getInstance();
        //set up an alert receiver
        alertReceiver = new AlertBroadcastReceiver();

        mDateTextView = findViewById(R.id.dateView);
        mDateTextView.setText(DateTimeFormatter.ofPattern("MMMM dd, YYYY").format(LocalDate.now()));

        viewTermsButton = findViewById(R.id.viewTermsButton);

        mCurrentTermView= findViewById(R.id.currentTermText);

        //initialize current item arrays, populated in onStart()
        mCurrentCourses = new ArrayList<>();
        mCurrentAssessments = new ArrayList<>();

        //set up Current Course List
        mCurrentCourseList = findViewById(R.id.currentCoursesList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        mCurrentCourseList.setLayoutManager(layoutManager);

        mCurrentCourseAdapter = new ItemAdapter<>(mCurrentCourses);
        mCurrentCourseList.setAdapter(mCurrentCourseAdapter);

        //set up Current Course List
        mCurrentAssessmentList = findViewById(R.id.currentAssessmentsList);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        mCurrentAssessmentList.setLayoutManager(layoutManager2);

        mCurrentAssessmentAdapter = new ItemAdapter<>(mCurrentAssessments);
        mCurrentAssessmentList.setAdapter(mCurrentAssessmentAdapter);
    }

    @Override
    public void onStart(){
        super.onStart();

        loadCurrentItems();
        if(mCurrentTerm != null) {
            mCurrentTermView.setText(mCurrentTerm.getTitle() + "\n" + mCurrentTerm.getDetailString());
            mCurrentCourseAdapter.updateItems(mCurrentCourses);
            mCurrentAssessmentAdapter.updateItems(mCurrentAssessments);
        }
    }

    private void loadCurrentItems(){
        //Loads the current term, courses, and assessments, if there are any.
        mCurrentTerm = userSchedule.getCurrentTerm();

        //get all current courses
        if (mCurrentTerm != null){
            mCurrentCourses = userSchedule.getCourses(mCurrentTerm.getTermId());
        }
        //get all current assessments
        if (mCurrentCourses.size() > 0){
            for (Course course : mCurrentCourses){
                mCurrentAssessments.addAll(userSchedule.getAssessments(course.getCourseId()));
            }
        }
    }

    public void viewTermsClick(View view){
        Intent intent = new Intent(MainActivity.this, TermView.class);
        startActivity(intent);
    }

    public void testAlertClick(View view){
        alertReceiver.setAlerts(this, LocalDate.now(), LocalDate.now(), "This is a startDate test", "This is an endDate test", AlertBroadcastReceiver.ALERT_TEST);
    }

    public class ItemHolder<Item> extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{

        private Item mItem;
        private TextView mTextView;


        public ItemHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.recycler_detail_view_items, parent, false));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mTextView = itemView.findViewById(R.id.itemTextView);
        }

        public void bind(Item item, int position) {
            mItem = item;
            mTextView.setText(item.toString());
            mTextView.setBackgroundColor(Color.WHITE);

            /*if( mSelectedItemPosition == position){
                // Make selected subject stand out
                mTextView.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.teal_200));
            }
            else{
                mTextView.setBackgroundColor(Color.WHITE);
            }*/
        }

        @Override
        public void onClick(View view) {
            //nothing yet
        }

        @Override
        public boolean onLongClick(View view) {
            //nothing yet
            return true;
        }
    }

    public class ItemAdapter<Item> extends RecyclerView.Adapter<ItemHolder<Item>> {

        private ArrayList<Item> mItemList;

        public ItemAdapter(ArrayList<Item> items) {
            mItemList = items;
        }

        public void updateItems(ArrayList<Item> items){
            mItemList = items;
            notifyDataSetChanged();
        }

        public void addItem(Item item) {
            // Add the new subject at the beginning of the list
            mItemList.add(0, item);

            // Notify the adapter that item was added to the beginning of the list
            notifyItemInserted(0);

            // Scroll to the top
            //mItemListView.scrollToPosition(0);
        }

        public void removeItem(Item item) {
            // Find subject in the list
            int index = mItemList.indexOf(item);
            if (index >= 0) {
                // Remove the subject
                mItemList.remove(index);

                // Notify adapter of subject removal
                notifyItemRemoved(index);
            }

        }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            return new ItemHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ItemHolder<Item> holder, int position){
            holder.bind(mItemList.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mItemList.size();
        }
    }

}