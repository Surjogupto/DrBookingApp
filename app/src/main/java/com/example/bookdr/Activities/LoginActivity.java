package com.example.bookdr.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookdr.AppData.Appdata;
import com.example.bookdr.NetworkState.NetworsStateCheck;
import com.example.bookdr.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity{


    public static Activity LoginActivity;
    TextInputEditText user_name,password;
    TextInputLayout InputLayoutName,InputLayoutPasswd;
    TextView signup;
    Button Login;
    ProgressBar pgsBar;

    public static final String USER_FILE = "USER_FILE";
    CheckBox keeplog;

    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginActivity = this;

        final String url = Appdata.Url_Login;

        signup      = findViewById(R.id.signup);
        user_name   = findViewById(R.id.user_name);
        password    = findViewById(R.id.passwd);
        Login       = findViewById(R.id.Login);
        InputLayoutName = findViewById(R.id.input_layout_name);
        InputLayoutPasswd = findViewById(R.id.input_layout_passwd);


        keeplog = (CheckBox) findViewById(R.id.RememberMe);
        user_name.setText(Appdata.Username);


        pgsBar = (ProgressBar)findViewById(R.id.pBar);


        /* Text Watcher to in put fields */
        user_name.addTextChangedListener(new MyTextWatcher(user_name));
        password.addTextChangedListener(new MyTextWatcher(password));

        /* ///////  Button Click for Register */
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //signup.setEnabled(false);
                startActivity(new Intent(LoginActivity.this,RegisterActivitiy.class));
            }
        });


        /*//////
        *       Button for Login
        * */
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///////// Checking Vailability of network
                if(NetworsStateCheck.isOnline(getApplicationContext())) {
                    if (check_field()) {
                        Login(url);
                        pgsBar.setVisibility(View.VISIBLE);
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Network Not Available ",Toast.LENGTH_LONG).show();
                }
            }
        });

        keeplog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    flag = true;
                }
            }
        });
    }

    private void Login(String url){

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj=new JSONObject(response);
                    String success = obj.getString("Sucess");

                    if(success.equalsIgnoreCase("1")){
                        JSONObject UserDetails = obj.getJSONObject("UserDetails");
                        Appdata.UserId = UserDetails.getString("userid");
                        Appdata.UserFname = UserDetails.getString("fname");
                        Appdata.UserLname = UserDetails.getString("lname");
                        Appdata.Username = UserDetails.getString("username");

                        if(flag){
                            SharedPreferences.Editor editor = getSharedPreferences(USER_FILE, 0).edit();
                            editor.putString("NAME", user_name.getText().toString());
                            editor.putString("userid",Appdata.UserId);
                            editor.putString("fname",Appdata.UserFname);
                            editor.putString("lname",Appdata.UserLname);
                            editor.putString("KEEP_LOGIN", "Y");
                            editor.apply();
                        }else{
                            SharedPreferences.Editor editor = getSharedPreferences(USER_FILE, MODE_PRIVATE).edit();
                            editor.putString("NAME", user_name.getText().toString());
                            editor.putString("KEEP_LOGIN", "N");
                            editor.apply();
                        }
                        pgsBar.setVisibility(View.GONE);
                        startActivity(new Intent(LoginActivity.this, DashBoard.class));
                        finish();
                    }else {
                        pgsBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this,obj.getString("Message"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<>();
                map.put("username",user_name.getText().toString());
                map.put("pwd",password.getText().toString());
                return map;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(sr);
    }

    /* Validate Login
    *  Forn Data
    *
    *
    *
    * */

    private boolean check_field(){

        if (!validateName()) {
         return false;
        }

        if (!validatePassword()) {
            return false;
        }
        return true;
    }

    private boolean validateName() {
        if (user_name.getText().toString().trim().isEmpty()) {
            InputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(user_name);
            return false;
        } else {
            InputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (password.getText().toString().trim().isEmpty()) {
            InputLayoutPasswd.setError(getString(R.string.err_msg_password));
            requestFocus(password);
            return false;
        } else {
            InputLayoutPasswd.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.user_name:
                    validateName();
                    break;
                case R.id.passwd:
                    validatePassword();
                    break;
            }
        }
    }




}