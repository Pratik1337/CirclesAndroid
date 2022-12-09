package com.example.circlesv0401;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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
import com.zolad.zoominimageview.ZoomInImageViewAttacher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    StorageReference storageReference, sr,deletePhotoReference;
    ArrayList<CirclePosts> circlePosts;
    Context context;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference,toDelete, commentsReference, getCommentsReference, likesReference;
    String userID, thisUserID, postID;
    Boolean likeFlag;
    MyAdapter3 adapter3;

    StaggeredGridLayoutManager staggeredGridLayoutManager;






    // shared preference section


    // shared preference section
//
//    String POSTS_DB = "CirclesPostv02";
//    String LIKES_DB = "CirclesLikeDB" ;
//    String COMMENT_DB = "CirclesCommentDB";
//    String IMAGE_DB = "Post Images/";



    public MyAdapter(Context ct, ArrayList<CirclePosts> circlePostsArrayList){

        this.context = ct;
        this.circlePosts = circlePostsArrayList;


    }


    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.post_template, parent, false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        thisUserID = firebaseUser.getUid();




        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder,  int position) {


        holder.template_fullname.setVisibility(View.VISIBLE);
        holder.template_username.setVisibility(View.VISIBLE);
        holder.template_image_profile.setVisibility(View.VISIBLE);
        holder.template_date.setVisibility(View.VISIBLE);
        holder.img_icon_comment.setVisibility(View.VISIBLE);
        holder.img_icon_bin.setVisibility(View.VISIBLE);
        holder.img_icon_smiley.setVisibility(View.VISIBLE);
        holder.txt_likes_count.setVisibility(View.VISIBLE);
        holder.tempalte_description.setVisibility(View.VISIBLE);
        holder.template_image_post.setVisibility(View.VISIBLE);
        holder.txt_comment_count.setVisibility(View.VISIBLE);
        holder.txt_likes_count.setText("");
        holder.img_icon_smiley.setImageResource(R.drawable.smiler_like_bw);

        holder.edt_post_comment.setVisibility(View.GONE);
        holder.rec_nested.setVisibility(View.GONE );
        //holder.rec_nested.setAdapter(null);
        holder.template_image_profile.setImageResource(R.drawable.usr1);
        holder.template_image_post.setImageResource(R.drawable.place_holder_post);
        holder.img_icon_comment.setTag("mode0");


//        if(flg == 0){
//
//            holder.template_fullname.setVisibility(View.INVISIBLE);
//            holder.template_username.setVisibility(View.INVISIBLE);
//            holder.template_image_profile.setVisibility(View.INVISIBLE);
//            holder.template_date.setVisibility(View.INVISIBLE);
//            holder.img_icon_comment.setVisibility(View.INVISIBLE);
//            holder.img_icon_bin.setVisibility(View.INVISIBLE);
//            holder.img_icon_smiley.setVisibility(View.INVISIBLE);
//            holder.txt_likes_count.setVisibility(View.INVISIBLE);
//            holder.tempalte_description.setVisibility(View.INVISIBLE);
//
//            holder.template_image_post.setVisibility(View.INVISIBLE);
//            holder.txt_comment_count.setVisibility(View.INVISIBLE);
//
//            flg = 1;
//
//
//        }

        CirclePosts c1 = circlePosts.get(position);
        holder.tempalte_description.setText(c1.getPostDescription());
        holder.template_date.setText(c1.getPostDate());
        String post_id = c1.getPostID();
        String img_post_name = c1.getPostImageName();


        postID  = c1.getPostID();

        final String huserID=firebaseUser.getUid();
        //final String postID = c1.getPostID();
        final String postkey = c1.getPostID();

        final int pos  = position;


        holder.getLikeButtonStatus(postkey, huserID);

        //String id = c1.getPostOwner();

        userID = c1.getPostOwner();
       // postID  = c1.getPostID();


        ///comments declerations
        ArrayList<CircleComment> forComments;
        forComments = new ArrayList<>();


//        final String img_post_name = c1.getPostImageName();
//        storageReference = FirebaseStorage.getInstance().getReference("Post Images/"+img_post_name);



        holder.setPostImage(img_post_name);


        databaseReference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    CircleUser circleUser = snapshot.getValue(CircleUser.class);
//                CirclePosts c4 = circlePosts.get(pos);
//                String img = c4.getPostImageName();
//                holder.setPostImage(img);

                if(circleUser!=null){
                    String fullName = circleUser.getUserFullName();
                    String userName = circleUser.getUserUsername();
                    String email = circleUser.getUserEmail();
                    String photo = circleUser.getUserProfilePhoto();

                    holder.template_fullname.setText(fullName);
                    holder.template_username.setText(userName);
                    holder.hidden_email.setText(email);
                    holder.hidden_image_name.setText(photo);

//                    sr = FirebaseStorage.getInstance().getReference("Profile Photos/"+photo);

                    sr = FirebaseStorage.getInstance().getReference("Profile Photos/");


                    sr.child(photo).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Glide.with(context).load(uri).placeholder(R.drawable.usr1).circleCrop().into(holder.template_image_profile);
                        }
                    });


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //for current active user and its profile



        // getting image by old method


//        String img_post_name = c1.getPostImageName();
//        storageReference = FirebaseStorage.getInstance().getReference("Post Images/"+img_post_name);
//        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//
//                Glide.with(context).load(uri).placeholder(R.drawable.place_holder_post).into(holder.template_image_post);
//
//            }
//        });



        if(thisUserID.equals(userID)){
            holder.img_icon_bin.setVisibility(View.VISIBLE);

            holder.template_image_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context,MyProfile.class));

                }
            });


        }else{
            holder.img_icon_bin.setVisibility(View.GONE);
            holder.template_image_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, SelectedProfile.class);
                    intent.putExtra("selected_fullname", holder.template_fullname.getText());
                    intent.putExtra("selected_username",holder.template_username.getText());
                    intent.putExtra("selected_email",holder.hidden_email.getText());
                    intent.putExtra("selected_image_name",holder.hidden_image_name.getText());
                    context.startActivity(intent);

                }
            });

        }



        CirclePosts c2 = circlePosts.get(position);
        final String pid = c2.getPostID();



        holder.img_icon_bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder
                        = new AlertDialog
                        .Builder(context);


                builder.setTitle("Delete Post");
                builder.setMessage("Are you sure you want to delete this post?");

                builder.setCancelable(false);
                builder.setIcon(R.drawable.confirm_delete_icon);


                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        toDelete.child(post_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){

                                    Toast.makeText(context.getApplicationContext(), "Post Deleted ", Toast.LENGTH_SHORT).show();
                                    context.startActivity(new Intent(context, CircleHome.class));

                                    notifyDataSetChanged();
                                    //notifyDataSetChanged();


                                }else{
                                    Toast.makeText(context.getApplicationContext(), "Post not Deleted ", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                        if(!img_post_name.equals("DefaultPost.jpg")){
                            //deletePhotoReference = FirebaseStorage.getInstance().getReference(IMAGE_DB+img_post_name);

                            deletePhotoReference.child(img_post_name).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(context, "Post deleted", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }




                    }
                });

                builder.setNegativeButton("No",new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which)
                    {

                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();


                alertDialog.getWindow().setLayout(200,800);
                alertDialog.setCancelable(true);
                alertDialog.show();


            }
        });


        holder.img_icon_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tg_comment = String.valueOf(holder.img_icon_comment.getTag());
                if(tg_comment.equals("mode0")) {
                    // opened for comment on
                    holder.img_icon_comment.setTag("mode1");
                    holder.edt_post_comment.setVisibility(View.VISIBLE);
                    holder.rec_nested.setVisibility(View.VISIBLE);
                    //holder.edt_post_comment.requestFocus();


//                    holder.rec_nested.setAdapter(null);
//                    holder.rec_nested.setLayoutManager(null);
                  //  holder.rec_nested.setAdapter(adapter3);
//                    holder.rec_nested.setLayoutManager(staggeredGridLayoutManager);
//                    holder.rec_nested.setAdapter(adapter3);

                }else {
                    //open for comment off

                    holder.img_icon_comment.setTag("mode0");
                    holder.edt_post_comment.setVisibility(View.GONE);
                    holder.rec_nested.setVisibility(View.GONE);

                    //holder.rec_nested.setAdapter(null);
                   // holder.rec_nested.setLayoutManager(null);
                }
            }
        });


        // retrive comments

        getCommentsReference.child(pid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                forComments.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    CircleComment c1 = dataSnapshot.getValue(CircleComment.class);
                    forComments.add(0,c1);
                    //adapter3.notifyItemInserted(position);
                    //adapter3.notifyDataSetChanged();
                }
                //adapter3.notifyDataSetChanged();

                holder.rec_nested.setLayoutManager(null);
                holder.rec_nested.setAdapter(null);

                staggeredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
                adapter3 = new MyAdapter3(context,forComments);

                staggeredGridLayoutManager.setReverseLayout(true);
                holder.rec_nested.setLayoutManager(staggeredGridLayoutManager);
                holder.rec_nested.setAdapter(adapter3);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



//        holder.img_icon_send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String comment_txt = holder.edt_post_comment.getText().toString().trim();
//
//                if(comment_txt != null){
//
//                    String comment_maker = thisUserID;
//
//                    CircleComment circleComment = new CircleComment(comment_maker,comment_txt);
//
////                    commentsReference.addListenerForSingleValueEvent(new ValueEventListener() {
////                        @Override
////                        public void onDataChange(@NonNull DataSnapshot snapshot) {
////                            commentsReference.child(post_id).push().setValue(circleComment);
////                            Toast.makeText(context, "Comment inserted", Toast.LENGTH_SHORT).show();
////                            adapter3.notifyDataSetChanged();
////
////                        }
////
////                        @Override
////                        public void onCancelled(@NonNull DatabaseError error) {
////                            Toast.makeText(context, "Failed to  insert comment", Toast.LENGTH_SHORT).show();
////                        }
////                    });
//
//                    commentsReference.child(post_id).push().setValue(circleComment).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if(task.isSuccessful()){
//                                holder.edt_post_comment.setText("");
//                                Toast.makeText(context, "Comment added", Toast.LENGTH_SHORT).show();
//                            }else{
//                                Toast.makeText(context, "Comment could not be added", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//
//                }else{
//                    Toast.makeText(context.getApplicationContext(), "Type something first!", Toast.LENGTH_SHORT).show();
//                }
//
//
//            }
//
//        });


        holder.edt_post_comment.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {


                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String comment_txt = holder.edt_post_comment.getText().toString().trim();
                    holder.edt_post_comment.setText("");
                    if(comment_txt != null){

                        String comment_maker = thisUserID;

                        CircleComment circleComment = new CircleComment(comment_maker,comment_txt);

                    commentsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            commentsReference.child(post_id).push().setValue(circleComment);
                           Toast.makeText(context, "Comment inserted", Toast.LENGTH_SHORT).show();


                           //trying to upodate comments


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(context, "Failed to  insert comment", Toast.LENGTH_SHORT).show();
                        }
                    });

//                    commentsReference.child(post_id).push().setValue(circleComment).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if(task.isSuccessful()){
//                                holder.edt_post_comment.setText("");
//                                Toast.makeText(context, "Comment added", Toast.LENGTH_SHORT).show();
//                            }else{
//                                Toast.makeText(context, "Comment could not be added", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });

                    }else{
                        Toast.makeText(context.getApplicationContext(), "Type something first!", Toast.LENGTH_SHORT).show();
                    }



                }
                return false;

            }
        });




//        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
//
//        holder.rec_nested.setLayoutManager(staggeredGridLayoutManager);
//
//        forComments = new ArrayList<>();
//
//        adapter3 = new MyAdapter3(context,forComments);
//        holder.rec_nested.setAdapter(adapter3);
//
//        CirclePosts c2 = circlePosts.get(position);
//        String pid = c2.getPostID();
//
//       getCommentsReference.child(pid).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    CircleComment c1 = dataSnapshot.getValue(CircleComment.class);
//                    forComments.add(0,c1);
//                }
//                adapter3.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//       });


        /// likes section

        //FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        holder.img_icon_smiley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // likeFlag = true;
                //postID  = c1.getPostID();
                likeFlag = true;
               likesReference.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {

                       if(likeFlag == true){

                           if(snapshot.child(postkey).hasChild(huserID)){
                               likesReference.child(postkey).child(huserID).removeValue();
                               likeFlag = false;
                               holder.img_icon_smiley.setImageResource(R.drawable.smiler_like_bw);
                              // holder.getLikeButtonStatus(postID, thisUserID);


                           }else {
                               likesReference.child(postkey).child(huserID).setValue(true);
                               likeFlag = false;
                               holder.img_icon_smiley.setImageResource(R.drawable.smiler_like_color);
                              // holder.getLikeButtonStatus(postID, thisUserID);


                           }

                       }

                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                       Toast.makeText(context, "DataBase Error In Likes Section", Toast.LENGTH_SHORT).show();

                   }
               });


                //holder.getLikeButtonStatus(postID, thisUserID);




                //String tg_smile = String.valueOf(holder.img_icon_smiley.getTag());
              //  if(tg_smile.equals("mode0")) {
//                    holder.img_icon_smiley.setImageResource(R.drawable.smiler_like_color);
//                    holder.img_icon_smiley.setTag("mode1");




             //   }else {
//                    holder.img_icon_smiley.setImageResource(R.drawable.smiler_like_bw);
//                    holder.img_icon_smiley.setTag("mode0");
             //   }
            }
        });


        holder.txt_likes_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                likesReference.child(postkey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String like_key = "";

                        ArrayList<String> ar = new ArrayList<>();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            //CircleComment c1 = dataSnapshot.getValue(CircleComment.class);
                            like_key = dataSnapshot.getKey();

                            ar.add(like_key);

                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                        View dialogueView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.activity_likes_view,null);

                        RecyclerView dialogue_box_rec_likeView;

                        dialogue_box_rec_likeView = dialogueView.findViewById(R.id.rv_likes_main);

                        MyAdapter4 adapter4;
                        adapter4 = new MyAdapter4(context, ar);

                        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);

                        dialogue_box_rec_likeView.setAdapter(adapter4);
                        dialogue_box_rec_likeView.setLayoutManager(staggeredGridLayoutManager);



                        builder.setView(dialogueView);

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        //alertDialog.getWindow().setLayout(900, 1000);
//                        builder.show();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                /// like adpater and dialogue box here




            }
        });




        holder.template_image_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent pass = new Intent(context, SinglePostSelected.class);

                pass.putExtra("userID",c1.getPostOwner());
                pass.putExtra("postID",c1.getPostID());

                context.startActivity(pass);

            }
        });


       // commentCount(c1.getPostID());

        final String cID = c1.getPostID();

        commentsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child(cID).hasChildren()){
                    int comment_count = (int)snapshot.child(cID).getChildrenCount();
                    holder.txt_comment_count.setText(""+comment_count);


                }else{
                    holder.txt_comment_count.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }




    private void restartThis() {

        context.startActivity(new Intent(context, CircleHome.class));

    }

    @Override
    public int getItemCount() {
        return circlePosts.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView template_fullname, template_username, template_date, tempalte_description, hidden_email, hidden_image_name, txt_likes_count, txt_comment_count;
        ImageView template_image_profile, template_image_post, img_icon_smiley, img_icon_bin, img_icon_heart, img_icon_comment,img_icon_send;
        EditText edt_post_comment;
        RecyclerView rec_nested;
        CardView cv_template;
        ConstraintLayout inner_cl;



        public static final String MY_CIRCLES_PREFS = "Circles_Timelines";




        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            SharedPreferences pref2;
            pref2= context.getSharedPreferences(MY_CIRCLES_PREFS,Context.MODE_PRIVATE);

            String POSTS_DB = pref2.getString("timeline", "CirclesPostv02");
            String LIKES_DB = pref2.getString("likes","CirclesLikeDB" );
            String COMMENT_DB = pref2.getString("comments", "CirclesCommentDB");
            String IMAGE_DB = pref2.getString("images", "Post Images/");


            edt_post_comment = (EditText) itemView.findViewById(R.id.ed_post_comment_input);
            template_fullname = itemView.findViewById(R.id.tx_template_user_fullname);
            tempalte_description = itemView.findViewById(R.id.tx_others_profile_description);
            template_username = itemView.findViewById(R.id.tx_template_username);
            template_date = itemView.findViewById(R.id.tx_template_date);
            hidden_email = itemView.findViewById(R.id.tx_template_hidden_email);
            hidden_image_name = itemView.findViewById(R.id.tx_template_hidden_image_name);

            cv_template = itemView.findViewById(R.id.cv_template_card);
            inner_cl = itemView.findViewById(R.id.inner_cl);

            img_icon_smiley = itemView.findViewById(R.id.im_icon_smiley);
            img_icon_bin = itemView.findViewById(R.id.im_icon_bin);
            //img_icon_heart = itemView.findViewById(R.id.im_icon_heart);
            img_icon_comment = itemView.findViewById(R.id.im_icon_comment);


            txt_likes_count = itemView.findViewById(R.id.tx_template_likes_count);
            txt_comment_count = itemView.findViewById(R.id.tx_template_comment_count);


            rec_nested = itemView.findViewById(R.id.rv_nested);


            template_image_post = itemView.findViewById(R.id.im_template_post_image);
            template_image_profile = itemView.findViewById(R.id.im_template_profilephoto);


            databaseReference = FirebaseDatabase.getInstance().getReference("CircleUsersDatabase");
            toDelete = FirebaseDatabase.getInstance().getReference(POSTS_DB);
            commentsReference = FirebaseDatabase.getInstance().getReference(COMMENT_DB);
            getCommentsReference = FirebaseDatabase.getInstance().getReference().child(COMMENT_DB);
            likesReference = FirebaseDatabase.getInstance().getReference(LIKES_DB);
            storageReference = FirebaseStorage.getInstance().getReference(IMAGE_DB);
            deletePhotoReference = FirebaseStorage.getInstance().getReference(IMAGE_DB);

        }

        public void getLikeButtonStatus(final String postkey, final String uid) {

            //likesReference=FirebaseDatabase.getInstance().getReference(likes_db);
            likesReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child(postkey).hasChild(uid)){
                        int likeCount =(int) snapshot.child(postkey).getChildrenCount();
                        img_icon_smiley.setImageResource(R.drawable.smiler_like_color);
                        //txt_likes_count.setText(likeCount+" Likes");
                        if(likeCount>1){
                            txt_likes_count.setText(likeCount+" Likes");
////                            Notification notification = new NotificationCompat.Builder(context,CircleNotify.POSTS_NOTIFCATION)
////                                    .setSmallIcon(R.drawable.smiler_like_bw)
////                                    .setContentTitle("Circles")
////                                    .setContentText("Image Liked")
////                                    .setCategory(NotificationCompat.CATEGORY_MESSAGE).build();
////
////
////                            NotificationManagerCompat nfmc = NotificationManagerCompat.from(context);
//
//                            nfmc.notify(1,notification);
                            return;
                        }else if(likeCount==1){
                            txt_likes_count.setText(likeCount+" Like");
                            return;
                        }else if(likeCount ==0){
                            txt_likes_count.setText("");

                            return;
                        }

                    }else {

                        int likeCount =(int) snapshot.child(postkey).getChildrenCount();
                        img_icon_smiley.setImageResource(R.drawable.smiler_like_bw);
                        //txt_likes_count.setText(likeCount+" Likes");
                        if(likeCount>1){
                            txt_likes_count.setText(likeCount+" Likes");
                            return;
                        }else if(likeCount==1){
                            txt_likes_count.setText(likeCount+" Like");
                            return;
                        }else if (likeCount == 0){
                            txt_likes_count.setText("");
                            return;
                        }
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }


        private void setPostImage(String img ) {

            if(img.equals("DefaultPost.jpg")){

                template_image_post.setVisibility(View.GONE);


            }else {
                //storageReference = FirebaseStorage.getInstance().getReference(IMAGE_DB+img);
                storageReference.child(img).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Glide.with(context).load(uri).placeholder(R.drawable.place_holder_post).into(template_image_post);
                        ZoomInImageViewAttacher mIvAttacter = new ZoomInImageViewAttacher();

                        mIvAttacter.setZoomReleaseAnimDuration(0250);
                        mIvAttacter.attachImageView(template_image_post);

                    }
                });
            }



        }


    }




}
