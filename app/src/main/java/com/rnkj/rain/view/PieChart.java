package com.rnkj.rain.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.rnkj.rain.R;
import com.rnkj.rain.utils.ChartUtils;
import com.rnkj.rain.utils.ViewUtil;

/**
 * Created by francis on 2015/10/8.
 */
public class PieChart extends View {

    private Context context;

    private float cirX;
    private float cirY;
    private float radius;
    private float arcOffset;

    private float mMaxWidth;
    private float mMaxHeight;

    private int currentWidth;
    private int currentHeight;

    private int[] startLocation;
    //圆和扇形的间距
    private float gapOffset;

    private float startCirAngle;
    private float endCirAngle;
    private float currentCirAngle;
    private float start2StopGap;

    private RectF arcDirectionRF, arcRF;

    private ChartUtils.Position directionStartPos, directionStopPos;

    private ChartUtils.Position circleStartPos, circleStopPos, circleCurrentPos;

    private ChartUtils.Position arcStartPos, arcStopPos, arcCurrentPos;

    private ChartUtils.Position arcGapStartPos, arcGapStopPos, arcGapCurrentPos;

    public static final int DIRECTION_CLOCKWISE = 0;//顺时针
    public static final int DIRECTION_ANTICLOCKWISE = 1;//顺时针

    private int direction = DIRECTION_CLOCKWISE;

    private static final float ARC_START_ANGLE = 30;
    private static final float ARC_STOP_ANGLE = 60;

    private Paint[] arrPaintArc;
    private Paint textPaint, linePaint = null;
    private int view_background, pointerColor;

    public PieChart(Context context) {
        super(context);
    }

    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PieChart);
        view_background = a.getColor(R.styleable.PieChart_view_background, Color.WHITE);
        int enabledRunBackground = a.getColor(R.styleable.PieChart_enabled_run_background, 0xFF949FB5);
        int unabledRunBackground = a.getColor(R.styleable.PieChart_unabled_run_background, 0xFF34C2BC);
        pointerColor = a.getColor(R.styleable.PieChart_current_pointer_color, Color.RED);
        int[] arrColor = {unabledRunBackground, enabledRunBackground};
        //解决4.1版本 以下canvas.drawTextOnPath()不显示问题
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mMaxWidth = ViewUtil.dip2px(context, 200);
        mMaxHeight = ViewUtil.dip2px(context, 200);
        //设置边缘特殊效果
        BlurMaskFilter PaintBGBlur = new BlurMaskFilter(1, BlurMaskFilter.Blur.INNER);
        arrPaintArc = new Paint[2];
        for (int i = 0; i <= 1; i++) {
            arrPaintArc[i] = new Paint();
            arrPaintArc[i].setColor(arrColor[i]);
            arrPaintArc[i].setStyle(Paint.Style.FILL);
            arrPaintArc[i].setStrokeWidth(4);
            arrPaintArc[i].setMaskFilter(PaintBGBlur);
        }
        startCirAngle = ChartUtils.CalcArcAngle(90);
        endCirAngle = ChartUtils.CalcArcAngle(0);
        currentCirAngle = ChartUtils.CalcArcAngle(200);
        start2StopGap = endCirAngle - startCirAngle;
        arcOffset = ViewUtil.dip2px(context, 10);
    }

    public void onDraw(Canvas canvas) {
        getCircleInfoByWidthAndHeight();
        initView();
        canvas.drawColor(view_background);
        arcRF = new RectF(gapOffset, gapOffset, currentWidth - gapOffset, currentHeight - gapOffset);
        arcDirectionRF = new RectF(gapOffset, gapOffset, currentWidth - gapOffset + arcOffset, currentHeight - gapOffset + arcOffset);
        Path pathArc = new Path();
        pathArc.addCircle(cirX, cirY, radius, Path.Direction.CW);
        canvas.drawPath(pathArc, arrPaintArc[0]);

        canvas.drawArc(arcRF, startCirAngle, start2StopGap, true, arrPaintArc[1]);

        if (startCirAngle == endCirAngle) {
            canvas.drawText("起/终点", arcGapStartPos.posX, arcGapStartPos.posY, textPaint);
        } else if (Math.abs(startCirAngle - currentCirAngle) < 5 && Math.abs(startCirAngle - currentCirAngle) >= 0) {
            canvas.drawText("当前/起点", arcGapCurrentPos.posX, arcGapCurrentPos.posY, textPaint);
        } else if (Math.abs(endCirAngle - currentCirAngle) < 5 && Math.abs(endCirAngle - currentCirAngle) >= 0) {
            canvas.drawText("当前/终点", arcGapCurrentPos.posX, arcGapCurrentPos.posY, textPaint);
        } else if (startCirAngle == endCirAngle && startCirAngle == currentCirAngle && startCirAngle == 270) {
            canvas.drawText("起点/终点/当前", arcGapStartPos.posX, arcGapStartPos.posY, textPaint);
        } else {
            canvas.drawText("起点", arcGapStartPos.posX, arcGapStartPos.posY, textPaint);
            canvas.drawText("终点", arcGapStopPos.posX, arcGapStopPos.posY, textPaint);
            canvas.drawText("当前", arcGapCurrentPos.posX, arcGapCurrentPos.posY, textPaint);
        }
        canvas.drawLine(cirX, cirY, circleCurrentPos.posX, circleCurrentPos.posY, linePaint);
        drawTriangle(canvas, directionStartPos.posX, directionStartPos.posY, directionStopPos.posX, directionStopPos.posY, true);
        drawTriangle(canvas, arcStartPos.posX, arcStartPos.posY, circleStartPos.posX, circleStartPos.posY, false);
        drawTriangle(canvas, arcStopPos.posX, arcStopPos.posY, circleStopPos.posX, circleStopPos.posY, false);
        drawTriangle(canvas, arcCurrentPos.posX, arcCurrentPos.posY, circleCurrentPos.posX, circleCurrentPos.posY, false);
    }

    public void drawTriangle(Canvas canvas, float sx, float sy, float ex, float ey, boolean isDrawLine) {
        double H, L;
        if (isDrawLine) {
            H = ViewUtil.dip2px(context, 10);
            L = ViewUtil.dip2px(context, 5);
            ex = ex + ViewUtil.dip2px(context, 1.6f);
            ey = ey + ViewUtil.dip2px(context, 1.6f);
            sx = sx + ViewUtil.dip2px(context, 1.6f);
            sy = sy + ViewUtil.dip2px(context, 1.6f);
        } else {
            H = ViewUtil.dip2px(context, 7);
            L = ViewUtil.dip2px(context, 3);
        }
        double awrad = Math.atan(L / H); // 箭头角度
        double arraow_len = Math.sqrt(L * L + H * H); // 箭头的长度
        double[] arrXY_1 = ChartUtils.rotateVec(ex - sx, ey - sy, awrad, true, arraow_len);
        double[] arrXY_2 = ChartUtils.rotateVec(ex - sx, ey - sy, -awrad, true, arraow_len);
        double x_3 = ex - arrXY_1[0];
        double y_3 = ey - arrXY_1[1];
        double x_4 = ex - arrXY_2[0];
        double y_4 = ey - arrXY_2[1];
        Double X3 = new Double(x_3);
        int x3 = X3.intValue();
        Double Y3 = new Double(y_3);
        int y3 = Y3.intValue();
        Double X4 = new Double(x_4);
        int x4 = X4.intValue();
        Double Y4 = new Double(y_4);
        int y4 = Y4.intValue();
        if (isDrawLine) {
            canvas.drawArc(arcDirectionRF, ARC_START_ANGLE, ARC_STOP_ANGLE - ARC_START_ANGLE, false, getLinePaint(Color.BLACK, Paint.Style.STROKE));
        }
        Path triangle = new Path();
        triangle.moveTo(ex, ey);
        triangle.lineTo(x3, y3);
        triangle.lineTo(x4, y4);
        triangle.close();
        canvas.drawPath(triangle, getLinePaint(Color.BLACK, Paint.Style.FILL));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            currentWidth = widthSize;
        } else {
            currentWidth = (int) mMaxWidth;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            currentHeight = heightSize;
        } else {
            currentHeight = (int) mMaxHeight;
        }
        setMeasuredDimension(currentWidth, currentHeight);
    }

    private void getCircleInfoByWidthAndHeight() {
        startLocation = new int[2];
        this.getLocationOnScreen(startLocation);
        cirX = currentWidth / 2;
        cirY = currentWidth / 2;
        if (currentHeight > currentWidth) {
            radius = (float) ((currentWidth / 2) * 0.7);
            gapOffset = currentWidth / 2 - radius;

        } else {
            radius = (float) ((currentHeight / 2) * 0.7);
            gapOffset = currentWidth / 2 - radius;
        }
    }


    private void initView() {
        initPaint();
        switch (direction) {
            case DIRECTION_CLOCKWISE:
                directionStartPos = ChartUtils.CalcArcEndPointXY(cirX, cirY, radius + arcOffset, ARC_STOP_ANGLE - 5, 0);
                directionStopPos = ChartUtils.CalcArcEndPointXY(cirX, cirY, radius + arcOffset, ARC_STOP_ANGLE, 0);
                break;
            case DIRECTION_ANTICLOCKWISE:
                directionStartPos = ChartUtils.CalcArcEndPointXY(cirX, cirY, radius + arcOffset, ARC_START_ANGLE + 5, 0);
                directionStopPos = ChartUtils.CalcArcEndPointXY(cirX, cirY, radius + arcOffset, ARC_START_ANGLE, 0);
                break;
        }
        circleStartPos = ChartUtils.CalcArcEndPointXY(cirX, cirY, radius, startCirAngle, 0);
        circleStopPos = ChartUtils.CalcArcEndPointXY(cirX, cirY, radius, endCirAngle, 0);
        circleCurrentPos = ChartUtils.CalcArcEndPointXY(cirX, cirY, radius, currentCirAngle, 0);
        arcStartPos = ChartUtils.CalcArcEndPointXY(cirX, cirY, radius + arcOffset, startCirAngle, 0);
        arcStopPos = ChartUtils.CalcArcEndPointXY(cirX, cirY, radius + arcOffset, endCirAngle, 0);
        arcCurrentPos = ChartUtils.CalcArcEndPointXY(cirX, cirY, radius + arcOffset, currentCirAngle, 0);
        arcGapStartPos = ChartUtils.CalcArcEndPointXY(cirX, cirY, radius + arcOffset, startCirAngle, 20);
        arcGapStopPos = ChartUtils.CalcArcEndPointXY(cirX, cirY, radius + arcOffset, endCirAngle, 20);
        arcGapCurrentPos = ChartUtils.CalcArcEndPointXY(cirX, cirY, radius + arcOffset, currentCirAngle, 20);
    }


    private void initPaint() {
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(ViewUtil.sp2px(context, 10));
        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setStrokeWidth(4);
        linePaint.setColor(pointerColor);
        linePaint.setAntiAlias(true);
    }


    private Paint getLinePaint(int color, Paint.Style style) {
        Paint linePaint = new Paint();
        linePaint.setStyle(style);
        linePaint.setStrokeWidth(4);
        linePaint.setColor(color);
        linePaint.setAntiAlias(true);
        return linePaint;
    }

    public void setCirAngleInfo(float startCirAngle, float endCirAngle, float currentCirAngle, int direction) {
        if (startCirAngle < 0 || startCirAngle > 360 || endCirAngle < 0 || endCirAngle > 360 || currentCirAngle < 0 || currentCirAngle > 360) {
            return;
        } else {
            this.startCirAngle = ChartUtils.CalcArcAngle(startCirAngle);
            this.endCirAngle = ChartUtils.CalcArcAngle(endCirAngle);
            this.currentCirAngle = ChartUtils.CalcArcAngle(currentCirAngle);
            if(startCirAngle >= endCirAngle){
                this.start2StopGap = 360 - (startCirAngle - endCirAngle);
            }else{
                this.start2StopGap = endCirAngle - startCirAngle;
            }
        }
        this.direction = direction;
        postInvalidate();
    }
}
