package com.jimmy.dongsik.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TotalMenu(
    val sangrokwon3F : Restaurant? = null,
    val sangrokwon2F : Restaurant? = null,
    val sangrokwon1F : Restaurant? = null,
    val gurutoki : Restaurant? = null,
    val panAndNoodle : Restaurant? = null,
    val gardenCook : Restaurant? = null,
    val dormitoryRestaurant : Restaurant? = null
):Parcelable
