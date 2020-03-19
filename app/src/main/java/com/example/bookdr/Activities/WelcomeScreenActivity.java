package com.example.bookdr.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bookdr.AppData.Appdata;
import com.example.bookdr.NetworkState.NetworsStateCheck;
import com.example.bookdr.R;

import static com.example.bookdr.Activities.LoginActivity.USER_FILE;

public class WelcomeScreenActivity extends AppCompatActivity {

    ProgressBar pgsBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        pgsBar = (ProgressBar)findViewById(R.id.pBar);

        /*  Check Network available Or not */
        if(NetworsStateCheck.isOnline(getApplicationContext())) {

            new CountDownTimer(2000, 1000) {
                @Override
                public void onTick(long l) { }
                @Override
                public void onFinish() {
                    SharedPreferences prefs = getSharedPreferences(USER_FILE, MODE_PRIVATE);
                    String KEEP_LOGIN = prefs.getString("KEEP_LOGIN", "No name defined");
                    if( KEEP_LOGIN.equalsIgnoreCase("Y")){
                        Appdata.UserId  = prefs.getString("userid", "No name defined");
                        Appdata.UserFname = prefs.getString("fname", "No name defined");
                        Appdata.UserLname = prefs.getString("lname", "No name defined");
                        startActivity(new Intent(WelcomeScreenActivity.this, DashBoard.class));
                        finish();
                    }else{
                        pgsBar.setVisibility(View.GONE);
                        startActivity(new Intent(WelcomeScreenActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            }.start();
        }else{
            /* If Network Not Available Close The APP*/
            Toast.makeText(getApplicationContext(),"Network Not Available ",Toast.LENGTH_SHORT).show();
            new CountDownTimer(3000,1000){
                 @Override
                public void onTick(long millisUntilFinished) {}

                @Override
                public void onFinish() {
                    finish();
                }
            }.start();
        }
    }
}
