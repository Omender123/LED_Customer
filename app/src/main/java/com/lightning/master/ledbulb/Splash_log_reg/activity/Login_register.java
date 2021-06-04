package com.lightning.master.ledbulb.Splash_log_reg.activity;

import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.view.WindowManager;

import com.lightning.master.ledbulb.R;
import com.lightning.master.ledbulb.Splash_log_reg.WelcomeScreen;

public class Login_register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        FragmentTransaction fragmentTransaction =getSupportFragmentManager().beginTransaction();
          fragmentTransaction.add(R.id.container_splash,new WelcomeScreen());
        fragmentTransaction.commit();
    }
}
