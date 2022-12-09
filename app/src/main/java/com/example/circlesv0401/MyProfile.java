package com.example.circlesv0401;


import androidx.annotation.NonNull;
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

public class MyProfile extends AppCompatActivity {


    TextView txt_fullname, txt_username, txt_email;
    ProgressBar pgb_profile;
    ImageView img_profiephoto;

    Button btn_update;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String userID;
    String photo, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        txt_email = findViewById(R.id.tx_selected_email);
        txt_fullname = findViewById(R.id.tx_selected_fullname);
        txt_username = findViewById(R.id.tx_selected_username);
        pgb_profile = findViewById(R.id.pb_selected_profile);
        img_profiephoto = findViewById(R.id.im_selected_profilephoto);
        btn_update = findViewById(R.id.bt_myprofile_update);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("CircleUsersDatabase");
        userID = firebaseUser.getUid();


        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                CircleUser circleUser = snapshot.getValue(CircleUser.class);

                if(circleUser!=null){
                    String fullName = circleUser.getUserFullName();
                    String userName = circleUser.getUserUsername();
                    String email = circleUser.getUserEmail();
                    photo = circleUser.getUserProfilePhoto();
                    password = circleUser.getUserPassword();

                    txt_email.setText(email);
                    txt_fullname.setText(fullName);
                    txt_username.setText(userName);

                    try {
                        putImage(photo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    pgb_profile.setVisibility(View.GONE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UpdateMyProfile.class);
                intent.putExtra("user_id", userID);
                intent.putExtra("user_fullname", txt_fullname.getText());
                intent.putExtra("user_username",txt_username.getText());
                intent.putExtra("user_email",txt_email.getText());
                intent.putExtra("user_photo",photo);
                intent.putExtra("user_pass",password);
                startActivity(intent);

            }
        });


    }

    private void putImage(String pht) throws IOException {

        storageReference = FirebaseStorage.getInstance().getReference("Profile Photos/"+pht);

//        File localfile = File.createTempFile("tempfile", ".jpeg");
//
//
//        storageReference.getFile(localfile).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
//
//                if(task.isSuccessful()){
//
//                    Picasso.get().load(localfile).transform(new CircleTransform()).into(img_profiephoto);
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
                Glide.with(getApplicationContext()).load(uri).placeholder(R.drawable.usr1).circleCrop().into(img_profiephoto);
            }
        });


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