package com.vip.wallet.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.vip.wallet.R;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.utils.ColorUtil;

import java.util.List;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2017/3/27 11:42   <br/><br/>
 * 描述:	      ${TODO}
 */
public class LineChartView extends LineChart {

    /**
     * X轴展示数据
     */
    private List<String> xFormatterData;
    private LineDataSet mDataSet;
    private Drawable mFillDrawable;
    //    private MyMarkerView mMarker;

    public LineChartView(Context context) {
        super(context);
    }

    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();
        initObj();
        initAttrs();
        initXAxis();
        initYAxis();
    }

    private void initObj() {
        mFillDrawable = getResources().getDrawable(R.drawable.shape_fade_blue);
    }

    /**
     * 初始化属性
     */
    protected void initAttrs() {
        getDescription().setEnabled(false);
        setTouchEnabled(true);
        setDragEnabled(true);
        setScaleEnabled(true);
        setDrawGridBackground(false);
        setHighlightPerDragEnabled(true);
        setPinchZoom(true);
        setBackgroundColor(Color.TRANSPARENT);
        //线条说明
        Legend l = getLegend();

        l.setForm(Legend.LegendForm.CIRCLE);

        l.setFormSize(1f);

        l.setTextColor(Color.WHITE);
        //水平排列
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //文字位置
        l.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        //位置
        l.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);
        //		mMarker = new MyMarkerView(getContext(), R.layout.hint_table_click, isIntValue);
        //		setMarker(mMarker);
    }

    /**
     * 初始化右边Axis
     */
    protected void initYAxis() {
        YAxis rightAxis = getAxisRight();
        //        rightAxis.setEnabled(false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisLineWidth(1.5f);
        rightAxis.setAxisLineColor(Color.WHITE);
        rightAxis.setTextColor(Color.WHITE);
        rightAxis.setTextSize(9f);


        YAxis axisLeft = getAxisLeft();
        //		axisLeft.setDrawLimitLinesBehindData(false);
        //		axisLeft.enableGridDashedLine(8f, 6f, 0f);
        axisLeft.setTextColor(Color.WHITE);
        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisLineWidth(1.5f);
        axisLeft.setAxisLineColor(Color.WHITE);
        axisLeft.setTextSize(9f);

     /*   axisLeft.setValueFormatter((value, axis) -> {
            if (value >= 1000) {
                String str = String.valueOf(value / 1000);
                String[] split = str.split("\\.");
                if (split.length == 2 && Integer.parseInt(split[1]) == 0) {
                    return split[0] + "k";
                }
                return str + "k";
            }
            return String.valueOf((int) value);
        });*/
    }

    /**
     * 初始化XAxis
     */
    protected void initXAxis() {
        XAxis xAxis = getXAxis();
        xAxis.setTypeface(Typeface.DEFAULT);
        xAxis.setTextSize(9f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGridColor(Color.WHITE);
        //        xAxis.enableGridDashedLine(8f, 6f, 0f);
        //        xAxis.setGridLineWidth(1f);
        xAxis.setAxisLineWidth(1.5f);
        //                xAxis.setDrawAxisLine(false);
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setValueFormatter((value, axis) -> {
            if (xFormatterData == null || xFormatterData.size() == 0) {
                return String.valueOf(value);
            }
            int position = Math.round(value);
            if (position < 0 || position >= xFormatterData.size()) {
                return String.valueOf(value);
            }
            return xFormatterData.get(position);
        });
    }

    private boolean isIntValue;

    public void setIsIntValue(boolean isIntValue) {
        this.isIntValue = isIntValue;
        //        mMarker.setIntValue(isIntValue);
    }

    /**
     * 设置X轴展示数据
     */
    public void setXFormatterData(List<String> data) {
        xFormatterData = data;
    }

    /**
     * 设置数据并刷新
     */
    public void setDataAndFresh(List<Entry> values, List<String> formatterData) {
        //        mMarker.setData(values);
        setXFormatterData(formatterData);
        LineDataSet dataSet = getLineDataSet();
        dataSet.setValues(values);
        // create a data object with the datasets
        LineData data = new LineData(dataSet);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(Typeface.DEFAULT_BOLD);
        data.setValueTextSize(12f);
        data.setDrawValues(false);
        // set data
        setData(data);
//        animateY(600, Easing.EasingOption.Linear);
//        animateX(600, Easing.EasingOption.Linear);
        //        invalidate();
    }


    /**
     * 设置数据并刷新
     */
    public void setDataAndFresh(List<ILineDataSet> dataSets, List<String> formatterData, String str) {
        setXFormatterData(formatterData);
        LineData data = new LineData(dataSets);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(Typeface.DEFAULT_BOLD);
        data.setValueTextSize(12f);
        data.setDrawValues(false);
        // set data
        setData(data);
//        animateY(600, Easing.EasingOption.Linear);
//        animateX(600, Easing.EasingOption.Linear);
        //        invalidate();
    }

    public void setDataAndFresh(List<Entry> values) {
        setDataAndFresh(values, null);
    }

    public LineDataSet getLineDataSet() {

        if (mDataSet != null) {
            return mDataSet;
        }

        mDataSet = new LineDataSet(null, "订单量");

        mDataSet.setColor(Color.BLUE);

        mDataSet.setCircleColor(Color.WHITE);

        mDataSet.setCircleColorHole(Color.BLUE);

        mDataSet.setLineWidth(2f);

        mDataSet.setCircleRadius(5f);

        mDataSet.setCircleHoleRadius(3.5f);

        mDataSet.setDrawCircleHole(true);

        mDataSet.setFillAlpha(65);
        //填充颜色
        mDataSet.setDrawFilled(true);
        //渐变
        mDataSet.setFillDrawable(mFillDrawable);
        //选中高亮线
        mDataSet.setHighLightColor(Color.BLUE);
        //变成虚线
        mDataSet.enableDashedHighlightLine(8f, 6f, 0f);

        mDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        //		set1.enableDashedLine(10f, 5f, 0f);
        mDataSet.setValueTextSize(9f);
        //		set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        mDataSet.setFormSize(8f);
        mDataSet.setFormLineWidth(1f);
        //			set1.setFillFormatter(new MyFillFormatter(0f));
        //			set1.setDrawHorizontalHighlightIndicator(false);
        //			set1.setVisible(false);
        //			set1.setCircleHoleColor(Color.WHITE);

        // create a dataset and give it a type
        return mDataSet;
    }

    public LineDataSet getCountLineDataSet() {

        LineDataSet linedataset = new LineDataSet(null, "数量");

        linedataset.setColor(Color.WHITE);

        linedataset.setCircleColor(Color.WHITE);

        linedataset.setCircleColorHole(Color.WHITE);

        linedataset.setLineWidth(1f);

        linedataset.setCircleRadius(0f);

        linedataset.setDrawCircles(false);

        linedataset.setCircleHoleRadius(0f);

        linedataset.setDrawCircleHole(false);

        linedataset.setFillAlpha(65);
        //填充颜色
        linedataset.setDrawFilled(true);
        //渐变
        linedataset.setFillDrawable(ContextCompat.getDrawable(ScApplication.getInstance(), R.drawable.shape_fade_blue));
        //选中高亮线
        linedataset.setHighLightColor(Color.WHITE);
        //变成虚线
        linedataset.enableDashedHighlightLine(8f, 6f, 0f);

        linedataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        //		set1.enableDashedLine(10f, 5f, 0f);
        linedataset.setValueTextSize(9f);
        //		set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        linedataset.setFormSize(8f);
        linedataset.setFormLineWidth(1f);
        //			set1.setFillFormatter(new MyFillFormatter(0f));
        //			set1.setDrawHorizontalHighlightIndicator(false);
        //			set1.setVisible(false);
        //			set1.setCircleHoleColor(Color.WHITE);

        // create a dataset and give it a type
        return linedataset;
    }

    public LineDataSet getPropertyLineDataSet() {

        LineDataSet linedataset = new LineDataSet(null, "资产");

        int violet = ColorUtil.getColor(R.color.violet);

        linedataset.setColor(violet);

        linedataset.setLineWidth(1f);

        linedataset.setDrawCircles(false);

        linedataset.setDrawCircleHole(false);

        linedataset.setFillAlpha(65);
        //填充颜色
        linedataset.setDrawFilled(true);
        //渐变
        linedataset.setFillDrawable(ContextCompat.getDrawable(ScApplication.getInstance(), R.drawable.shape_fade_violet));
        //选中高亮线
        linedataset.setHighLightColor(violet);
        //变成虚线
        linedataset.enableDashedHighlightLine(8f, 6f, 0f);

        linedataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        //		set1.enableDashedLine(10f, 5f, 0f);
        linedataset.setValueTextSize(9f);
        //		set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        linedataset.setFormSize(8f);
        linedataset.setFormLineWidth(1f);
        //			set1.setFillFormatter(new MyFillFormatter(0f));
        //			set1.setDrawHorizontalHighlightIndicator(false);
        //			set1.setVisible(false);
        //			set1.setCircleHoleColor(Color.WHITE);

        // create a dataset and give it a type
        return linedataset;
    }
}
