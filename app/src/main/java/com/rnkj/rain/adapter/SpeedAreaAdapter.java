package com.rnkj.rain.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.rnkj.rain.R;
import com.rnkj.rain.bean.Area;

import java.util.LinkedList;

/**
 * Created by francis on 2015/12/2.
 */
public class SpeedAreaAdapter extends BaseAdapter {

    private Context mContext;

    private LinkedList<Area> areas;

    private OnSpeedClick onSpeedClick;

    public void setOnSpeedClick(OnSpeedClick onSpeedClick){
        this.onSpeedClick = onSpeedClick;
    }

    public SpeedAreaAdapter(Context context, LinkedList<Area> areas) {
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
                    R.layout.speed_area_item, null);
            viewHolder = new ViewHolder();
            viewHolder.speed_area_start = (EditText)convertView.findViewById(R.id.id_speed_area_start);
            viewHolder.speed_area_end = (EditText)convertView.findViewById(R.id.id_speed_area_end);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.speed_area_start.setText(areas.get(position).getStart()+"");
        viewHolder.speed_area_end.setText(areas.get(position).getEnd()+"");

        return convertView;
    }

    public class ViewHolder{
        EditText speed_area_start,speed_area_end;
    }

    public interface OnSpeedClick{
        void onAdd(int position,Area area);
        void onSubtract(int position,Area area);
    }
}
