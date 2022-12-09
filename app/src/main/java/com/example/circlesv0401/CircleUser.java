package com.example.circlesv0401;


public class CircleUser {

    String userFullName, userUsername, userEmail, userProfilePhoto, userPassword;

    public CircleUser() {
    }

    public CircleUser(String userFullName, String userUsername, String userEmail, String userProfilePhoto, String userPassword) {
        this.userFullName = userFullName;
        this.userUsername = userUsername;
        this.userEmail = userEmail;
        this.userProfilePhoto = userProfilePhoto;
        this.userPassword = userPassword;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserUsername() {
        return userUsername;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserProfilePhoto() {
        return userProfilePhoto;
    }

    public void setUserProfilePhoto(String userProfilePhoto) {
        this.userProfilePhoto = userProfilePhoto;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
