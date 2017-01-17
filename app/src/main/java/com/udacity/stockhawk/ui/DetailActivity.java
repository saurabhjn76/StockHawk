package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import butterknife.ButterKnife;
import timber.log.Timber;

import static android.R.attr.entries;
public class DetailActivity extends AppCompatActivity  {

    private LineChart mChart;
    String symbol;
    double maxYvalue=0;
    double minYvalue=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // To make full screen layout
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        symbol = getIntent().getStringExtra("SYMBOL");

        mChart = (LineChart) findViewById(R.id.linechart);
        mChart.setDrawGridBackground(false);
        mChart.setBackgroundColor(Color.BLUE);

        // add data
        setData();

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);
        Description description =new Description();
        description.setText(symbol);
        description.setTextSize(12f);
        // no description text
          //mChart.setDescription("Demo Line Chart");
        mChart.setDescription(description);
        mChart.setNoDataText("No Data available");

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        LimitLine upper_limit = new LimitLine(130f, "Upper Limit");
        upper_limit.setLineWidth(4f);
        //upper_limit.enableDashedLine(10f, 10f, 0f);
        upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upper_limit.setTextSize(10f);

        LimitLine lower_limit = new LimitLine(-30f, "Lower Limit");
        lower_limit.setLineWidth(4f);
        //lower_limit.enableDashedLine(10f, 10f, 0f);
        lower_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        lower_limit.setTextSize(10f);

        YAxis leftAxis = mChart.getAxisLeft();
        XAxis xAxis =mChart.getXAxis();
            xAxis.setDrawLabels(true);
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
       // leftAxis.addLimitLine(upper_limit);
        //leftAxis.addLimitLine(lower_limit);
        leftAxis.setAxisMaxValue((float) maxYvalue+100);
        leftAxis.setAxisMinValue((float) minYvalue-100);

        //leftAxis.setYOffset(20f);
       // leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(true);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        mChart.animateX(2500, Easing.EasingOption.EaseInOutQuart);

        //  dont forget to refresh the drawing
        mChart.invalidate();

    }

    private ArrayList<String> setXAxisValues() {
        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("10");
        xVals.add("20");
        xVals.add("30");
        xVals.add("30.5");
        xVals.add("40");

        return xVals;
    }

    private ArrayList<Entry> setYAxisValues() {
        ArrayList<Entry>   yVals = new ArrayList<Entry>();
        yVals.add(new Entry(40, 0));
        yVals.add(new Entry(48, 1));
        yVals.add(new Entry(70.5f, 2));
        yVals.add(new Entry(100, 3));
        yVals.add(new Entry(180.9f, 4));

        return yVals;
    }

    private void setData() {
        ArrayList<String> xVals = setXAxisValues();
        ArrayList<Entry> yVals= setSymbolData(symbol);



        LineDataSet set1,set2;
        Collections.sort(yVals, new EntryXComparator());

        // create a dataset and give it a type
        set1 = new LineDataSet(yVals, "Prices");
       // set2 = new LineDataSet(xVals,"Dates");

        set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        //   set1.enableDashedLine(10f, 5f, 0f);
        // set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1);// add the datasets
        ArrayList<ILineDataSet> dataSets1 = new ArrayList<ILineDataSet>();
        dataSets.add(set1);



        // create a data object with the datasets
        LineData data = new LineData(dataSets);

        // set data
        mChart.setData(data);

    }
    ArrayList<Entry> setSymbolData(String symbol){
        ArrayList<Entry>   entries = new ArrayList<Entry>();
        minYvalue=99999999;
        maxYvalue=-99999999;
        int counter=1;
        ArrayList<Date> dateArrayList = new ArrayList<>();
        Cursor data = getContentResolver().query(Contract.Quote.makeUriForStock(symbol), Contract.Quote.QUOTE_COLUMNS,null,null,null);
        if(data.moveToFirst()){
            do{
                Timber.e(data.getString(data.getColumnIndex(Contract.Quote.COLUMN_HISTORY)));
                String priceHistory = data.getString(data.getColumnIndex(Contract.Quote.COLUMN_HISTORY));
                for(String datapoint : priceHistory.split("\n")){
                    if(Float.valueOf(datapoint.split(",")[1].trim())<minYvalue) {
                        minYvalue=Float.valueOf(datapoint.split(",")[1].trim());
                    }
                    else if(Float.valueOf(datapoint.split(",")[1].trim())> maxYvalue){
                        maxYvalue=Float.valueOf(datapoint.split(",")[1].trim());
                    }
                    entries.add(new Entry(priceHistory.split("\n").length-counter++,Float.valueOf(datapoint.split(",")[1].trim())));
                    dateArrayList.add(new Date(Long.valueOf(datapoint.split(",")[0].trim())));
                }
            }while (data.moveToNext());
        }
        Timber.e(entries.toString());
        Timber.e(dateArrayList.toString());
        return entries;
    }


}



