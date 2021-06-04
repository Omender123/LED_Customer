package com.lightning.master.ledbulb.Complaint.fragfortabs.Complain_Content;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import androidx.core.view.ViewCompat;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lightning.master.ledbulb.Complaint.fragfortabs.Complaint_Detail;
import com.lightning.master.ledbulb.Complaint.fragfortabs.TabFragment2;
import com.lightning.master.ledbulb.New_MapsActivity2;
import com.lightning.master.ledbulb.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Geeta on 27-Feb-18.
 */

public class ComplainAdapter extends RecyclerView.Adapter<ComplainAdapter.MyViewHolder> {
    Context mContext;
    ComplainModel complainModel;
    SharedPreferences sharedPref;
    boolean eng = false;

    ProgressDialog progressDialog;

    String user_id = "";

    public ComplainAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comaplinlist, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        complainModel = TabFragment2.complainModelArrayList.get(position);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);


        holder.tv_complainId.setText(complainModel.getBooking_code());
        holder.tv_complainStatus.setText(complainModel.getBooking_status());
        holder.tv_location.setText(complainModel.getBooking_location());


        Glide.with(mContext)
                .load(complainModel.getBooking_img_path())
                .into(holder.iv_image);

        holder.iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(mContext);
                dialog.show();
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.item_extra_images);
                ImageView iv_extra=dialog.findViewById(R.id.iv_extra);
                ProgressBar imageloader=dialog.findViewById(R.id.imageloader);
                ImageView back=dialog.findViewById(R.id.back);

                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                complainModel = TabFragment2.complainModelArrayList.get(position);

                Glide.with(mContext)
                        .load(complainModel.getBooking_img_path())
                        .into(iv_extra);



            }
        });

        if (complainModel.getPickup_otp().equals("")) {
            holder.tv_otp.setText("");
        } else {
            holder.tv_otp.setText("OTP : " + complainModel.getPickup_otp());
        }

        if (complainModel.getBooking_status().equals("BOOKING_RECIEVED")) {
            holder.tv_findElectrician.setVisibility(View.VISIBLE);
        } else {
            holder.tv_findElectrician.setVisibility(View.GONE);
        }

        String requestDate = complainModel.getBooking_date();
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = " dd MMM yyyy ";
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
        holder.tv_date.setText(" " + str);


        String requestDate1 = complainModel.getBooking_date();
        String inputPattern1 = "yyyy-MM-dd HH:mm:ss";
        String outputPattern1 = " hh:mm";
        SimpleDateFormat inputFormat1 = new SimpleDateFormat(inputPattern1);
        SimpleDateFormat outputFormat1 = new SimpleDateFormat(outputPattern1);

        Date date1 = null;
        String str1 = null;

        try {
            date1 = inputFormat1.parse(requestDate1);
            str1 = outputFormat1.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
            str1 = requestDate + "";
        }
        holder.tv_time.setText(" " + str1);


        /////////////////////////////
        if (complainModel.getBooking_status().equals("BOOKING_RECIEVED")) {
            holder.tv_complainStatus.setText(" Booking Recieved");
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP && holder.tv_complainStatus instanceof AppCompatButton) {
                ((AppCompatButton) holder.tv_complainStatus).setSupportBackgroundTintList(mContext.getResources().getColorStateList(R.color.blueLight));
            } else {
                ViewCompat.setBackgroundTintList(holder.tv_complainStatus,mContext.getResources().getColorStateList(R.color.blueLight));
            }
        } else if (complainModel.getBooking_status().equals("ASSIGNED_FOR_PICKUP")) {
            holder.tv_complainStatus.setText("Assigned at Pick Up");
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP && holder.tv_complainStatus instanceof AppCompatButton) {
                ((AppCompatButton) holder.tv_complainStatus).setSupportBackgroundTintList(mContext.getResources().getColorStateList(R.color.redLight));
            } else {
                ViewCompat.setBackgroundTintList(holder.tv_complainStatus,mContext.getResources().getColorStateList(R.color.redLight));
            }
        } else if (complainModel.getBooking_status().equals("AT_DROP_POINT")) {
            holder.tv_complainStatus.setText("Assigned at Drop Point");
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP && holder.tv_complainStatus instanceof AppCompatButton) {
                ((AppCompatButton) holder.tv_complainStatus).setSupportBackgroundTintList(mContext.getResources().getColorStateList(R.color.redLight));
            } else {
                ViewCompat.setBackgroundTintList(holder.tv_complainStatus,mContext.getResources().getColorStateList(R.color.redLight));
            }
        } else if (complainModel.getBooking_status().equals("PICKED_UP")) {
            holder.tv_complainStatus.setText("Picked Up");
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP && holder.tv_complainStatus instanceof AppCompatButton) {
                ((AppCompatButton) holder.tv_complainStatus).setSupportBackgroundTintList(mContext.getResources().getColorStateList(R.color.redLight));
            } else {
                ViewCompat.setBackgroundTintList(holder.tv_complainStatus,mContext.getResources().getColorStateList(R.color.redLight));
            }
        } else if (complainModel.getBooking_status().equals("RECIEVED_IN_HUB")) {
            holder.tv_complainStatus.setText("Reached at Hub");
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP && holder.tv_complainStatus instanceof AppCompatButton) {
                ((AppCompatButton) holder.tv_complainStatus).setSupportBackgroundTintList(mContext.getResources().getColorStateList(R.color.redLight));
            } else {
                ViewCompat.setBackgroundTintList(holder.tv_complainStatus,mContext.getResources().getColorStateList(R.color.redLight));
            }
        } else if (complainModel.getBooking_status().equals("IN_QUALITY_CHECK")) {
            holder.tv_complainStatus.setText("At Quality Check");
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP && holder.tv_complainStatus instanceof AppCompatButton) {
                ((AppCompatButton) holder.tv_complainStatus).setSupportBackgroundTintList(mContext.getResources().getColorStateList(R.color.redLight));
            } else {
                ViewCompat.setBackgroundTintList(holder.tv_complainStatus,mContext.getResources().getColorStateList(R.color.redLight));
            }
        } else if (complainModel.getBooking_status().equals("SERVICE_COMPLETE")) {
            holder.tv_complainStatus.setText("Service Completed");
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP && holder.tv_complainStatus instanceof AppCompatButton) {
                ((AppCompatButton) holder.tv_complainStatus).setSupportBackgroundTintList(mContext.getResources().getColorStateList(R.color.yellowLight));
            } else {
                ViewCompat.setBackgroundTintList(holder.tv_complainStatus,mContext.getResources().getColorStateList(R.color.yellowLight));
            }
        } else if (complainModel.getBooking_status().equals("ASSIGNED_FOR_DELIVERY")) {
            holder.tv_complainStatus.setText("Assigned For Deliver");
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP && holder.tv_complainStatus instanceof AppCompatButton) {
                ((AppCompatButton) holder.tv_complainStatus).setSupportBackgroundTintList(mContext.getResources().getColorStateList(R.color.yellowLight));
            } else {
                ViewCompat.setBackgroundTintList(holder.tv_complainStatus,mContext.getResources().getColorStateList(R.color.yellowLight));
            }
        } else if (complainModel.getBooking_status().equals("OUT_FOR_DELIVERY")) {
            holder.tv_complainStatus.setText("Out For Deliver");
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP && holder.tv_complainStatus instanceof AppCompatButton) {
                ((AppCompatButton) holder.tv_complainStatus).setSupportBackgroundTintList(mContext.getResources().getColorStateList(R.color.yellowLight));
            } else {
                ViewCompat.setBackgroundTintList(holder.tv_complainStatus,mContext.getResources().getColorStateList(R.color.yellowLight));
            }
        } else if (complainModel.getBooking_status().equals("DELIVERED")) {
            holder.tv_complainStatus.setText("DELIVERED");
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP && holder.tv_complainStatus instanceof AppCompatButton) {
                ((AppCompatButton) holder.tv_complainStatus).setSupportBackgroundTintList(mContext.getResources().getColorStateList(R.color.greenLight));
            } else {
                ViewCompat.setBackgroundTintList(holder.tv_complainStatus,mContext.getResources().getColorStateList(R.color.greenLight));
            }
        } else {

        }

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complainModel = TabFragment2.complainModelArrayList.get(position);

                mContext.startActivity(new Intent(mContext, Complaint_Detail.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("booking_id", complainModel.getId()));

            }
        });

        holder.tv_findElectrician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complainModel = TabFragment2.complainModelArrayList.get(position);

                mContext.startActivity(new Intent(mContext, New_MapsActivity2.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("startFrom", "FindElectrician")
                        .putExtra("bookingID", complainModel.getId()));

            }
        });

    }

    @Override
    public int getItemCount() {
        return TabFragment2.complainModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date, tv_complainId, tv_time, tv_location, tv_complainStatus, tv_otp,tv_findElectrician;

        CardView cardview;
        ImageView iv_image;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_date             = (TextView) itemView.findViewById(R.id.tv_date);
            tv_complainId       = (TextView) itemView.findViewById(R.id.tv_complainId);
            tv_complainStatus   = (TextView) itemView.findViewById(R.id.tv_complainStatus);
            tv_time             = (TextView) itemView.findViewById(R.id.tv_time);
            tv_location         = (TextView) itemView.findViewById(R.id.tv_location);
            tv_otp              = (TextView) itemView.findViewById(R.id.tv_otp);
            tv_findElectrician  = (TextView) itemView.findViewById(R.id.tv_findElectrician);
            cardview            = (CardView) itemView.findViewById(R.id.cardview);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_image);

        }
    }
}