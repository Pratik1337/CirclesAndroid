package com.example.circlesv0401;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.zolad.zoominimageview.ZoomInImageViewAttacher;

import java.util.ArrayList;

public class SinglePostSelected extends AppCompatActivity {


    TextView txt_full_name, txt_username,txt_post_date, txt_like_cnt, txt_comment_cnt, txt_post_description;
    ImageView img_profile_photo, img_post_photo, img_smiley_icon, img_comment_icon;
    RecyclerView rec_comments;
    EditText edt_write_comment;

    DatabaseReference postReference, userReference, commentsReference, likesReference;
    StorageReference postProfile, postImages;
    String userID;
    String postID;
    String pref_userID;
    MyAdapter3 adapter3;
    boolean likeFlag = true;

    public static final String MY_PREFS_NAME = "Circle_User_Detail_Preferences";
    SharedPreferences pref;

    public static final String MY_CIRCLES_PREFS = "Circles_Timelines";
    SharedPreferences pref2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post_selected);


        txt_full_name = findViewById(R.id.tx_selectedPost_user_fullname);
        txt_username = findViewById(R.id.tx_selectedPost_username);
        txt_post_date = findViewById(R.id.tx_selectedPost_date);
        txt_like_cnt = findViewById(R.id.tx_selectedPost_likes_count);
        txt_comment_cnt = findViewById(R.id.tx_selectedPost_comment_count);
        txt_post_description = findViewById(R.id.tx_selectedPost_description);

        img_profile_photo = findViewById(R.id.im_selectedPost_profilephoto);
        img_post_photo = findViewById(R.id.im_selectedPost_post_image);
        img_smiley_icon = findViewById(R.id.im_selectedPost_icon_smiley);
        img_comment_icon = findViewById(R.id.im_selectedPost_icon_comment);

        rec_comments = findViewById(R.id.rv_selectedPost_nested);

        edt_write_comment = findViewById(R.id.ed_selectedPost_post_comment_input);


        accessSharedPref2();

        String POSTS_DB = pref2.getString("timeline", "CirclesPostv02");
        String LIKES_DB = pref2.getString("likes","CirclesLikeDB" );
        String COMMENT_DB = pref2.getString("comments", "CirclesCommentDB");
        String IMAGE_DB = pref2.getString("images", "Post Images/");

        postReference = FirebaseDatabase.getInstance().getReference(POSTS_DB);
        userReference = FirebaseDatabase.getInstance().getReference("CircleUsersDatabase");
        commentsReference = FirebaseDatabase.getInstance().getReference(COMMENT_DB);
        likesReference = FirebaseDatabase.getInstance().getReference(LIKES_DB);

       // postProfile = FirebaseStorage.getInstance().getReference("Profile Photos");
        //postImages = FirebaseStorage.getInstance().getReference("Post Images");



        getShared();
        pref_userID= pref.getString("c_user_id", null);

        getDataFromIntent();


       // Glide.with(getApplicationContext()).

        getUserData();

        String img_db = IMAGE_DB;
        getPostData(img_db);

        getCommentData();

        getLikeButtonStatus(postID, pref_userID);

        getCommentCount(postID);




        edt_write_comment.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {


                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    String comment_txt =  edt_write_comment.getText().toString().trim();
                    edt_write_comment.clearFocus();

                    if(comment_txt != null){
                        String comment_maker = pref_userID;

                        CircleComment circleComment = new CircleComment(comment_maker,comment_txt);

                        commentsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                commentsReference.child(postID).push().setValue(circleComment);

                                rec_comments.setLayoutManager(null);
                                rec_comments.setAdapter(null);
                                getCommentData();

                                Toast.makeText(getApplicationContext(), "Comment inserted", Toast.LENGTH_SHORT).show();
                                edt_write_comment.setText("");
                                //adapter3.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getApplicationContext(), "Failed to  insert comment", Toast.LENGTH_SHORT).show();
                            }
                        });



                    }else{
                        Toast.makeText(getApplicationContext().getApplicationContext(), "Type something first!", Toast.LENGTH_SHORT).show();
                    }



                }



                return true;


            }
        });




        img_smiley_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // likeFlag = true;
                //postID  = c1.getPostID();
                likeFlag = true;
                likesReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(likeFlag == true){

                            if(snapshot.child(postID).hasChild(pref_userID)){
                                likesReference.child(postID).child(pref_userID).removeValue();
                                likeFlag = false;
                               img_smiley_icon.setImageResource(R.drawable.smiler_like_bw);
                                // holder.getLikeButtonStatus(postID, thisUserID);

                            }else {
                                likesReference.child(postID).child(pref_userID).setValue(true);
                                likeFlag = false;
                                img_smiley_icon.setImageResource(R.drawable.smiler_like_color);
                                // holder.getLikeButtonStatus(postID, thisUserID);

                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Toast.makeText(getApplicationContext(), "DataBase Error In Likes Section", Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });




    }



    private void accessSharedPref2() {

        pref2 = getApplicationContext().getSharedPreferences(MY_CIRCLES_PREFS, MODE_PRIVATE);
    }

    private void getCommentCount(String postID) {

        commentsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child(postID).hasChildren()){
                    int comment_count = (int)snapshot.child(postID).getChildrenCount();
                    txt_comment_cnt.setText(""+comment_count);
                }else{
                    txt_comment_cnt.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void getLikeButtonStatus(final String postkey, final String uid) {

        //likesReference=FirebaseDatabase.getInstance().getReference("CirclesLikeDB");
        likesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(postkey).hasChild(uid)){
                    int likeCount =(int) snapshot.child(postkey).getChildrenCount();
                    img_smiley_icon.setImageResource(R.drawable.smiler_like_color);
                    //txt_likes_count.setText(likeCount+" Likes");
                    if(likeCount>1){
                        txt_like_cnt.setText(likeCount+" Likes");
                        return;
                    }else if(likeCount==1){
                        txt_like_cnt.setText(likeCount+" Like");
                        return;
                    }else if(likeCount ==0){
                        txt_like_cnt.setText("");
                        return;
                    }

                }else {

                    int likeCount =(int) snapshot.child(postkey).getChildrenCount();
                    img_smiley_icon.setImageResource(R.drawable.smiler_like_bw);
                    //txt_likes_count.setText(likeCount+" Likes");
                    if(likeCount>1){
                        txt_like_cnt.setText(likeCount+" Likes");
                        return;
                    }else if(likeCount==1){
                        txt_like_cnt.setText(likeCount+" Like");
                        return;
                    }else if (likeCount == 0){
                        txt_like_cnt.setText("");
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        txt_like_cnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                likesReference.child(postkey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String like_key = "";

                        ArrayList<String> ar = new ArrayList<>();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            like_key = dataSnapshot.getKey();
                            ar.add(like_key);

                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                        View dialogueView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.activity_likes_view,null);

                        RecyclerView dialogue_box_rec_likeView;

                        dialogue_box_rec_likeView = dialogueView.findViewById(R.id.rv_likes_main);

                        MyAdapter4 adapter4;
                        adapter4 = new MyAdapter4(getApplicationContext(), ar);

                        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);

                        dialogue_box_rec_likeView.setAdapter(adapter4);
                        dialogue_box_rec_likeView.setLayoutManager(staggeredGridLayoutManager);



                        builder.setView(dialogueView);

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        alertDialog.getWindow().setLayout(900, 1000);
//                      builder.show();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });


    }


    private void getShared() {

        pref = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

    }

    private void getDataFromIntent() {

        userID = getIntent().getStringExtra("userID");
        postID = getIntent().getStringExtra("postID");

    }

    private void getUserData() {

        userReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                CircleUser circleUser = snapshot.getValue(CircleUser.class);

                if(circleUser!=null){
                    String fullName = circleUser.getUserFullName();
                    String userName = circleUser.getUserUsername();
                    String photo = circleUser.getUserProfilePhoto();

                    txt_full_name.setText(fullName);
                    txt_username.setText(userName);

                    postProfile = FirebaseStorage.getInstance().getReference("Profile Photos/"+photo);
//
                    postProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Glide.with(getApplicationContext()).load(uri).placeholder(R.drawable.usr1).circleCrop().into(img_profile_photo);
                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void getPostData(String image_db) {

        postReference.child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                CirclePosts circlePosts = snapshot.getValue(CirclePosts.class);

                String postDate = circlePosts.getPostDate();
                String postDesc = circlePosts.getPostDescription();
                String postPhoto = circlePosts.getPostImageName();

                txt_post_date.setText(postDate);
                txt_post_description.setText(postDesc);

                postImages = FirebaseStorage.getInstance().getReference(image_db+postPhoto);


                ZoomInImageViewAttacher mIvAttacter = new ZoomInImageViewAttacher();


                postImages.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {


                        Glide.with(getApplicationContext()).load(uri).placeholder(R.drawable.place_holder_post).into(img_post_photo);

                        mIvAttacter.setZoomReleaseAnimDuration(0250);
                        mIvAttacter.attachImageView(img_post_photo);

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void getCommentData() {

        ArrayList<CircleComment> forComments;
        forComments = new ArrayList<>();


        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setReverseLayout(true);
        adapter3 = new MyAdapter3(getApplicationContext(),forComments);
        //adapter3.notifyDataSetChanged();


        commentsReference.child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    CircleComment c1 = dataSnapshot.getValue(CircleComment.class);
                    forComments.add(0,c1);
                    //adapter3.notifyDataSetChanged();
                   // adapter3.notifyItemInserted(0);
                }

                adapter3.notifyDataSetChanged();
//                rec_comments.setAdapter(null);
//                rec_comments.setLayoutManager(null);
//                rec_comments.setLayoutManager(staggeredGridLayoutManager);
//                rec_comments.setAdapter(adapter3);
                rec_comments.setLayoutManager(staggeredGridLayoutManager);
                rec_comments.setAdapter(adapter3);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        //adapter3.notifyDataSetChanged();
//        rec_comments.setLayoutManager(staggeredGridLayoutManager);
//        rec_comments.setAdapter(adapter3);
        //adapter3.notifyDataSetChanged();

    }


}