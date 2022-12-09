package com.example.circlesv0401;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MyAdapter3 extends RecyclerView.Adapter<MyAdapter3.MyViewHolder> {

    Context context;
    ArrayList<CircleComment> circleComments;

    DatabaseReference gettingSingleUser;

    StorageReference commentProfilePhoto;

    String fullName, photo,userName,email;


    public MyAdapter3(Context ct, ArrayList<CircleComment>comments){

        this.context = ct;
        this.circleComments = comments;

    }


    @NonNull
    @Override
    public MyAdapter3.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.comment_template, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter3.MyViewHolder holder, int position) {

        CircleComment comment1 = circleComments.get(position);

        String userID_of_owner = comment1.getCommentUserID();

        gettingSingleUser.child(userID_of_owner).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                CircleUser circleUser1 = snapshot.getValue(CircleUser.class);

                if(circleUser1!=null){

                    userName = circleUser1.getUserUsername();
                    photo = circleUser1.getUserProfilePhoto();
                    holder.txt_coment_maker.setText(userName);


                    commentProfilePhoto = FirebaseStorage.getInstance().getReference("Profile Photos/"+photo);

                    commentProfilePhoto.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(context).load(uri).placeholder(R.drawable.usr1).circleCrop().into(holder.img_comment_photo);

                        }
                    });


                }else {
                    Toast.makeText(context, "Comment username could not be retrived", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Action Failed", Toast.LENGTH_SHORT).show();

            }
        });

        holder.txt_coment_text.setText(comment1.getCommentText());

    }

    @Override
    public int getItemCount() {
        return circleComments.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_coment_maker, txt_coment_text;
        ImageView img_comment_photo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_coment_maker = itemView.findViewById(R.id.tx_comment_maker);
            txt_coment_text = itemView.findViewById(R.id.tx_comment_text);
            gettingSingleUser = FirebaseDatabase.getInstance().getReference("CircleUsersDatabase");

            img_comment_photo = itemView.findViewById(R.id.im_comment_photo);

        }
    }
}
