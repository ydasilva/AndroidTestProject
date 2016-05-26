package com.youssoufdasilva.mylogintest60;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by youssoufdasilva on 5/18/16.
 */
public class LoginTestApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
        .applicationId("1234")
        .clientKey("5678")
                .server("http://mylogintest60.herokuapp.com/parse/")
                .build());

//        ParseObject testObject = new ParseObject("TestObject");
//        testObject.put("foo", "bar");
//        testObject.saveInBackground();

    }
}
