package com.example.bookdr.AppData;

public class Appdata {

    public static final String ServerIP                   = "103.230.103.93";
    public static final String Url_baseurl                = "http://"+ServerIP+"/drbookingapp/bookingapp.asmx";
    public static final String Url_Login                  = "http://"+ServerIP+"/drbookingapp/bookingapp.asmx/UserLogin";
    public static final String Url_UserRegistration       = "http://"+ServerIP+"/drbookingapp/bookingapp.asmx/UserRegistration";
    public static final String Url_Departments            = "http://"+ServerIP+"/drbookingapp/bookingapp.asmx/GetDepartment";
    public static final String Url_GetDoctor              = "http://"+ServerIP+"/drbookingapp/bookingapp.asmx/GetDoctor";
    public static final String Url_GetAppointmentSchedule = "http://"+ServerIP+"/drbookingapp/bookingapp.asmx/GetAppointmentSchedule";
    public static final String Url_BookingDr              = "http://"+ServerIP+"/drbookingapp/bookingapp.asmx/BookingDr";

    ////////#### Register
    public static String Username;

    ///////## Login
    public static String UserId;
    public static String UserFname;
    public static String UserLname;


    ////////##### Get Department data ########////////
    public static String departmentid, Schedule_date;


    ////////###### Dr List Data  #######//////////
    public static String scheduleid , doctorid, doctorname;

    //////##  GetAppointmentScheduleSetGet ##//
    public static String timeslotid,slotfrom,slotto,Message;
    public Appdata(String UserFname,String UserLname,String Schedule_date,String Message,String doctorname,String slotfrom,String slotto) {

    }
}
