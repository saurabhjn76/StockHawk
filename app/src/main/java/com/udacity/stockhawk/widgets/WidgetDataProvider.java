package com.udacity.stockhawk.widgets;

/**
 * Created by saurabh on 18/1/17. THanks to http://dharmangsoni.blogspot.in/2014/03/collection-widget-with-event-handling.html
 */

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;
import android.widget.Toast;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.PrefUtils;

import timber.log.Timber;

import static com.udacity.stockhawk.R.id.error;

@SuppressLint("NewApi")
public class WidgetDataProvider implements RemoteViewsFactory,LoaderManager.LoaderCallbacks<Cursor> {

   ArrayList<String> symbols = new ArrayList<>();
    ArrayList<Float> prices = new ArrayList<>();
    ArrayList<Float> changes = new ArrayList<>();
    ArrayList<Float> percentageChange = new ArrayList<>();

    private Cursor cursor;
    private static final int STOCK_LOADER = 0;
    private final DecimalFormat dollarFormat= (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
    private final DecimalFormat dollarFormatWithPlus= (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);

    private final DecimalFormat percentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());


    Context mContext = null;

    public WidgetDataProvider(Context context, Intent intent) {
        mContext = context;

    }
    void setCursor(Cursor cursor) {
        this.cursor = cursor;

      ///  notifyDataSetChanged();
    }

   /* String getSymbolAtPosition(int position) {

        cursor.moveToPosition(position);
        return cursor.getString(Contract.Quote.POSITION_SYMBOL);
    }*/

    @Override
    public int getCount() {
        /*int count = 0;
        if (cursor != null) {
            count = cursor.getCount();
        }
        return count;*/
        Log.e ("SIze:",symbols.toString ());
      return   symbols.size ();

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }
    @Override
    public RemoteViews getViewAt(int position) {
        //cursor.moveToPosition(position);
        RemoteViews mView = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
        mView.setTextViewText(R.id.widget_symbol,symbols.get (position));
      //  Log.e("Remote",getSymbolAtPosition(position));
        mView.setTextViewText(R.id.widget_price,dollarFormat.format(prices.get (position)));

        float rawAbsoluteChange = changes.get (position);
        float
        percentagChange = percentageChange.get(position);

        if (rawAbsoluteChange > 0) {
            mView.setInt(R.id.widget_change,"setBackgroundResource",R.drawable.percent_change_pill_green);
        } else {
            mView.setInt(R.id.widget_change,"setBackgroundResource",R.drawable.percent_change_pill_red);
        }


        String change = dollarFormatWithPlus.format(rawAbsoluteChange);
        String percentage = percentageFormat.format(percentagChange / 100);
        if (PrefUtils.getDisplayMode(mContext)
                .equals(mContext.getString(R.string.pref_display_mode_absolute_key))) {
           mView.setTextViewText(R.id.widget_change,(change));
        } else {
            mView.setTextViewText(R.id.widget_change,(percentage));
        }
        mView.setTextColor(R.id.widget_symbol, Color.BLACK);

        final Intent fillInIntent = new Intent();
        fillInIntent.setAction(WidgetProvider.ACTION_TOAST);
        final Bundle bundle = new Bundle();
        bundle.putString(WidgetProvider.EXTRA_STRING, symbols.get (position));
        fillInIntent.putExtras(bundle);
        mView.setOnClickFillInIntent(R.id.symbol, fillInIntent);
        return mView;
    }


    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {

        init();
    }
    void init(){
        dollarFormatWithPlus.setPositivePrefix("+$");
        percentageFormat.setMaximumFractionDigits(2);
        percentageFormat.setMinimumFractionDigits(2);
        percentageFormat.setPositivePrefix("+");
        symbols.clear ();
        prices.clear ();
        changes.clear ();
        percentageChange.clear ();
        // getSupportLoaderManager.initLoader(STOCK_LOADER, null, this);   Help needed can't resolve the method
        mContext.grantUriPermission ("com.udacity.stockhawk",Contract.Quote.makeBaseUri (),Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cursor = mContext.getContentResolver().query(Contract.Quote.makeBaseUri (), Contract.Quote.QUOTE_COLUMNS, null, null, null);
        if(cursor!=null){
            while (cursor.moveToNext ()){
                symbols.add (cursor.getString(Contract.Quote.POSITION_SYMBOL));
                prices.add (cursor.getFloat(Contract.Quote.POSITION_PRICE));
                changes.add (cursor.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE));
                percentageChange.add (cursor.getFloat (Contract.Quote.POSITION_PERCENTAGE_CHANGE));
            }
            cursor.close ();
        }
        Log.e ("Cursor",symbols.toString ());
       // cursor.moveToFirst ();
        //Timber.e (cursor.getString (Contract.Quote.POSITION_SYMBOL));
        //setCursor (cursor);
    }

    @Override
    public void onDataSetChanged() {
      //  cursor = mContext.getContentResolver().query(Contract.Quote.makeBaseUri(), Contract.Quote.QUOTE_COLUMNS,null,null,null);
        //setCursor(cursor);
        init ();
    }



    @Override
    public void onDestroy() {

    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mContext,
                Contract.Quote.URI,
                Contract.Quote.QUOTE_COLUMNS,
                null, null, Contract.Quote.COLUMN_SYMBOL);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
       // swipeRefreshLayout.setRefreshing(false);

        if (data.getCount() != 0) {
          //  error.setVisibility(View.GONE);
           // if(!networkUp() && FIRST_LOAD){
            //    Toast.makeText(mContext, R.string.outdated,Toast.LENGTH_SHORT).show();
             //   FIRST_LOAD=false;
           // }
        }
        setCursor(data);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
       // swipeRefreshLayout.setRefreshing(false);
        setCursor(null);
    }


}

