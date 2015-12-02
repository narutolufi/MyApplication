package com.rnkj.rain.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rnkj.rain.R;
import com.rnkj.rain.bean.Machine;

import java.util.LinkedList;

/**
 * Created by francis on 2015/10/13.
 */
public class MachineAdapter extends BaseAdapter{

    private Context mContext;

    private LinkedList<Machine> machines;

    private OnMachineDetailClick onMachineDetailClick;

    private OnMachineSettingClick onMachineSettingClick;


    public void setOnMachineSettingClick(OnMachineSettingClick onMachineSettingClick){
        this.onMachineSettingClick = onMachineSettingClick;
    }

    public void setOnMachineDetailClick(OnMachineDetailClick onMachineDetailClick){
        this.onMachineDetailClick = onMachineDetailClick;
    }

    public MachineAdapter(Context context,LinkedList<Machine> machines) {
        this.mContext = context;
        this.machines = machines;
    }

    @Override
    public int getCount() {
        return machines.size();
    }

    @Override
    public Object getItem(int position) {
        return machines.get(position) == null ? null : machines.get(position);
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
                    R.layout.machine_item, null);

            viewHolder = new ViewHolder();
            viewHolder.mac_name = (TextView)convertView.findViewById(R.id.mac_name);
            viewHolder.txt_mac_status = (TextView)convertView.findViewById(R.id.txt_mac_status);
            viewHolder.txt_progress = (TextView)convertView.findViewById(R.id.txt_progress);
            viewHolder.wifi_status = (ImageView)convertView.findViewById(R.id.img_wifi_status);
            viewHolder.mac_status = (ImageView)convertView.findViewById(R.id.img_machine_status);
            viewHolder.setting_img = (ImageView)convertView.findViewById(R.id.setting_img);
            viewHolder.detail_img = (ImageView)convertView.findViewById(R.id.detail_img);
            viewHolder.txt_wifi_status = (TextView) convertView.findViewById(R.id.txt_wifi_status);
            viewHolder.txt_machine_status = (TextView) convertView.findViewById(R.id.txt_machine_status);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.mac_name.setText(machines.get(position).getName());
        if(machines.get(position).getStartSwitch().equalsIgnoreCase(Machine.START_SWITCH_OFF)){
            String result = String.format(mContext.getResources().getString(R.string.mac_status),mContext.getResources().getString(R.string.mac_status_stop));
            viewHolder.txt_mac_status.setText(result);
        }else{
            String result = String.format(mContext.getResources().getString(R.string.mac_status),mContext.getResources().getString(R.string.mac_status_running));
            viewHolder.txt_mac_status.setText(result);
        }

        viewHolder.txt_progress.setText(String.format(mContext.getResources().getString(R.string.mac_progress),machines.get(position).getProgress() + "%"));

        if(machines.get(position).getWorkingStatus().equalsIgnoreCase(Machine.WORKING_STATUS_BROKEN)){
            viewHolder.mac_status.setBackgroundResource(R.drawable.to_be_inspected);
            viewHolder.txt_machine_status.setText("损坏");
        }else if(machines.get(position).getWorkingStatus().equalsIgnoreCase(Machine.WORKING_STATUS_NORMAL)){
            viewHolder.mac_status.setBackgroundResource(R.drawable.normal);
            viewHolder.txt_machine_status.setText("正常");
        }else{
            viewHolder.mac_status.setBackgroundResource(R.drawable.error);
            viewHolder.txt_machine_status.setText("故障");
        }
        if(machines.get(position).getNetworkStatus().equalsIgnoreCase(Machine.NETWORK_STATUS_OFFLINE)){
            viewHolder.wifi_status.setBackgroundResource(R.drawable.online);
            viewHolder.txt_wifi_status.setText("在线");
        }else{
            viewHolder.wifi_status.setBackgroundResource(R.drawable.offline);
            viewHolder.txt_wifi_status.setText("离线");
        }

        viewHolder.setting_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onMachineSettingClick != null){
                    onMachineSettingClick.onClick(machines.get(position));
                }
            }
        });

        viewHolder.detail_img.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(onMachineDetailClick != null){
                    onMachineDetailClick.onClick(machines.get(position));
                }
            }
        });
        return convertView;
    }

    public class ViewHolder{
        TextView mac_name,txt_mac_status,txt_progress,txt_wifi_status,txt_machine_status;
        ImageView mac_status,wifi_status,setting_img,detail_img;
    }


    public interface OnMachineSettingClick{
        public void onClick(Machine machine);
    }


    public interface OnMachineDetailClick{
        public void onClick(Machine machine);
    }
}
