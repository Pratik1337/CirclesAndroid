package com.example.circlesv0401;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.IOException;

public class TempLandingPage extends AppCompatActivity {

    Button btn_view_profile, btn_temp_logout, btn_register, btn_temp_upload_post, btn_temp_circle_home;
    TextView txt_landing_tag;
    ProgressBar pbg_temp_landing;
    ImageView img_logo_tem;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String userID;

    String fullName, userName, photoid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_landing_page);

        btn_view_profile= findViewById(R.id.bt_view_profile);

        txt_landing_tag = findViewById(R.id.tx_landing_tag);
        btn_temp_logout = findViewById(R.id.bt_temp_logout);
        btn_register = findViewById(R.id.bt_register_user);
        txt_landing_tag = findViewById(R.id.tx_landing_tag);

        pbg_temp_landing = findViewById(R.id.pb_temp_landing);

        btn_temp_upload_post = findViewById(R.id.bt_temp_upload_post);

        btn_temp_circle_home = findViewById(R.id.bt_temp_circle_home);

        img_logo_tem = findViewById(R.id.imageView3);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("CircleUsersDatabase");
        userID = firebaseUser.getUid();



        btn_view_profile.setEnabled(false);
        btn_register.setEnabled(false);
        btn_temp_circle_home.setEnabled(false);
        btn_temp_upload_post.setEnabled(false);


        int nightModeFlags =
                getApplicationContext().getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                img_logo_tem.setImageResource(R.drawable.circles_written_white);
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                img_logo_tem.setImageResource(R.drawable.circles_written_logo_black);
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                img_logo_tem.setImageResource(R.drawable.circles_written_white);
                break;
        }



        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                CircleUser circleUser = snapshot.getValue(CircleUser.class);

                if(circleUser!=null){
                    fullName = circleUser.getUserFullName();
                    userName = circleUser.getUserUsername();
                    photoid = circleUser.getUserProfilePhoto();

                    txt_landing_tag.setText(userName);
                    pbg_temp_landing.setVisibility(View.GONE);
                    btn_view_profile.setEnabled(true);
                    btn_register.setEnabled(true);
                    btn_temp_circle_home.setEnabled(true);
                    btn_temp_upload_post.setEnabled(true);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        btn_temp_circle_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), CircleHome.class);
//                intent.putExtra("current_uid", userID);
//                intent.putExtra("current_user_photo", photoid);
                startActivity(intent);

            }
        });



        btn_temp_upload_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), UploadPost.class);
//                intent.putExtra("current_uid", userID);
                startActivity(intent);

            }
        });

        btn_view_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), MyProfile.class));

            }
        });


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), RegisterUser.class));

            }
        });



        btn_temp_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), LogoutMain.class));

            }
        });
    }
}