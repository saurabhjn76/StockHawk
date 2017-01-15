package com.udacity.stockhawk.ui;

import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.util.ArrayList;

import butterknife.ButterKnife;
import timber.log.Timber;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    LineChart lineChart;
    String symbol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
       getSupportLoaderManager().initLoader(0,null,this);
        lineChart =(LineChart)findViewById(R.id.linechart);
        lineChart.setDrawGridBackground(false);
        lineChart.setViewPortOffsets(0,0,0,0);
        XAxis xAxis= lineChart.getXAxis();
        YAxis yAxis =lineChart.getAxisLeft();
        symbol = getIntent().getStringExtra("SYMBOL");
        Toast.makeText(getApplicationContext(),symbol,Toast.LENGTH_SHORT).show();

        // dataSet =new LineDataSet()
        ArrayList<Entry> entries =new ArrayList<>();

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        Timber.e(symbol,data.toString());

    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                Contract.Quote.makeUriForStock(symbol),
                Contract.Quote.QUOTE_COLUMNS,
                Contract.Quote.COLUMN_SYMBOL + "=?", new String[]{symbol}, Contract.Quote.COLUMN_SYMBOL);
    }
}
