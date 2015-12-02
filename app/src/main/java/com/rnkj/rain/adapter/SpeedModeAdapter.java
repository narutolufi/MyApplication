package com.rnkj.rain.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.rnkj.rain.R;
import com.rnkj.rain.bean.Area;
import com.rnkj.rain.view.SwitchView;

import java.util.LinkedList;

/**
 * Created by francis on 2015/12/2.
 */
public class SpeedModeAdapter extends BaseAdapter {

    private Context mContext;

    private LinkedList<Area> areas;

    private OnSpeedModeSwitchListener onSpeedModeSwitchListener;

    public void setOnSpeedModeSwitchListener(OnSpeedModeSwitchListener onSpeedModeSwitchListener){
        this.onSpeedModeSwitchListener = onSpeedModeSwitchListener;
    }

    public SpeedModeAdapter(Context context, LinkedList<Area> areas) {
        this.mContext = context;
        this.areas = areas;
    }

    @Override
    public int getCount() {
        return areas.size();
    }

    @Override
    public Object getItem(int position) {
        return areas.get(position) == null ? null : areas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.speed_mode_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mSwitchView = (SwitchView)convertView.findViewById(R.id.mode_switchview);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        if(areas.get(position).getPump().equalsIgnoreCase("ON")){
            viewHolder.mSwitchView.switching(SwitchView.TYPE_LEFT);
        }else{
            viewHolder.mSwitchView.switching(SwitchView.TYPE_RIGHT);
        }

        if(onSpeedModeSwitchListener != null){
            viewHolder.mSwitchView.setOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onCheck(SwitchView sv, boolean checkLeft, boolean checkRight) {
                    onSpeedModeSwitchListener.onCheck(position,areas.get(position),sv,checkLeft,checkRight);
                }
            });
        }

        return convertView;
    }

    public class ViewHolder{
        SwitchView  mSwitchView;
    }

    public interface OnSpeedModeSwitchListener{
        void onCheck(int position,Area area,SwitchView sv, boolean checkLeft, boolean checkRight);
    }
}
