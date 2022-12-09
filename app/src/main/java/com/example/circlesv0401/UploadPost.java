package com.example.circlesv0401;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UploadPost extends AppCompatActivity {

    Button btn_select, btn_upload;
    EditText edt_desc;
    ImageView img_selected;
    ProgressBar pgb_uploadPost;

    CirclePosts circlePosts ;

    Uri selected_image;

    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String current_id; //= getIntent().getStringExtra("current_uid");


    public static final String MY_PREFS_NAME = "Circle_User_Detail_Preferences";
    SharedPreferences pref;


    // shared preference section

    public static final String MY_CIRCLES_PREFS = "Circles_Timelines";
    SharedPreferences pref2;



    // shared preference section


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_post);


        getThisPreference();

        current_id= pref.getString("c_user_id", null);

//        Intent i = getIntent();
//        current_id = i.getStringExtra("current_uid");



        pref2 = getApplicationContext().getSharedPreferences(MY_CIRCLES_PREFS, MODE_PRIVATE);

        String POSTS_DB = pref2.getString("timeline", "CirclesPostv02");
//        String LIKES_DB = pref2.getString("likes","CirclesLikeDB" );
//        String COMMENT_DB = pref2.getString("comments", "CirclesCommentDB");
        String IMAGE_DB = pref2.getString("images", "Post Images/");




        btn_select = findViewById(R.id.bt_image_selector);
        btn_upload = findViewById(R.id.bt_upload_post);

        edt_desc = findViewById(R.id.ed_post_description);

        img_selected = findViewById(R.id.im_post_selectedimage);

        pgb_uploadPost = findViewById(R.id.pb_upload_post);



        circlePosts = new CirclePosts();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(POSTS_DB).push();
        String pkey = databaseReference.getKey();

        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pgb_uploadPost.setVisibility(View.VISIBLE);
                selectImage();

            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pgb_uploadPost.setVisibility(View.VISIBLE);
                String image_db = IMAGE_DB;
                uploadPostData(pkey, image_db);

            }
        });

    }


    private void getThisPreference() {

        pref = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
    }

    private void uploadPostData(String pkey, String image_db) {


        String p_maker_uid = current_id;

        String p_made_description = edt_desc.getText().toString().trim();

        String p_made_image_name;

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.CANADA);
        Date unique_date = new Date();

        String p_made_date= format.format(unique_date);

        SimpleDateFormat format2 = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss", Locale.CANADA);
        Date unique_date2 = new Date();


        if(selected_image!=null) {
            p_made_image_name = format2.format(unique_date2);
            storageReference = FirebaseStorage.getInstance().getReference(image_db+p_made_image_name);
            storageReference.putFile(selected_image);
        }else{

            p_made_image_name = "DefaultPost.jpg";

        }


        String post_key = pkey;
        addData(p_maker_uid, p_made_description,p_made_date,p_made_image_name, post_key);

    }

    private void addData(String id, String desc, String date, String image, String postID) {

        circlePosts =new CirclePosts(id,date,desc,image,postID);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                databaseReference.setValue(circlePosts);

                Toast.makeText(getApplicationContext(), "Insertion Successful", Toast.LENGTH_SHORT).show();
                pgb_uploadPost.setVisibility(View.GONE);

                finish();
                startActivity(new Intent(getApplicationContext(), CircleHome.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getApplicationContext(), "Insertion Failed", Toast.LENGTH_SHORT).show();
                pgb_uploadPost.setVisibility(View.GONE);

            }
        });

    }

//
//    private void selectImage() {
//
//        Intent i = new Intent();
//        i.setType("image/*");
//        i.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(i, 200);
//    }

//    private void selectImage() {
//
//        String[] type ={ "image/*"} ;
//
//        Intent i = ImagePicker.with(UploadPost.this)
//                .crop()	    			//Crop image(Optional), Check Customization for more option
//                .cropFreeStyle()	    //Let the user to resize crop bounds
//                .galleryOnly()
//                .maxResultSize(4080, 4080,true)	//Final image resolution will be less than 1080 x 1080(Optional)
//                .createIntent();
//        startActivityForResult(i, 200);
//
//
//    }


    private void selectImage() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setOutputCompressQuality(80).setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                .start(this);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);




//        if(requestCode == 200 && data!= null) {
//
//            selected_image = data.getData();
//            img_selected.setImageURI(selected_image);
//
//
//            if (img_selected.getDrawable() == null){
//                //Image doesn´t exist.
//                Toast.makeText(getApplicationContext(), "Failed To get Image", Toast.LENGTH_SHORT).show();
//                pgb_uploadPost.setVisibility(View.GONE);
//            }else{
//                //Image Exists!.
//                Toast.makeText(getApplicationContext(), "Image Selected", Toast.LENGTH_SHORT).show();
//                pgb_uploadPost.setVisibility(View.GONE);
//            }
//
//        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                selected_image = resultUri;
                img_selected.setImageURI(selected_image);
                pgb_uploadPost.setVisibility(View.GONE);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                pgb_uploadPost.setVisibility(View.GONE);
                Exception error = result.getError();
            }
        }



    }
}