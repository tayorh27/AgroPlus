package com.ap.agroplus.information;

/**
 * Created by sanniAdewale on 10/03/2017.
 */

public class User {

    public int id;
    public String username, email, mobile, password, image_path, local_path;

    public User(int id, String username, String email, String mobile, String password, String image_path, String local_path) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.image_path = image_path;
        this.local_path = local_path;
    }
}
