package com.dsmith.myedu;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.NumberPicker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.time.DateTimeException;
import java.time.LocalDate;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddTermView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddTermView extends Fragment {

    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String START_DATE = "startDate";
    private static final String END_DATE = "endDate";

    private int mId;
    private String mTitle;
    private LocalDate mStartDate;
    private LocalDate mEndDate;

    private TextView mTitleView;
    private NumberPicker mStartMonthPicker;
    private NumberPicker mStartDayPicker;
    private NumberPicker mStartYearPicker;
    private NumberPicker mEndMonthPicker;
    private NumberPicker mEndDayPicker;
    private NumberPicker mEndYearPicker;

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

    public AddTermView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param term The term to edit
     * @return A new instance of fragment editTermFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddTermView newInstance(Term term) {
        AddTermView fragment = new AddTermView();
        Bundle args = new Bundle();
        args.putInt(ID, term.getTermId());
        args.putString(TITLE, term.getTitle());
        args.putString(START_DATE, term.getStartDate().toString());
        args.putString(END_DATE, term.getEndDate().toString());

        fragment.setArguments(args);
        return fragment;
    }

    public static AddTermView newInstance() {
        AddTermView fragment = new AddTermView();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getInt(ID);
            mTitle = getArguments().getString(TITLE);
            mStartDate = LocalDate.parse(getArguments().getString(START_DATE));
            mEndDate = LocalDate.parse(getArguments().getString(END_DATE));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_term, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddTermView.SaveFinishedListener) {
            mSavedListener = (AddTermView.SaveFinishedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AddTermView.SaveFinishedListener");
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        mTitleView = view.findViewById(R.id.addTermTitleText);
        mTitleView.setOnFocusChangeListener(new FieldFocusChangeListener());

        int currYear = LocalDate.now().getYear();

        mStartMonthPicker = view.findViewById(R.id.addTermStartMonthPicker);
        mStartMonthPicker.setMaxValue(12);
        mStartMonthPicker.setMinValue(1);
        mStartMonthPicker.setFormatter(numFormat);
        mStartDayPicker = view.findViewById(R.id.addTermStartDayPicker);
        mStartDayPicker.setMaxValue(31);
        mStartDayPicker.setMinValue(1);
        mStartDayPicker.setFormatter(numFormat);
        mStartYearPicker = view.findViewById(R.id.addTermStartYearPicker);
        mStartYearPicker.setMaxValue(currYear + 4);
        mStartYearPicker.setMinValue(currYear - 1);
        mStartYearPicker.setValue(currYear);


        mEndMonthPicker = view.findViewById(R.id.addTermEndMonthPicker);
        mEndMonthPicker.setMaxValue(12);
        mEndMonthPicker.setMinValue(1);
        mEndMonthPicker.setFormatter(numFormat);
        mEndDayPicker = view.findViewById(R.id.addTermEndDayPicker);
        mEndDayPicker.setMaxValue(31);
        mEndDayPicker.setMinValue(1);
        mEndDayPicker.setFormatter(numFormat);
        mEndYearPicker = view.findViewById(R.id.addTermEndYearPicker);
        mEndYearPicker.setMaxValue(currYear + 4);
        mEndYearPicker.setMinValue(currYear - 1);
        mEndYearPicker.setValue(currYear);

        mSaveButton = view.findViewById(R.id.addTermSaveButton);
        mCancelButton = view.findViewById(R.id.addTermCancelButton);

        if(getArguments() != null){
            mTitleView.setText(mTitle);

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
        int width = getResources().getDimensionPixelSize(R.dimen.addTermWidth);
        int height = getResources().getDimensionPixelSize(R.dimen.addTermHeight);
        getDialog().getWindow().setLayout(width, height);
    }
     */

    public void save(){
        mTitle = mTitleView.getText().toString();

        try{
            //check if dates are acceptable for current term
            mStartDate = LocalDate.of(mStartYearPicker.getValue(),
                    mStartMonthPicker.getValue(),
                    mStartDayPicker.getValue());
            mEndDate = LocalDate.of(mEndYearPicker.getValue(),
                    mEndMonthPicker.getValue(),
                    mEndDayPicker.getValue());

            if(getArguments() == null){
                MainActivity.userSchedule.addTerm(mTitle,mStartDate,mEndDate);
                Toast.makeText(getActivity(), "Term " + mTitle + " added.",Toast.LENGTH_SHORT).show();
            }
            else {
                MainActivity.userSchedule.updateTerm(mId, mTitle, mStartDate, mEndDate);
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

    public interface SaveFinishedListener{
        //used to notify TermView to remove addTerm fragment.
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