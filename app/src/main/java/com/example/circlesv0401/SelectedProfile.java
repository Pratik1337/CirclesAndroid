package com.example.circlesv0401;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.IOException;

public class SelectedProfile extends AppCompatActivity {

    TextView txt_selected_fullname, txt_selected_username, txt_selected_email;
    ProgressBar pgb_selected_profile;
    ImageView img_selected_profiephoto;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_profile);
        //setContentView(R.layout.activity_my_profile);

        txt_selected_email = findViewById(R.id.tx_selected_email);
        txt_selected_fullname = findViewById(R.id.tx_selected_fullname);
        txt_selected_username = findViewById(R.id.tx_selected_username);
        img_selected_profiephoto = findViewById(R.id.im_selected_profilephoto);

        pgb_selected_profile = findViewById(R.id.pb_selected_profile);

        String fname = getIntent().getStringExtra("selected_fullname");
        String uname = getIntent().getStringExtra("selected_username");
        String email = getIntent().getStringExtra("selected_email");
        String photo = getIntent().getStringExtra("selected_image_name");

        txt_selected_email.setText(email);
        txt_selected_fullname.setText(fname);
        txt_selected_username.setText(uname);

        try {
            putImage(photo);
        } catch (IOException e) {
            e.printStackTrace();
        }

        pgb_selected_profile.setVisibility(View.GONE);

    }

    private void putImage(String pht) throws IOException {

        storageReference = FirebaseStorage.getInstance().getReference("Profile Photos/"+pht);

      //  File localfile = File.createTempFile("tempfile", ".jpeg");
//        storageReference.getFile(localfile).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
//
//                if(task.isSuccessful()){
//
//                    // Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
//
//                    // img_profiephoto.setImageBitmap(bitmap);
//                    //Picasso.get().load(pht).into(img_profiephoto);
//                    Picasso.get().load(localfile).transform(new MyProfile.CircleTransform()).into(img_selected_profiephoto);
//
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
                Glide.with(getApplicationContext()).load(uri).placeholder(R.drawable.usr1).circleCrop().into(img_selected_profiephoto);
            }
        });



    }

}