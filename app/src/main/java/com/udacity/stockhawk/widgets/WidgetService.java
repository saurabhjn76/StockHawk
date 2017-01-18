package com.udacity.stockhawk.widgets;

/**
 * Created by saurabh on 18/1/17.THanks to http://dharmangsoni.blogspot.in/2014/03/collection-widget-with-event-handling.html
 */

import android.annotation.SuppressLint;
import android.content.Intent;
import android.widget.RemoteViewsService;

@SuppressLint("NewApi")
public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        WidgetDataProvider dataProvider = new WidgetDataProvider(
                getApplicationContext(), intent);
        return dataProvider;
    }

}
