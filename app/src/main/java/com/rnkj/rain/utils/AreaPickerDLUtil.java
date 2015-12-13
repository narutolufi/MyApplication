package com.rnkj.rain.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.rnkj.rain.R;
import com.rnkj.rain.bean.Area;
import com.rnkj.rain.fragment.SpeedFragment;

/**
 * Created by francis on 2015/11/10.
 */
public class AreaPickerDLUtil {

    private NumberPicker startArea,endArea;
    private AlertDialog ad;
    private Activity activity;

    private SettingsAreaCallback mSettingsAreaCallback;

    private int areaStart_value = 0,areaEnd_value = 270;

    private Area selectedArea;

    private int selectedPosition;

    private int rangeStart_value = 0,rangeEnd_value = 0;

    private int areaSize = 0;

    public AreaPickerDLUtil(Activity activity,Area area,int position) {
        this.activity = activity;
        this.selectedArea = area;
        this.selectedPosition = position;
        this.areaSize = SpeedFragment.speed.getAreaList().size();

    }

    public void setAreaCallback(SettingsAreaCallback settingsAreaCallback){
        this.mSettingsAreaCallback = settingsAreaCallback;
    }

    private void initAreaPicker(NumberPicker startArea,NumberPicker endArea,int start_value,int end_value) {
        startArea.setMaxValue(start_value);
        startArea.setMinValue(0);
        startArea.setOnValueChangedListener(new areaValueChargeListener());
        endArea.setMaxValue(end_value);
        endArea.setMinValue(0);
        endArea.setOnValueChangedListener(new areaValueChargeListener());
        startArea.setValue(selectedArea.getStart());
        endArea.setValue(selectedArea.getEnd());
    }


    public AlertDialog areaPicKDialog() {
        LinearLayout dateTimeLayout = (LinearLayout) activity
                .getLayoutInflater().inflate(R.layout.dialog_areapicker, null);
        startArea = (NumberPicker) dateTimeLayout.findViewById(R.id.start_area);
        endArea = (NumberPicker) dateTimeLayout.findViewById(R.id.end_area);
        handlerRanges(startArea,endArea);
        ad = new AlertDialog.Builder(activity)
                .setTitle("设置起点~终点")
                .setView(dateTimeLayout)
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(mSettingsAreaCallback != null){
                            mSettingsAreaCallback.onCallback();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
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

    public interface SettingsAreaCallback{
        void onCallback();
    }


    public int getAreaStart_value(){
        return areaStart_value;
    }

    public  int getAreaEnd_value(){
        return areaEnd_value;
    }




    private void handlerRanges(NumberPicker startArea,NumberPicker endArea){
        if(areaSize == 1){
            rangeStart_value = selectedArea.getStart();
            rangeEnd_value = selectedArea.getEnd();
        }
        if(areaSize > 1){
            if(selectedPosition == 0){
                rangeStart_value = selectedArea.getStart();
                rangeEnd_value = SpeedFragment.speed.getAreaList().get(selectedPosition + 1).getEnd() - 1;
            }else if(selectedPosition == (SpeedFragment.speed.getAreaList().size() -1)){
                rangeStart_value = SpeedFragment.speed.getAreaList().get(selectedPosition -1).getStart() + 1;
                rangeEnd_value = selectedArea.getEnd();
            }else{
                rangeStart_value = SpeedFragment.speed.getAreaList().get(selectedPosition -1).getStart() + 1;
                rangeEnd_value = SpeedFragment.speed.getAreaList().get(selectedPosition + 1).getEnd() - 1;
            }
        }
        initAreaPicker(startArea,endArea,rangeStart_value,rangeEnd_value);
    }

}
