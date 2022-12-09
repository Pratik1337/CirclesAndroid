package com.example.circlesv0401;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginMain extends AppCompatActivity {

    EditText edt_email, edt_pass;
    Button btn_login;
    TextView txt_register, txt_verification, txt_forgot;
    ProgressBar pgb_login;

    FirebaseAuth auth;

    public static final String MY_PREFS_NAME = "Circle_User_Detail_Preferences";
    SharedPreferences pref;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        edt_email = findViewById(R.id.ed_forgotpassword_email);
        edt_pass = findViewById(R.id.ed_pass);
        btn_login = findViewById(R.id.bt_reset_pass);
        txt_register = findViewById(R.id.tx_register);
        pgb_login = findViewById(R.id.pb_login);
        txt_verification = findViewById(R.id.tx_email_verification);
        txt_forgot = findViewById(R.id.tx_login_forgot);

        auth = FirebaseAuth.getInstance();

        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterUser.class);
                startActivity(intent);

            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edt_pass.clearFocus();
                userLogin();

            }
        });

        txt_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String entered_email = null;
                entered_email = edt_email.getText().toString().trim();

                Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                intent.putExtra("email", entered_email);
                startActivity(intent);

            }
        });


    }

    private void userLogin() {

        String entered_email = edt_email.getText().toString().trim();
        String entered_password = edt_pass.getText().toString().trim();

        if(entered_email.isEmpty()){
            edt_email.setError("Email Address Required");
            edt_email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(entered_email).matches()){
            edt_email.setError("Enter Valid Email");
            edt_email.requestFocus();
            return;

        }
        if(entered_password.length()<6){
            edt_pass.setError("Minimum 6 Characters required");
            edt_pass.requestFocus();
            return;
        }
        if(entered_password.isEmpty()){
            edt_pass.setError("Password is Required");
            edt_pass.requestFocus();
            return;
        }

        pgb_login.setVisibility(View.VISIBLE);



        auth.signInWithEmailAndPassword(entered_email,entered_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()){

                        pgb_login.setVisibility(View.GONE);
                        txt_verification.setText(null);

                        getAllDetailsOfCurrentUser(entered_password);

                        finish();
                        startActivity(new Intent(getApplicationContext(),CircleHome.class));

                    }else{

                        txt_verification.setText("Verification email has been sent. Please Verify Email.");
                        Toast.makeText(getApplicationContext(), "Email not Verified", Toast.LENGTH_LONG).show();
                        pgb_login.setVisibility(View.GONE);
                        user.sendEmailVerification();

                    }


                }else {
                    pgb_login.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void getAllDetailsOfCurrentUser(String pass_word) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("CircleUsersDatabase");
        userID = firebaseUser.getUid();


        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                CircleUser circleUser = snapshot.getValue(CircleUser.class);

                if(circleUser!=null){

                    String fullName = circleUser.getUserFullName();
                    String userName = circleUser.getUserUsername();
                    String photoid = circleUser.getUserProfilePhoto();
                    String email = circleUser.getUserEmail();
//                    String password = circleUser.getUserPassword();

                    editSharedPreference(userID,fullName,userName,email,photoid,pass_word);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }
    private void editSharedPreference(String c_id, String c_fname, String c_uname, String c_email, String c_photo, String c_pass ) {

        pref = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("c_user_id", c_id);
        editor.putString("c_user_fullName",c_fname);
        editor.putString("c_user_userName",c_uname);
        editor.putString("c_user_email",c_email);
        editor.putString("c_user_profilePhoto",c_photo);
        editor.putString("c_user_password",c_pass);
        editor.apply();

    }


}