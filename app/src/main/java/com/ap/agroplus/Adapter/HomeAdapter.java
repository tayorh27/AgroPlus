package com.ap.agroplus.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ap.agroplus.Callbacks.ClickCallback;
import com.ap.agroplus.R;
import com.ap.agroplus.TimeUpdate;
import com.ap.agroplus.information.Products;
import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.bijoysingh.starter.images.ImageLoaderManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sanniAdewale on 10/03/2017.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {

    Context context;
    LayoutInflater inflater;
    ArrayList<Products> productsArrayList = new ArrayList<>();
    ClickCallback clickCallback;

    public HomeAdapter(Context context, ClickCallback clickCallback) {
        this.context = context;
        this.clickCallback = clickCallback;
        inflater = LayoutInflater.from(context);
    }

    public void LoadRecyclerView(ArrayList<Products> productsArrayList) {
        this.productsArrayList = productsArrayList;
        notifyDataSetChanged();
    }

    @Override
    public HomeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.home_layout, parent, false);
        HomeHolder holder = new HomeHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(HomeHolder holder, int position) {
        Products current = productsArrayList.get(position);
        Date getTime = new Date(current.time);
        long milli = getTime.getTime();
        holder.username.setText(current.username);
        holder.location.setText(current.location);
        holder.caption.setText(current.caption);
        holder.time.setText(TimeUpdate.setAgo(milli));
        holder.price.setText("₦" + current.price);
        holder.cat.setText(current.category);
        String image_dp_url = current.image_dp.replace(" ", "%20");
        String initial_url = "";
        try {
            JSONArray jsonArray = new JSONArray(current.product_image);
            initial_url = jsonArray.getString(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String product_image_url = initial_url.replace(" ", "%20");
        Glide.with(context).load(image_dp_url).override(48, 48).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(holder.imageDp);
        Glide.with(context).load(product_image_url).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().crossFade().into(holder.productDp);
        //ImageLoaderManager.displayImage(context, image_dp_url, holder.imageDp);
        //ImageLoaderManager.displayImage(context, product_image_url, holder.productDp);
    }

    @Override
    public int getItemCount() {
        return productsArrayList.size();
    }

    class HomeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        BootstrapCircleThumbnail imageDp;
        AwesomeTextView username, location, time, caption, price;
        ImageView productDp, call, email, share;
        BootstrapButton cat;

        HomeHolder(View itemView) {
            super(itemView);
            imageDp = (BootstrapCircleThumbnail) itemView.findViewById(R.id.image_dp);
            username = (AwesomeTextView) itemView.findViewById(R.id.username);
            location = (AwesomeTextView) itemView.findViewById(R.id.location);
            time = (AwesomeTextView) itemView.findViewById(R.id.tv_time);
            caption = (AwesomeTextView) itemView.findViewById(R.id.caption);
            price = (AwesomeTextView) itemView.findViewById(R.id.tv_price);
            productDp = (ImageView) itemView.findViewById(R.id.full_image);
            call = (ImageView) itemView.findViewById(R.id.btn_call);
            email = (ImageView) itemView.findViewById(R.id.btn_email);
            share = (ImageView) itemView.findViewById(R.id.btn_share);
            cat = (BootstrapButton) itemView.findViewById(R.id.category);

            imageDp.setOnClickListener(this);
            username.setOnClickListener(this);
            cat.setOnClickListener(this);
            productDp.setOnClickListener(this);
            call.setOnClickListener(this);
            email.setOnClickListener(this);
            share.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (clickCallback != null) {
                clickCallback.onHomeClick(v, getPosition());
            }
        }
    }
}
