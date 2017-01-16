package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.util.ArrayList;

import butterknife.ButterKnife;
import timber.log.Timber;

public class DetailActivity extends AppCompatActivity  {

    LineChart lineChart;
    String symbol;
    ArrayList<Entry> entries;
    ArrayList<String> xVals;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        lineChart =(LineChart)findViewById(R.id.linechart);
        lineChart.setDrawGridBackground(false);
        lineChart.setViewPortOffsets(0,0,0,0);
        XAxis xAxis= lineChart.getXAxis();
        YAxis yAxis =lineChart.getAxisLeft();
        symbol = getIntent().getStringExtra("SYMBOL");
        Toast.makeText(getApplicationContext(),symbol,Toast.LENGTH_SHORT).show();



        entries=new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("10");
        xVals.add("20");
        xVals.add("30");
        xVals.add("30.5");
        xVals.add("40");
        setData(symbol);
        Timber.e(entries.toString());
        LineDataSet  dataSet = new LineDataSet(entries,"Vlue");
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(dataSet); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(dataSets);

        // set data
        lineChart.setData(data);
        lineChart.animate();



    }

    void setData(String symbol){
        Cursor data = getContentResolver().query(Contract.Quote.makeUriForStock(symbol), Contract.Quote.QUOTE_COLUMNS,null,null,null);
        if(data.moveToFirst()){
            do{
                Timber.e(data.getString(data.getColumnIndex(Contract.Quote.COLUMN_HISTORY)));
                String priceHistory = data.getString(data.getColumnIndex(Contract.Quote.COLUMN_HISTORY));
                for(String datapoint : priceHistory.split("\n")){
                    entries.add(new Entry(Long.valueOf(datapoint.split(",")[0].trim()),Float.valueOf(datapoint.split(",")[1].trim())));
                }
            }while (data.moveToNext());
        }
    }



    }



