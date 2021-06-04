package com.lightning.master.ledbulb.ExtraImagesSection;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.lightning.master.ledbulb.R;


/**
 * Created by Geeta on 27-Feb-18.
 */

public class ExtraImagesAdapter extends RecyclerView.Adapter<ExtraImagesAdapter.MyViewHolder> {
    Context mContext;

    SharedPreferences sharedPref;
    boolean eng = false;

    ProgressDialog progressDialog;

    String user_id = "";

    public ExtraImagesAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_extra_images, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.imageloading);
            requestOptions.error(R.drawable.noimage);
            Glide.with(mContext)
                    .setDefaultRequestOptions(requestOptions)
                    .load(ExtraImagesFetch.imagesArray.get(position))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.iv_extra);


    }

    @Override
    public int getItemCount() {
        return ExtraImagesFetch.imagesArray.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_extra;
        ProgressBar progressBar;
        public MyViewHolder(View itemView) {
            super(itemView);

            iv_extra=(ImageView)itemView.findViewById(R.id.iv_extra);
            progressBar=(ProgressBar)itemView.findViewById(R.id.imageloader);

        }
    }


}