package com.youssoufdasilva.mylogintest60;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class ViewImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);

//        Uri imageUri = getIntent().getData();
//        imageView.se

        byte[] byteArray = getIntent().getByteArrayExtra(ParseConstants.KEY_FILE_BYTES);
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//        ImageView image = (ImageView) findViewById(R.id.imageView);

        imageView.setImageBitmap(bmp);
    }
}
