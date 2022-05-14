package com.dsmith.myedu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TermView extends AppCompatActivity implements AddTermView.SaveFinishedListener {



    private final String TERM_DETAILS_TAG = "TermDetailsFragment";
    private final String TERM_EDIT_TAG = "TermEditFragment";
    private final String TERM_ID = "TermID";

    private TermAdapter mTermAdapter;
    private RecyclerView mTermListView;
    private TermDetailsView mTermDetailsView;
    private AddTermView mAddTermView;

    private Term mSelectedTerm;
    private int mSelectedTermPosition = RecyclerView.NO_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_view);

        mTermListView = findViewById(R.id.termList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        mTermListView.setLayoutManager(layoutManager);

        //only create new details fragment if creating fresh, not after recreating activity
        if(getSupportFragmentManager().findFragmentByTag(TERM_DETAILS_TAG) == null &&
                getSupportFragmentManager().findFragmentByTag(TERM_EDIT_TAG) == null){
            mTermDetailsView = TermDetailsView.newInstance(null);
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.termDetailsLayout, mTermDetailsView, TERM_DETAILS_TAG)
                    .commit();
        }
        if(getSupportFragmentManager().findFragmentByTag(TERM_DETAILS_TAG) != null){
            mTermDetailsView = (TermDetailsView)getSupportFragmentManager().findFragmentByTag(TERM_DETAILS_TAG);
        }


        mTermAdapter = new TermAdapter(loadTerms());
        mTermListView.setAdapter(mTermAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                addTerm();
                return true;

            case R.id.action_edit:
                editTerm();
                return true;

            case R.id.action_delete:
                deleteTerm();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ArrayList<Term> loadTerms() {
        return MainActivity.userSchedule.getAllTerms();
    }

    public void addTermClick(View view){
        addTerm();
    }

    public void addTerm(){
        //start add term fragment
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(mTermDetailsView);
        mAddTermView = AddTermView.newInstance();
        ft.replace(R.id.termDetailsLayout, mAddTermView, TERM_EDIT_TAG);
        ft.commit();
        getSupportFragmentManager().popBackStackImmediate();
    }

    public void deleteTerm(){
        if(mAddTermView != null) {
        Toast.makeText(getApplicationContext(), "Delete Failed: please close term edit form before deleting.",Toast.LENGTH_LONG).show();
        }
        else if(mSelectedTerm.getTermCourses().size() > 0) {
            Toast.makeText(getApplicationContext(), "Delete Failed: please remove all Courses in selected term before deleting.",Toast.LENGTH_LONG).show();
        }
        else if(mSelectedTerm != null){
            Toast.makeText(getApplicationContext(), "Term \'" + mSelectedTerm.getTitle() + "\' Deleted",Toast.LENGTH_LONG).show();
            MainActivity.userSchedule.deleteTerm(mSelectedTerm);
            mTermAdapter.notifyDataSetChanged();
            mSelectedTerm = null;

            mTermDetailsView.setItemInfo(null);
        }
    }

    public void editCoursesClick(View view){
        startEditCoursesActivity();
    }

    private void editTerm(){
        //start edit term fragment
        if (mSelectedTerm != null){
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.remove(mTermDetailsView);
            mAddTermView = AddTermView.newInstance(mSelectedTerm);
            ft.replace(R.id.termDetailsLayout, mAddTermView, TERM_EDIT_TAG);
            ft.commit();
            getSupportFragmentManager().popBackStackImmediate();
        }
    }

    private void startEditCoursesActivity(){
        //start view/edit term activity
        if(mSelectedTerm != null){
            Intent intent = new Intent(TermView.this, CourseView.class);

            intent.putExtra(TERM_ID, mSelectedTerm.getTermId());
            startActivity(intent);
        }
    }

    public void showTermDetailsFragment(){
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //if add term view is active
        if(mAddTermView != null){
            ft.remove(mAddTermView);
        }
        if(mSelectedTerm != null) {
            mTermDetailsView = TermDetailsView.newInstance(mSelectedTerm);
        }
        else{
            mTermDetailsView = TermDetailsView.newInstance(null);
        }
        ft.replace(R.id.termDetailsLayout, mTermDetailsView, TERM_DETAILS_TAG);
        ft.commit();
        getSupportFragmentManager().popBackStackImmediate();

    }

    public void onTermCancelButtonClick(View view){
        showTermDetailsFragment();
        if(mSelectedTerm != null){
            mTermDetailsView.setItemInfo(mSelectedTerm);
        }
    }

    public void onTermSaveButtonClick(View view){
        mAddTermView.save();
    }

    @Override
    public void onSaveFinished(){
        showTermDetailsFragment();
        mTermAdapter.notifyDataSetChanged();

        if(mSelectedTerm != null){
            mTermDetailsView.setItemInfo(mSelectedTerm);
        }
    }

    private class TermHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{

        private Term mTerm;
        private TextView mTextView;

        public TermHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.recycler_view_items, parent, false));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mTextView = itemView.findViewById(R.id.itemTextView);
        }

        public void bind(Term term, int position) {
            mTerm = term;
            mTextView.setText(term.getTitle());

            if( mSelectedTermPosition == position){
                // Make selected subject stand out
                mTextView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.teal_200));
            }
            else{
                mTextView.setBackgroundColor(Color.WHITE);
            }
        }

        @Override
        public void onClick(View view) {
            //pass information to the termDetailsView fragment.
            mSelectedTermPosition = getAdapterPosition();
            mSelectedTerm = mTerm;
            mTermDetailsView.setItemInfo(mSelectedTerm);

            // Highlight last selected using rebinds
            mTermAdapter.notifyDataSetChanged();
        }

        @Override
        public boolean onLongClick(View view) {
            //nothing yet
            return true;
        }
    }

    private class TermAdapter extends RecyclerView.Adapter<TermHolder> {

        private ArrayList<Term> mTermList;

        public TermAdapter(ArrayList<Term> terms) {
            mTermList = terms;
        }

        public void addTerm(Term term) {
            // Add the new subject at the beginning of the list
            mTermList.add(0, term);

            // Notify the adapter that item was added to the beginning of the list
            notifyItemInserted(0);

            // Scroll to the top
            mTermListView.scrollToPosition(0);
        }

        public void removeTerm(Term term) {
            // Find subject in the list
            int index = mTermList.indexOf(term);
            if (index >= 0) {
                // Remove the subject
                mTermList.remove(index);

                // Notify adapter of subject removal
                notifyItemRemoved(index);
            }

        }

        @Override
        public TermHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            return new TermHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(TermHolder holder, int position){
            holder.bind(mTermList.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mTermList.size();
        }
    }


}