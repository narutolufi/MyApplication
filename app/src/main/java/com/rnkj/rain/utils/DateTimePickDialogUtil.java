package com.rnkj.rain.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.rnkj.rain.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by francis on 2015/11/9.
 */
public class DateTimePickDialogUtil implements DatePicker.OnDateChangedListener,
        TimePicker.OnTimeChangedListener {
    private DatePicker datePicker;
    private TimePicker timePicker;
    private AlertDialog ad;
    private String dateTime,timeTime;
    private Activity activity;

    private EditText ed_datePicker,ed_timePicker;
    private Calendar date_calendar,time_calendar;

    public DateTimePickDialogUtil(Activity activity) {
        this.activity = activity;
    }

    private void initDatePicker(DatePicker datePicker) {
        date_calendar = Calendar.getInstance();
        datePicker.init(date_calendar.get(Calendar.YEAR),date_calendar.get(Calendar.MONTH),date_calendar.get(Calendar.DAY_OF_MONTH), this);
        date_calendar.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        String str_week = DateUtils.getDayForFormat(date_calendar.getTimeInMillis());
        dateTime = sdf.format(date_calendar.getTime());
        dateTime = dateTime + str_week;
    }

    private void initTimerPicker(TimePicker timePicker) {
        time_calendar = Calendar.getInstance();
        timePicker.setCurrentHour(time_calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(time_calendar.get(Calendar.MINUTE));
        time_calendar.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        timeTime = sdf.format(time_calendar.getTime());
    }

    public AlertDialog datePicKDialog(EditText inputDate) {
        LinearLayout dateTimeLayout = (LinearLayout) activity
                .getLayoutInflater().inflate(R.layout.dialog_datepicker, null);
        datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.datepicker);
        initDatePicker(datePicker);
        ed_datePicker = inputDate;
        ad = new AlertDialog.Builder(activity)
                .setTitle("设置日期")
                .setView(dateTimeLayout)
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ed_datePicker.setText(dateTime);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ed_datePicker.setText("");
                    }
                }).show();

        return ad;
    }

    public AlertDialog timePicKDialog(final EditText inputDate) {
        LinearLayout dateTimeLayout = (LinearLayout) activity
                .getLayoutInflater().inflate(R.layout.dialog_timepicker, null);
        timePicker = (TimePicker) dateTimeLayout.findViewById(R.id.timepicker);
        initTimerPicker(timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(this);
        ed_timePicker = inputDate;
        if(datePicker == null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    T.show(activity,"请先选择日期",3000);
                }
            });
            return null;
        }
        ad = new AlertDialog.Builder(activity)
                .setTitle("设置时间")
                .setView(dateTimeLayout)
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ed_timePicker.setText(timeTime);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ed_timePicker.setText("");
                    }
                }).show();
        return ad;
    }

    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        time_calendar = Calendar.getInstance();
        if(hourOfDay == 0 && minute == 0){
            time_calendar.setTimeInMillis(System.currentTimeMillis());
        }else{
            time_calendar.set(datePicker.getYear(), datePicker.getMonth(),datePicker.getDayOfMonth(),hourOfDay,minute);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        timeTime = sdf.format(time_calendar.getTime());
    }

    public void onDateChanged(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
        date_calendar = Calendar.getInstance();
        if(year == 0 && monthOfYear == 0 && dayOfMonth == 0){
            date_calendar.setTimeInMillis(System.currentTimeMillis());
        }else{
            date_calendar.set(year, monthOfYear,dayOfMonth);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        String str_week = DateUtils.getDayForFormat(date_calendar.getTimeInMillis());
        dateTime = sdf.format(date_calendar.getTime());
        dateTime = dateTime + str_week;
    }


    public DatePicker getDatePicker() {
        return datePicker;
    }

    public void setDatePicker(DatePicker datePicker) {
        this.datePicker = datePicker;
    }

    public TimePicker getTimePicker() {
        return timePicker;
    }

    public void setTimePicker(TimePicker timePicker) {
        this.timePicker = timePicker;
    }

    public long getMillSeconds(){
        if(time_calendar != null)
            return time_calendar.getTimeInMillis();
        return 0;
    }
}
