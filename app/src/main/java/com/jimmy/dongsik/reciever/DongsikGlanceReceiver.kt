package com.jimmy.dongsik.reciever

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.jimmy.dongsik.model.Name
import com.jimmy.dongsik.model.Restaurant
import com.jimmy.dongsik.ui.widget.DongsikGlanceWidget
import com.jimmy.dongsik.ui.widget.MenuRefreshCallback
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DongsikGlanceReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = DongsikGlanceWidget()

    private val coroutineScope = MainScope()

    val db = Firebase.firestore
    val menuCollection = db.collection("menu")

    private val TAG = "DongsikGlanceReceiver"

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        fetchData(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == MenuRefreshCallback.UPDATE_ACTION) {
            fetchData(context)
        }
    }

    fun fetchData(context: Context) {
        val today = LocalDate.now(ZoneId.of("JST"))
        val dateTimeFormatterForAppbar = DateTimeFormatter.ofPattern("MM월 dd일 E")
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyMMdd")
        val formattedToday = today.format(dateTimeFormatter)
        menuCollection.document(formattedToday).get().addOnSuccessListener { document ->
            if (document != null) {
                Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                val arr = ArrayList<Restaurant>()
                document.data?.forEach { key, data ->
                    arr.add(
                        Restaurant(
                            name = Name.valueOf(key),
                            breakfast = (data as Map<String, List<String>>).get("breakfast"),
                            lunch = (data as Map<String, List<String>>).get("lunch"),
                            dinner = (data as Map<String, List<String>>).get("dinner")
                        )
                    )
                }
                val gson = Gson()
                coroutineScope.launch {
                    val glanceId =
                        GlanceAppWidgetManager(context).getGlanceIds(DongsikGlanceWidget::class.java)
                            .firstOrNull()

                    glanceId?.let {
                        updateAppWidgetState(
                            context,
                            PreferencesGlanceStateDefinition,
                            glanceId
                        ) { pref ->
                            pref.toMutablePreferences().apply {
                                for (rest in arr) {
                                    this[stringPreferencesKey(rest.name.toString())] =
                                        gson.toJson(rest)
                                }
                                this[DATE] = today.format(dateTimeFormatterForAppbar)
                            }
                        }
                        glanceAppWidget.update(context,it)
                    }
                }

            } else {
                Log.d(TAG, "No such document")
            }
        }
    }


    companion object {
        val DATE = stringPreferencesKey("date")
    }

}