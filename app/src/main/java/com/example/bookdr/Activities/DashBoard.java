package com.example.bookdr.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookdr.AppData.Appdata;
import com.example.bookdr.R;
import com.example.bookdr.SetGetClasses.DepartmentSetGet;

import android.view.LayoutInflater;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.bookdr.Activities.LoginActivity.USER_FILE;

public class DashBoard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final Calendar c = Calendar.getInstance();
    final Context context = this;

    public static Activity DeptList;

    public Toolbar toolbar;
    public GridView deptList;

    ProgressBar pgsBar;
    // ListView deptList;
    public ArrayList<DepartmentSetGet> arrayList = new ArrayList<>();
    String url = Appdata.Url_Departments;
    // TextView Department = findViewById(R.id.Department);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        ///// Setting ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Login.LoginActivity.finish();
        define_id();
        // toolbar_call();
        drawer_call();
        /////Getting Department List
        DeptList = this;

        ///// On Department Click
        deptList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String _year = String.valueOf(year);
                        String _month = (month+1) < 10 ? "0" + (month+1) : String.valueOf(month+1);
                        String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                        String _pickedDate = _month + "/" + _date+ "/" + year ;
                        Appdata.Schedule_date = _pickedDate;

                        startActivity(new Intent(DashBoard.this,DrListActivity.class));
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
                Appdata.departmentid = arrayList.get(i).getDepartmentid();
            }
        });

        GetDeptList(url);
    }


    @SuppressLint("WrongViewCast")
    public void define_id(){
        toolbar         = findViewById(R.id.toolbar);
        deptList        = findViewById(R.id.DeptList);
        pgsBar          = findViewById(R.id.pBar);

    }

    public void drawer_call(){
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.LogOut){
            SharedPreferences.Editor editor = getSharedPreferences(USER_FILE, MODE_PRIVATE).edit();
            editor.putString("KEEP_LOGIN", "N");
            editor.apply();
            startActivity(new Intent(DashBoard.this,LoginActivity.class));
            finish();
        }else if(id == R.id.Appointments){

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }










    private void GetDeptList(String url){
        StringRequest DeptList = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj=new JSONObject(response);
                    JSONArray arr= obj.getJSONArray("DepartmentDetails");

                    for(int i = 0;i<arr.length();i++){

                        DepartmentSetGet sg=new DepartmentSetGet();

                        JSONObject DeptList=arr.getJSONObject(i);
                        sg.setDepartmentid(DeptList.getString("departmentid"));
                        sg.setDepartmentname(DeptList.getString("departmentname"));
                        arrayList.add(sg);
                        Appdata.departmentid = arrayList.get(i).getDepartmentid();
                    }
                    if (arrayList.size()>0){
                        pgsBar.setVisibility(View.GONE);
                        deptList.setAdapter(new DeptList());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DashBoard.this," Server Error",Toast.LENGTH_LONG).show();
            }
        });

        Volley.newRequestQueue(getApplicationContext()).add(DeptList);
    }

    public class DeptList extends BaseAdapter {

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
            View row = inf.inflate(R.layout.child_department,null);
            TextView Department = row.findViewById(R.id.Department);
            Department.setText(arrayList.get(i).getDepartmentname());
            return row;
        }
    }


}
