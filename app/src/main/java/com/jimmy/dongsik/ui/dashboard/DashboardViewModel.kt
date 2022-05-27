package com.jimmy.dongsik.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jimmy.dongsik.data.MenuRepository
import com.jimmy.dongsik.model.Restaurant
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private const val TAG = "DashboardViewModel"

class DashboardViewModel : ViewModel() {

    val menuRepository = MenuRepository

    private val _menu = MutableLiveData<List<Restaurant>?>()

    val menu: LiveData<List<Restaurant>?> = _menu

    private val _date = MutableLiveData<String?>()

    val date:LiveData<String?> = _date

    fun fetchData(){
        val today = LocalDate.now(ZoneId.of("JST")).plusDays(1)
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyMMdd")
        val dateTimeFormatterForAppbar = DateTimeFormatter.ofPattern("MM월 dd일 E")
        val formattedToday = today.format(dateTimeFormatter)
        viewModelScope.launch {
            val arr = menuRepository.getMenu(formattedToday)
            _menu.value = arr.sortedBy { it.name!!.ko }
            _date.value = today.format(dateTimeFormatterForAppbar)
        }
        Log.d(TAG, "fetchData: ${_menu.value}")
    }

    init {
        fetchData()
    }

}