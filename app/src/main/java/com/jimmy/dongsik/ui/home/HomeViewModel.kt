package com.jimmy.dongsik.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.jimmy.dongsik.model.Name
import com.jimmy.dongsik.model.Restaurant
import com.jimmy.dongsik.model.TotalMenu
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

private const val TAG = "HomeViewModel"

class HomeViewModel : ViewModel() {

    val db = Firebase.firestore
    val menuCollection = db.collection("menu")

    private val _menu = MutableLiveData<List<Restaurant>?>()

    val menu:LiveData<List<Restaurant>?> = _menu

    private val _date = MutableLiveData<String?>()

    val date:LiveData<String?> = _date

    fun fetchData(){
        val today = LocalDate.now(ZoneId.of("JST"))
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyMMdd")
        val dateTimeFormatterForAppbar = DateTimeFormatter.ofPattern("MM월 dd일 E")
        val formattedToday = today.format(dateTimeFormatter)
        menuCollection.document(formattedToday).get().addOnSuccessListener { document ->
            if (document != null) {
                Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                val arr = ArrayList<Restaurant>()
                for (cafeteria in document.data!!){
                    arr.add(
                        Restaurant(name = Name.valueOf(cafeteria.key),
                        breakfast = (cafeteria.value as Map<String,List<String>>).get("breakfast"),
                        lunch = (cafeteria.value as Map<String,List<String>>).get("lunch"),
                        dinner = (cafeteria.value as Map<String,List<String>>).get("dinner")
                    )
                    )
                }
                _menu.value = arr.sortedBy { it.name!!.ko }
                _date.value = today.format(dateTimeFormatterForAppbar)
            } else {
                Log.d(TAG, "No such document")
                _date.value = "정보를 가져오지 못했습니다."
            }
        }.addOnFailureListener { exception ->
            Log.d(TAG, "get failed with ", exception)
            _date.value = "정보를 가져오지 못했습니다."
        }

    }
    init {
        fetchData()
    }
}