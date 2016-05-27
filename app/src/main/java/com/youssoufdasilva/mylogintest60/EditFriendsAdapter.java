package com.youssoufdasilva.mylogintest60;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by youssoufdasilva on 5/20/16.
 */
public class EditFriendsAdapter extends RecyclerView.Adapter<EditFriendsAdapter.EditFriendsViewHolder> {

    public static final String TAG = EditFriendsActivity.class.getSimpleName();

    private List<ParseUser> mUsers;
    private List<ParseUser> mFriends;
    private ParseRelation<ParseUser> mFriendsRelation;
    private ParseUser mCurrentUser;
    private Boolean isRelation = false;

    // Provide a suitable constructor (depends on the kind of dataset)
    public EditFriendsAdapter() {
        mUsers = new ArrayList<>();
        mFriends = new ArrayList<>();
    }

    public void setUsers(List<ParseUser> mUsers,List<ParseUser> mFriends) {
        this.mUsers = mUsers;
        this.mFriends = mFriends;
    }

    @Override
    public EditFriendsAdapter.EditFriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new EditFriendsViewHolder(inflater.inflate(R.layout.edit_friends_view, parent, false));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(EditFriendsViewHolder holder, int position) {
        holder.setUser(mUsers.get(position));

        for (ParseUser friend : mFriends) {

            if (friend.getObjectId().equals(mUsers.get(position).getObjectId())) {
                //set check-mark here
                //  getListView().setItemChecked(i, true);
                holder.isChecked();

            }
        }
        // - get element from your data-set at this position
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

        private ParseUser user;
        private TextView txtUsername;
        private ImageView imgCheckedOff;
        private ImageView imgCheckedOn;

        public EditFriendsViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            txtUsername =  (TextView) itemView.findViewById(R.id.text_username);
            imgCheckedOff =  (ImageView) itemView.findViewById(R.id.check_image_off);
            imgCheckedOn =  (ImageView) itemView.findViewById(R.id.check_image_on);
        }

        public void setUser(ParseUser user) {
            this.user = user;
            txtUsername.setText(user.getUsername());
        }

        public void isChecked (){
            imgCheckedOff.setVisibility(View.GONE);
            imgCheckedOn.setVisibility(View.VISIBLE);
        }

        @Override
        public void onClick(View v) {

            if (imgCheckedOn.getVisibility() == View.VISIBLE){
                imgCheckedOn.setVisibility(View.GONE);
                imgCheckedOff.setVisibility(View.VISIBLE);

                //add friend
//                mFriendsRelation.add(user);
            } else {
                imgCheckedOn.setVisibility(View.VISIBLE);
                imgCheckedOff.setVisibility(View.GONE);

                //remove friend
//                mFriendsRelation.remove(user);
            }

            ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e){
                    if (e != null){
                        Log.e(TAG, e.getMessage());
                    }
                }
            });


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
