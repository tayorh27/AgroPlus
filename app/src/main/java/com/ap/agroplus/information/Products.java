package com.ap.agroplus.information;

/**
 * Created by sanniAdewale on 10/03/2017.
 */

public class Products {

    public int id;
    public String username, location, image_dp, product_image, email, mobile, time, category, caption, price, product_link;

    public Products(int id, String username, String location, String image_dp, String product_image, String email, String mobile, String time, String caption, String price, String category, String product_link) {
        this.id = id;
        this.username = username;
        this.location = location;
        this.image_dp = image_dp;
        this.product_image = product_image;
        this.email = email;
        this.mobile = mobile;
        this.time = time;
        this.category = category;
        this.caption = caption;
        this.price = price;
        this.product_link = product_link;
    }

}
