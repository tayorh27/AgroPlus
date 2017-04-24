package com.ap.agroplus.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ap.agroplus.Activity.ViewImageActivity;
import com.ap.agroplus.Callbacks.ClickCallback;
import com.ap.agroplus.R;
import com.ap.agroplus.information.Search;
import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * Created by sanniAdewale on 15/04/2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder> {

    Context context;
    LayoutInflater inflater;
    ClickCallback clickCallback;
    ArrayList<Search> searchArrayList = new ArrayList<>();

    public SearchAdapter(Context context, ClickCallback clickCallback) {
        this.context = context;
        this.clickCallback = clickCallback;
        inflater = LayoutInflater.from(context);
    }

    public void LoadView(ArrayList<Search> searchArrayList) {
        this.searchArrayList = searchArrayList;
        notifyDataSetChanged();
    }

    @Override
    public SearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.search_layout, parent, false);
        return new SearchHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchHolder holder, int position) {
        Search search = searchArrayList.get(position);
        holder.tv.setText(search.username);
        String use_link = search.image_path.replace(" ", "%20");
        Glide.with(context).load(use_link).fitCenter().centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.iv);

    }

    @Override
    public int getItemCount() {
        return searchArrayList.size();
    }

    class SearchHolder extends RecyclerView.ViewHolder {

        BootstrapCircleThumbnail iv;
        AwesomeTextView tv;
        RelativeLayout linearLayout;

        SearchHolder(View itemView) {
            super(itemView);
            iv = (BootstrapCircleThumbnail) itemView.findViewById(R.id.image_dp);
            tv = (AwesomeTextView) itemView.findViewById(R.id.username);
            linearLayout = (RelativeLayout) itemView.findViewById(R.id.relative1);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickCallback != null) {
                        clickCallback.onHomeClick(v, getPosition());
                    }
                }
            });
        }
    }
}
