package com.rnkj.rain.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.rnkj.rain.R;

/**
 * Created by francis on 2015/11/10.
 */
public class NumPickerDLUtil {

    private NumberPicker numberPicker,startArea,endArea;
    private AlertDialog ad;
    private Activity activity;

    private SettingsAreaCallback mSettingsAreaCallback;

    private int areaStart_value = 0,areaEnd_value = 270,times_value = 1;

    public NumPickerDLUtil(Activity activity) {
        this.activity = activity;
    }

    public void setAreaCallback(SettingsAreaCallback settingsAreaCallback){
        this.mSettingsAreaCallback = settingsAreaCallback;
    }

    private void initAreaPicker(NumberPicker startPicker,NumberPicker endPicker,int start_value,int end_value) {
        startPicker.setMaxValue(359);
        startPicker.setMinValue(0);
        startPicker.setOnValueChangedListener(new areaValueChargeListener());
        endPicker.setMaxValue(359);
        endPicker.setMinValue(0);
        endPicker.setOnValueChangedListener(new areaValueChargeListener());
        if (start_value > 0 || end_value > 0){
            startPicker.setValue(start_value);
            endPicker.setValue(end_value);
        }
    }

    private void initTimesPicker(NumberPicker numberPicker) {
        numberPicker.setMaxValue(20);
        numberPicker.setMinValue(1);
        numberPicker.setOnValueChangedListener(new timesValueChargeListener());
    }


    public AlertDialog areaPicKDialog(final EditText startInput,final EditText endInput) {
        LinearLayout dateTimeLayout = (LinearLayout) activity
                .getLayoutInflater().inflate(R.layout.dialog_areapicker, null);
        startArea = (NumberPicker) dateTimeLayout.findViewById(R.id.start_area);
        endArea = (NumberPicker) dateTimeLayout.findViewById(R.id.end_area);
        initAreaPicker(startArea,endArea,areaStart_value,areaEnd_value);
        ad = new AlertDialog.Builder(activity)
                .setTitle("设置起点~终点")
                .setView(dateTimeLayout)
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        startInput.setText(String.format(activity.getResources().getString(R.string.str_area_mark), areaStart_value));
                        endInput.setText(String.format(activity.getResources().getString(R.string.str_area_mark), areaEnd_value));
                        if(mSettingsAreaCallback != null){
                            mSettingsAreaCallback.onCallback(areaStart_value,areaEnd_value);
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                }).show();

        return ad;
    }

    public AlertDialog timesPicKDialog(final EditText inputDate) {
        LinearLayout dateTimeLayout = (LinearLayout) activity
                .getLayoutInflater().inflate(R.layout.dialog_numberpicker, null);
        numberPicker = (NumberPicker) dateTimeLayout.findViewById(R.id.numberPicker);
        initTimesPicker(numberPicker);
        ad = new AlertDialog.Builder(activity)
                .setTitle("设置次数")
                .setView(dateTimeLayout)
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        inputDate.setText(times_value + "");
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        inputDate.setText("");
                    }
                }).show();

        return ad;
    }


    class areaValueChargeListener implements NumberPicker.OnValueChangeListener {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            switch (picker.getId()){
                case R.id.start_area:
                    areaStart_value = newVal;
                    break;
                case R.id.end_area:
                    areaEnd_value = newVal;
                    break;
            }
        }
    }

    class timesValueChargeListener implements NumberPicker.OnValueChangeListener {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            times_value = newVal;
        }
    }



    public interface SettingsAreaCallback{
        void onCallback(int start_area,int end_area);
    }


    public int getAreaStart_value(){
        return areaStart_value;
    }

    public  int getAreaEnd_value(){
        return areaEnd_value;
    }

}
