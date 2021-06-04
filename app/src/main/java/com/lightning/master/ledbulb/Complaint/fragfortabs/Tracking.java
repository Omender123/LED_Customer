package com.lightning.master.ledbulb.Complaint.fragfortabs;

import android.os.Build;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lightning.master.ledbulb.R;


public class Tracking extends AppCompatActivity {
    ImageView iv_back, book_image, drop_image, picked_image, reach_image,
    quality_image, service_image, deliver_image,assigne_for_deliver,out_for_deliver;

    TextView complaintid, Status_track, tv_booking_type,assign_for_delivered;

    View book_view, drop_view, picked_view, reach_view, picked_quality,
            service_view,assign_deliver_line,out_for_deliver_line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        init();
    }

    void init() {

        iv_back = (ImageView) findViewById(R.id.iv_back_tracking);
        book_image = (ImageView) findViewById(R.id.book_image);
        drop_image = (ImageView) findViewById(R.id.drop_image);
        picked_image = (ImageView) findViewById(R.id.picked_image);
        reach_image = (ImageView) findViewById(R.id.reach_image);
        quality_image = (ImageView) findViewById(R.id.quality_image);
        service_image = (ImageView) findViewById(R.id.service_image);
        deliver_image = (ImageView) findViewById(R.id.deliver_image);
        assigne_for_deliver = (ImageView) findViewById(R.id.assigne_for_deliver);
        out_for_deliver = (ImageView) findViewById(R.id.out_for_deliver);

        book_view = (View) findViewById(R.id.book_view);
        drop_view = (View) findViewById(R.id.drop_view);
        picked_view = (View) findViewById(R.id.picked_view);
        reach_view = (View) findViewById(R.id.reach_view);
        picked_quality = (View) findViewById(R.id.picked_quality);
        service_view = (View) findViewById(R.id.service_view);
        assign_deliver_line = (View) findViewById(R.id.assign_deliver_line);
        out_for_deliver_line = (View) findViewById(R.id.out_for_deliver_line);

        complaintid = (TextView) findViewById(R.id.complaintid);
        Status_track = (TextView) findViewById(R.id.Status_track);
        tv_booking_type = (TextView) findViewById(R.id.tv_booking_type);
        assign_for_delivered = (TextView) findViewById(R.id.assign_for_delivered);

        tv_booking_type.setText(Complaint_Detail.booking_type);
        complaintid.setText(Complaint_Detail.complaint_id);

        if(Complaint_Detail.booking_type.equals("PICKUP"))
        {
            tv_booking_type.setText("Assigned at Pick Up");
        }else if (Complaint_Detail.booking_type.equals("DROP")) {
            Status_track.setText("Assigned at Drop Point");
        }
        if (Complaint_Detail.booking_status.equals("BOOKING_RECIEVED")) {
            Status_track.setText(" Booking Recieved");
            Booking_Recieved();

        } else if (Complaint_Detail.booking_status.equals("ASSIGNED_FOR_PICKUP")) {
            Status_track.setText("Assigned at Pick Up");
            ASSIGNED_FOR_PICKUP();

        } else if (Complaint_Detail.booking_status.equals("AT_DROP_POINT")) {
            Status_track.setText("Assigned at Drop Point");
            AT_DROP_POINT();

        } else if (Complaint_Detail.booking_status.equals("PICKED_UP")) {
            Status_track.setText("Picked Up");
            PICKED_UP();

        } else if (Complaint_Detail.booking_status.equals("RECIEVED_IN_HUB")) {
            Status_track.setText("Reached at Hub");
            RECIEVED_IN_HUB();
        } else if (Complaint_Detail.booking_status.equals("IN_QUALITY_CHECK")) {
            Status_track.setText("At Quality Check");
            IN_QUALITY_CHECK();
        } else if (Complaint_Detail.booking_status.equals("SERVICE_COMPLETE")) {
            Status_track.setText("Service Completed");
            SERVICE_COMPLETE();
        }
        else if (Complaint_Detail.booking_status.equals("ASSIGNED_FOR_DELIVERY")) {
            Status_track.setText("Assigned For Deliver");
            ASSIGNED_FOR_DELIVERY();
        }
        else if (Complaint_Detail.booking_status.equals("OUT_FOR_DELIVERY")) {
            Status_track.setText("Out For Deliver");
            OUT_FOR_DELIVERY();
        }else if (Complaint_Detail.booking_status.equals("DELIVERED")) {
            Status_track.setText("DELIVERED");
            DELIVERED();
        } else {
            Status_track.setText("");
        }

        onClicks();
    }


    void onClicks() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }

    public void Booking_Recieved() {
        book_image.setImageResource(R.drawable.track);
        drop_image.setImageResource(R.drawable.not_track);
        picked_image.setImageResource(R.drawable.not_track);
        reach_image.setImageResource(R.drawable.not_track);
        quality_image.setImageResource(R.drawable.not_track);
        service_image.setImageResource(R.drawable.not_track);
        deliver_image.setImageResource(R.drawable.not_track);
    }


    public void ASSIGNED_FOR_PICKUP()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            book_view.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
        }
        book_image.setImageResource(R.drawable.track);
        drop_image.setImageResource(R.drawable.track);
    }

    public void AT_DROP_POINT()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            book_view.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
        }
        book_image.setImageResource(R.drawable.track);
        drop_image.setImageResource(R.drawable.track);
    }
    public void PICKED_UP()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            book_view.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
            drop_view.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
        }
        book_image.setImageResource(R.drawable.track);
        drop_image.setImageResource(R.drawable.track);
        picked_image.setImageResource(R.drawable.track);
    }

    public void RECIEVED_IN_HUB()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            book_view.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
            drop_view.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
            picked_view.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
        }
        book_image.setImageResource(R.drawable.track);
        drop_image.setImageResource(R.drawable.track);
        picked_image.setImageResource(R.drawable.track);
        reach_image.setImageResource(R.drawable.track);
    }

    public void IN_QUALITY_CHECK()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            book_view.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
            drop_view.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
            picked_view.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
            reach_view.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
        }
        book_image.setImageResource(R.drawable.track);
        drop_image.setImageResource(R.drawable.track);
        picked_image.setImageResource(R.drawable.track);
        reach_image.setImageResource(R.drawable.track);
        quality_image.setImageResource(R.drawable.track);
    }

    public void SERVICE_COMPLETE()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            book_view.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
            drop_view.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
            picked_view.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
            reach_view.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
            picked_quality.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
            service_view.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
        }
        book_image.setImageResource(R.drawable.track);
        drop_image.setImageResource(R.drawable.track);
        picked_image.setImageResource(R.drawable.track);
        reach_image.setImageResource(R.drawable.track);
        quality_image.setImageResource(R.drawable.track);
        service_image.setImageResource(R.drawable.track);
    }
    public void ASSIGNED_FOR_DELIVERY()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            book_view.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
            drop_view.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
            picked_view.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
            reach_view.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
            picked_quality.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
            assign_deliver_line.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
            service_view.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
        }
        book_image.setImageResource(R.drawable.track);
        drop_image.setImageResource(R.drawable.track);
        picked_image.setImageResource(R.drawable.track);
        reach_image.setImageResource(R.drawable.track);
        quality_image.setImageResource(R.drawable.track);
        service_image.setImageResource(R.drawable.track);
        assigne_for_deliver.setImageResource(R.drawable.track);
    }

    public void OUT_FOR_DELIVERY()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            book_view.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
            drop_view.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
            picked_view.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
            reach_view.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
            picked_quality.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
            assign_deliver_line.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
            out_for_deliver_line.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
            service_view.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
        }
        book_image.setImageResource(R.drawable.track);
        drop_image.setImageResource(R.drawable.track);
        picked_image.setImageResource(R.drawable.track);
        reach_image.setImageResource(R.drawable.track);
        quality_image.setImageResource(R.drawable.track);
        service_image.setImageResource(R.drawable.track);
        assigne_for_deliver.setImageResource(R.drawable.track);
        out_for_deliver.setImageResource(R.drawable.track);
    }

    public void DELIVERED()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            book_view.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
            drop_view.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
            picked_view.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
            reach_view.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
            picked_quality.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
            assign_deliver_line.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
            out_for_deliver_line.setBackgroundTintList(ContextCompat.getColorStateList(Tracking.this, R.color.colorAccent));
        }
        book_image.setImageResource(R.drawable.track);
        drop_image.setImageResource(R.drawable.track);
        picked_image.setImageResource(R.drawable.track);
        reach_image.setImageResource(R.drawable.track);
        quality_image.setImageResource(R.drawable.track);
        service_image.setImageResource(R.drawable.track);
        assigne_for_deliver.setImageResource(R.drawable.track);
        deliver_image.setImageResource(R.drawable.track);
        out_for_deliver.setImageResource(R.drawable.track);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
