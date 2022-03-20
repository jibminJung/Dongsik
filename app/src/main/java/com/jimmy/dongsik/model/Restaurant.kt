package com.jimmy.dongsik.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Restaurant(
    val name:Name? = null,
    val breakfast : List<String>? = null,
    val lunch : List<String>? = null,
    val dinner : List<String>? = null
):Parcelable,Serializable
