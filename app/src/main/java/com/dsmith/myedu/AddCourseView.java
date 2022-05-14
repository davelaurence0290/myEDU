package com.dsmith.myedu;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddCourseView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCourseView extends Fragment {

    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String START_DATE = "startDate";
    private static final String END_DATE = "endDate";
    private static final String STATUS = "status";
    private static final String INSTRUCTOR_ID = "instructorId";
    private static final String TERM_ID = "termId";

    private int mId;
    private String mTitle;
    private String mDescription;
    private LocalDate mStartDate;
    private LocalDate mEndDate;
    private int mInstructorId;
    private Course.Status mStatus;
    private int mTermId;

    private TextView mTitleView;
    private EditText mDescriptionView;
    private ArrayAdapter<Course.Status> mStatusAdapter;
    private Spinner mStatusSpinner;
    private ArrayAdapter<Instructor> mInstructorAdapter;
    private Spinner mInstructorSpinner;
    private NumberPicker mStartMonthPicker;
    private NumberPicker mStartDayPicker;
    private NumberPicker mStartYearPicker;
    private NumberPicker mEndMonthPicker;
    private NumberPicker mEndDayPicker;
    private NumberPicker mEndYearPicker;

    private Button mSaveButton;
    private Button mCancelButton;

    private CourseSaveFinishedListener mSavedListener;

    // Show 2 digits in NumberPickers
    NumberPicker.Formatter numFormat = new NumberPicker.Formatter() {
        @Override
        public String format(int i) {
            return new DecimalFormat("00").format(i);
        }
    };

    public AddCourseView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param course The course to edit
     * @return A new instance of fragment editCourseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddCourseView newInstance(Course course) {
        AddCourseView fragment = new AddCourseView();
        Bundle args = new Bundle();
        args.putInt(ID, course.getCourseId());
        args.putString(TITLE, course.getTitle());
        args.putString(DESCRIPTION, course.getDescription());
        args.putString(START_DATE, course.getStartDate().toString());
        args.putString(END_DATE, course.getEndDate().toString());
        args.putString(STATUS, course.getStatus().toString());
        args.putInt(INSTRUCTOR_ID, course.getInstructorId());
        args.putInt(TERM_ID, course.getTermId());

        fragment.setArguments(args);
        return fragment;
    }

    public static AddCourseView newInstance() {
        AddCourseView fragment = new AddCourseView();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getInt(ID);
            mTitle = getArguments().getString(TITLE);
            mDescription = getArguments().getString(DESCRIPTION);
            mStartDate = LocalDate.parse(getArguments().getString(START_DATE));
            mEndDate = LocalDate.parse(getArguments().getString(END_DATE));
            mStatus = Course.getStatusFromString(getArguments().getString(STATUS));
            mInstructorId = getArguments().getInt(INSTRUCTOR_ID);
            mTermId = getArguments().getInt(TERM_ID);
        }
        if (savedInstanceState != null){
            mTermId = savedInstanceState.getInt(TERM_ID);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putInt(TERM_ID, mTermId);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_course, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CourseSaveFinishedListener) {
            mSavedListener = (CourseSaveFinishedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CourseSaveFinishedListener");
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        mTitleView = view.findViewById(R.id.addCourseTitleText);
        mTitleView.setOnFocusChangeListener(new FieldFocusChangeListener());

        mDescriptionView = view.findViewById(R.id.addCourseDescriptionText);
        mDescriptionView.setOnFocusChangeListener(new FieldFocusChangeListener());

        LocalDate currTermStartDate = MainActivity.userSchedule.getTerm(mTermId).getStartDate();
        int currYear = currTermStartDate.getYear();

        mStartMonthPicker = view.findViewById(R.id.addCourseStartMonthPicker);
        mStartMonthPicker.setMaxValue(12);
        mStartMonthPicker.setMinValue(1);
        mStartMonthPicker.setFormatter(numFormat);
        mStartMonthPicker.setValue(currTermStartDate.getMonthValue());
        mStartDayPicker = view.findViewById(R.id.addCourseStartDayPicker);
        mStartDayPicker.setMaxValue(31);
        mStartDayPicker.setMinValue(1);
        mStartDayPicker.setFormatter(numFormat);
        mStartDayPicker.setValue(currTermStartDate.getDayOfMonth());
        mStartYearPicker = view.findViewById(R.id.addCourseStartYearPicker);
        mStartYearPicker.setMaxValue(currYear + 4);
        mStartYearPicker.setMinValue(currYear - 1);
        mStartYearPicker.setValue(currYear);


        mEndMonthPicker = view.findViewById(R.id.addCourseEndMonthPicker);
        mEndMonthPicker.setMaxValue(12);
        mEndMonthPicker.setMinValue(1);
        mEndMonthPicker.setFormatter(numFormat);
        mEndMonthPicker.setValue(currTermStartDate.getMonthValue());
        mEndDayPicker = view.findViewById(R.id.addCourseEndDayPicker);
        mEndDayPicker.setMaxValue(31);
        mEndDayPicker.setMinValue(1);
        mEndDayPicker.setFormatter(numFormat);
        mEndDayPicker.setValue(currTermStartDate.getDayOfMonth());
        mEndYearPicker = view.findViewById(R.id.addCourseEndYearPicker);
        mEndYearPicker.setMaxValue(currYear + 4);
        mEndYearPicker.setMinValue(currYear - 1);
        mEndYearPicker.setValue(currYear);

        mStatusSpinner = view.findViewById(R.id.courseStatusSpinner);
        mStatusAdapter = new ArrayAdapter<>(this.getContext(),
                android.R.layout.simple_spinner_item,
                Course.Status.values());
        mStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item );
        mStatusSpinner.setAdapter(mStatusAdapter);

        mInstructorSpinner = getActivity().findViewById(R.id.courseInstructorSpinner);
        updateInstructorSpinner();

        mSaveButton = view.findViewById(R.id.addCourseSaveButton);
        mCancelButton = view.findViewById(R.id.addCourseCancelButton);

        if(getArguments() != null){
            mTitleView.setText(mTitle);
            mDescriptionView.setText(mDescription);
            mStartMonthPicker.setValue(mStartDate.getMonthValue());
            mStartDayPicker.setValue(mStartDate.getDayOfMonth());
            mStartYearPicker.setValue(mStartDate.getYear());

            mEndMonthPicker.setValue(mEndDate.getMonthValue());
            mEndDayPicker.setValue(mEndDate.getDayOfMonth());
            mEndYearPicker.setValue(mEndDate.getYear());

            mStatusSpinner.setSelection(mStatusAdapter.getPosition(mStatus));
            mInstructorSpinner.setSelection(mInstructorAdapter.getPosition(
                    MainActivity.userSchedule.getInstructor(mInstructorId)
            ));

        }
    }

    @Override
    public void onStart(){
        super.onStart();

        updateInstructorSpinner();
    }

    private void updateInstructorSpinner(){
        //refresh list of instructors
        mInstructorAdapter = new ArrayAdapter<>(this.getContext(),
                android.R.layout.simple_spinner_item,
                MainActivity.userSchedule.getAllInstructors());
        mInstructorSpinner.setAdapter(mInstructorAdapter);
    }
    /*
    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDimensionPixelSize(R.dimen.addCourseWidth);
        int height = getResources().getDimensionPixelSize(R.dimen.addCourseHeight);
        getDialog().getWindow().setLayout(width, height);
    }
     */

    public void save(){
        mTitle = mTitleView.getText().toString();
        mDescription = mDescriptionView.getText().toString();
        mStatus = (Course.Status)mStatusSpinner.getSelectedItem();
        mInstructorId = ((Instructor)mInstructorSpinner.getSelectedItem()).getInstructorId();

        try{
            mStartDate = LocalDate.of(mStartYearPicker.getValue(),
                    mStartMonthPicker.getValue(),
                    mStartDayPicker.getValue());
            mEndDate = LocalDate.of(mEndYearPicker.getValue(),
                    mEndMonthPicker.getValue(),
                    mEndDayPicker.getValue());
            //check if dates are acceptable for current term, throws IllegalArgumentException
            Term currentTerm = MainActivity.userSchedule.getTerm(mTermId);
            currentTerm.checkCourseDates(mStartDate,mEndDate);

            //if saving new course, create it
            if(getArguments() == null){
                MainActivity.userSchedule.addCourse(mTitle,mDescription,mStartDate,mEndDate,
                        mStatus,mInstructorId,mTermId);
                Toast.makeText(getActivity(), "Course " + mTitle + " added.",Toast.LENGTH_SHORT).show();
            }
            // updating existing course.
            else {
                MainActivity.userSchedule.updateCourse(mId, mTitle,mDescription,mStartDate,mEndDate,
                        mStatus,mInstructorId,mTermId);
            }
            mSavedListener.onCourseSaveFinished();

        }
        catch (DateTimeException dte){
            Toast.makeText(getActivity(), dte.getMessage(),Toast.LENGTH_SHORT).show();
        }
        catch (IllegalArgumentException e){
            Toast.makeText(getActivity(), e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public interface CourseSaveFinishedListener{
        //used to notify CourseView to remove addCourse fragment.
        public void onCourseSaveFinished();
    }

    private class FieldFocusChangeListener implements View.OnFocusChangeListener {

        public void onFocusChange(View v, boolean hasFocus){

            if(!hasFocus) {
                InputMethodManager imm =  (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        }
    }

    public void setTermId(int id){
        mTermId = id;
    }
}