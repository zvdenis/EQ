package com.SuperClub.EQ.Menu;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

public class PickerListener implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, View.OnClickListener {

    public enum PickType {
        DATE,
        TIME
    }

    int day;
    int month;
    int year;
    int hour;
    int minute;

    int savedDay = 11;
    int savedMonth = 4;
    int savedYear = 2020;
    int savedHour = 14;
    int savedMinute = 10;

    public Date date = Calendar.getInstance().getTime();

    private Context context;

    private TextView textView;
    private PickType type;

    public PickerListener(Context context, TextView textView, PickType type) {
        this.type = type;
        this.context = context;
        this.textView = textView;
    }

    private void getDateTimeCalendar() {
        Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        savedDay = day;
        savedMonth = month;
        savedYear = year;
        updateText();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        savedHour = hourOfDay;
        savedMinute = minute;
        updateText();
    }

    @Override
    public void onClick(View v) {
        getDateTimeCalendar();

        if (type.equals(PickType.DATE)) {
            new DatePickerDialog(context, this, year, month, day).show();
        }

        if (type.equals(PickType.TIME)) {
            new TimePickerDialog(context, this, hour, minute, true).show();
        }

    }

    private void updateText() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, savedYear);
        calendar.set(Calendar.MONTH, savedMonth);
        calendar.set(Calendar.DAY_OF_MONTH, savedDay);
        calendar.set(Calendar.HOUR_OF_DAY, savedHour);
        calendar.set(Calendar.MINUTE, savedMinute);
        date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = null;
        if (type.equals(PickType.DATE)) {
            simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        }
        if (type.equals(PickType.TIME)) {
            simpleDateFormat = new SimpleDateFormat("HH:mm");
        }

        String text = simpleDateFormat.format(date);
        textView.setText(text);
    }
}
