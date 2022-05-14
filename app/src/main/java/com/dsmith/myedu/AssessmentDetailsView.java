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
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TermDetailsView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssessmentDetailsView extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String ID = "assessmentId";

    private String mAssessmentName;
    private String mAssessmentDescription;
    private long mAssessmentId;

    private TextView mAssessmentNameView;
    private TextView mAssessmentDescriptionView;

    private ImageButton mSetAssessmentAlertsButton;

    public AssessmentDetailsView() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param assessment the Assessment.
     * @return A new instance of fragment AssessmentDetailsView.
     */
    // TODO: Rename and change types and number of parameters
    public static AssessmentDetailsView newInstance(Assessment assessment) {
        AssessmentDetailsView fragment = new AssessmentDetailsView();
        Bundle args = new Bundle();
        if (assessment != null){
            args.putString(NAME, assessment.getName());
            args.putString(DESCRIPTION, assessment.getDetailString());
            args.putLong(ID, assessment.getAssessmentId());
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
            mAssessmentName = getArguments().getString(NAME);
            mAssessmentDescription = getArguments().getString(DESCRIPTION);
            mAssessmentId = getArguments().getLong(ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_assessment_details_view, container, false);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        mAssessmentNameView = view.findViewById(R.id.assessmentTitle);
        mAssessmentDescriptionView = view.findViewById(R.id.assessmentDescription);

        mSetAssessmentAlertsButton = view.findViewById(R.id.setAssessmentAlertsButton);
        mSetAssessmentAlertsButton.setVisibility(View.INVISIBLE);
    }

    public void setItemInfo(Assessment assessment){
        if(assessment != null){
            mAssessmentName = assessment.getName();
            mAssessmentDescription = assessment.getDetailString();
            mAssessmentId = assessment.getAssessmentId();

            mSetAssessmentAlertsButton.setVisibility(View.VISIBLE);
        }
        else{
            mAssessmentName = getResources().getString(R.string.blank);
            mAssessmentDescription = getResources().getString(R.string.blank);
            mAssessmentId = 0;

            mSetAssessmentAlertsButton.setVisibility(View.INVISIBLE);
        }

        mAssessmentNameView.setText(mAssessmentName);
        mAssessmentDescriptionView.setText(mAssessmentDescription);
    }
}