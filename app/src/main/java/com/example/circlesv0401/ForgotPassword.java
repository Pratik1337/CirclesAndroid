package com.example.circlesv0401;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    EditText edt_forgotpass_email;
    Button btn_reset_pass;
    ProgressBar pgb_reset;
    TextView txt_forgot_desc3;

    FirebaseAuth auth;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edt_forgotpass_email = findViewById(R.id.ed_forgotpassword_email);
        btn_reset_pass = findViewById(R.id.bt_reset_pass);
        pgb_reset = findViewById(R.id.pg_reset);
        txt_forgot_desc3 = findViewById(R.id.tx_forgot_desc3);

        auth = FirebaseAuth.getInstance();



        Intent i = getIntent();
        email = i.getStringExtra("email");


        if(email!=null){
            edt_forgotpass_email.setText(email);

        }else{

        }
        btn_reset_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                passwordReset();

            }
        });


    }

    private void passwordReset() {

        String fentered_email = edt_forgotpass_email.getText().toString().trim();

        if(fentered_email.isEmpty()){
            edt_forgotpass_email.setError("Email Address Required");
            edt_forgotpass_email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(fentered_email).matches()){
            edt_forgotpass_email.setError("Enter Valid Email");
            edt_forgotpass_email.requestFocus();
            return;
        }

        pgb_reset.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(fentered_email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    txt_forgot_desc3.setText("An email to reset the password has been sent Please check your inbox");
                    Toast.makeText(getApplicationContext(), "Email Sent", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Faild to Send Email", Toast.LENGTH_SHORT).show();
                }
                pgb_reset.setVisibility(View.GONE);

            }
        });


    }
}