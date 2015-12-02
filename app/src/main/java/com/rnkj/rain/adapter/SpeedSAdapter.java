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

import java.util.LinkedList;

/**
 * Created by francis on 2015/12/2.
 */
public class SpeedSAdapter extends BaseAdapter {

    private Context mContext;

    private LinkedList<Area> areas;

    private OnSpeedClick onSpeedClick;

    public void setOnSpeedClick(OnSpeedClick onSpeedClick){
        this.onSpeedClick = onSpeedClick;
    }

    public SpeedSAdapter(Context context, LinkedList<Area> areas) {
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
                    R.layout.speed_item, null);
            viewHolder = new ViewHolder();
            viewHolder.speed_add = (Button)convertView.findViewById(R.id.id_view_area_confrim);
            viewHolder.speed_del = (Button)convertView.findViewById(R.id.id_view_area_cancel);
            viewHolder.speed_editview = (EditText)convertView.findViewById(R.id.id_view_area_speedviewset);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.speed_editview.setText(areas.get(position).getSpeed()+"");

        if(this.onSpeedClick != null){
            viewHolder.speed_del.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    onSpeedClick.onSubtract(position,areas.get(position));
                }
            });

            viewHolder.speed_add.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    onSpeedClick.onAdd(position, areas.get(position));
                }
            });
        }

        return convertView;
    }

    public class ViewHolder{
        Button speed_add,speed_del;
        EditText speed_editview;
    }

    public interface OnSpeedClick{
        void onAdd(int position, Area area);
        void onSubtract(int position, Area area);
    }
}
