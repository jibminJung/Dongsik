package com.jimmy.dongsik.data

import android.annotation.SuppressLint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jimmy.dongsik.model.Name
import com.jimmy.dongsik.model.Restaurant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@SuppressLint("StaticFieldLeak")
object MenuRepository {

    private val db = Firebase.firestore
    private val menuCollection = db.collection("menu")

    suspend fun getMenu(date : String) : List<Restaurant> = withContext(Dispatchers.IO){
        return@withContext try{
            val snapshot = menuCollection.document(date).get().await()
            val arr = ArrayList<Restaurant>()
            snapshot.data?.let{
                it.entries.forEach { cafeteria ->
                    arr.add(Restaurant(name = Name.valueOf(cafeteria.key),
                        breakfast = (cafeteria.value as Map<String,List<String>>).get("breakfast"),
                        lunch = (cafeteria.value as Map<String,List<String>>).get("lunch"),
                        dinner = (cafeteria.value as Map<String,List<String>>).get("dinner")
                    ))
                }
            }
            arr
        }catch (e:Throwable){
            return@withContext ArrayList()
        }
    }
}