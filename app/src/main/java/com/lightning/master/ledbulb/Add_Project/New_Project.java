package com.lightning.master.ledbulb.Add_Project;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lightning.master.ledbulb.HomeMain.Home;
import com.lightning.master.ledbulb.R;

public class New_Project extends AppCompatActivity {

    ImageView iv_back;
    int POS;


    TextView tv_proceed,tv_desc,tv_add,tv_img,tv_hint;
LinearLayout ll_add_img;
RelativeLayout rl_hint;
    EditText et_add,et_desc,et_length;
    CardView cc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__project);

        et_add=findViewById(R.id.et_address);
        et_desc=findViewById(R.id.et_desc);
        tv_desc=findViewById(R.id.tv_desc);
        tv_add=findViewById(R.id.tv_add);
        et_length=findViewById(R.id.et_lenth);
        rl_hint=findViewById(R.id.rl_hint);
        tv_hint=findViewById(R.id.hint);

        tv_hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            rl_hint.setVisibility(View.VISIBLE);


            }
        });


        ll_add_img=findViewById(R.id.ll_add_file);
        tv_img=findViewById(R.id.tv_add_imageex);






        tv_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_add_img.setVisibility(View.VISIBLE);
                tv_img.setCompoundDrawables(null, null, null, null);

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


        iv_back=findViewById(R.id.iv_back_new);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(New_Project.this, Home.class));
            }
        });

        tv_proceed=findViewById(R.id.tv_proceed_new);
        tv_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(New_Project.this, "Thanks We will contact You shortly", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(New_Project.this, Home.class));
            }
        });


    }
}
