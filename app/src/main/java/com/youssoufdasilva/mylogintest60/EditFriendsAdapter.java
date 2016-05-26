package com.youssoufdasilva.mylogintest60;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by youssoufdasilva on 5/20/16.
 */
public class EditFriendsAdapter extends RecyclerView.Adapter<EditFriendsAdapter.EditFriendsViewHolder> {

    protected List<ParseUser> mUsers;

    // Provide a suitable constructor (depends on the kind of dataset)
    public EditFriendsAdapter() {
        mUsers = new ArrayList<>();
    }

    public void setUsers(List<ParseUser> mUsers) {
        this.mUsers = mUsers;
    }

    @Override
    public EditFriendsAdapter.EditFriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new EditFriendsViewHolder(inflater.inflate(android.R.layout.simple_list_item_checked, parent, false));


        /*
        //create a new view
        View v = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.activity_edit_friends, parent, false);

        //set the view's size, margins, paddings and layout parameters
        //...

        EditFriendsViewHolder vh = new EditFriendsViewHolder((TextView) v);
        return vh;
        */
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(EditFriendsViewHolder holder, int position) {
        holder.setUser(mUsers.get(position));

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.mTextView.setText(mUsers.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    /**
     * Class for viewHolder
     */
    public static class EditFriendsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected ParseUser user;
        protected CheckedTextView txtUsername;

        public void setUser(ParseUser user) {
            this.user = user;
            txtUsername.setText(user.getUsername());
        }

        public EditFriendsViewHolder(View itemView) {
            super(itemView);
            txtUsername =  (CheckedTextView) itemView.findViewById(android.R.id.text1);
        }

        @Override
        public void onClick(View v) {
            txtUsername.setCheckMarkTintMode(PorterDuff.Mode.DARKEN);
           // txtUsername.setChecked(true);
        }

        /*
        // each data item is just a string in this case
        public TextView mTextView;
        public EditFriendsViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
        */
    }
}
