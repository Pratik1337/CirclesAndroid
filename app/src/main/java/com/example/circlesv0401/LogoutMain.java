package com.example.circlesv0401;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

public class LogoutMain extends AppCompatActivity {


    ProgressBar pgb_logout;
    public static final String MY_PREFS_NAME = "Circle_User_Detail_Preferences";
    SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout_main);

        pgb_logout = findViewById(R.id.pb_logout);


        FirebaseAuth.getInstance().signOut();
        clearShared();
        pgb_logout.setVisibility(View.GONE);
        startActivity(new Intent(getApplicationContext(), LoginMain.class));


    }

    private void clearShared() {
        pref = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }
}