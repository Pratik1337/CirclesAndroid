package com.example.circlesv0401;


public class CircleComment {


    String commentUserID, commentText;

    public CircleComment() {
    }

    public CircleComment(String commentUserID, String commentText) {
        this.commentUserID = commentUserID;
        this.commentText = commentText;
    }

    public String getCommentUserID() {
        return commentUserID;
    }

    public void setCommentUserID(String commentUserID) {
        this.commentUserID = commentUserID;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}
