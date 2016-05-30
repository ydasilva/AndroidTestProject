package com.youssoufdasilva.mylogintest60;

import android.support.v7.app.AlertDialog;
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
public class OldEditFriendsAdapter extends RecyclerView.Adapter<OldEditFriendsAdapter.EditFriendsViewHolder> {

    public static final String TAG = OldEditFriendsActivity.class.getSimpleName();

    private List<ParseUser> mUsers;
    private List<ParseUser> mFriends;

    private ParseUser mCurrentUser;
    private Boolean isRelation = false;

    // Provide a suitable constructor (depends on the kind of dataset)
    public OldEditFriendsAdapter() {
        mUsers = new ArrayList<>();
        mFriends = new ArrayList<>();
    }

    public void setUsers(List<ParseUser> mUsers,List<ParseUser> mFriends) {
        this.mUsers = mUsers;
        this.mFriends = mFriends;
    }

    public void setUsers(List<ParseUser> mUsers) {
        this.mUsers = mUsers;
    }

    @Override
    public OldEditFriendsAdapter.EditFriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new EditFriendsViewHolder(inflater.inflate(R.layout.edit_friends_view, parent, false));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(EditFriendsViewHolder holder, int position) {
        holder.setUser(mUsers.get(position));

        Log.i(TAG, "is inside onBindView");

        for (ParseUser friend : mFriends) {

            if (friend.getObjectId().equals(mUsers.get(position).getObjectId())) {
                Log.i(TAG, "isChecked");
                //set check-mark here
                //  getListView().setItemChecked(i, true);
                holder.isChecked();

            } else {
                Log.i(TAG, "is not checked");
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
        private ParseRelation<ParseUser> mFriendsRelation;

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
        public void onClick(final View v) {
            mFriendsRelation = ParseUser.getCurrentUser().getRelation(ParseConstants.KEY_FRIENDS_RELATION);
            if (imgCheckedOn.getVisibility() == View.VISIBLE) {
                imgCheckedOn.setVisibility(View.GONE);
                imgCheckedOff.setVisibility(View.VISIBLE);

                //add friend
                mFriendsRelation.add(user);
            } else {
                imgCheckedOn.setVisibility(View.VISIBLE);
                imgCheckedOff.setVisibility(View.GONE);

                //remove friend
                mFriendsRelation.remove(user);
            }

            ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        //everything is fine
                        mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
                            @Override
                            public void done(List<ParseUser> friends, ParseException e) {

                                if (e == null) {
                                    //list returned - look for a match
                                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                    builder.setMessage(friends.size()+" ")
                                            .setTitle(R.string.error_title)
                                            .setPositiveButton(android.R.string.ok, null);
                                    AlertDialog dialog = builder.create();
                                    dialog.show();


                                } else {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                    builder.setMessage("There was an error in relation")
                                            .setTitle(R.string.error_title)
                                            .setPositiveButton(android.R.string.ok, null);
                                    AlertDialog dialog = builder.create();
                                    dialog.show();

                                }
                            }
                        });
                    } else {
                        //not fine
                        Log.e(TAG, e.getMessage());
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("There was an error ")
                            .setTitle(R.string.error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    }
                }
            });


        }
    }
}
