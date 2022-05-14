package com.dsmith.myedu;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.stream.Collectors;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseDetailsView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseDetailsView extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TITLE = "courseTitle";
    private static final String DESCRIPTION = "courseDescription";
    private static final String ID = "courseId";

    private String mCourseTitle;
    private String mCourseDescription;
    private long mCourseId;
    private ArrayList<Assessment> mAssessmentList;
    private ArrayList<Note> mNoteList;

    private RecyclerView mCourseAssessmentListView;
    private ItemAdapter<Assessment> mAssessmentAdapter;

    private RecyclerView mCourseNoteListView;
    private ItemAdapter<Note> mNoteAdapter;

    private TextView mCourseTitleView;
    private TextView mCourseDescriptionView;
    private Button mEditCourseAssessmentsButton;
    private ImageButton mAddNoteButton;
    private ImageButton mEditNoteButton;
    private ImageButton mShareNotesButton;
    private ImageButton mSetCourseAlertsButton;


    private int mSelectedItemPosition = RecyclerView.NO_POSITION;
    private NoteSelectedListener mNoteListener;

    public CourseDetailsView() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param course the Course.
     * @return A new instance of fragment CourseDetailsView.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseDetailsView newInstance(Course course) {
        CourseDetailsView fragment = new CourseDetailsView();
        Bundle args = new Bundle();
        if (course != null){
            args.putString(TITLE, course.getTitle());
            args.putString(DESCRIPTION, course.getDetailString());
            args.putLong(ID, course.getCourseId());
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

            mCourseTitle = getArguments().getString(TITLE);
            mCourseDescription = getArguments().getString(DESCRIPTION);
            mCourseId = getArguments().getLong(ID);

            mAssessmentList = MainActivity.userSchedule.getAssessments((int)mCourseId);
            mNoteList = MainActivity.userSchedule.getNotes((int)mCourseId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_course_details_view, container, false);

        //set up Assessment List
        mCourseAssessmentListView = rootView.findViewById(R.id.courseViewAssessmentList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        mCourseAssessmentListView.setLayoutManager(layoutManager);

        mAssessmentAdapter = new ItemAdapter<>(mAssessmentList);
        mCourseAssessmentListView.setAdapter(mAssessmentAdapter);

        //set up Note list
        mCourseNoteListView = rootView.findViewById(R.id.courseViewNoteList);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        mCourseNoteListView.setLayoutManager(layoutManager2);

        mNoteAdapter = new ItemAdapter<>(mNoteList);
        mCourseNoteListView.setAdapter(mNoteAdapter);
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        mCourseTitleView = view.findViewById(R.id.courseTitle);
        mCourseDescriptionView = view.findViewById(R.id.courseDescription);

        mEditCourseAssessmentsButton = view.findViewById(R.id.editCourseAssessmentsButton);
        mEditCourseAssessmentsButton.setVisibility(View.INVISIBLE);

        mAddNoteButton = view.findViewById(R.id.addNoteButton);
        mAddNoteButton.setVisibility(View.INVISIBLE);

        mEditNoteButton = view.findViewById(R.id.editNoteButton);
        mEditNoteButton.setVisibility(View.INVISIBLE);

        mShareNotesButton = view.findViewById(R.id.shareNotesButton);
        mShareNotesButton.setVisibility(View.INVISIBLE);

        mSetCourseAlertsButton = view.findViewById(R.id.setCourseAlertsButton);
        mSetCourseAlertsButton.setVisibility(View.INVISIBLE);
    }


    public void updateAdapters(){
        mAssessmentList = MainActivity.userSchedule.getAssessments((int)mCourseId);
        mNoteList = MainActivity.userSchedule.getNotes((int)mCourseId);

        mAssessmentAdapter.notifyDataSetChanged();
        mNoteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NoteSelectedListener) {
            mNoteListener = (NoteSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement NoteSelectedListener");
        }
    }

    public void setItemInfo(Course course) {
        mSelectedItemPosition = RecyclerView.NO_POSITION;

        if (course != null) {
            mCourseTitle = course.getTitle();
            mCourseDescription = course.getDetailString();
            mCourseId = course.getCourseId();

            mCourseTitleView.setText(mCourseTitle);
            mCourseDescriptionView.setText(mCourseDescription);
            mAssessmentList = new ArrayList<>(MainActivity.userSchedule.getAllAssessments().stream()
                    .filter(a -> a.getCourseId() == mCourseId)
                    .collect(Collectors.toList())
            );
            mNoteList = new ArrayList<>(MainActivity.userSchedule.getAllNotes().stream()
                    .filter(n -> n.getCourseId() == mCourseId)
                    .collect(Collectors.toList())
            );

            mEditCourseAssessmentsButton.setVisibility(View.VISIBLE);
            mAddNoteButton.setVisibility(View.VISIBLE);
            mEditNoteButton.setVisibility(View.VISIBLE);
            mShareNotesButton.setVisibility(View.VISIBLE);
            mSetCourseAlertsButton.setVisibility(View.VISIBLE);
        }
        else{
            mCourseTitle = getResources().getString(R.string.blank);
            mCourseDescription = getResources().getString(R.string.blank);
            mCourseId = 0;

            mAssessmentList = new ArrayList<>();
            mNoteList = new ArrayList<>();

            mEditCourseAssessmentsButton.setVisibility(View.INVISIBLE);
            mAddNoteButton.setVisibility(View.INVISIBLE);
            mEditNoteButton.setVisibility(View.INVISIBLE);
            mShareNotesButton.setVisibility(View.INVISIBLE);
            mSetCourseAlertsButton.setVisibility(View.INVISIBLE);
        }

        mCourseTitleView.setText(mCourseTitle);
        mCourseDescriptionView.setText(mCourseDescription);

        mAssessmentAdapter.updateItems(mAssessmentList);
        mNoteAdapter.updateItems(mNoteList);

    }

    public void updateNoteList(){
        mNoteAdapter.notifyDataSetChanged();
    }

    //=====================================================

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

            if( mSelectedItemPosition == position){
                // Make selected subject stand out
                mTextView.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.teal_200));
            }
            else{
                mTextView.setBackgroundColor(Color.WHITE);
            }
        }

        @Override
        public void onClick(View view) {
            mSelectedItemPosition = getAdapterPosition();
            if(mItem instanceof Note){
                mNoteListener.onNoteSelected((Note)mItem);
                mNoteAdapter.notifyDataSetChanged();
            }
            if(mItem instanceof Assessment){
                mAssessmentAdapter.notifyDataSetChanged();
            }
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

    public interface NoteSelectedListener{
        //used to notify CourseView of selected notes.
        public void onNoteSelected(Note note);
    }
}