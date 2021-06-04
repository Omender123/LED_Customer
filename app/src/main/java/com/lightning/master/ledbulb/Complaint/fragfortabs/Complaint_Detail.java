package com.lightning.master.ledbulb.Complaint.fragfortabs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;
import com.lightning.master.ledbulb.Adapter.Adapter_Complaint_items;
import com.lightning.master.ledbulb.Adapter.Adapter_Complaint_items_after;
import com.lightning.master.ledbulb.Details_Adapter.Non_Repaire_Adapter;
import com.lightning.master.ledbulb.Details_Adapter.Repair_Adapter;
import com.lightning.master.ledbulb.Details_Model.Non_Repairable_Model;
import com.lightning.master.ledbulb.Details_Model.Repairable_Model;
import com.lightning.master.ledbulb.ExtraImagesSection.ExtraImagesFetch;
import com.lightning.master.ledbulb.HomeMain.Home_Drawer;
import com.lightning.master.ledbulb.Interface.ItemClickListner;
import com.lightning.master.ledbulb.Model.ModelPriceList;
import com.lightning.master.ledbulb.R;
import com.lightning.master.ledbulb.Utils;
import com.lightning.master.ledbulb.summary_customer.MyOrdersSummary_Adapter;
import com.lightning.master.ledbulb.summary_customer.NonMyOrdersSummary_Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class Complaint_Detail extends AppCompatActivity {
    int POS;

    TextView total, tv_proceed, tv_bookingStatus, tv_bookingId, tv_bookingDate, invoice,
            tv_bookingTransportedBy, tv_bookingAddress, tv_description, tv_bookingTime,
            tv_opt_textvalue, tv_DistributerContact, tv_GiveFeedbback, tv_numbrOfImages;
    ImageView iv_back, bookingImagenew, tv_tracking;
    RadioGroup radioGroup1;
    RadioButton btn_repaire, btn_non_repair;

    String Api_booking_id = "", scroll_bottom = "";
    ProgressBar progressBar;
    Dialog dialog_progress, dialog_summary;
    JSONObject invoiceData;
    JSONObject payment;

    public static Dialog dialog_invoice;

    public static String booking_status = "", complaint_id = "", booking_type = "", radioValue = "";
    public static boolean isEmpty = false;

    public static ArrayList<Repairable_Model> repair_modelArrayList = new ArrayList<>();
    public static ArrayList<Non_Repairable_Model> non_repairable_modelArrayList = new ArrayList<>();
    public static ArrayList<ModelPriceList> itemDataList = new ArrayList<>();

    Repair_Adapter repair_adapter;
    Non_Repaire_Adapter non_repaire_adapter;
    RecyclerView recycler_grid_price, recyclerViewnon;
    LinearLayout ll_repairable, ll_non_repairable, ll_price_json, ll_otp_layout, ll_moreImages, ll_detailsScroll;

    public static ArrayList<Integer> idcomplain = new ArrayList();
    public static ArrayList<String> responsearray = new ArrayList();
    public static ArrayList<Integer> non_idcomplain = new ArrayList();
    public static ArrayList<String> non_responsearray = new ArrayList();

    public static ArrayList<String> smry_type = new ArrayList();
    public static ArrayList<String> smry_category = new ArrayList();
    public static ArrayList<String> smry_qty = new ArrayList();
    public static ArrayList<String> smry_price = new ArrayList();
    public static ArrayList<String> smry_cmnt = new ArrayList();

    public static ArrayList<String> smry_type_non = new ArrayList();
    public static ArrayList<String> smry_category_non = new ArrayList();
    public static ArrayList<String> smry_qty_non = new ArrayList();
    public static ArrayList<String> smry_price_non = new ArrayList();
    public static ArrayList<String> smry_cmnt_non = new ArrayList();

    public static ArrayList<String> count = new ArrayList();

    SharedPreferences sharedPreferences;

    JSONArray jsonArray = new JSONArray();

    JSONObject cust;
    JSONArray repairable, nonrepairable;
    ScrollView nestedScrollview;
    boolean response_type_hasdata = false, nonresponse_type_hasdata = false;

    MyOrdersSummary_Adapter myOrdersSummary_adapter;
    NonMyOrdersSummary_Adapter nonMyOrdersSummary_adapter;


    String smileValue = "";
    Dialog feedbackdialog;
    RecyclerView recycler_summary, recycler_non_summary;
    String status = "";
    Context context;
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint__detail);

        context = Complaint_Detail.this;
        payment = new JSONObject();

        init();
    }

    void init() {


        recycler = (RecyclerView) findViewById(R.id.recycler);
        iv_back = (ImageView) findViewById(R.id.iv_back_detail);
        bookingImagenew = (ImageView) findViewById(R.id.bookingImagenew);
        tv_tracking = (ImageView) findViewById(R.id.tv_tracking);

        progressBar = (ProgressBar) findViewById(R.id.imageloader);

        tv_proceed = (TextView) findViewById(R.id.tv_proceed_payment);
        total = (TextView) findViewById(R.id.total);
        tv_DistributerContact = (TextView) findViewById(R.id.tv_DistributerContact);
        invoice = (TextView) findViewById(R.id.invoice);
        tv_opt_textvalue = (TextView) findViewById(R.id.tv_opt_textvalue);
        tv_bookingStatus = (TextView) findViewById(R.id.tv_bookingStatus);
        tv_bookingId = (TextView) findViewById(R.id.tv_bookingId);
        tv_bookingDate = (TextView) findViewById(R.id.tv_bookingDate);
        tv_bookingTransportedBy = (TextView) findViewById(R.id.tv_bookingTransportedBy);
        tv_bookingAddress = (TextView) findViewById(R.id.tv_bookingAddress);
        tv_description = (TextView) findViewById(R.id.tv_description);
        tv_bookingTime = (TextView) findViewById(R.id.tv_bookingTime);
        tv_GiveFeedbback = (TextView) findViewById(R.id.tv_GiveFeedbback);
        tv_numbrOfImages = (TextView) findViewById(R.id.tv_numbrOfImages);

        recycler_grid_price = (RecyclerView) findViewById(R.id.recycler_grid_price);
        recyclerViewnon = (RecyclerView) findViewById(R.id.recyclerViewnon);
        radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
        btn_repaire = (RadioButton) findViewById(R.id.btn_repaire);
        btn_non_repair = (RadioButton) findViewById(R.id.btn_non_repair);

        ll_repairable = (LinearLayout) findViewById(R.id.ll_repairable);
        ll_non_repairable = (LinearLayout) findViewById(R.id.ll_non_repairable);
        ll_price_json = (LinearLayout) findViewById(R.id.ll_price_json);
        ll_otp_layout = (LinearLayout) findViewById(R.id.ll_otp_layout);
        ll_moreImages = (LinearLayout) findViewById(R.id.ll_moreImages);
        ll_detailsScroll = (LinearLayout) findViewById(R.id.ll_detailsScroll);

        nestedScrollview = (ScrollView) findViewById(R.id.nestedScrollview);

        idcomplain.clear();
        responsearray.clear();
        non_idcomplain.clear();
        non_responsearray.clear();

        smry_type.clear();
        smry_category.clear();
        smry_qty.clear();
        smry_price.clear();
        smry_cmnt.clear();

        smry_type_non.clear();
        smry_category_non.clear();
        smry_qty_non.clear();
        smry_price_non.clear();
        smry_cmnt_non.clear();
        count.clear();


        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {

                if (key.equals("booking_id")) {
//                    status = getIntent().getExtras().get("booking_id") + "";
                    getbookingdetails(getIntent().getExtras().get("booking_id") + "");
//                    getbookingdetails(getIntent().getExtras().getString("booking_id"));
                    Log.d("booking_id: ", "" + getIntent().getExtras().get("booking_id") + "");
                }
                if (key.equals("scroll_bottom")) {

                    scroll_bottom = getIntent().getExtras().get("scroll_bottom") + "";
                    Log.d("scroll_bottom: ", "" + getIntent().getExtras().get("scroll_bottom"));
                }
            }

        }
        onClicks();
    }

    void onClicks() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        ll_moreImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), ExtraImagesFetch.class)
                        .putExtra("bookingID", Api_booking_id));
            }
        });

        tv_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isEmpty) {

                    Show_Summary_Dialog();
                } else {
                    Toast.makeText(Complaint_Detail.this, "Please Select Atleast One Item!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Show_invoice(invoiceData, payment);
            }
        });

        tv_GiveFeedbback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showFeedbackDialog();
            }
        });

        tv_tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Complaint_Detail.this, Tracking.class));
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, final int checkedId) {


                // find which radio button is selected
                if (checkedId == R.id.btn_repaire) {
//                    btn_repaire.setChecked(true);
                    radioValue = "REPAIRE";
                    ll_repairable.setVisibility(View.VISIBLE);
                    ll_non_repairable.setVisibility(View.GONE);


                } else if (checkedId == R.id.btn_non_repair) {
//                    btn_non_repair.setChecked(true);
                    radioValue = "NONREPAIRE";
                    ll_repairable.setVisibility(View.GONE);
                    ll_non_repairable.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    void showFeedbackDialog() {
        feedbackdialog = new Dialog(Complaint_Detail.this);
        feedbackdialog.setContentView(R.layout.feedback_layout);

        feedbackdialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        smileValue = "0";

        feedbackdialog.show();

        SmileRating ratingView = (SmileRating) feedbackdialog.findViewById(R.id.ratingView);
        final EditText et_comment = (EditText) feedbackdialog.findViewById(R.id.et_comment);
        Button btn_submit = (Button) feedbackdialog.findViewById(R.id.btn_submit);

        ratingView.setSelectedSmile(BaseRating.TERRIBLE);
        ratingView.setOnRatingSelectedListener(new SmileRating.OnRatingSelectedListener() {
            @Override
            public void onRatingSelected(int level, boolean reselected) {
                // level is from 1 to 5 (0 when none selected)
                // reselected is false when user selects different smiley that previously selected one
                // true when the same smiley is selected.
                // Except if it first time, then the value will be false.

                smileValue = level + "";
//                Toast.makeText(Complaint_Detail.this, "Rating : "+level, Toast.LENGTH_SHORT).show();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (smileValue.equals("")) {
                    Toast.makeText(Complaint_Detail.this, "Please Select Feedback Value", Toast.LENGTH_SHORT).show();
                } else {
                    servicefeeback(sharedPreferences.getString("user_id", ""), Api_booking_id,
                            smileValue, et_comment.getText().toString());
                }
            }
        });
    }

    private void Show_invoice(JSONObject jsonObject, JSONObject payment) {

        dialog_invoice = new Dialog(Complaint_Detail.this);
        dialog_invoice.setContentView(R.layout.dialog_invoice);
        dialog_invoice.getWindow().setBackgroundDrawableResource(R.drawable.backkkk);
        dialog_invoice.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialog_invoice.show();


        ImageView back = (ImageView) dialog_invoice.findViewById(R.id.bc);
        TextView tv_billing = (TextView) dialog_invoice.findViewById(R.id.tv_bill_amount);
        TextView discount = (TextView) dialog_invoice.findViewById(R.id.discount_amount);
        TextView wallet = (TextView) dialog_invoice.findViewById(R.id.wallet_amount);
        TextView total = (TextView) dialog_invoice.findViewById(R.id.tv_totalamount);
        TextView paid_via = (TextView) dialog_invoice.findViewById(R.id.paid_via);
        TextView tv_GST = (TextView) dialog_invoice.findViewById(R.id.tv_GST);

        if (!jsonObject.isNull("total_amount")) {
            try {
                tv_billing.setText(jsonObject.getString("total_amount"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            tv_billing.setText("0");

        }

        if (!jsonObject.isNull("gst_amount")) {
            try {
                tv_GST.setText("Rs. " + jsonObject.getString("gst_amount"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            tv_GST.setText("0");
        }

        if (!jsonObject.isNull("billing_amount")) {
            try {
                total.setText("Rs. " + jsonObject.getString("billing_amount"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            total.setText("0");
        }

        if (!jsonObject.isNull("wallet_discount")) {
            try {
                wallet.setText(jsonObject.getString("wallet_discount"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            wallet.setText("0");
        }

        if (!jsonObject.isNull("discount_amount")) {
            try {
                discount.setText(jsonObject.getString("discount_amount"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            discount.setText("0");
        }

        if (payment.has("payment_type")) {
            if (!payment.isNull("payment_type")) {
                try {
                    paid_via.setText(payment.getString("payment_type"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                paid_via.setText("Payment Pending");
            }
        } else {
            paid_via.setText("Payment Pending");
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_invoice.dismiss();
            }
        });
    }

    public void Show_Summary_Dialog() {
        dialog_summary = new Dialog(Complaint_Detail.this);
        dialog_summary.setContentView(R.layout.dialog_summary);
        dialog_summary.getWindow().setBackgroundDrawableResource(R.drawable.backkkk);
        // dialog_progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_summary.show();

        recycler_summary = (RecyclerView) dialog_summary.findViewById(R.id.recycler_summary);
        recycler_non_summary = (RecyclerView) dialog_summary.findViewById(R.id.recycler_non_summary);


        Button btn_proceed = (Button) dialog_summary.findViewById(R.id.btn_proceed);
        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                putDataInJsonArray();
            }
        });

        setData_Summary();
        setData_nonSummary();

        TextView tv_order_total = (TextView) dialog_summary.findViewById(R.id.tv_order_total);
        TextView tv_gst = (TextView) dialog_summary.findViewById(R.id.tv_gst);


        double sum = 0;
        for (int i = 0; i < smry_price.size(); i++) {

            try {
                sum += Double.parseDouble(smry_price.get(i));

            } catch (NumberFormatException e) {
                // p did not contain a valid double
            }
        }

        double sum_non = 0;
        for (int i = 0; i < smry_price_non.size(); i++) {

            try {
                sum_non += Double.parseDouble(smry_price_non.get(i));

            } catch (NumberFormatException e) {
                // p did not contain a valid double
            }
        }

        sum_non = sum_non + sum;

        double gst = sum_non * 18;
        gst = gst / 100;

        sum_non = sum_non + gst;

        tv_gst.setText("Rs." + gst + "");
        tv_order_total.setText("Rs." + sum_non + "");

    }

    private void setData_Summary() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_summary.setLayoutManager(linearLayoutManager);
        myOrdersSummary_adapter = new MyOrdersSummary_Adapter(getApplicationContext());

        recycler_summary.setAdapter(myOrdersSummary_adapter);
        myOrdersSummary_adapter.notifyDataSetChanged();
    }

    private void setData_nonSummary() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_non_summary.setLayoutManager(linearLayoutManager);
        nonMyOrdersSummary_adapter = new NonMyOrdersSummary_Adapter(getApplicationContext());

        recycler_non_summary.setAdapter(nonMyOrdersSummary_adapter);
        nonMyOrdersSummary_adapter.notifyDataSetChanged();
    }

    void putDataInJsonArray() {
        idcomplain.addAll(non_idcomplain);
        responsearray.addAll(non_responsearray);

        if (idcomplain.size() > 0) {


            jsonArray = new JSONArray();
            for (int i = 0; i < idcomplain.size(); i++) {
                try {

                    JSONObject cust = new JSONObject();
                    cust.put("id", idcomplain.get(i));
                    cust.put("response", responsearray.get(i));

                    jsonArray.put(cust);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            Log.i("jsonArray", jsonArray + "");
//             Toast.makeText(this, "jsonArray"+jsonArray, Toast.LENGTH_LONG).show();
            Proceed_API_Send_Response(sharedPreferences.getString("user_id", ""), jsonArray, getIntent().getExtras().getString("booking_id"));

        }
    }

    public void Proceed_API_Send_Response(String user_id, JSONArray response, String booking_id) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.add_price_response(user_id, response, booking_id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();

                    try {
                        String result = response.body().string();
                        JSONObject obj = new JSONObject(result);

                        if (obj.has("error")) {
                            String message = obj.getString("error_message");
                            Toast.makeText(getApplicationContext(), " " + message, Toast.LENGTH_LONG).show();

                        } else if (obj.has("warning")) {
                            String message = obj.getString("message");
                            Toast.makeText(getApplicationContext(), " " + message, Toast.LENGTH_LONG).show();

                        } else {
                            String message = obj.getString("message");
                            Toast.makeText(getApplicationContext(), " " + message, Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), Home_Drawer.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        }
//                        Toast.makeText(getApplicationContext(), " Successfully Add" , Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.dismiss();
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(getApplicationContext(), "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong at server!" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void getbookingdetails(String booking_id) {
        showloader("Please Wait..");

        Log.d("TAG", "getbookingdetails: " + booking_id);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.getbookingdetails(booking_id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    hideloader();
                    repair_modelArrayList.clear();
                    non_repairable_modelArrayList.clear();

                    try {
                        String result = response.body().string();
                        JSONObject obj = new JSONObject(result);

                        itemDataList.clear();
                        repair_modelArrayList.clear();
                        non_repairable_modelArrayList.clear();

                        if (obj.has("error")) {
                            String message = obj.getString("error_message");
                            hideloader();
                            Toast.makeText(getApplicationContext(), " " + message, Toast.LENGTH_LONG).show();

                        } else if (obj.has("warning")) {
                            String message = obj.getString("message");
                            hideloader();
                            Toast.makeText(getApplicationContext(), " " + message, Toast.LENGTH_LONG).show();

                        } else {


                            JSONArray data_array = obj.optJSONArray("items");

                            if (data_array.length() > 0) {
                                int sum = 0;
                                for (int i = 0; i < data_array.length(); i++) {
                                    JSONObject jsonObject = data_array.optJSONObject(i);
                                    ModelPriceList modelPriceList = new ModelPriceList();
                                    modelPriceList.setCHARGES(jsonObject.optInt("cost_repair"));
                                    modelPriceList.setTITLE(jsonObject.optString("item_title"));
                                    modelPriceList.setPIECES(jsonObject.optString("quantity"));
                                    modelPriceList.setWATT(jsonObject.optString("watts"));
                                    sum += jsonObject.optInt("cost_repair");
                                    itemDataList.add(modelPriceList);
                                }

                                total.setText("₹ "+sum);
                                setup_cart_items();
                            }

                            JSONObject dataObject = obj.getJSONObject("data");

                            if (!dataObject.isNull("id")) {
                                Api_booking_id = dataObject.getString("id");
                            } else {
                                Api_booking_id = "";
                            }

                            if (dataObject.has("distributor_contact")) {
                                if (!dataObject.isNull("distributor_contact")) {
                                    tv_DistributerContact.setText(dataObject.getString("distributor_contact"));
                                } else {
                                    tv_DistributerContact.setText("");
                                }
                            } else {
                                tv_DistributerContact.setText("");
                            }
                            if (dataObject.has("extra_images")) {
                                if (!dataObject.isNull("extra_images")) {

                                    int img = Integer.parseInt(dataObject.getString("extra_images"));

                                    if (img > 0) {
                                        ll_moreImages.setVisibility(View.VISIBLE);
                                        tv_numbrOfImages.setText("+" + dataObject.getString("extra_images"));
                                    } else {
                                        ll_moreImages.setVisibility(View.GONE);
                                    }

                                } else {
                                    tv_numbrOfImages.setText("");
                                    ll_moreImages.setVisibility(View.GONE);
                                }
                            } else {
                                ll_moreImages.setVisibility(View.GONE);
                                tv_numbrOfImages.setText("");
                            }
                            if (!dataObject.isNull("invoicedata")) {

                                invoiceData = dataObject.getJSONObject("invoicedata");

//                               Show_invoice(invoiceData);
                            }
                            if (!dataObject.isNull("paymentdata")) {
                                payment = dataObject.getJSONObject("paymentdata");
                            }
//                           JSONObject paymentdata=dataObject.getJSONObject("paymentdata");
//                            if (!paymentdata.isNull("payment_status")){
//
//                                if (!paymentdata.getString("payment_status").equals("SUCCESS")){
//
//                                }
//                                else{
//                                    payment=dataObject.getJSONObject("paymentdata");
//                                }
//                            }else {
//
//                            }


                            if (!dataObject.isNull("booking_img_path")) {
                                Glide.with(getApplicationContext())
                                        .load(dataObject.getString("booking_img_path"))
                                        .into(bookingImagenew);

                                RequestOptions requestOptions = new RequestOptions();
                                requestOptions.placeholder(R.drawable.imageloading);
                                requestOptions.error(R.drawable.noimage);

                                Glide.with(getApplicationContext())
                                        .setDefaultRequestOptions(requestOptions)
                                        .load(dataObject.getString("booking_img_path"))
                                        .listener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                progressBar.setVisibility(View.GONE);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                progressBar.setVisibility(View.GONE);
                                                return false;
                                            }
                                        })
                                        .into(bookingImagenew);
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Glide.with(getApplicationContext())
                                        .load(R.drawable.noimage)
                                        .into(bookingImagenew);
                            }
                            complaint_id = dataObject.getString("booking_code");
                            if (!dataObject.isNull("booking_code")) {
                                tv_bookingId.setText(dataObject.getString("booking_code"));
                            } else {
                                tv_bookingId.setText("");
                            }

                            if (!dataObject.isNull("pickup_otp")) {
                                tv_opt_textvalue.setText(dataObject.getString("pickup_otp"));
                            } else {

                                tv_opt_textvalue.setText("");
                                ll_otp_layout.setVisibility(View.GONE);
                            }

                            booking_status = dataObject.getString("booking_status");
                            if (dataObject.getString("booking_status").equals("BOOKING_RECIEVED")) {
                                tv_bookingStatus.setText("Booking Recieved");

                            } else if (dataObject.getString("booking_status").equals("ASSIGNED_FOR_PICKUP")) {
                                tv_bookingStatus.setText("Assigned at Pick Up");

                            } else if (dataObject.getString("booking_status").equals("AT_DROP_POINT")) {
                                tv_bookingStatus.setText("Assigned at Drop Point");

                            } else if (dataObject.getString("booking_status").equals("PICKED_UP")) {
                                tv_bookingStatus.setText("Picked Up");
                            } else if (dataObject.getString("booking_status").equals("RECIEVED_IN_HUB")) {
                                tv_bookingStatus.setText("Reached at Hub");
                            } else if (dataObject.getString("booking_status").equals("IN_QUALITY_CHECK")) {
                                tv_bookingStatus.setText("At Quality Check");
                            } else if (dataObject.getString("booking_status").equals("SERVICE_COMPLETE")) {
                                tv_bookingStatus.setText("Service Completed");
                            } else if (dataObject.getString("booking_status").equals("ASSIGNED_FOR_DELIVERY")) {
                                tv_bookingStatus.setText("Assigned For Delivered");
                            } else if (dataObject.getString("booking_status").equals("DELIVERED")) {
                                tv_bookingStatus.setText("Delivered");
                                tv_GiveFeedbback.setVisibility(View.VISIBLE);
                            } else {

//            holder.tv_complainStatus.setText("DELIVERED");
                            }
                            ///////////
                            if (!dataObject.isNull("booking_description")) {
                                tv_description.setText(dataObject.getString("booking_description"));
                            } else {
                                tv_description.setText("");
                            }
                            booking_type = dataObject.getString("booking_type");
                            if (!dataObject.isNull("booking_type")) {
                                tv_bookingTransportedBy.setText(dataObject.getString("booking_type"));

                            } else {
                                tv_bookingTransportedBy.setText("");
                            }
                            if (!dataObject.isNull("booking_date")) {

                                String dtStart = dataObject.getString("booking_date");

                                String inputPattern = "yyyy-MM-dd HH:mm:ss";
                                String outputPattern = "dd MMM yyyy";
                                SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                                SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                                Date date = null;
                                String str = null;

                                try {
                                    date = inputFormat.parse(dtStart);
                                    str = outputFormat.format(date);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    str = dtStart + "";
                                }
                                tv_bookingDate.setText(str);

                            } else {
                                tv_bookingDate.setText("");
                            }

                            //////////////
                            if (!dataObject.isNull("booking_date")) {

                                String dtStart = dataObject.getString("booking_date");

                                String inputPattern1 = "yyyy-MM-dd HH:mm:ss";
                                String outputPattern1 = "HH:mm";
                                SimpleDateFormat inputFormat1 = new SimpleDateFormat(inputPattern1);
                                SimpleDateFormat outputFormat1 = new SimpleDateFormat(outputPattern1);

                                Date date1 = null;
                                String str1 = null;

                                try {
                                    date1 = inputFormat1.parse(dtStart);
                                    str1 = outputFormat1.format(date1);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    str1 = dtStart + "";
                                }
                                tv_bookingTime.setText(str1);

                            } else {
                                tv_bookingTime.setText("");
                            }


                            /////////

                            if (!dataObject.isNull("booking_location")) {
                                tv_bookingAddress.setText(dataObject.getString("booking_location"));

                            } else {
                                tv_bookingAddress.setText("");
                            }

                            JSONObject items_obejct = dataObject.getJSONObject("items");
                            {
                                repairable = items_obejct.getJSONArray("repairable");
                                if (repairable.length() > 0) {

                                    for (int i = 0; i < repairable.length(); i++) {

                                        count.add("");

                                        smry_type.add("");
                                        smry_category.add("");
                                        smry_qty.add("");
                                        smry_price.add("");
                                        smry_cmnt.add("");


                                        JSONObject repair_object = repairable.getJSONObject(i);
                                        // Toast.makeText(Complaint_Detail.this, "Data repairable " + repair_object, Toast.LENGTH_SHORT).show();

                                        Repairable_Model repairable_model = new Repairable_Model();
                                        String id = repair_object.getString("id");

                                        if (!repair_object.isNull("id")) {
                                            idcomplain.add(Integer.valueOf(repair_object.getString("id")));
                                            repairable_model.setId(repair_object.getString("id"));
                                        } else {
                                            repairable_model.setId("");
                                            idcomplain.add(0);
                                        }

                                        if (!repair_object.isNull("response_type")) {
                                            repairable_model.setResponse_type(repair_object.getString("response_type"));
                                            responsearray.add(repair_object.getString("response_type"));


                                            if (repair_object.getString("response_type").equals("")) {
                                                response_type_hasdata = false;
                                                tv_proceed.setVisibility(View.VISIBLE);

//                                            Toast.makeText(Complaint_Detail.this, "ewaul type repair if case", Toast.LENGTH_SHORT).show();
                                            } else {

                                                response_type_hasdata = true;
                                                repairable_model.setResponse_type(repair_object.getString("response_type"));
                                                responsearray.add(repair_object.getString("response_type"));


                                                invoice.setVisibility(View.VISIBLE);
                                                tv_proceed.setVisibility(View.GONE);
                                            }

                                        } else {
                                            repairable_model.setResponse_type("");
                                            responsearray.add("");
                                        }

                                        if (!repair_object.isNull("quantity")) {
                                            repairable_model.setQuantity(repair_object.getString("quantity"));
                                        } else {
                                            repairable_model.setQuantity("");
                                        }
                                        if (!repair_object.isNull("cost_repair")) {
                                            repairable_model.setRepair_cost(repair_object.getString("cost_repair"));
                                        } else {
                                            repairable_model.setRepair_cost("");
                                        }
                                        if (!repair_object.isNull("cost_replace")) {
                                            repairable_model.setReplacement_cost(repair_object.getString("cost_replace"));
                                        } else {
                                            repairable_model.setReplacement_cost("");
                                        }
                                        if (!repair_object.isNull("cost_sale")) {
                                            repairable_model.setSale_cost(repair_object.getString("cost_sale"));
                                        } else {
                                            repairable_model.setSale_cost("");
                                        }
                                        if (!repair_object.isNull("cost_return")) {
                                            repairable_model.setReturn_cost(repair_object.getString("cost_return"));
                                        } else {
                                            repairable_model.setReturn_cost("");
                                        }
                                        if (!repair_object.isNull("comments")) {
                                            repairable_model.setComments(repair_object.getString("comments"));
                                        } else {
                                            repairable_model.setComments("");
                                        }
                                        repair_modelArrayList.add(repairable_model);

                                    }

                                    set_Data_Repaireable();
                                } else {
//                                        Toast.makeText(Complaint_Detail.this, "" + repairable, Toast.LENGTH_SHORT).show();
                                }

                                /////////////// NON Repair
                                nonrepairable = items_obejct.getJSONArray("nonrepairable");
                                if (nonrepairable.length() > 0) {

                                    count.add("");

                                    smry_type_non.add("");
                                    smry_category_non.add("");
                                    smry_qty_non.add("");
                                    smry_price_non.add("");
                                    smry_cmnt_non.add("");

                                    for (int i = 0; i < nonrepairable.length(); i++) {
                                        JSONObject non_repair_object = nonrepairable.getJSONObject(i);
                                        //   Toast.makeText(Complaint_Detail.this, "Data nonrepairable" + non_repair_object, Toast.LENGTH_SHORT).show();

                                        Non_Repairable_Model non_repairable_model = new Non_Repairable_Model();

                                        if (!non_repair_object.isNull("id")) {
                                            non_repairable_model.setId(non_repair_object.getString("id"));
                                            non_idcomplain.add(Integer.valueOf(non_repair_object.getString("id")));
                                        } else {
                                            non_repairable_model.setId("");
                                            non_idcomplain.add(0);
                                        }
                                        if (!non_repair_object.isNull("response_type")) {
                                            non_repairable_model.setResponse_type(non_repair_object.getString("response_type"));
                                            non_responsearray.add(non_repair_object.getString("response_type"));


                                            if (non_repair_object.getString("response_type").equals("")) {
                                                nonresponse_type_hasdata = false;
                                                tv_proceed.setVisibility(View.VISIBLE);
                                            } else {

                                                nonresponse_type_hasdata = true;
                                                non_repairable_model.setResponse_type(non_repair_object.getString("response_type"));
                                                non_responsearray.add(non_repair_object.getString("response_type"));

                                                tv_proceed.setVisibility(View.GONE);
                                            }


                                        } else {
                                            non_repairable_model.setResponse_type("");
                                            non_responsearray.add("");
                                        }


                                        if (!non_repair_object.isNull("quantity")) {
                                            non_repairable_model.setQuantity_non(non_repair_object.getString("quantity"));
                                        } else {
                                            non_repairable_model.setQuantity_non("");
                                        }
                                        if (!non_repair_object.isNull("cost_replace")) {
                                            non_repairable_model.setNon_replacement_cost(non_repair_object.getString("cost_replace"));
                                        } else {
                                            non_repairable_model.setNon_replacement_cost("");
                                        }
                                        if (!non_repair_object.isNull("cost_sale")) {
                                            non_repairable_model.setNon_sale_cost(non_repair_object.getString("cost_sale"));
                                        } else {
                                            non_repairable_model.setNon_sale_cost("");
                                        }
                                        if (!non_repair_object.isNull("cost_return")) {
                                            non_repairable_model.setNon_return_cost(non_repair_object.getString("cost_return"));
                                        } else {
                                            non_repairable_model.setNon_return_cost("");
                                        }

                                        if (!non_repair_object.isNull("comments")) {
                                            non_repairable_model.setComment(non_repair_object.getString("comments"));
                                        } else {
                                            non_repairable_model.setComment("");
                                        }
                                        non_repairable_modelArrayList.add(non_repairable_model);
                                    }

                                    set_Data_Non_Repairable();
                                } else {

                                }

                                if (repairable.length() == 0 && nonrepairable.length() == 0) {
                                    ll_price_json.setVisibility(View.GONE);
                                    tv_proceed.setVisibility(View.GONE);

                                }
                            }
                            hideloader();
                        }
                        hideloader();


                    } catch (Exception e) {
                        hideloader();
                        e.printStackTrace();
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
                Toast.makeText(getApplicationContext(), "Something went wrong at server!" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void set_Data_Repaireable() {
        repair_adapter = new Repair_Adapter(getApplicationContext());
        recycler_grid_price.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recycler_grid_price.setAdapter(repair_adapter);
        repair_adapter.notifyDataSetChanged();

    }

    public void set_Data_Non_Repairable() {
        non_repaire_adapter = new Non_Repaire_Adapter(getApplicationContext());
        recyclerViewnon.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewnon.setAdapter(non_repaire_adapter);
        non_repaire_adapter.notifyDataSetChanged();

    }

    public void showloader(String message) {
        dialog_progress = new Dialog(Complaint_Detail.this);
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

                        if (scroll_bottom.equals("1")) {
                            nestedScrollview.smoothScrollTo(0, ll_price_json.getBottom());
                            ll_price_json.requestFocus();
                        } else {
                        }
                    }
                }, 1200);
    }

    public interface Service {

        @Headers("Authkey:APPLEDBDMPL")
        @GET("getbookingdetails/{booking_id}")
        Call<ResponseBody> getbookingdetails(@Path("booking_id") String booking_id);

        @Headers("Authkey:APPLEDBDMPL")
        @POST("addpriceresponse")
        @FormUrlEncoded
        Call<ResponseBody> add_price_response(@Field("user_id") String user_id,
                                              @Field("response") JSONArray response,
                                              @Field("booking_id") String booking_id);

        @Headers("Authkey:APPLEDBDMPL")
        @POST("servicefeeback")
        @FormUrlEncoded
        Call<ResponseBody> servicefeeback(@Field("user_id") String user_id,
                                          @Field("booking_id") String booking_id,
                                          @Field("rating") String rating,
                                          @Field("comments") String comments);
    }

    public void servicefeeback(String user_id, String booking_id, String rating, String comment) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Log.i("smiley_user_id", user_id);
        Log.i("smiley_booking_id", booking_id);
        Log.i("smiley_rating", rating);
        Log.i("smiley_comment", comment);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.servicefeeback(user_id, booking_id, rating, comment);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();

                    try {
                        String result = response.body().string();
                        JSONObject obj = new JSONObject(result);

                        if (obj.has("error")) {
                            String message = obj.getString("error_message");
                            Toast.makeText(getApplicationContext(), " " + message, Toast.LENGTH_LONG).show();

                        } else if (obj.has("warning")) {
                            String message = obj.getString("message");
                            Toast.makeText(getApplicationContext(), " " + message, Toast.LENGTH_LONG).show();

                        } else {
                            String message = obj.getString("message");
                            Toast.makeText(getApplicationContext(), " " + message, Toast.LENGTH_LONG).show();

                            if (feedbackdialog.isShowing()) {
                                feedbackdialog.dismiss();
                            } else {
                                feedbackdialog.dismiss();
                            }
                        }
//                        Toast.makeText(getApplicationContext(), " Successfully Add" , Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.dismiss();
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(getApplicationContext(), "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong at server!" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setup_cart_items() {


        Adapter_Complaint_items_after adapter_brand = new Adapter_Complaint_items_after(context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recycler.setLayoutManager(linearLayoutManager);
        ((SimpleItemAnimator) recycler.getItemAnimator()).setSupportsChangeAnimations(false);
        recycler.setAdapter(adapter_brand);

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
