package com.jimmy.dongsik.ui.home

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.jimmy.dongsik.MainActivity
import com.jimmy.dongsik.databinding.FragmentHomeBinding
import com.jimmy.dongsik.model.Restaurant
import com.jimmy.dongsik.ui.dashboard.DashboardViewModel
import com.jimmy.dongsik.ui.widget.ErrorScreen
import com.jimmy.dongsik.ui.widget.RestaurantList

class HomeFragment : Fragment() {

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

}

@Composable
private fun MyScreen(
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val menuList by viewModel.menu.observeAsState()
    val date by viewModel.date.observeAsState()
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = date ?: "오늘의 학식")
        })
    }) {
        if (menuList?.isEmpty() == true) {
            ErrorScreen()
        } else {
            menuList?.let {
                RestaurantList(restaurants = it)
            }
        }
    }
}
