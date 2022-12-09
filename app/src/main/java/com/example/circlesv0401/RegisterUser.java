package com.example.circlesv0401;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegisterUser extends AppCompatActivity {

    EditText edt_full_name, edt_username,edt_email,edt_password1,edt_password2;

    Button btn_register, btn_profile_pic;
    ProgressBar pgb_register;
    Uri image_selected;
    StorageReference storageReference;

    FirebaseAuth auth;

    String profile_photo_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        edt_full_name = findViewById(R.id.ed_register_full_name);
        edt_username = findViewById(R.id.ed_register_username);
        edt_email = findViewById(R.id.ed_register_email);
        edt_password1 = findViewById(R.id.ed_register_password1);
        edt_password2 = findViewById(R.id.ed_register_password2);

        btn_register = findViewById(R.id.bt_register);
        btn_profile_pic = findViewById(R.id.bt_photo);

        pgb_register = findViewById(R.id.pb_register);

        auth = FirebaseAuth.getInstance();


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerUser();
            }
        });


        btn_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectUpload();

            }
        });



    }

    private void selectUpload() {

//        Intent i = new Intent();
//        i.setType("image/*");
//        i.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(i, 100);

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setOutputCompressQuality(96).setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                .start(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if(requestCode == 100 && data!=null){
//
//            image_selected = data.getData();
//            Toast.makeText(getApplicationContext(), "Image Selected", Toast.LENGTH_SHORT).show();
//
//
//
//        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                image_selected = resultUri;
                Toast.makeText(getApplicationContext(), "Image Selected", Toast.LENGTH_SHORT).show();
               // img_selected.setImageURI(image_selected);
                //pgb_uploadPost.setVisibility(View.GONE);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                //pgb_uploadPost.setVisibility(View.GONE);
                Exception error = result.getError();
            }
        }


    }

    private void registerUser() {

        String fullname = edt_full_name.getText().toString().trim();
        String username = edt_username.getText().toString().trim();
        String email = edt_email.getText().toString().trim();
        String password1 = edt_password1.getText().toString().trim();
        String password2 = edt_password2.getText().toString().trim();
        String main_password = "123456" ;

        if(fullname.isEmpty()){

            edt_full_name.setError("Full Name is Required");
            edt_full_name.requestFocus();
            return;
        }
        if(username.isEmpty()){
            edt_username.setError("Username is Required");
            edt_username.requestFocus();
            return;
        }
        if(email.isEmpty()){
            edt_email.setError("Email is Required");
            edt_email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edt_email.setError("Provide Valid Email");
            edt_email.requestFocus();
            return;
        }

        if(!password1.equals(password2)){

            edt_password1.setError("Password Does Not Match");
            edt_password2.setError("Password Does Not Match");
            edt_password1.setText(null);
            edt_password2.setText(null);
            edt_password1.requestFocus();
            return;


        }

        if(password1.equals(password2)){

            main_password = password1;
            if(main_password.length()<6){
                edt_password1.setError("Minimum 6 Characters required");
                edt_password1.requestFocus();
                edt_password1.setText(null);
                edt_password2.setText(null);

                return;
            }

        }

        String username_lower = username.toLowerCase();
        String special_username = "@"+ username_lower;


        //Toast.makeText(getApplicationContext(), "Good Work " + main_password, Toast.LENGTH_SHORT).show();

        pgb_register.setVisibility(View.VISIBLE);

        SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy hh mm ss", Locale.CANADA);
        Date d1 = new Date();
        String p_photo_name = format.format(d1);

        storageReference = FirebaseStorage.getInstance().getReference("Profile Photos/"+p_photo_name);


        if(image_selected!=null) {

            profile_photo_name = p_photo_name;

            storageReference.putFile(image_selected)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            if (task.isSuccessful()) {
                                profile_photo_name = p_photo_name;
                                pgb_register.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(getApplicationContext(), "Failed to Upload Image", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

        }else{
            profile_photo_name = "Default.png";

        }


        auth.createUserWithEmailAndPassword(email, main_password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                        CircleUser circleUser = new CircleUser(fullname,special_username,email,profile_photo_name,password1);

                        FirebaseDatabase.getInstance().getReference("CircleUsersDatabase")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(circleUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "User Data Saved"+toString(), Toast.LENGTH_SHORT).show();

                                }else {
                                    Toast.makeText(getApplicationContext(), "User Data Could Not be uploaded"+toString(), Toast.LENGTH_SHORT).show();

                                }

                            }
                        });


                        Toast.makeText(getApplicationContext(), "User Registered "+toString(), Toast.LENGTH_SHORT).show();
                        //pgb_register.setVisibility(View.GONE);
                        startActivity(new Intent(getApplicationContext(), LogoutMain.class));

                    }else{
                        Toast.makeText(getApplicationContext(), "Failed To Create new User", Toast.LENGTH_SHORT).show();
                        pgb_register.setVisibility(View.GONE);
                    }

                }
            });




    }
}