package com.dsmith.myedu;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link DialogFragment} subclass.
 * Use the {@link AddNoteView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNoteView extends DialogFragment {

    private static final String ID = "id";
    private static final String TEXT = "text";
    private static final String COURSE_ID = "courseId";

    private int mId;
    private String mText;
    private int mCourseId;

    private NoteSaveFinishedListener mSavedListener;
    private EditText mNoteText;

    private Button mNoteSaveButton;
    private Button mNoteCancelButton;


    public AddNoteView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param note the note to edit, if present.
     * @return A new instance of fragment AddNoteView.
     */
    // TODO: Rename and change types and number of parameters
    public static AddNoteView newInstance(Note note) {
        AddNoteView fragment = new AddNoteView();
        Bundle args = new Bundle();
        args.putInt(ID, note.getNoteId());
        args.putString(TEXT, note.getText());
        args.putInt(COURSE_ID, note.getCourseId());
        fragment.setArguments(args);
        return fragment;
    }

    public static AddNoteView newInstance() {
        AddNoteView fragment = new AddNoteView();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getInt(ID);
            mText = getArguments().getString(TEXT);
            mCourseId = getArguments().getInt(COURSE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_note, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNoteText = view.findViewById(R.id.addNoteText);
        mNoteSaveButton = view.findViewById(R.id.addNoteSaveButton);
        mNoteCancelButton = view.findViewById(R.id.addNoteCancelButton);

        if(getArguments() != null){
            mNoteText.setText(mText);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NoteSaveFinishedListener) {
            mSavedListener = (NoteSaveFinishedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement NoteSaveFinishedListener");
        }
    }

    public void save(){
        mText = mNoteText.getText().toString();

        try{
            //if saving new course, create it
            if(getArguments() == null){
                MainActivity.userSchedule.addNote(mText,mCourseId);
                Toast.makeText(getActivity(), "Note added.",Toast.LENGTH_SHORT).show();
            }
            // updating existing course.
            else {
                MainActivity.userSchedule.updateNote(mId, mText,mCourseId);
            }
            mSavedListener.onNoteSaveFinished();

        }
        catch (IllegalArgumentException e){
            Toast.makeText(getActivity(), e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public interface NoteSaveFinishedListener{
        //used to notify CourseView to remove addCourse fragment.
        public void onNoteSaveFinished();
    }

    public void setCourseId(int id){
        mCourseId = id;
    }
}