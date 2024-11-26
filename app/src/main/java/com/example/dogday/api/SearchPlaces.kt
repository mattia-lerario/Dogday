package com.example.dogday.api

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.dogday.models.Breeder
import com.example.dogday.models.Kennel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object SearchPlaces {

    private const val API_KEY = "AIzaSyC6Krt10uCwyajM12ZMC9e8yUIdnTo6whY"

    private fun uploadKennelsToFirebase(kennels: List<Kennel>, onComplete: () -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val batch = db.batch()
        for (kennel in kennels) {
            val docRef = db.collection("kennels").document(kennel.id)
            batch.set(docRef, kennel)
        }
        batch.commit().addOnCompleteListener { onComplete() }
    }

    private fun uploadBreedersToFirebase(breeders: List<Breeder>, onComplete: () -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val batch = db.batch()
        for (breeder in breeders) {
            val docRef = db.collection("breeders").document(breeder.id)
            batch.set(docRef, breeder)
        }
        batch.commit().addOnCompleteListener { onComplete() }
    }

    fun searchKennelsByKeyword(
        kennelsList: MutableList<Kennel> = mutableListOf(),
        pageToken: String? = null,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val urlBuilder = StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?")
        urlBuilder.append("query=dog+kennel+in+Norway")
        urlBuilder.append("&key=$API_KEY")
        if (pageToken != null) {
            urlBuilder.append("&pagetoken=$pageToken")
        }
        val url = urlBuilder.toString()

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
                    //We take the values we need and add them to our Kennel Objects
                    for (i in 0 until results.length()) {
                        val place = results.getJSONObject(i)
                        val id = place.optString("place_id")
                        val name = place.optString("name")
                        val address = place.optString("formatted_address")
                        val lat = place.getJSONObject("geometry").getJSONObject("location").getDouble("lat")
                        val lng = place.getJSONObject("geometry").getJSONObject("location").getDouble("lng")
                        val businessStatus = place.optString("business_status")?: ""
                        val openingHours = place.optJSONObject("opening_hours")?.optBoolean("open_now")
                        val rating = place.optDouble("rating", -1.0).takeIf { it >= 0 }
                        val userRatingsTotal = place.optInt("user_ratings_total", -1).takeIf { it >= 0 }
                        val photoReference = place.optJSONArray("photos")?.optJSONObject(0)?.optString("photo_reference")
                        val iconUrl = place.optString("icon")?: ""
                        val types = place.optJSONArray("types")?.let { jsonArray ->
                            List(jsonArray.length()) { index -> jsonArray.optString(index) }
                        }

                        val kennel = Kennel(
                            id = id,
                            name = name,
                            address = address,
                            coordinates = GeoPoint(lat, lng),
                            contactInfo = null,
                            businessStatus = businessStatus,
                            openingHours = openingHours,
                            rating = rating,
                            userRatingsTotal = userRatingsTotal,
                            photoReference = photoReference,
                            iconUrl = iconUrl,
                            types = types
                        )
                        kennelsList.add(kennel)
                    }

                    if (json.has("next_page_token")) {
                        val nextPageToken = json.getString("next_page_token")
                        val scheduler = Executors.newSingleThreadScheduledExecutor()
                        scheduler.schedule({
                            searchKennelsByKeyword(kennelsList, nextPageToken, onSuccess, onFailure)
                            scheduler.shutdown()
                        }, 2, TimeUnit.SECONDS)
                    } else {
                        uploadKennelsToFirebase(kennelsList) { onSuccess() }
                    }
                } ?: onFailure(Exception("Empty response body"))
            }
        })
    }

    //Function for fetching breeders with pagination token
    fun searchBreedersByKeyword(
        breedersList: MutableList<Breeder> = mutableListOf(),
        pageToken: String? = null,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val urlBuilder = StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?")
        urlBuilder.append("query=dog+breeder+in+Norway")
        urlBuilder.append("&key=$API_KEY")
        if (pageToken != null) {
            urlBuilder.append("&pagetoken=$pageToken")
        }
        val url = urlBuilder.toString()

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
                    //We take the values we need and add them to our Breeder Objects
                    for (i in 0 until results.length()) {
                        val place = results.getJSONObject(i)
                        val id = place.optString("place_id")
                        val name = place.optString("name")
                        val address = place.optString("formatted_address")
                        val lat = place.getJSONObject("geometry").getJSONObject("location").getDouble("lat")
                        val lng = place.getJSONObject("geometry").getJSONObject("location").getDouble("lng")
                        val businessStatus = place.optString("business_status")?: ""
                        val openingHours = place.optJSONObject("opening_hours")?.optBoolean("open_now")
                        val rating = place.optDouble("rating", -1.0).takeIf { it >= 0 }
                        val userRatingsTotal = place.optInt("user_ratings_total", -1).takeIf { it >= 0 }
                        val photoReference = place.optJSONArray("photos")?.optJSONObject(0)?.optString("photo_reference")
                        val iconUrl = place.optString("icon")?: ""
                        val types = place.optJSONArray("types")?.let { jsonArray ->
                            List(jsonArray.length()) { index -> jsonArray.optString(index) }
                        }

                        val breeder = Breeder(
                            id = id,
                            name = name,
                            address = address,
                            coordinates = GeoPoint(lat, lng),
                            contactInfo = null,
                            dogBreeds = arrayListOf(),
                            businessStatus = businessStatus,
                            openingHours = openingHours,
                            rating = rating,
                            userRatingsTotal = userRatingsTotal,
                            photoReference = photoReference,
                            iconUrl = iconUrl,
                            types = types
                        )
                        breedersList.add(breeder)
                    }

                    if (json.has("next_page_token")) {
                        val nextPageToken = json.getString("next_page_token")
                        val scheduler = Executors.newSingleThreadScheduledExecutor()
                        scheduler.schedule({
                            searchBreedersByKeyword(breedersList, nextPageToken, onSuccess, onFailure)
                            scheduler.shutdown()
                        }, 2, TimeUnit.SECONDS)
                    } else {
                        uploadBreedersToFirebase(breedersList) { onSuccess() }
                    }
                } ?: onFailure(Exception("Empty response body"))
            }
        })
    }
}
// Worker that searches for kennels and breeders using keywords, waits for both to complete, and then uploads data to Firebase.

class FetchAndUploadWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val latch = CountDownLatch(2)
        var kennelsSuccess = false
        var breedersSuccess = false

        SearchPlaces.searchKennelsByKeyword(
            onSuccess = {
                kennelsSuccess = true
                latch.countDown()
            },
            onFailure = {
                kennelsSuccess = false
                latch.countDown()
            }
        )

        SearchPlaces.searchBreedersByKeyword(
            onSuccess = {
                breedersSuccess = true
                latch.countDown()
            },
            onFailure = {
                breedersSuccess = false
                latch.countDown()
            }
        )

        try {
            latch.await(15, TimeUnit.MINUTES)
        } catch (e: InterruptedException) {
            return Result.failure()
        }

        return if (kennelsSuccess && breedersSuccess) {
            Result.success()
        } else {
            Result.retry()
        }
    }
}
// It retries the work if either search fails, and runs as a scheduled task every 24 Hours.
fun scheduleFetchAndUploadWork(context: Context) {
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val workRequest = PeriodicWorkRequestBuilder<FetchAndUploadWorker>(24, TimeUnit.HOURS)
        .setConstraints(constraints)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "FetchAndUploadWork",
        ExistingPeriodicWorkPolicy.KEEP,
        workRequest
    )
}