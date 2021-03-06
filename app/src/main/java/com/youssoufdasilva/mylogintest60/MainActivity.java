package com.youssoufdasilva.mylogintest60;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    public static final int TAKE_PHOTO_REQUEST = 0;
    public static final int TAKE_VIDEO_REQUEST = 1;
    public static final int PICK_PHOTO_REQUEST = 2;
    public static final int PICK_VIDEO_REQUEST = 3;

    public static final int MEDIA_TYPE_IMAGE = 4;
    public static final int MEDIA_TYPE_VIDEO = 5;


    public static final int FILE_SIZE_LIMIT = 10 * 1024 * 1024; // 10 MB

    protected Uri mMediaUri;
    protected TextView mCurrentUser;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private DialogInterface.OnClickListener mDialogListener =
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0: // Take Picture
                            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

                            if (mMediaUri == null) {
                                //display an error
                                Toast.makeText(MainActivity.this, R.string.error_external_storage, Toast.LENGTH_LONG).show();
                            } else {
                                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                                startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
                            }
                            break;
                        case 1: // Take Video
                            Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                            mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

                            if (mMediaUri == null) {
                                //display an error
                                Toast.makeText(MainActivity.this, R.string.error_external_storage, Toast.LENGTH_LONG).show();
                            } else {
                                videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                                videoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5); //limit is 5 seconds
                                videoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                                startActivityForResult(videoIntent, TAKE_VIDEO_REQUEST);
                            }
                            break;
                        case 2: // Choose Picture
                            Intent choosePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            choosePhotoIntent.setType("image/*");
                            startActivityForResult(choosePhotoIntent, PICK_PHOTO_REQUEST);
                            break;
                        case 3: // Choose Video
                            Intent chooseVideoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            chooseVideoIntent.setType("video/*");
                            Toast.makeText(MainActivity.this, R.string.video_file_size_warning, Toast.LENGTH_LONG).show();
                            startActivityForResult(chooseVideoIntent, PICK_VIDEO_REQUEST);
                            break;
                    }
                }

                private Uri getOutputMediaFileUri(int mediaType) {
                    //To be safe, you should check that the SDCard is mounted
                    //using Environment.getExternalStorageState() before doing this.
                    if (isExternalStorageAvailable()) {
                        //get the Uri

                        //1. Get the external storage directory
                        String appName = MainActivity.this.getString(R.string.app_name);
                        File mediaStorageDir = new File(
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                                appName);
                        //2. Create our own subdirectory
                        if (!mediaStorageDir.exists()) {
                            if (!mediaStorageDir.mkdirs()) {
                                Log.e(TAG, "Failed to create directory.");
                                return null;
                            }
                        }
                        //3. Create a file name
                        //4. Create the actual file
                        File mediaFile;
                        Date now = new Date();
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(now);
                        String path = mediaStorageDir.getPath() + File.separator;
                        if (mediaType == MEDIA_TYPE_IMAGE) {
                            mediaFile = new File(path + "IMG_" + timeStamp + ".jpg");
                        } else if (mediaType == MEDIA_TYPE_VIDEO) {
                            mediaFile = new File(path + "VID_" + timeStamp + ".mp4");
                        } else {
                            return null;
                        }

                        Log.d(TAG, "File: " + Uri.fromFile(mediaFile));
                        //5. Return the file's URI
                        return Uri.fromFile(mediaFile);
                    } else {
                        return null;
                    }
                }

                private boolean isExternalStorageAvailable() {
                    String state = Environment.getExternalStorageState();

                    if (state.equals(Environment.MEDIA_MOUNTED)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Log.i(TAG, currentUser.getUsername());
            mCurrentUser = (TextView) findViewById(R.id.current_user_txt);
            mCurrentUser.setText(currentUser.getUsername());

            mSectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);

        } else {
            navigateToLogin();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_PHOTO_REQUEST || requestCode == PICK_VIDEO_REQUEST) {
                if (data == null) {
                    Toast.makeText(this, R.string.general_error, Toast.LENGTH_LONG).show();
                } else {
                    mMediaUri = data.getData();
                }
                Log.i(TAG, "Media URI: " + mMediaUri);
                if (requestCode == PICK_VIDEO_REQUEST) {
                    // make sure the file is less than 10 MB
                    int fileSize = 0;
                    InputStream inputStream = null;

                    try {
                        inputStream = getContentResolver().openInputStream(mMediaUri);
                        fileSize = inputStream.available();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(this, R.string.error_opening_file, Toast.LENGTH_LONG).show();
                        return;
                    } catch (IOException e) {
                        Toast.makeText(this, R.string.error_opening_file, Toast.LENGTH_LONG).show();
                        return;
                    } finally {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (fileSize >= FILE_SIZE_LIMIT) {
                        Toast.makeText(this, R.string.error_file_size_too_large, Toast.LENGTH_LONG).show();
                        return;
                    }

                }
            } else {
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(mMediaUri);
                sendBroadcast(mediaScanIntent);
            }

            Intent recipientIntent = new Intent(this, RecipientsActivity.class);
            recipientIntent.setData(mMediaUri); // this is where the media (Picture or video) gets passed to the recipientActivity

            String fileType;
            if (requestCode == PICK_PHOTO_REQUEST || requestCode == TAKE_PHOTO_REQUEST) {
                fileType = ParseConstants.TYPE_IMAGE;
            } else {
                fileType = ParseConstants.TYPE_VIDEO;
            }
            recipientIntent.putExtra(ParseConstants.KEY_FILE_TYPE, fileType);

            startActivity(recipientIntent);
        } else if (resultCode != RESULT_CANCELED) {
            Toast.makeText(this, R.string.general_error, Toast.LENGTH_LONG).show();
        }
    }

    private void navigateToLogin() {
        Intent myIntent = new Intent(this, LoginActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(myIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_edit_friends:
                Intent friendIntent = new Intent(this, EditFriendsActivity.class);
//                Intent friendIntent = new Intent (this, OldEditFriendsActivity.class);
                startActivity(friendIntent);
                return true;
            case R.id.action_logout:
                ParseUser.logOut();
                navigateToLogin();
                return true;
            case R.id.action_camera:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setItems(R.array.camera_choices, mDialogListener);
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            case R.id.action_basic_activity:
                Intent intent = new Intent(this, BasicActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}