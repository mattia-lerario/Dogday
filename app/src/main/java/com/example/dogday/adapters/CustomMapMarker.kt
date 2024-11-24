package com.example.dogday.adapters

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import coil.ImageLoader
import coil.request.ImageRequest
import com.example.dogday.R
import com.example.dogday.models.HikeData
import com.example.dogday.models.Kennel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomMapMarker(
    private val inflater: LayoutInflater,
    private val map: GoogleMap
) : GoogleMap.InfoWindowAdapter {

    override fun getInfoWindow(marker: Marker): View? {
        // Use default frame for the info window
        return null
    }

    override fun getInfoContents(marker: Marker): View {
        // Inflate the custom info window layout
        val view = inflater.inflate(R.layout.custom_info_window, null)

        // Reference views in the layout
        val titleTextView = view.findViewById<TextView>(R.id.info_window_title)
        val snippetTextView = view.findViewById<TextView>(R.id.info_window_snippet)
        val imageView = view.findViewById<ImageView>(R.id.info_window_image)

        // Handle different types of data objects (Kennel or HikeData)
        when (val data = marker.tag) {
            is Kennel -> {
                // Populate view with Kennel data
                titleTextView.text = data.name
                snippetTextView.text = "${data.address}\nContact: ${data.contactInfo}"

                // Load image for Kennel using Coil
                val imageLoader = ImageLoader(inflater.context)
                val request = ImageRequest.Builder(inflater.context)
                    .data(data.photoReference)
                    .size(50, 50)
                    .target(
                        onStart = {
                            imageView.setImageResource(android.R.drawable.ic_menu_gallery)
                        },
                        onSuccess = { result ->
                            imageView.setImageDrawable(result)
                            // Force marker to refresh
                            marker.hideInfoWindow()
                            marker.showInfoWindow()
                        },
                        onError = {
                            imageView.setImageResource(android.R.drawable.ic_dialog_alert)
                        }
                    )
                    .build()
                imageLoader.enqueue(request)
            }
            is HikeData -> {
                // Populate view with HikeData data
                titleTextView.text = data.title
                snippetTextView.text = data.description

                // Set default hike image or hide image view
                imageView.setImageResource(android.R.drawable.ic_menu_compass)
            }
            else -> {
                // Handle case where neither Kennel nor HikeData is available
                titleTextView.text = marker.title ?: ""
                snippetTextView.text = marker.snippet ?: ""
                imageView.setImageResource(android.R.drawable.ic_menu_directions)
            }
        }

        return view
    }
}
