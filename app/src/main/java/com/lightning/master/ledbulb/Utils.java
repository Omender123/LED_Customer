package com.lightning.master.ledbulb;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Geeta on 22-Sep-17.
 */

public class Utils {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 235;
// public static String base_url = "http://bespokedigitalmedia.in/Android/ledapp/api/v1/";
    public static String url = "http://thelightingmasters.com/test/api/";
    public static String base_url = url+"v1/";
    public static String base_urlv3 = url+"v3/";
// public static String base_url = site_url + "index.php/";

    // **************************************Connection check**************************************
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    // **************************************Connection Dialog**************************************
    public static void conDialog(final Context c) {
        AlertDialog.Builder alert = new AlertDialog.Builder(
                c);

        alert.setTitle("Internet connection unavailable.");
        alert.setMessage("You must be connected to an internet connection via WI-FI or Mobile Connection.");
        alert.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        c.startActivity(new Intent(
                                Settings.ACTION_WIRELESS_SETTINGS));
                    }
                });

        alert.show();
    }
    public  static void snack(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();

    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean checkPermissionLocation(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Location permission is required to calculated your distance from lab");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static String streamToString(InputStream is) throws IOException {
        String str = "";

        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is));

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                reader.close();
            } finally {
                is.close();
            }

            str = sb.toString();
        }
        return str;
    }

    public static void initiateScan(Activity activity){
        IntentIntegrator intentIntegrator=new IntentIntegrator(activity);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        intentIntegrator.setPrompt("Scan");
        intentIntegrator.setCameraId(0);
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setBarcodeImageEnabled(false);
        intentIntegrator.initiateScan();
    }
    public static void updatedtoken(final Context context,String user_id,String token ) {
//        final ProgressDialog progressDialog = new ProgressDialog(context);
//        progressDialog.setMessage("Please Wait...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.updatedtoken(user_id,token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                   // progressDialog.dismiss();
                    try {
                        String result = response.body().string();
                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");
                        String message = obj.optString("message");

                        String role="";

                        if (obj.has("error")) {
// vibrate_alert(500);
                            String error_message = obj.getString("error_message");
                            Toast.makeText(context, "Error : "+error_message, Toast.LENGTH_SHORT).show();


                        } else if (obj.has("warning")) {

// vibrate_alert(500);
                            String error_message = obj.getString("error_message");
                            Toast.makeText(context, "warning : "+error_message, Toast.LENGTH_SHORT).show();


                        } else {

                         //   Toast.makeText(context,obj.optString("message"),Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                      //  progressDialog.dismiss();
                        e.printStackTrace();
                        Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
//                    progressDialog.dismiss();
                    Toast.makeText(context, "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {
//                    progressDialog.dismiss();
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(context, "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                progressDialog.dismiss();
                Toast.makeText(context, "Something went wrong at server!"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    public interface Service {
        @Headers("Authkey:APPLEDBDMPL")
        @POST("updatedtoken/{user_id}")
        @FormUrlEncoded
        Call<ResponseBody> updatedtoken(@Path("user_id") String user_id,
                                        @Field("token") String token);

    }
}