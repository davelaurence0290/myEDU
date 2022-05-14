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
public class InstructorDetailsView extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String NAME = "instructorName";
    private static final String DESCRIPTION = "instructorDescription";
    private static final String ID = "instructorId";

    private String mInstructorName;
    private String mInstructorDescription;
    private long mInstructorId;


    private TextView mInstructorNameView;
    private TextView mInstructorDescriptionView;

    public InstructorDetailsView() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param instructor the Instructor.
     * @return A new instance of fragment InstructorDetailsView.
     */
    // TODO: Rename and change types and number of parameters
    public static InstructorDetailsView newInstance(Instructor instructor) {
        InstructorDetailsView fragment = new InstructorDetailsView();
        Bundle args = new Bundle();
        if (instructor != null){
            args.putString(NAME, instructor.getName());
            args.putString(DESCRIPTION, instructor.getDetailString());
            args.putLong(ID, instructor.getInstructorId());
        }
        else{
            args.putString(NAME, "--");
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

            mInstructorName = getArguments().getString(NAME);
            mInstructorDescription = getArguments().getString(DESCRIPTION);
            mInstructorId = getArguments().getLong(ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_instructor_details_view, container, false);
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        mInstructorNameView = view.findViewById(R.id.instructorName);
        mInstructorDescriptionView = view.findViewById(R.id.instructorDescription);
    }

    public void setItemInfo(Instructor instructor){
        if(instructor != null){
            mInstructorName = instructor.getName();
            mInstructorDescription = instructor.getDetailString();
            mInstructorId = instructor.getInstructorId();
        }
        else{
            mInstructorName = getResources().getString(R.string.blank);
            mInstructorDescription = getResources().getString(R.string.blank);
            mInstructorId = 0;
        }

        mInstructorNameView.setText(mInstructorName);
        mInstructorDescriptionView.setText(mInstructorDescription);
    }




}