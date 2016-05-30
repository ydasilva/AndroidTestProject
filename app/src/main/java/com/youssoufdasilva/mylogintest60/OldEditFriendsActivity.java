package com.youssoufdasilva.mylogintest60;

import android.app.ProgressDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;

public class OldEditFriendsActivity extends AppCompatActivity {

    public static final String TAG = OldEditFriendsActivity.class.getSimpleName();

    protected RecyclerView mRecyclerView;
    protected ProgressDialog mProgressDialog;
    protected OldEditFriendsAdapter mAdapter;
    private ParseRelation<ParseUser> mFriendsRelation;
    private ParseUser mCurrentUser;

//    private ListActivity activityList = new ListActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_edit_friends);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        //specifying an adapter
        mAdapter = new OldEditFriendsAdapter();
        mRecyclerView.setAdapter(mAdapter);

        //using the linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadFriends();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    private void loadFriends() {
        //creating progress dialog
        mProgressDialog = new ProgressDialog(OldEditFriendsActivity.this);
        mProgressDialog.setTitle(getString(R.string.progress_title));
        mProgressDialog.setMessage(getString(R.string.progress_message));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.orderByAscending(ParseConstants.KEY_USERNAME);
        query.setLimit(1000);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(final List<ParseUser> users, ParseException e) {

                if (e == null) {
                    //success
//                    AlertDialog.Builder builder = new AlertDialog.Builder(OldEditFriendsActivity.this);
//                    builder.setMessage(users.size()+" ")
//                            .setTitle(R.string.error_title)
//                            .setPositiveButton(android.R.string.ok, null);
//                    AlertDialog dialog = builder.create();
//                    dialog.show();


                    mFriendsRelation = ParseUser.getCurrentUser().getRelation(ParseConstants.KEY_FRIENDS_RELATION);
                    mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> friends, ParseException e) {
                            mProgressDialog.hide();
                            if (e == null) {
                                //list returned - look for a match

                             mAdapter.setUsers(users,friends);
                              mAdapter.notifyDataSetChanged();


                            } else {
                                mAdapter.setUsers(users);
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    });


                } else {
                    //failed
                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(OldEditFriendsActivity.this);
                    builder.setMessage(e.getMessage())
                            .setTitle(R.string.error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProgressDialog = null;
    }

}
