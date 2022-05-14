package com.dsmith.myedu;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class Utils{



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

            /*if( mSelectedItemPosition == position){
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



}
