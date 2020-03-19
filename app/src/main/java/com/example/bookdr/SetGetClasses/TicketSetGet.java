package com.example.bookdr.SetGetClasses;

public class TicketSetGet {
    String userFname, userLname, schedule_date, message,doctorname,slotfrom, slotto;


    public TicketSetGet() {

    }

    public TicketSetGet(String userFname, String userLname, String schedule_date, String message, String doctorname, String slotfrom, String slotto) {
        this.userFname = userFname;
        this.userLname = userLname;
        this.schedule_date = schedule_date;
        this.message = message;
        this.doctorname = doctorname;
        this.slotfrom = slotfrom;
        this.slotto = slotto;
    }
}
