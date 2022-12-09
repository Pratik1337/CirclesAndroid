package com.example.circlesv0401;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class MainActivity extends AppCompatActivity {


    ProgressBar pgb_main;

    public static final String MY_PREFS_NAME = "Circle_User_Detail_Preferences";
    SharedPreferences pref;

    String pref_email ;
    String pref_pass ;
    FirebaseAuth auth;

    ImageView img_main_logo;

    ImageView img_logo, img_color;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        img_main_logo = findViewById(R.id.im_main_text_logo2);

        img_main_logo.animate().alpha(1f).setDuration(1000);


        auth = FirebaseAuth.getInstance();

        pgb_main = findViewById(R.id.pb_main);



        createSharedPreferences();

        pref_email= pref.getString("c_user_email", null);
        pref_pass= pref.getString("c_user_password",null);

        img_logo = findViewById(R.id.im_welcome_main_logo);
        img_color = findViewById(R.id.im_color_text);

        img_logo.animate().alpha(1f).setDuration(2000);
        img_color.animate().alpha(1f).setDuration(2000);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {




                if(pref_email==null && pref_pass==null){

                    pgb_main.setVisibility(View.GONE);
                    startActivity(new Intent(getApplicationContext(),LoginMain.class));


                }else {

                    tryLogin(pref_email,pref_pass);

                }

            }
        }, 1000);




    }


    private void tryLogin(String email, String pass) {
        pgb_main.setVisibility(View.VISIBLE);

        auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    finish();
                    startActivity(new Intent(getApplicationContext(), CircleHome.class));
                    Toast.makeText(getApplicationContext(), "User Found", Toast.LENGTH_SHORT).show();
                    pgb_main.setVisibility(View.GONE);

                    }
                else {
                        pgb_main.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "User Found Please Log in ", Toast.LENGTH_SHORT).show();
                }

            }

        });

    }

    private void createSharedPreferences() {

        pref = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

    }
}