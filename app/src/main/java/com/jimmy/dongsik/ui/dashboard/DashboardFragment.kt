package com.jimmy.dongsik.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.jimmy.dongsik.MainActivity
import com.jimmy.dongsik.model.Name
import com.jimmy.dongsik.model.Restaurant

class DashboardFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MyScreen()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}

@Composable
private fun MyScreen(
    viewModel: DashboardViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val menuList by viewModel.menu.observeAsState()
    val date by viewModel.date.observeAsState()
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = date ?: "내일의 학식")
        })
    }) {
        menuList?.let {
            RestaurantList(restaurants = it)
        }
    }
}

@Composable
fun RestaurantList(restaurants: List<Restaurant>) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.background(Color.Gray)
    )
    {
        itemsIndexed(restaurants) { index, item ->
            RestaurantCard(restaurant = item)
        }
    }
}

@Composable
fun RestaurantCard(restaurant: Restaurant) {
    Box(modifier = Modifier.background(Color.White, RoundedCornerShape(5.dp))) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Text(textAlign = TextAlign.Start, text = restaurant.name?.ko ?: "", fontSize = 24.sp)
            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)

            ) {

                restaurant.breakfast?.let {
                    Text(text = "조식", fontSize = 19.sp)
                    Text(
                        text = it.joinToString("\n")?.trim(),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
                restaurant.lunch?.let {
                    Text(text = "중식", fontSize = 19.sp)
                    Text(
                        text = it.joinToString("\n")?.trim(),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
                restaurant.dinner?.let {
                    Text(text = "석식", fontSize = 19.sp)
                    Text(
                        text = it.joinToString("\n")?.trim(),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}