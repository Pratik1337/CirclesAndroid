package com.example.circlesv0401;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.MyViewHolder> {

    int circles[];
    Context context;

    public MyAdapter2(Context ct, int  img[]){

        context = ct;
        circles = img;

    }

    @NonNull
    @Override
    public MyAdapter2.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        LayoutInflater inflater= LayoutInflater.from(context);
        View v2 = inflater.inflate(R.layout.circles_selector,parent,false);

        return new MyViewHolder(v2);

    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter2.MyViewHolder holder, int position) {


        final int pos = position;


        holder.img1.setImageResource(circles[position]);


        holder.img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(pos == 6){
                    String timelineDB = "CirclesPostv02";
                    String commentDB = "CirclesCommentDB";
                    String likesDB = "CirclesLikeDB";
                    String imageDB = "Post Images/";

                    SharedPreferences.Editor editor = holder.pref.edit();
                    editor.putString("timeline", null);
                    editor.putString("likes", null);
                    editor.putString("comments",null);
                    editor.putString("images",null);

                    editor.apply();
                    restart();
//
//                    String POSTS_DB = pref2.getString("timeline", "CirclesPostv02");
//                    String LIKES_DB = pref2.getString("likes","CirclesLikeDB" );
//                    String COMMENT_DB = pref2.getString("comments", "CirclesCommentDB");
//                    String IMAGE_DB = pref2.getString("images", "Post Images/");
//




                }else if(pos == 5){
                    String timelineDB = "CP3";
                    String commentDB = "CC3";
                    String likesDB = "CL3";
                    String imageDB = "Post Images 3/";

                   // holder.pref = context.getSharedPreferences(holder.MY_CIRCLES_PREFS, context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = holder.pref.edit();
                    editor.putString("timeline", timelineDB);
                    editor.putString("likes", commentDB);
                    editor.putString("comments",likesDB);
                    editor.putString("images",imageDB);

                    editor.apply();

                    restart();

                }

                else if(pos == 4){
                    String timelineDB = "CP4";
                    String commentDB = "CC4";
                    String likesDB = "CL4";
                    String imageDB = "Post Images 4/";

                    // holder.pref = context.getSharedPreferences(holder.MY_CIRCLES_PREFS, context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = holder.pref.edit();
                    editor.putString("timeline", timelineDB);
                    editor.putString("likes", commentDB);
                    editor.putString("comments",likesDB);
                    editor.putString("images",imageDB);

                    editor.apply();

                    restart();

                }

                else if(pos == 0){
                    String timelineDB = "CirclesPost";
                    String commentDB = "CC5";
                    String likesDB = "CL5";
                    String imageDB = "Post Images/";

                    // holder.pref = context.getSharedPreferences(holder.MY_CIRCLES_PREFS, context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = holder.pref.edit();
//                    editor.putString("timeline", null);
//                    editor.putString("likes", null);
//                    editor.putString("comments",null);
//                    editor.putString("images",null);

                    editor.putString("timeline", timelineDB);
                    editor.putString("likes", commentDB);
                    editor.putString("comments",likesDB);
                    editor.putString("images",imageDB);

                    editor.apply();

                    restart();

                }


            }
        });


    }

    private void restart() {

        ((CircleHome)context).finish();
        context.startActivity(new Intent(context,CircleHome.class));

    }


    @Override
    public int getItemCount() {
        return circles.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView img1;

        public static final String MY_CIRCLES_PREFS = "Circles_Timelines";
        SharedPreferences pref;
        String selection;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img1 = itemView.findViewById(R.id.image_circle);

            pref = context.getSharedPreferences(MY_CIRCLES_PREFS, context.MODE_PRIVATE);
            //selection = pref.getString("timeline", "CirclesPostv02");


        }
    }

}
