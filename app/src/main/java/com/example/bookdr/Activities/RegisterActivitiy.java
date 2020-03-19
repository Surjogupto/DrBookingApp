package com.example.bookdr.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookdr.AppData.Appdata;
import com.example.bookdr.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivitiy extends AppCompatActivity {


    EditText fname,lname,username,pwd,address,phoneno,email;
    Button Register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activitiy);

        fname    = findViewById(R.id.fname);
        lname    = findViewById(R.id.lname);
        username = findViewById(R.id.username);
        pwd      = findViewById(R.id.pwd);
        address  = findViewById(R.id.address);
        phoneno  = findViewById(R.id.phoneno);
        email    = findViewById(R.id.email);
        Register    = findViewById(R.id.Rregister);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_field()){
                    Register_user(Appdata.Url_UserRegistration);
                }
            }
        });
    }

    private void Register_user(String url){

        StringRequest Register_user = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String Message  = obj.getString("Message");
                    String Sucess  = obj.getString("Sucess");

                    if(Sucess.equalsIgnoreCase("1")){
                        Appdata.Username = username.getText().toString();
                        startActivity(new Intent(RegisterActivitiy.this,LoginActivity.class));
                        Toast.makeText(RegisterActivitiy.this,"Login With Userid and Password",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(RegisterActivitiy.this,"Username Exist try another username",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<>();

                map.put("fname",fname.getText().toString());
                map.put("lname",lname.getText().toString());
                map.put("username",username.getText().toString());
                map.put("pwd",pwd.getText().toString());
                map.put("address",address.getText().toString());
                map.put("phoneno",phoneno.getText().toString());
                map.put("email",email.getText().toString());

                return map;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(Register_user);

    }

    private boolean check_field(){

        if( fname.getText().toString().isEmpty()){
            fname.setError( "First Name is required!" );
            return false;
        }else if(lname.getText().toString().isEmpty()){
            lname.setError( "Last Name is required!" );
            return false;
        }else if(username.getText().toString().isEmpty()){
            username.setError( "User Name is required!" );
            return false;
        }else if(pwd.getText().toString().isEmpty()){
            pwd.setError( "Password is required!" );
            return false;
        }else if(address.getText().toString().isEmpty()){
            address.setError( "Address is required!" );
            return false;
        }else if(phoneno.getText().toString().isEmpty()){
            phoneno.setError( "Phone Number is required!" );
            return false;
        }else if(email.getText().toString().isEmpty()){
            email.setError( "Email is required!" );
            return false;
        }
        return true;
    }

}
