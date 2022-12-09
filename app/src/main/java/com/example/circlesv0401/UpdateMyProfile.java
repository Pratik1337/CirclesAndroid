package com.example.circlesv0401;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UpdateMyProfile extends AppCompatActivity {


    EditText edt_update_name, edt_update_username,edt_update_email;
    Button btn_update_image_selector, btn_update_done;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    ImageView img_update_image;
    ProgressBar pgb_update;
    CircleUser circleUser;

    FloatingActionButton fab_change_photo;

    Uri image_selected;

    String passed_name, passed_email, passed_username, passed_id, passed_photo_name, profile_photo_name,new_profile_photo_name,passed_password;
    int was_image_changed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_my_profile);

        was_image_changed = 0;

        edt_update_email = findViewById(R.id.ed_update_email);
        edt_update_name = findViewById(R.id.ed_update_name);
        edt_update_username = findViewById(R.id.ed_update_username);

        btn_update_done = findViewById(R.id.bt_update_done);
//        btn_update_image_selector = findViewById(R.id.bt_update_image);
        img_update_image = findViewById(R.id.im_update_image);
        pgb_update = findViewById(R.id.pb_update);

        pgb_update.setVisibility(View.VISIBLE);

        fab_change_photo = findViewById(R.id.fb_change_photo);

        Intent intent = getIntent();

        passed_id = intent.getStringExtra("user_id");
        passed_name = intent.getStringExtra("user_fullname");
        passed_username = intent.getStringExtra("user_username");
        passed_email = intent.getStringExtra("user_email");
        passed_photo_name = intent.getStringExtra("user_photo");
        passed_password = intent.getStringExtra("user_pass");


        StringBuilder sb1 = new StringBuilder(passed_username);
        sb1 = sb1.deleteCharAt(0);

        edt_update_name.setText(passed_name);
        edt_update_username.setText(sb1);
        edt_update_email.setText(passed_email);
        edt_update_email.setEnabled(false);

//        try {
            putImage(passed_photo_name);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        fab_change_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pgb_update.setVisibility(View.VISIBLE);
                selectImage();

            }
        });


//        btn_update_image_selector.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//
//            }
//        });


        btn_update_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                pgb_update.setVisibility(View.VISIBLE);
                putUpdateData();
                finish();
                startActivity(new Intent(getApplicationContext(), CircleHome.class));

            }
        });


    }

    private void putUpdateData() {

        String new_name= edt_update_name.getText().toString().trim();
        String new_username = edt_update_username.getText().toString().trim();
        String new_email = passed_email;
        new_profile_photo_name = passed_photo_name;


        if(new_name.isEmpty()){

            edt_update_name.setError("Full Name is Required");
            edt_update_name.requestFocus();
            return;
        }
        if(new_username.isEmpty()){
            edt_update_username.setError("Username is Required");
            edt_update_username.requestFocus();
            return;
        }

        String username_lower = new_username.toLowerCase();


        String new_special_username ;

        new_special_username = "@"+ username_lower;



        if(was_image_changed >0){

            SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy hh mm ss", Locale.CANADA);
            Date d1 = new Date();
            String p_photo_name = format.format(d1);

            storageReference = FirebaseStorage.getInstance().getReference("Profile Photos/"+p_photo_name);


            if(image_selected!=null) {

                new_profile_photo_name = p_photo_name;

                storageReference.putFile(image_selected)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                if (task.isSuccessful()) {
                                    new_profile_photo_name = p_photo_name;

                                    Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
                                } else {

                                    Toast.makeText(getApplicationContext(), "Failed to Upload Image", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

            }else{
                new_profile_photo_name = "Default.png";

            }

        }

        circleUser = new CircleUser(new_name,new_special_username,new_email,new_profile_photo_name,passed_password);

        FirebaseDatabase.getInstance().getReference("CircleUsersDatabase")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(circleUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "User Data Updated"+toString(), Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(getApplicationContext(), "User Data Could Not be Updated"+toString(), Toast.LENGTH_SHORT).show();

                }
            }
        });

        pgb_update.setVisibility(View.GONE);

    }

    private void selectImage() {
//
//        Intent i = new Intent();
//        i.setType("image/*");
//        i.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(i, 300);

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setOutputCompressQuality(96).setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                .start(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if(requestCode == 300 && data!=null){
//
//            image_selected = data.getData();
//            //Picasso.get().load(image_selected).transform(new MyProfile.CircleTransform()).into(img_update_image);
//
//
//
//            Glide.with(getApplicationContext()).load(image_selected).placeholder(R.drawable.usr1).circleCrop().into(img_update_image);
//
//
//            Toast.makeText(getApplicationContext(), "New Image Selected", Toast.LENGTH_SHORT).show();
//            was_image_changed = 1;
//
//        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                image_selected = resultUri;
                Toast.makeText(getApplicationContext(), "Image Selected", Toast.LENGTH_SHORT).show();

                Glide.with(getApplicationContext()).load(image_selected).placeholder(R.drawable.usr1).circleCrop().into(img_update_image);

                was_image_changed = 1;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                //pgb_uploadPost.setVisibility(View.GONE);
                Exception error = result.getError();
            }
        }


        pgb_update.setVisibility(View.GONE);
    }



    private void putImage(String pht){

        storageReference = FirebaseStorage.getInstance().getReference("Profile Photos/"+pht);

//        File localfile = File.createTempFile("tempfile", ".jpeg");
//        storageReference.getFile(localfile).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
//
//                if(task.isSuccessful()){
//
//                    Picasso.get().load(localfile).transform(new MyProfile.CircleTransform()).into(img_update_image);
//
//                }else{
//                    Toast.makeText(getApplicationContext(), "Image not Found", Toast.LENGTH_SHORT).show();
//
//                }
//
//            }
//        });


        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).placeholder(R.drawable.usr1).circleCrop().into(img_update_image);

            }
        });


        pgb_update.setVisibility(View.GONE);

    }




//    public static class CircleTransform implements Transformation {
//        @Override
//        public Bitmap transform(Bitmap source) {
//            int size = Math.min(source.getWidth(), source.getHeight());
//
//            int x = (source.getWidth() - size) / 2;
//            int y = (source.getHeight() - size) / 2;
//
//            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
//            if (squaredBitmap != source) {
//                source.recycle();
//            }
//
//            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());
//
//            Canvas canvas = new Canvas(bitmap);
//            Paint paint = new Paint();
//            BitmapShader shader = new BitmapShader(squaredBitmap,
//                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//            paint.setShader(shader);
//            paint.setAntiAlias(true);
//
//            float r = size / 2f;
//            canvas.drawCircle(r, r, r, paint);
//
//            squaredBitmap.recycle();
//            return bitmap;
//        }
//
//        @Override
//        public String key() {
//            return "circle";
//        }
//    }


}