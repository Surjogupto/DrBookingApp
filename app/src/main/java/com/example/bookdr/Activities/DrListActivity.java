package com.example.bookdr.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
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
import com.example.bookdr.R;
import com.example.bookdr.SetGetClasses.DrListSetGet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DrListActivity extends AppCompatActivity {
    ListView DrListView;
    ArrayList<DrListSetGet> arrayList   = new ArrayList<>();
    String url                          = Appdata.Url_GetDoctor;

    ProgressBar pgsBar;
    public static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dr_list);

        fa = this;
        DrListView = findViewById(R.id.DrList);
        pgsBar = (ProgressBar)findViewById(R.id.pBar);

        DrListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Appdata.scheduleid = arrayList.get(i).getScheduleid();
            Appdata.doctorid   = arrayList.get(i).getDoctorid();
            Appdata.doctorname = arrayList.get(i).getDoctorname();
            //Toast.makeText(DrList.this,Appdata.scheduleid+Appdata.doctorid+Appdata.Schedule_date,Toast.LENGTH_SHORT).show();
            startActivity(new Intent(DrListActivity.this,GetAppointmentActivity.class));
            }
        });
        GetDrList(url);
    }

    private void GetDrList(String url){
        final StringRequest DrList = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject obj = new JSONObject(response);
                    JSONArray arr = obj.getJSONArray("DoctorSchedule");
                    for(int i = 0; i < arr.length();i++){
                        DrListSetGet sg = new DrListSetGet();
                        JSONObject DrList = arr.getJSONObject(i);
                        JSONObject DrDetails = DrList.getJSONObject("doctorde");
                        sg.setDoctorname(DrDetails.getString("doctorname"));
                        sg.setDoctorid(DrDetails.getString("doctorid"));
                        sg.setScheduleid(DrList.getString("scheduleid"));
                        arrayList.add(sg);
                    }
                    if(arrayList.size()>0){
                        pgsBar.setVisibility(View.GONE);
                        DrListView.setAdapter(new DrListAdapter()); }
                } catch (JSONException e) { e.printStackTrace(); }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DrListActivity.this," Server Error",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("departmentid",Appdata.departmentid);
                map.put("bookingdate",Appdata.Schedule_date);
                return map;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(DrList);

    }

    public class DrListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inf = getLayoutInflater();
            View row = inf.inflate(R.layout.child_dr_list,null);
            TextView DrList = row.findViewById(R.id.Dr);
            DrList.setText(arrayList.get(i).getDoctorname());
            return row;
        }
    }
}
