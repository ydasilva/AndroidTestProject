package com.youssoufdasilva.mylogintest60;

import android.app.ProgressDialog;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;

public class EditFriendsActivity extends AppCompatActivity {

    public static final String TAG = EditFriendsActivity.class.getSimpleName();

    protected RecyclerView mRecyclerView;
    protected ProgressDialog mProgressDialog;
    protected EditFriendsAdapter mAdapter;
    private ParseRelation<ParseUser> mFriendsRelation;
    private ParseUser mCurrentUser;

//    private ListActivity activityList = new ListActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friends);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        //specifying an adapter
        mAdapter = new EditFriendsAdapter();
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
        mProgressDialog = new ProgressDialog(EditFriendsActivity.this);
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
                mProgressDialog.hide();
                if (e == null) {
                    //success

                    mCurrentUser = ParseUser.getCurrentUser();
                    mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
                    mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> friends, ParseException e) {
                            if (e == null) {
                                //list returned - look for a match

                                mAdapter.setUsers(users,friends);
                                mAdapter.notifyDataSetChanged();
                            } else {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    });


                } else {
                    //failed
                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditFriendsActivity.this);
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
