package com.dsmith.myedu;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TermDetailsView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TermDetailsView extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TITLE = "termTitle";
    private static final String DESCRIPTION = "termDescription";
    private static final String ID = "termId";

    private String mTermTitle;
    private String mTermDescription;
    private long mTermId;
    private ArrayList<Course> mCourseList;

    private RecyclerView mTermCourseListView;
    private CourseAdapter mCourseAdapter;

    private TextView mTermTitleView;
    private TextView mTermDescriptionView;
    private Button mEditTermCoursesButton;

    public TermDetailsView() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param term the Term.
     * @return A new instance of fragment TermDetailsView.
     */
    // TODO: Rename and change types and number of parameters
    public static TermDetailsView newInstance(Term term) {
        TermDetailsView fragment = new TermDetailsView();
        Bundle args = new Bundle();
        if (term != null){
            args.putString(TITLE, term.getTitle());
            args.putString(DESCRIPTION, term.getDetailString());
            args.putLong(ID, term.getTermId());
        }
        else{
            args.putString(TITLE, "--");
            args.putString(DESCRIPTION, "--");
            args.putLong(ID, 0);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            mTermTitle = getArguments().getString(TITLE);
            mTermDescription = getArguments().getString(DESCRIPTION);
            mTermId = getArguments().getLong(ID);

            mCourseList = new ArrayList<>(MainActivity.userSchedule.getAllCourses().stream()
                    .filter(c -> c.getTermId() == mTermId)
                    .collect(Collectors.toList())
            );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_term_details_view, container, false);

        mTermCourseListView = rootView.findViewById(R.id.termViewCourseList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        mTermCourseListView.setLayoutManager(layoutManager);

        mCourseAdapter = new CourseAdapter(mCourseList);
        mTermCourseListView.setAdapter(mCourseAdapter);
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        mTermTitleView = view.findViewById(R.id.termTitle);
        mTermDescriptionView = view.findViewById(R.id.termDescription);
        mEditTermCoursesButton = view.findViewById(R.id.editTermCoursesButton);
        mEditTermCoursesButton.setVisibility(View.INVISIBLE);
    }

    public void setItemInfo(Term term){
        if (term != null) {
            mTermTitle = term.getTitle();
            mTermDescription = term.getDetailString();
            mTermId = term.getTermId();

            mCourseList = new ArrayList<>(MainActivity.userSchedule.getAllCourses().stream()
                    .filter(c -> c.getTermId() == mTermId)
                    .collect(Collectors.toList())
            );
            //Make button visible, now that term is selected.
            mEditTermCoursesButton.setVisibility(View.VISIBLE);
        }
        else{
            mTermTitle = getResources().getString(R.string.blank);
            mTermDescription = getResources().getString(R.string.blank);
            mTermId = 0;

            mCourseList = new ArrayList<>();
            //Make button visible, now that term is selected.
            mEditTermCoursesButton.setVisibility(View.INVISIBLE);
        }


        mTermTitleView.setText(mTermTitle);
        mTermDescriptionView.setText(mTermDescription);

        mCourseAdapter.updateCourses(mCourseList);
    }

    private class CourseHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{

        private Course mCourse;
        private TextView mTextView;

        public CourseHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.recycler_detail_view_items, parent, false));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mTextView = itemView.findViewById(R.id.itemTextView);
        }

        public void bind(Course course, int position) {
            mCourse = course;
            mTextView.setText(course.getTitle());
            mTextView.setBackgroundColor(Color.WHITE);

            /*if( mSelectedCoursePosition == position){
                // Make selected subject stand out
                mTextView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.teal_200));
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

    private class CourseAdapter extends RecyclerView.Adapter<CourseHolder> {

        private ArrayList<Course> mCourseList;

        public CourseAdapter(ArrayList<Course> courses) {
            mCourseList = courses;
        }

        public void updateCourses(ArrayList<Course> courses){
            mCourseList = courses;
            notifyDataSetChanged();
        }

        public void addCourse(Course course) {
            // Add the new subject at the beginning of the list
            mCourseList.add(0, course);

            // Notify the adapter that item was added to the beginning of the list
            notifyItemInserted(0);

            // Scroll to the top
            mTermCourseListView.scrollToPosition(0);
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

        @Override
        public CourseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
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