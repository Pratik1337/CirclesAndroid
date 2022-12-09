package com.example.circlesv0401;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class MyAdapter4 extends RecyclerView.Adapter<MyAdapter4.MyViewHolder> {


    Context context;
    ArrayList<String> likeList;
    DatabaseReference userDB =  FirebaseDatabase.getInstance().getReference("CircleUsersDatabase");
    StorageReference profilePhotoLocation = FirebaseStorage.getInstance().getReference("Profile Photos");

    public MyAdapter4(Context ct, ArrayList<String> l1){

        this.context = ct;
        this.likeList = l1;

    }



    @NonNull
    @Override
    public MyAdapter4.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.like_single_row, parent, false);

        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter4.MyViewHolder holder, int position) {



        String likeElement = likeList.get(position);


        userDB.child(likeElement).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                CircleUser circleUser = snapshot.getValue(CircleUser.class);

                if(circleUser!=null){
                    String userName = circleUser.getUserUsername();
                    String photo = circleUser.getUserProfilePhoto();

                    holder.txt_like_row_username.setText(userName);

//                    profilePhotoLocation.child(photo).getBytes(8024*8024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                        @Override
//                        public void onSuccess(byte[] bytes) {
//
//                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                            holder.img_like_row_photo.setImageBitmap(bitmap);
//
//                        }
//                    });

                    profilePhotoLocation.child(photo).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(context).load(uri).placeholder(R.drawable.usr1).circleCrop().into(holder.img_like_row_photo);

                        }
                    });


                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //holder.txt_like_row_username.setText(likeElement);



    }

    @Override
    public int getItemCount() {
        return likeList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txt_like_row_username;
        ImageView img_like_row_photo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);




            txt_like_row_username = itemView.findViewById(R.id.tx_like_row_username);
            img_like_row_photo = itemView.findViewById(R.id.im_like_row_user_photo);




        }
    }


}
