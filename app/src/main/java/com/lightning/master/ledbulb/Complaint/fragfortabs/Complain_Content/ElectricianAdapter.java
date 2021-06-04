package com.lightning.master.ledbulb.Complaint.fragfortabs.Complain_Content;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.lightning.master.ledbulb.Complaint.fragfortabs.Complaint;
import com.lightning.master.ledbulb.Complaint.fragfortabs.ElectricianListActivity;
import com.lightning.master.ledbulb.R;
import com.lightning.master.ledbulb.Utils;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;


/**
 * Created by Geeta on 27-Feb-18.
 */

public class ElectricianAdapter extends RecyclerView.Adapter<ElectricianAdapter.MyViewHolder> {
    Context mContext;

    SharedPreferences sharedPref;
    boolean eng = false;

    public ElectricianAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_electricianlist, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tv_ElectricianName.setText(ElectricianListActivity.NameList.get(position));
        holder.tv_PhoneNumber.setText(ElectricianListActivity.PhoneNumberList.get(position));

        if (ElectricianListActivity.isSelected.get(position).equals("0")){

            holder.btn_assign.setVisibility(View.VISIBLE);
        }else{
            holder.btn_assign.setVisibility(View.GONE);
        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.imageloading);
        requestOptions.error(R.drawable.noimage);

        Glide.with(mContext)
                .setDefaultRequestOptions(requestOptions)
                .load(ElectricianListActivity.ImageList.get(position))
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
                .into(holder.iv_imageElectri);

        holder.btn_assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setTitle("Confirmation");
                alert.setIcon(R.mipmap.ic_launcher);
                alert.setMessage("Are You Sure To Want To Select This Electrician ?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                selectelectrician(holder,ElectricianListActivity.BookingID,ElectricianListActivity.idList.get(position));

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return ElectricianListActivity.idList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_ElectricianName, tv_PhoneNumber;

        ImageView iv_imageElectri;
        Button btn_assign;
        ProgressBar progressBar;

        public MyViewHolder(View itemView)
        {
            super(itemView);

            tv_ElectricianName   = (TextView) itemView.findViewById(R.id.tv_ElectricianName);
            tv_PhoneNumber       = (TextView) itemView.findViewById(R.id.tv_PhoneNumber);

            iv_imageElectri      = (ImageView) itemView.findViewById(R.id.iv_imageElectri);

            btn_assign           = (Button) itemView.findViewById(R.id.btn_assign);
            progressBar          = (ProgressBar) itemView.findViewById(R.id.progressBar);

        }
    }

    public void selectelectrician(final MyViewHolder myViewHolder, String booking_id, String electrician_id) {


        Log.i("booking_id : ", "" + booking_id);
        Log.i("electrician_id : ", "" + electrician_id);
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.selectelectrician(booking_id,electrician_id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();

                    try {
                        String result = response.body().string();
                        JSONObject obj = new JSONObject(result);
                        Log.i("result : ", "" + result);
                        if (obj.has("error")) {
                            String message = obj.getString("error_message");
                            Toast.makeText(mContext, " " + message, Toast.LENGTH_LONG).show();

                        } else if (obj.has("warning")) {
                            String message = obj.getString("message");
                            Toast.makeText(mContext, " " + message, Toast.LENGTH_LONG).show();

                        } else {
                            String message = obj.getString("message");
                            Toast.makeText(mContext, " " + message, Toast.LENGTH_LONG).show();

                            myViewHolder.btn_assign.setVisibility(View.GONE);

                            Complaint.i=1;
                            Intent intent = new Intent(mContext,Complaint.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            mContext.startActivity(intent);
                        }

                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        Toast.makeText(mContext, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
                    progressDialog.dismiss();
                    Toast.makeText(mContext, "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.dismiss();
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(mContext, "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(mContext, "Something went wrong at server!" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public interface Service {

        @Headers("Authkey:APPLEDBDMPL")
        @POST("selectelectrician")
        @FormUrlEncoded
        Call<ResponseBody> selectelectrician(@Field("booking_id") String booking_id,
                                          @Field("electrician_id") String electrician_id);
    }

}