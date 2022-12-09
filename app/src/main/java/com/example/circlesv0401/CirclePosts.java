package com.example.circlesv0401;


public class CirclePosts {

    //String p_owner_uid, p_date_stamp, p_desciption, p_image_name;

    String postOwner, postDate, postDescription, postImageName, postID;



    public CirclePosts() {
    }


    public CirclePosts(String postOwner, String postDate, String postDescription, String postImageName, String postID) {
        this.postOwner = postOwner;
        this.postDate = postDate;
        this.postDescription = postDescription;
        this.postImageName = postImageName;
        this.postID = postID;
    }

    public String getPostOwner() {
        return postOwner;
    }

    public void setPostOwner(String postOwner) {
        this.postOwner = postOwner;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPostImageName() {
        return postImageName;
    }

    public void setPostImageName(String postImageName) {
        this.postImageName = postImageName;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }
}
