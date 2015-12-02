package com.rnkj.rain.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rnkj.rain.R;
import com.rnkj.rain.bean.Plan;
import com.rnkj.rain.utils.DateUtils;

import java.util.LinkedList;

/**
 * Created by francis on 2015/10/13.
 */
public class PlanHistoryAdapter extends BaseAdapter{

    private Context mContext;

    private LinkedList<Plan> plans;


    public PlanHistoryAdapter(Context context, LinkedList<Plan> plans) {
        this.mContext = context;
        this.plans = plans;
    }

    @Override
    public int getCount() {
        return plans.size();
    }

    @Override
    public Object getItem(int position) {
        return plans.get(position) == null ? null : plans.get(position);
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
                    R.layout.plan_item, null);
            viewHolder = new ViewHolder();
            viewHolder.plan_status = (TextView)convertView.findViewById(R.id.id_plan_status);
            viewHolder.plan_startdate = (TextView)convertView.findViewById(R.id.id_plan_startdate);
            viewHolder.plan_area = (TextView)convertView.findViewById(R.id.id_plan_area);
            viewHolder.plan_times = (TextView) convertView.findViewById(R.id.id_plan_times);
            viewHolder.plan_progress = (TextView) convertView.findViewById(R.id.id_plan_progress);
            viewHolder.plan_timecost = (TextView) convertView.findViewById(R.id.id_plan_timecost);
            viewHolder.view_status = (LinearLayout) convertView.findViewById(R.id.id_view_status);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        if(plans.get(position).getTimeCost() == 0){
            viewHolder.plan_timecost.setText("0分");
        }

        if(plans.get(position).getStatus().equalsIgnoreCase(Plan.PLAN_HISTORY_STATUS_COMPLETED)){
            viewHolder.view_status.setBackgroundColor(mContext.getResources().getColor(R.color.plan_txt_status_readying));
            viewHolder.plan_status.setText(mContext.getResources().getString(R.string.plan_history_status_completed));
            viewHolder.plan_timecost.setText("用时 : " + DateUtils.getMillon(plans.get(position).getTimeCost()));
        }else if(plans.get(position).getStatus().equalsIgnoreCase(Plan.PLAN_HISTORY_STATUS_CANCELED)){
            viewHolder.view_status.setBackgroundColor(mContext.getResources().getColor(R.color.plan_txt_status_cancel));
            viewHolder.plan_status.setText(mContext.getResources().getString(R.string.plan_history_status_cancel));
            viewHolder.plan_timecost.setText("已用时 : " + DateUtils.getCountDownTime(plans.get(position).getStartDate()));
        }
        if(plans.get(position).getProgress() > 0 && plans.get(position).getProgress() < 100){
            viewHolder.plan_progress.setText(String.format(mContext.getResources().getString(R.string.plan_history_progress),plans.get(position).getProgress() + "%"));
        }else{
            viewHolder.plan_progress.setVisibility(View.INVISIBLE);
        }
        viewHolder.plan_times.setText(plans.get(position).getTimes() + "次");
        viewHolder.plan_area.setText(plans.get(position).getArea_start() + "°" + "~" + plans.get(position).getArea_end() + "°");
        viewHolder.plan_startdate.setText(DateUtils.getDay(plans.get(position).getStartDate()));
        return convertView;
    }

    public class ViewHolder{
        TextView plan_status,plan_startdate,plan_area,plan_times,plan_progress,plan_timecost;
        LinearLayout view_status;
    }
}
