package com.lightning.master.ledbulb.Complaint.fragfortabs.Complain_Content;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lightning.master.ledbulb.Complaint.fragfortabs.TabFragment1;
import com.lightning.master.ledbulb.R;
import com.squareup.picasso.Picasso;

import java.io.File;

public class AddImagesAdapter extends RecyclerView.Adapter<AddImagesAdapter.MyViewHolder> {

    Context context;

    Dialog dialog_progress;
    SharedPreferences sharedPreferences;
    BottomSheetDialog bottomSheetDialog;
    TabFragment1 addImagesActivity;

    public AddImagesAdapter(Context context, TabFragment1 addImagesActivity) {
        this.context = context;
        this.addImagesActivity=addImagesActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_addpic, viewGroup, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        myViewHolder.img_add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ///////////// Add Image
                addImagesActivity.image_Picker(i);

            }
        });

        myViewHolder.remove_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TabFragment1.current_add_limit= TabFragment1.current_add_limit-1;
                TabFragment1.selected_images.remove(i);
                notifyDataSetChanged();

                Log.i("selected_images ", TabFragment1.selected_images + "");

            }
        });

        if (!TabFragment1.selected_images.get(i).equals("")){
            myViewHolder.tv_add_img.setVisibility(View.GONE);
            Picasso.get()
                    .load(Uri.fromFile(new File(TabFragment1.selected_images.get(i))))
                    .error(R.drawable.upload_pic)
                    .into(myViewHolder.img_add_post, new com.squareup.picasso.Callback() {

                                @Override
                                public void onSuccess() {
                                    //   holder.progress.setVisibility(View.GONE);
                                }

                        @Override
                        public void onError(Exception e) {
                            myViewHolder.img_add_post.setVisibility(View.GONE);
                        }

                            }
                    );
        }else {
            myViewHolder.tv_add_img.setVisibility(View.VISIBLE);
            Picasso.get().load(R.drawable.upload_pic).into(myViewHolder.img_add_post);
        }
    }

    @Override
    public int getItemCount() {
        return TabFragment1.current_add_limit;
    }

    public interface ImagePicker {
        void image_Picker(int i);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img_add_post,remove_img;
        TextView tv_add_img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_add_post = (ImageView) itemView.findViewById(R.id.img_add_post);
            remove_img=(ImageView)itemView.findViewById(R.id.remove_img);
            tv_add_img=(TextView) itemView.findViewById(R.id.tv_add_img);
        }
    }

}
