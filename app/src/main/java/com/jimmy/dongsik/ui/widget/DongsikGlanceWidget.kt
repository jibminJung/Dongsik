package com.jimmy.dongsik.ui.widget

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.*
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.layout.*
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.google.gson.Gson
import com.jimmy.dongsik.R
import com.jimmy.dongsik.model.Name
import com.jimmy.dongsik.model.Restaurant
import com.jimmy.dongsik.reciever.DongsikGlanceReceiver


class DongsikGlanceWidget : GlanceAppWidget() {
    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition
    private val TAG = "DongsikGlanceWidget"

    @Composable
    override fun Content() {
        val gson = Gson()
        val prefs = currentState<Preferences>()
        val restaurants = mutableListOf<Restaurant?>()
        val date = prefs[DongsikGlanceReceiver.DATE]
        Name.values().forEach {
            restaurants.add(
                gson.fromJson(
                    prefs[stringPreferencesKey(it.toString())],
                    Restaurant::class.java
                )
            )
        }
        MyWidget(restaurants = restaurants, date ?: "오늘의 학식")
    }


}

@Composable
fun MyWidget(restaurants: List<Restaurant?>, date: String) {
    Column() {
        Row() {
            Text(
                text = date,
                modifier = GlanceModifier.clickable(actionRunCallback(MenuRefreshCallback::class.java))
            )
            Image(
                ImageProvider(R.drawable.ic_baseline_refresh_24),
                contentDescription = "Refresh"
            )

        }
        LazyColumn(modifier = GlanceModifier.background(ImageProvider(R.drawable.rounded_widget_bg))) {
            items(restaurants) { item ->
                item?.let {
                    RestaurantCard(restaurant = item)
                } ?: kotlin.run {
                    Log.d("DongsikGlanceWidget", "MyWidget: null")
                }
            }
        }
    }

}

@Composable
fun RestaurantCard(restaurant: Restaurant) {
    Box() {
        Column(
            modifier = GlanceModifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Text(text = restaurant.name?.ko ?: "")
            Column(
                modifier = GlanceModifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            ) {

                restaurant.breakfast?.let {
                    val str = it.joinToString("\n").trim()
                    if (str.isNotBlank()) {
                        Text(text = "조식")
                        Text(
                            text = str,
                            modifier = GlanceModifier.fillMaxWidth(),
                            style = TextStyle(textAlign = TextAlign.Center)
                        )
                    }
                }
                restaurant.lunch?.let {
                    val str = it.joinToString("\n").trim()
                    if (str.isNotBlank()) {
                        Text(text = "중식", style = TextStyle(fontSize = 19.sp))
                        Text(
                            text = str,
                            modifier = GlanceModifier.fillMaxWidth(),
                            style = TextStyle(textAlign = TextAlign.Center)
                        )
                    }
                }
                restaurant.dinner?.let {
                    val str = it.joinToString("\n").trim()
                    if (str.isNotBlank()) {
                        Text(text = "석식", style = TextStyle(fontSize = 19.sp))
                        Text(
                            text = str,
                            modifier = GlanceModifier.fillMaxWidth(),
                            style = TextStyle(textAlign = TextAlign.Center)
                        )
                    }
                }
                Text(
                    text = "-",
                    modifier = GlanceModifier.fillMaxWidth(),
                    style = TextStyle(textAlign = TextAlign.Center)
                )
            }
        }
    }

}