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
import com.parse.ParseUser;

import java.util.List;

public class EditFriendsActivity extends AppCompatActivity {

    public static final String TAG = EditFriendsActivity.class.getSimpleName();

    protected RecyclerView mRecyclerView;
    protected ProgressDialog mProgressDialog;
    protected EditFriendsAdapter mAdapter;

//    private ListActivity activityList = new ListActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friends);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
//        mRecyclerView.setHasFixedSize(true);

        //specifying an adapter
        mAdapter = new EditFriendsAdapter();
        mRecyclerView.setAdapter(mAdapter);

        //using the linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));



        loadFriends();

//        activityList.getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
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
            public void done(List<ParseUser> users, ParseException e) {
                mProgressDialog.hide();
                if (e == null){
                    //success

                    mAdapter.setUsers(users);

//                    for(ParseUser user : users) {
//                        mUsers.add(user);
//                    }
                    mAdapter.notifyDataSetChanged();

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
