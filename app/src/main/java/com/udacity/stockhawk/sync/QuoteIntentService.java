package com.udacity.stockhawk.sync;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;

import com.udacity.stockhawk.widgets.WidgetProvider;

import timber.log.Timber;


public class QuoteIntentService extends IntentService {

    public QuoteIntentService() {
        super(QuoteIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Timber.d("Intent handled");
        QuoteSyncJob.getQuotes(getApplicationContext());
        //http://stackoverflow.com/questions/3455123/programmatically-update-widget-from-activity-service-receiver
        Intent wIntent = new Intent(this, WidgetProvider.class);
        wIntent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName (getApplication(), WidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        sendBroadcast(wIntent);
    }
}
