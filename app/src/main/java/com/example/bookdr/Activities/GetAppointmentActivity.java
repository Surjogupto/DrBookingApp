package com.example.bookdr.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
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
import com.example.bookdr.SetGetClasses.GetAppointmentScheduleSetGet;
import com.example.bookdr.SetGetClasses.TicketSetGet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetAppointmentActivity extends AppCompatActivity {

    public static Activity GetAppointment;
    ListView availableschedule;
    LinearLayout bookdr;
    ArrayList<GetAppointmentScheduleSetGet> arrayList = new ArrayList<>();
    String url = Appdata.Url_GetAppointmentSchedule;
    String MSG;

    ProgressBar pgsBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_appointment);

        GetAppointment = this;
        availableschedule = findViewById(R.id.availableschedule);
        GetSchedule(Appdata.Url_GetAppointmentSchedule);
        bookdr = findViewById(R.id.bookdr);

        pgsBar = (ProgressBar)findViewById(R.id.pBar);
    }

    private void GetSchedule(String url){

        final StringRequest GetAppointmentSchedule = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray arr = obj.getJSONArray("availableschedule");
                    for(int i = 0; i<arr.length();i++){
                        GetAppointmentScheduleSetGet sg = new GetAppointmentScheduleSetGet();
                        JSONObject availableschedule = arr.getJSONObject(i);

                        sg.setTimeslotid(availableschedule.getString("timeslotid"));
                        sg.setStatus(availableschedule.getString("status"));
                        sg.setSlotfrom(availableschedule.getString("slotfrom"));
                        sg.setSlotto(availableschedule.getString("slotto"));

                        arrayList.add(sg);
                    }
                    if(arrayList.size()>0){
                        pgsBar.setVisibility(View.GONE);
                        availableschedule.setAdapter(new GetAppointmentScheduleAdapter());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GetAppointmentActivity.this,"Server Error",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("scheduleid",Appdata.scheduleid);
                map.put("date",Appdata.Schedule_date);
                map.put("doctorid",Appdata.doctorid);
                return map;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(GetAppointmentSchedule);
    }

    private void BookDr(String url){
        StringRequest BookingDr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject obj = new JSONObject(response);
                    MSG = obj.getString("Message");

                    String Success = obj.getString("Sucess");
                    if(Success.equalsIgnoreCase("1")){
                        pgsBar.setVisibility(View.GONE);
                        Appdata.Message = MSG.replace("Your booking no is: ","");

                        arrayList.clear();
                        GetSchedule(Appdata.Url_GetAppointmentSchedule);
                        DashBoard.DeptList.finish();
                        DrListActivity.fa.finish();
                        Appointment();
                    }else{
                        pgsBar.setVisibility(View.GONE);
                        Toast.makeText(GetAppointmentActivity.this,MSG,Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GetAppointmentActivity.this,"Server Error",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("doctorid",Appdata.doctorid);
                map.put("userid",Appdata.UserId);
                map.put("timeslotid",Appdata.timeslotid);
                map.put("bookingdate",Appdata.Schedule_date);
                return map;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(BookingDr);
    }


    public class GetAppointmentScheduleAdapter extends BaseAdapter {

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
        public View getView(final int i, View view, ViewGroup viewGroup) {

            String input = arrayList.get(i).getSlotfrom();
            String getSlotfrom = input.substring(0, 5);
            input = arrayList.get(i).getSlotto();
            String getSlotto = input.substring(0, 5);

            LayoutInflater inf = getLayoutInflater();
            final View row = inf.inflate(R.layout.child_get_appointment_schedule,null);
            LinearLayout llrow = row.findViewById(R.id.llrow);
            TextView slotfrom = row.findViewById(R.id.slotfrom);
            TextView slotto = row.findViewById(R.id.slotto);
            LinearLayout bookdr = row.findViewById(R.id.bookdr);
            final TextView bookdrText = row.findViewById(R.id.bookdrText);

            slotfrom.setText(getSlotfrom);
            slotto.setText(getSlotto);
            bookdrText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Appdata.timeslotid = arrayList.get(i).getTimeslotid();
                    Appdata.slotfrom = arrayList.get(i).getSlotfrom();
                    Appdata.slotto = arrayList.get(i).getSlotto();
                    pgsBar.setVisibility(View.VISIBLE);
                    BookDr(Appdata.Url_BookingDr);
                }
            });

            if(arrayList.get(i).getStatus().equals("N")){
                bookdrText.setText("Not Available");
                bookdrText.setTextColor(Color.parseColor("#FFEE0909"));
            }
            return row;
        }
    }

    private void Appointment(){

        AlertDialog.Builder builder = new AlertDialog.Builder(
                GetAppointmentActivity.this);
        builder.setTitle("Appointment Ticket");
        builder.setMessage(Appdata.UserFname + " " + Appdata.UserLname + "\n" +
                "Appointment Boked: " + Appdata.Schedule_date + "\n" +
                      MSG + "\n" +
                "Doctor: " + Appdata.doctorname + "\n"
                + "From: " + Appdata.slotfrom + "\n" +
                "To: " + Appdata.slotto);

        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                startActivity(new Intent(GetAppointmentActivity.this, DashBoard.class));
                finish();
            }
        });

        builder.show();

    }
}
