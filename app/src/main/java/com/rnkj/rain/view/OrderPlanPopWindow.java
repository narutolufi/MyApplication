package com.rnkj.rain.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.rnkj.rain.R;
import com.rnkj.rain.bean.Plan;
import com.rnkj.rain.fragment.PlanFragment;

public class OrderPlanPopWindow extends PopupWindow {

    private View conentView;

    private doOrderPlanInterface doOrderPlanInterface;

    private ImageView dateImg, timecostImg;


    public void setDoOrderPlanInterface(doOrderPlanInterface doOrderPlanInterface) {
        this.doOrderPlanInterface = doOrderPlanInterface;
    }


    public OrderPlanPopWindow(final Activity context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.order_plan_popup, null);
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        this.setContentView(conentView);
        this.setWidth(w / 2 + 50);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);
        LinearLayout dateLinearlayout = (LinearLayout) conentView.findViewById(R.id.id_pop_plan_order_date);
        LinearLayout timeLinearlayout = (LinearLayout) conentView.findViewById(R.id.id_pop_plan_order_timecost);

        dateImg = (ImageView) conentView.findViewById(R.id.id_pop_plan_order_date_img);
        timecostImg = (ImageView) conentView.findViewById(R.id.id_pop_plan_order_timecost_img);

        dateLinearlayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dateImg.setVisibility(View.VISIBLE);
                timecostImg.setVisibility(View.INVISIBLE);
                if (doOrderPlanInterface != null) {
                    doOrderPlanInterface.doOrder(PlanFragment.PLAN_ORDER_BY_DATE);
                }
                OrderPlanPopWindow.this.dismiss();
            }
        });

        timeLinearlayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                timecostImg.setVisibility(View.VISIBLE);
                dateImg.setVisibility(View.INVISIBLE);
                if (doOrderPlanInterface != null) {
                    doOrderPlanInterface.doOrder(PlanFragment.PLAN_ORDER_BY_COST);
                }
                OrderPlanPopWindow.this.dismiss();
            }
        });
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            if (PlanFragment.current_order_type == PlanFragment.PLAN_ORDER_BY_DATE) {
                dateImg.setVisibility(View.VISIBLE);
                timecostImg.setVisibility(View.INVISIBLE);
            } else if (PlanFragment.current_order_type == PlanFragment.PLAN_ORDER_BY_COST) {
                dateImg.setVisibility(View.INVISIBLE);
                timecostImg.setVisibility(View.VISIBLE);
            }
            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);
        } else {
            this.dismiss();
        }
    }


    public interface doOrderPlanInterface {
        public void doOrder(int orderBy);
    }
}
