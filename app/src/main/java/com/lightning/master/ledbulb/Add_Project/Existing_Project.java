package com.lightning.master.ledbulb.Add_Project;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.lightning.master.ledbulb.HomeMain.Home_Drawer;
import com.lightning.master.ledbulb.R;
import com.lightning.master.ledbulb.Utils;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class Existing_Project extends AppCompatActivity {

    ImageView iv_back;

    TextView toolname,tv_proceed,tv_desc,tv_add,tv_img,tv_hint,tv_add_imageex,text,tv_procced_existing;
    int POS;
    RelativeLayout rl_hint;
    EditText area_type,et_lenth,et_width,et_Height,et_Lux,number_led,et_address,et_add,et_desc;
    LinearLayout ll_measure,ll_add_file,ll_numOf_led;
    ImageView add_project_file_before,add_project_file_after;
    String status="",img_path="";
    private int pageNumber = 0;
    MultipartBody requestBody;
    Dialog dialog_progress;

    private String pdfFileName;
    private PDFView pdfView;
    public ProgressDialog pDialog;
    public static final int FILE_PICKER_REQUEST_CODE = 1;
    private String pdfPath="";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exsisting__project);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String type=getIntent().getExtras().getString("ProjectType","");

        area_type=(EditText)findViewById(R.id.area_type);
        et_lenth=(EditText)findViewById(R.id.et_lenth);
        et_width=(EditText)findViewById(R.id.et_width);
        et_Height=(EditText)findViewById(R.id.et_Height);
        et_Lux=(EditText)findViewById(R.id.et_Lux);
        number_led=(EditText)findViewById(R.id.number_led);
        et_address=(EditText)findViewById(R.id.et_address);
        add_project_file_before=(ImageView) findViewById(R.id.add_project_file_before);
        add_project_file_after=(ImageView) findViewById(R.id.add_project_file_after);

        iv_back=findViewById(R.id.iv_back_existing);
        toolname=(TextView) findViewById(R.id.toolname);
        tv_procced_existing=(TextView) findViewById(R.id.tv_procced_existing);
        text=(TextView) findViewById(R.id.text);
        tv_add_imageex=(TextView) findViewById(R.id.tv_add_imageex);
        et_add=findViewById(R.id.et_address);
        et_desc=findViewById(R.id.et_desc);
        tv_desc=findViewById(R.id.tv_desc);
        tv_add=findViewById(R.id.tv_add);
        tv_img=findViewById(R.id.tv_add_imageex);
        ll_add_file=findViewById(R.id.ll_add_file);
        rl_hint=findViewById(R.id.rl_hint_existing);
        tv_hint=findViewById(R.id.hint_existing);
        ll_numOf_led=(LinearLayout) findViewById(R.id.ll_numOf_led);
        tv_proceed=findViewById(R.id.tv_procced_existing);

        if(type.equals("Existing"))
        {
            ll_numOf_led.setVisibility(View.VISIBLE);
            toolname.setText("Existing Project");
            status = "CURRENT";

        }else
        {
            ll_numOf_led.setVisibility(View.GONE);
            toolname.setText("New Project");
            status = "NEW";
        }

        tv_hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_hint.setVisibility(View.VISIBLE);


            }
        });


        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_add.setVisibility(View.VISIBLE);
              tv_add.setCompoundDrawables(null, null, null, null);
            }
        });


        tv_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_desc.setVisibility(View.VISIBLE);
                tv_desc.setCompoundDrawables(null, null, null, null);

            }
        });

        tv_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_add_file.setVisibility(View.VISIBLE);
                tv_img.setCompoundDrawables(null, null, null, null);

            }
        });

        tv_add_imageex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_project_file_before.setVisibility(View.VISIBLE);
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(Existing_Project.this, Home.class));
                finish();
            }
        });

        add_project_file_before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchPicker();
            }
        });

        tv_procced_existing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(area_type.getText().toString().isEmpty())
                {
                    area_type.setError("Enter the Area Type");
                }else if(et_lenth.getText().toString().isEmpty())
                {
                    et_lenth.setError("Enter the Lenght");
                }
                else if(et_width.getText().toString().isEmpty())
                {
                    et_width.setError("Enter the Width ");
                }else if(et_Height.getText().toString().isEmpty())
                {
                    et_Height.setError("Enter the Height");
                }
                else if(et_Lux.getText().toString().isEmpty())
                {
                    et_Lux.setError("Enter the Total Lux ");
                }
                else if(et_address.getText().toString().isEmpty())
                {
                    et_address.setError("Enter the Address");
                }
                else if(et_desc.getText().toString().isEmpty())
                {
                    et_desc.setError("Enter the Description ");
                }
                else if (status.equals("CURRENT") && number_led.getText().toString().isEmpty())
                {
                    number_led.setError("Enter No. Of Led");
                }
                else if(pdfPath.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Select an File", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    addproject(sharedPreferences.getString("user_id", "")
                    ,area_type.getText().toString(),et_lenth.getText().toString(),et_width.getText().toString()
                            ,et_Height.getText().toString(),et_Lux.getText().toString()
                            ,status,et_address.getText().toString()
                            ,pdfPath,et_desc.getText().toString(),number_led.getText().toString());
                }


//                Toast.makeText(Existing_Project.this, "Thanks We will contact You shortly", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void launchPicker() {
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(FILE_PICKER_REQUEST_CODE)
//                .withHiddenFiles(true)
                .withFilter(Pattern.compile(".*\\.pdf$"))
                .withTitle("Select PDF file")
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            String path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            File file = new File(path);
//            displayFromFile(file);
            if (path != null) {
                Log.d("Path: ", path);
                pdfPath = path;
                add_project_file_before.setVisibility(View.GONE);
                add_project_file_after.setVisibility(View.VISIBLE);
//                text.setText(pdfPath);
//                Toast.makeText(this, "Picked file: " + path, Toast.LENGTH_LONG).show();
            }
        }

    }
    public void addproject(String user_id, String area_type, String area_length, String area_width
            , String area_height, String total_lux, String project_type,String address,String project_file, String description,String current_led_count) {
        showloader("Please Wait..");

        try {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS).build();
// Change base URL to your upload server URL.
            Existing_Project.Service uploadService = new Retrofit.Builder()
                    .baseUrl(Utils.base_url)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(Existing_Project.Service.class);

            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
// builder.addFormDataPart("signature", photoSignatureModel.getSign_path());
            builder.addFormDataPart("user_id", user_id);
            builder.addFormDataPart("area_type", area_type);
            builder.addFormDataPart("area_length", area_length);
            builder.addFormDataPart("area_width", area_width);
            builder.addFormDataPart("area_height", area_height);
            builder.addFormDataPart("total_lux", total_lux);
            builder.addFormDataPart("project_type", project_type);
            builder.addFormDataPart("project_file", project_file);
            builder.addFormDataPart("address", address);
            builder.addFormDataPart("description", description);
            builder.addFormDataPart("current_led_count", current_led_count);

            File sign = new File(pdfPath);

            Log.i("img_path :", "" + pdfPath);
            Log.i("sign.getName() :", "" + sign.getName());

            builder.addFormDataPart("project_file", sign.getName(),
                    RequestBody.create(MediaType.parse("multipart/form-data"), sign));

            requestBody = builder.build();


            Log.i("requestBody", requestBody.toString());

            Call<ResponseBody> call;

            call = uploadService.newbooking(requestBody);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        hideloader();
//                        showloader("Booking is Done");
//                        showloader("Your project details are submitted successfully.We will reach you with all details ASAP");

                        try {
                            String result = response.body().string();

                            Log.i("Get All Categories", "" + result);
                            JSONObject obj = new JSONObject(result);
                            String success = obj.optString("success");

                            if (obj.has("error")) {
                                hideloader();
                                Toast.makeText(getApplicationContext(), "" + obj.getString("error_message"), Toast.LENGTH_SHORT).show();
//                                showloader(obj.getString("message"));

                            } else {
//                                Toast.makeText(getApplicationContext(), "" + obj.getString("message"), Toast.LENGTH_SHORT).show();
                                showloader1(obj.getString("message"));
                            }
                            hideloader();

                        } catch (Exception e) {
                            e.printStackTrace();
                            hideloader();
                            Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else if (response.code() == 401) {
                        hideloader();

                        Toast.makeText(getApplicationContext(), "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                    } else {
                        hideloader();

                        Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                        Toast.makeText(getApplicationContext(), "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                  hideloader();
                    Toast.makeText(getApplicationContext(), "Something went wrong at server!", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception ee) {

        }
    }

    public interface Service {
        @Headers("Authkey:APPLEDBDMPL")
        @POST("addprojectinquiry")
// @FormUrlEncoded
        Call<ResponseBody> newbooking(@Body RequestBody file);
    }

    public void showloader1(String message) {
//        Toast.makeText(getContext(), "Loader Working", Toast.LENGTH_SHORT).show();
        dialog_progress = new Dialog(this);
        dialog_progress.setContentView(R.layout.gif_layout);
        dialog_progress.show();
        dialog_progress.setCancelable(false);

        TextView tv_text = (TextView) dialog_progress.findViewById(R.id.tv_text);
        tv_text.setText(message);
        hideloader1();
    }
    public void hideloader1() {
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        dialog_progress.dismiss();
                        Intent intent = new Intent(getApplicationContext(), Home_Drawer.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                }, 5000);
    }

    public void showloader(String message) {
        dialog_progress = new Dialog(Existing_Project.this);
        dialog_progress.setContentView(R.layout.dialog_progress);
        dialog_progress.getWindow().setBackgroundDrawableResource(R.drawable.backkkk);
        // dialog_progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_progress.show();
        dialog_progress.setCancelable(false);

        TextView tv_text = (TextView) dialog_progress.findViewById(R.id.tv_text);
        tv_text.setText(message);
    }
    public void hideloader() {
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        dialog_progress.dismiss();
                    }
                }, 1200);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
