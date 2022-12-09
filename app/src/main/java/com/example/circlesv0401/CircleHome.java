package com.example.circlesv0401;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class CircleHome extends AppCompatActivity {

    RecyclerView rec_main_timeline, rec_circles_select;
    ImageView img_home_profilephoto;
    ProgressBar pgb_circle_home;


    public static final String MY_CIRCLES_PREFS = "Circles_Timelines";
    SharedPreferences pref;


    //String POSTS_DB = "CirclesPostv02";


    MyAdapter adapter;
    MyAdapter2 adapter2;

    StorageReference sr;
    LinearLayoutManager linearLayoutManager2;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    ArrayList<CirclePosts> list;

    FirebaseUser currentUser;

    DatabaseReference forProfilePic;

    String current_user_profile_photo;
    int flg = 0;

    FloatingActionButton fab_reset, fab_profile, fab_upload, fab_logout ;

   // int[] circle_images;
    //int circle_images[] = {R.drawable.circle_7,R.drawable.circle_6,R.drawable.circle_5,R.drawable.circle_4,R.drawable.nature_timeline,R.drawable.food_timeline,R.drawable.main_timeline};

    int circle_images_1[] = {R.drawable.circle_7,R.drawable.circle_6,R.drawable.circle_5,R.drawable.circle_4,R.drawable.nature_timeline,R.drawable.food_timeline,R.drawable.c1_selected};
    int circle_images_2[] = {R.drawable.circle_7,R.drawable.circle_6,R.drawable.circle_5,R.drawable.circle_4,R.drawable.nature_timeline,R.drawable.c2_selected,R.drawable.main_timeline};
    int circle_images_3[] = {R.drawable.circle_7,R.drawable.circle_6,R.drawable.circle_5,R.drawable.circle_4,R.drawable.c3_selected,R.drawable.food_timeline,R.drawable.main_timeline};
    int circle_images_4[] = {R.drawable.circle_7,R.drawable.circle_6,R.drawable.circle_5,R.drawable.circle_4,R.drawable.nature_timeline,R.drawable.food_timeline,R.drawable.main_timeline};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_home);

        accessSharedPref();

        String POSTS_DB = pref.getString("timeline", "CirclesPostv02");


//        //makeselector(circle_images);
//        linearLayoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,true);
//            linearLayoutManager2.setReverseLayout(true);
//            linearLayoutManager2.setStackFromEnd(true);
//            rec_circles_select.setLayoutManager(linearLayoutManager2);
//
//
//
//            adapter2 = new MyAdapter2(this,circle_images);
//            rec_circles_select.setAdapter(adapter2);



        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference root = db.getReference().child(POSTS_DB);


        pgb_circle_home = findViewById(R.id.pb_circle_home);

        rec_main_timeline = findViewById(R.id.rc_circles_timeline);
        rec_circles_select = findViewById(R.id.rc_circles_select);

        img_home_profilephoto = findViewById(R.id.im_home_profilephoto);


        fab_reset = findViewById(R.id.fb_refresh);
        fab_profile = findViewById(R.id.fb_profile);
        fab_upload = findViewById(R.id.fb_upload);
        fab_logout = findViewById(R.id.fb_logout);




        if(POSTS_DB.equals("CirclesPostv02")){

//            linearLayoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,true);
//            linearLayoutManager2.setReverseLayout(true);
//            linearLayoutManager2.setStackFromEnd(true);
//            rec_circles_select.setLayoutManager(linearLayoutManager2);
//            adapter2 = new MyAdapter2(this,circle_images);
//            rec_circles_select.setAdapter(adapter2);
            makeselector(circle_images_1);

        }else if (POSTS_DB.equals("CP3")){

            makeselector(circle_images_2);

        }else if(POSTS_DB.equals("CP4")){
            makeselector(circle_images_3);

        }else if(POSTS_DB.equals("CirclesPost")) {
            makeselector(circle_images_4);
        }




        fab_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                startActivity(getIntent());

            }
        });

        fab_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), MyProfile.class));

            }
        });

        fab_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), UploadPost.class));

            }
        });

        fab_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                startActivity(new Intent(getApplicationContext(), LogoutMain.class));

            }
        });




        img_home_profilephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), MyProfile.class));
            }
        });

//        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);
//        rec_main_timeline.setLayoutManager(linearLayoutManager);


        list = new ArrayList<>();
        adapter = new MyAdapter(this, list);


//        CirclePosts p_dummy = new CirclePosts("---------","------","-------------","DefaultPost.jpg","-----------");


        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    CirclePosts circlePosts = dataSnapshot.getValue(CirclePosts.class);
                    //list.add(circlePosts);

//                    if(flg ==0){
//                        list.add(0,p_dummy);
//                        flg= 1;
//                    }
//                    list.add(1,circlePosts);
                    list.add(0,circlePosts);
                    //adapter.notifyItemChanged(1);
                    //adapter.notifyDataSetChanged();

                }
               //adapter.notifyDataSetChanged();

                staggeredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);

                //staggeredGridLayoutManager.setReverseLayout(true);
                rec_main_timeline.setAdapter(null);
                rec_main_timeline.setLayoutManager(staggeredGridLayoutManager);
                rec_main_timeline.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //adapter.notifyDataSetChanged();

//
//        linearLayoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,true);
//        linearLayoutManager2.setReverseLayout(true);
//        linearLayoutManager2.setStackFromEnd(true);
//        rec_circles_select.setLayoutManager(linearLayoutManager2);
//
//
//
//        adapter2 = new MyAdapter2(this,circle_images);
//        rec_circles_select.setAdapter(adapter2);



        forProfilePic = FirebaseDatabase.getInstance().getReference("CircleUsersDatabase");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID = currentUser.getUid();

        forProfilePic.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CircleUser circleUser = snapshot.getValue(CircleUser.class);

                if(circleUser!=null){
                    String fullName = circleUser.getUserFullName();
                    String userName = circleUser.getUserUsername();
                    String email = circleUser.getUserEmail();
                    current_user_profile_photo = circleUser.getUserProfilePhoto();


                    sr = FirebaseStorage.getInstance().getReference("Profile Photos/"+current_user_profile_photo);
//                    try {
//                        File file2 = File.createTempFile("tempfile",".jpg");
//                        sr.getFile(file2).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//
//                                Picasso.get().load(file2).transform(new MyProfile.CircleTransform()).into(img_home_profilephoto);
//                                pgb_circle_home.setVisibility(View.GONE);
//                            }
//                        });
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                    sr.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(getApplicationContext()).load(uri).placeholder(R.drawable.usr1).circleCrop().into(img_home_profilephoto);
                            pgb_circle_home.setVisibility(View.GONE);
                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


    }

    private void makeselector(int[] cir) {

        linearLayoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,true);
        linearLayoutManager2.setReverseLayout(true);
        linearLayoutManager2.setStackFromEnd(true);
        rec_circles_select.setLayoutManager(linearLayoutManager2);



        adapter2 = new MyAdapter2(this,cir);
        rec_circles_select.setAdapter(adapter2);

    }

    private void accessSharedPref() {

        pref = getApplicationContext().getSharedPreferences(MY_CIRCLES_PREFS, MODE_PRIVATE);

    }


}