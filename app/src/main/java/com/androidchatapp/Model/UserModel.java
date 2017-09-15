package com.androidchatapp.Model;

/**
 * Created by Admin on 01-08-2017.
 */

public class UserModel {

    private String Name;
    private String pro_thumbnail;

    public UserModel() {
    }

    public UserModel(String Name,String pro_thumbnail) {
        this.Name = Name;
        this.pro_thumbnail= pro_thumbnail;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPro_thumbnail() {
        return pro_thumbnail;
    }

    public void setPro_thumbnail(String pro_thumbnail) {
        this.pro_thumbnail = pro_thumbnail;
    }
}
