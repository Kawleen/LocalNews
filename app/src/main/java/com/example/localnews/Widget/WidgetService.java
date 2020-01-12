package com.example.localnews.Widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.SystemClock;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.localnews.R;
import com.example.localnews.SqlLite.DatabaseHelper;

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetItemFactory(getApplicationContext(), intent);
    }

    class WidgetItemFactory implements RemoteViewsFactory{
        private Context context;
        private int appWidgetId;
        private String[] fav = new String[1000];

        private DatabaseHelper mysqlDb;
        private int count;

        WidgetItemFactory(Context context, Intent intent) {
            this.context = context;
            this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            mysqlDb = new DatabaseHelper(context);
        }

        @Override
        public void onCreate() {
            getWidgetData();
            SystemClock.sleep(3000);
        }

        public void getWidgetData(){
            Cursor result = mysqlDb.getAllData();
            count = result.getCount();
            int counter =1;
            if(count == 0){
                // Empty Activity
            }
            while (result.moveToNext()) {
                final String link = result.getString(1);
                final String pubDate = result.getString(2);
                final String content = result.getString(3);
                final String title = result.getString(4);
                final String checkBox = result.getString(5);

                fav[count-counter] = title;
                counter +=1 ;
            }
        }


        @Override
        public void onDataSetChanged() {
            getWidgetData();
            SystemClock.sleep(3000);
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            return count;
        }


        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.wideget_item);
            views.setTextViewText(R.id.example_widget_item_text, fav[position]);
            SystemClock.sleep(500);
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }


        @Override
        public int getViewTypeCount() {
            return 1;
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
