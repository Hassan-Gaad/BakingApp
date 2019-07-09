package com.example.baking;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.baking.models.Cake;
import com.example.baking.models.Ingredients;

import java.util.ArrayList;
import java.util.List;


public class CackIngredientsWidget extends AppWidgetProvider {

    private static final String SELECTED_CAKE = "WidgetSelectedCake";
    public static List<Ingredients> ingredients = new ArrayList<>();
    private static String cakeName;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.cack_ingredients_widget);
        views.setTextViewText(R.id.tv_cake_title_widget, cakeName);

        Intent intent = new Intent(context, AppWidgetService.class);
        views.setRemoteAdapter(R.id.lv_widget, intent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.hasExtra(SELECTED_CAKE)) {
            Cake selectedCake = intent.getParcelableExtra(SELECTED_CAKE);
            cakeName = selectedCake.getName();
            ingredients = selectedCake.getIngredients();
            Log.d("CackIngredientsWidget",ingredients.get(0).getQuantity());

            Toast.makeText(context, cakeName + " " + "ingredients added to the widgets", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("CackIngredientsWidget","No cake selected");
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
        ComponentName thisWidget = new ComponentName(context.getApplicationContext(),AppWidgetProvider.class);

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_widget);

        if (appWidgetIds != null && appWidgetIds.length > 0) {
            onUpdate(context, appWidgetManager, appWidgetIds);
        }

        super.onReceive(context, intent);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}

