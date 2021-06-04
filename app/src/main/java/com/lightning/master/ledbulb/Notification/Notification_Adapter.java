package com.lightning.master.ledbulb.Notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lightning.master.ledbulb.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Notification_Adapter extends RecyclerView.Adapter<Notification_Adapter.MyViewHolder> {
    Context mContext;
    SharedPreferences sharedPref;


    public Notification_Adapter(Context mContext)
    {
        this.mContext=mContext;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notification,viewGroup,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder,final int position) {

        sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        myViewHolder.tv_notification.setText(Notification.NOTIFICATION.get(position));

        String requestDate = Notification.ADDED.get(position);
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "EEE, dd MMM yyyy, hh:mm";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(requestDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            str = requestDate + "";
        }
        myViewHolder.tv_time.setText(" On "+str);
    }

    @Override
    public int getItemCount() {
        return Notification.ID.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_notification,tv_time;
        ImageView image_view;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_notification=(TextView)itemView.findViewById(R.id.tv_notification);
            tv_time=(TextView)itemView.findViewById(R.id.tv_time);
//            image_view2=(ImageView) itemView.findViewById(R.id.image_view2);
            image_view=(ImageView)itemView.findViewById(R.id.image_view);

        }
    }
}
