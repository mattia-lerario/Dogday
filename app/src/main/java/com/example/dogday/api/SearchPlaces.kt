package com.example.dogday.api

import com.example.dogday.models.Breeder
import com.example.dogday.models.Kennel
import com.google.firebase.firestore.GeoPoint
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

object SearchPlaces {

    private const val API_KEY = "AIzaSyC6Krt10uCwyajM12ZMC9e8yUIdnTo6whY"

    fun searchKennelsByKeyword(onSuccess: (List<Kennel>) -> Unit, onFailure: (Exception) -> Unit) {
        val url = "https://maps.googleapis.com/maps/api/place/textsearch/json?" +
                "query=dog+kennel+in+Norway&key=$API_KEY"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                onFailure(e)
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                response.body?.let { responseBody ->
                    val json = JSONObject(responseBody.string())
                    val results = json.getJSONArray("results")
                    val kennels = mutableListOf<Kennel>()

                    for (i in 0 until results.length()) {
                        val place = results.getJSONObject(i)
                        val id = place.optString("place_id")
                        val name = place.optString("name")
                        val address = place.optString("formatted_address")
                        val lat = place.getJSONObject("geometry").getJSONObject("location").getDouble("lat")
                        val lng = place.getJSONObject("geometry").getJSONObject("location").getDouble("lng")

                        val kennel = Kennel(
                            id = id,
                            name = name,
                            address = address,
                            coordinates = GeoPoint(lat, lng),
                            contactInfo = ""  // Add additional details if needed
                        )
                        kennels.add(kennel)
                    }
                    onSuccess(kennels)
                } ?: onFailure(Exception("Empty response body"))
            }
        })
    }

    fun searchBreedersByKeyword(onSuccess: (List<Breeder>) -> Unit, onFailure: (Exception) -> Unit) {
        val url = "https://maps.googleapis.com/maps/api/place/textsearch/json?" +
                "query=dog+breeder+in+Norway&key=$API_KEY"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                onFailure(e)
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                response.body?.let { responseBody ->
                    val json = JSONObject(responseBody.string())
                    val results = json.getJSONArray("results")
                    val breeders = mutableListOf<Breeder>()

                    for (i in 0 until results.length()) {
                        val place = results.getJSONObject(i)
                        val id = place.optString("place_id")
                        val name = place.optString("name")
                        val address = place.optString("formatted_address")
                        val lat = place.getJSONObject("geometry").getJSONObject("location").getDouble("lat")
                        val lng = place.getJSONObject("geometry").getJSONObject("location").getDouble("lng")

                        val breeder = Breeder(
                            id = id,
                            name = name,
                            address = address,
                            coordinates = GeoPoint(lat, lng),
                            dogBreeds = arrayListOf()  // Add additional details if needed
                        )
                        breeders.add(breeder)
                    }
                    onSuccess(breeders)
                } ?: onFailure(Exception("Empty response body"))
            }
        })
    }
}