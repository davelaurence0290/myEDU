package com.dsmith.myedu;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.time.DateTimeException;
import java.time.LocalDate;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddInstructorView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddInstructorView extends Fragment {

    private static final String ID = "id";
    private static final String NAME = "title";
    private static final String EMAIL = "startDate";
    private static final String PHONE = "endDate";

    private int mId;
    private String mName;
    private String mEmail;
    private String mPhone;

    private TextView mNameView;
    private TextView mEmailView;
    private TextView mPhoneView;

    private Button mSaveButton;
    private Button mCancelButton;

    private SaveFinishedListener mSavedListener;

    public AddInstructorView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param instructor The instructor to edit
     * @return A new instance of fragment editInstructorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddInstructorView newInstance(Instructor instructor) {
        AddInstructorView fragment = new AddInstructorView();
        Bundle args = new Bundle();
        args.putInt(ID, instructor.getInstructorId());
        args.putString(NAME, instructor.getName());
        args.putString(EMAIL, instructor.getEmail());
        args.putString(PHONE, instructor.getPhone());

        fragment.setArguments(args);
        return fragment;
    }

    public static AddInstructorView newInstance() {
        AddInstructorView fragment = new AddInstructorView();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getInt(ID);
            mName = getArguments().getString(NAME);
            mEmail = getArguments().getString(EMAIL);
            mPhone = getArguments().getString(PHONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_instructor, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddInstructorView.SaveFinishedListener) {
            mSavedListener = (AddInstructorView.SaveFinishedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AddInstructorView.SaveFinishedListener");
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        mNameView = view.findViewById(R.id.addInstructorNameText);
        mNameView.setOnFocusChangeListener(new FieldFocusChangeListener());

        mEmailView = view.findViewById(R.id.addInstructorEmailText);
        mEmailView.setOnFocusChangeListener(new FieldFocusChangeListener());

        mPhoneView = view.findViewById(R.id.addInstructorPhoneText);
        mPhoneView.setOnFocusChangeListener(new FieldFocusChangeListener());

        mSaveButton = view.findViewById(R.id.addInstructorSaveButton);
        mCancelButton = view.findViewById(R.id.addInstructorCancelButton);

        if(getArguments() != null){
            mNameView.setText(mName);
            mEmailView.setText(mEmail);
            mPhoneView.setText(mPhone);
        }
    }

    public void save(){
        mName = mNameView.getText().toString();
        mEmail = mEmailView.getText().toString();
        mPhone = mPhoneView.getText().toString();

        try{

            if(getArguments() == null){
                MainActivity.userSchedule.addInstructor(mName,mEmail,mPhone);
                Toast.makeText(getActivity(), "Instructor " + mName + " added.",Toast.LENGTH_SHORT).show();
            }
            else {
                MainActivity.userSchedule.updateInstructor(mId, mName, mEmail, mPhone);
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
        //used to notify InstructorView to remove addInstructor fragment.
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