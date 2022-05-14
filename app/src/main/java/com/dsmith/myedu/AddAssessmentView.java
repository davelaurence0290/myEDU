package com.dsmith.myedu;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddAssessmentView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddAssessmentView extends Fragment {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String TYPE = "type";
    private static final String START_DATE = "startDate";
    private static final String END_DATE = "endDate";
    private static final String COURSE_ID = "courseId";

    private int mId;
    private String mName;
    private Assessment.AssessmentType mType;
    private LocalDate mStartDate;
    private LocalDate mEndDate;
    private int mCourseId;

    private TextView mNameView;
    private NumberPicker mStartMonthPicker;
    private NumberPicker mStartDayPicker;
    private NumberPicker mStartYearPicker;
    private NumberPicker mEndMonthPicker;
    private NumberPicker mEndDayPicker;
    private NumberPicker mEndYearPicker;

    private Spinner mAssessmentTypeSpinner;
    private ArrayAdapter<Assessment.AssessmentType> mTypeAdapter;

    private Button mSaveButton;
    private Button mCancelButton;

    private SaveFinishedListener mSavedListener;

    // Show 2 digits in NumberPickers
    NumberPicker.Formatter numFormat = new NumberPicker.Formatter() {
        @Override
        public String format(int i) {
            return new DecimalFormat("00").format(i);
        }
    };

    public AddAssessmentView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param assessment The assessment to edit
     * @return A new instance of fragment editAssessmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddAssessmentView newInstance(Assessment assessment) {
        AddAssessmentView fragment = new AddAssessmentView();
        Bundle args = new Bundle();
        args.putInt(ID, assessment.getAssessmentId());
        args.putString(NAME, assessment.getName());
        args.putString(TYPE, assessment.getType().toString());
        args.putString(START_DATE, assessment.getStartDate().toString());
        args.putString(END_DATE, assessment.getEndDate().toString());
        args.putInt(COURSE_ID, assessment.getCourseId());

        fragment.setArguments(args);
        return fragment;
    }

    public static AddAssessmentView newInstance() {
        AddAssessmentView fragment = new AddAssessmentView();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getInt(ID);
            mName = getArguments().getString(NAME);
            mType = Assessment.getTypeFromString(getArguments().getString(TYPE));
            mStartDate = LocalDate.parse(getArguments().getString(START_DATE));
            mEndDate = LocalDate.parse(getArguments().getString(END_DATE));
            mCourseId = getArguments().getInt(COURSE_ID);
        }if (savedInstanceState != null){
            mCourseId = savedInstanceState.getInt(COURSE_ID);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putInt(COURSE_ID, mCourseId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_assessment, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddAssessmentView.SaveFinishedListener) {
            mSavedListener = (AddAssessmentView.SaveFinishedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AddAssessmentView.SaveFinishedListener");
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        mNameView = view.findViewById(R.id.addAssessmentNameText);
        mNameView.setOnFocusChangeListener(new FieldFocusChangeListener());

        LocalDate currCourseStartDate = MainActivity.userSchedule.getCourse(mCourseId).getStartDate();
        int currYear = currCourseStartDate.getYear();

        mStartMonthPicker = view.findViewById(R.id.addAssessmentStartMonthPicker);
        mStartMonthPicker.setMaxValue(12);
        mStartMonthPicker.setMinValue(1);
        mStartMonthPicker.setFormatter(numFormat);
        mStartMonthPicker.setValue(currCourseStartDate.getMonthValue());
        mStartDayPicker = view.findViewById(R.id.addAssessmentStartDayPicker);
        mStartDayPicker.setMaxValue(31);
        mStartDayPicker.setMinValue(1);
        mStartDayPicker.setFormatter(numFormat);
        mStartDayPicker.setValue(currCourseStartDate.getDayOfMonth());
        mStartYearPicker = view.findViewById(R.id.addAssessmentStartYearPicker);
        mStartYearPicker.setMaxValue(currYear + 4);
        mStartYearPicker.setMinValue(currYear - 1);
        mStartYearPicker.setValue(currYear);


        mEndMonthPicker = view.findViewById(R.id.addAssessmentEndMonthPicker);
        mEndMonthPicker.setMaxValue(12);
        mEndMonthPicker.setMinValue(1);
        mEndMonthPicker.setFormatter(numFormat);
        mEndMonthPicker.setValue(currCourseStartDate.getMonthValue());
        mEndDayPicker = view.findViewById(R.id.addAssessmentEndDayPicker);
        mEndDayPicker.setMaxValue(31);
        mEndDayPicker.setMinValue(1);
        mEndDayPicker.setFormatter(numFormat);
        mEndDayPicker.setValue(currCourseStartDate.getDayOfMonth());
        mEndYearPicker = view.findViewById(R.id.addAssessmentEndYearPicker);
        mEndYearPicker.setMaxValue(currYear + 4);
        mEndYearPicker.setMinValue(currYear - 1);
        mEndYearPicker.setValue(currYear);

        mAssessmentTypeSpinner = view.findViewById(R.id.assessmentTypeSpinner);
        mTypeAdapter = new ArrayAdapter<>(this.getContext(),
                android.R.layout.simple_spinner_item,
                Assessment.AssessmentType.values());
        mTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item );
        mAssessmentTypeSpinner.setAdapter(mTypeAdapter);

        mSaveButton = view.findViewById(R.id.addAssessmentSaveButton);
        mCancelButton = view.findViewById(R.id.addAssessmentCancelButton);

        if(getArguments() != null){
            mNameView.setText(mName);

            mAssessmentTypeSpinner.setSelection(mTypeAdapter.getPosition(mType));

            mStartMonthPicker.setValue(mStartDate.getMonthValue());
            mStartDayPicker.setValue(mStartDate.getDayOfMonth());
            mStartYearPicker.setValue(mStartDate.getYear());

            mEndMonthPicker.setValue(mEndDate.getMonthValue());
            mEndDayPicker.setValue(mEndDate.getDayOfMonth());
            mEndYearPicker.setValue(mEndDate.getYear());
        }
    }
    /*
    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDimensionPixelSize(R.dimen.addAssessmentWidth);
        int height = getResources().getDimensionPixelSize(R.dimen.addAssessmentHeight);
        getDialog().getWindow().setLayout(width, height);
    }
     */

    public void save(){
        mName = mNameView.getText().toString();

        mType = (Assessment.AssessmentType)mAssessmentTypeSpinner.getSelectedItem();

        try{
            mStartDate = LocalDate.of(mStartYearPicker.getValue(),
                    mStartMonthPicker.getValue(),
                    mStartDayPicker.getValue());
            mEndDate = LocalDate.of(mEndYearPicker.getValue(),
                    mEndMonthPicker.getValue(),
                    mEndDayPicker.getValue());
            //check if dates are acceptable for current course
            Course currentCourse = MainActivity.userSchedule.getCourse(mCourseId);
            currentCourse.checkAssessmentDates(mStartDate,mEndDate);

            if(getArguments() == null){
                MainActivity.userSchedule.addAssessment(mName,mType,mStartDate,mEndDate,mCourseId);
                Toast.makeText(getActivity(), "Assessment " + mName + " added.",Toast.LENGTH_SHORT).show();
            }
            else {
                MainActivity.userSchedule.updateAssessment(mId, mName,mType,mStartDate,mEndDate,mCourseId);
            }
            mSavedListener.onSaveFinished();

        }
        catch (DateTimeException dte){
            Toast.makeText(getActivity(), dte.getMessage(),Toast.LENGTH_SHORT).show();
        }
        catch (IllegalArgumentException e){
            Toast.makeText(getActivity(), e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public void setCourseId(int id){
        mCourseId = id;
    }

    public interface SaveFinishedListener{
        //used to notify AssessmentView to remove addAssessment fragment.
        public void onSaveFinished();
    }

    private class FieldFocusChangeListener implements View.OnFocusChangeListener {

        public void onFocusChange(View v, boolean hasFocus){

            if(!hasFocus) {
                InputMethodManager imm =  (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        }
    }
}