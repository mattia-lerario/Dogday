package com.example.dogday.adapters

import coil.ImageLoader
import coil.request.ImageRequest
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.dogday.R
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

        // Get the Kennel object from the marker's tag
        val kennel = marker.tag as? Kennel

        if (kennel != null) {
            titleTextView.text = kennel.name
            snippetTextView.text = "${kennel.address}\nContact: ${kennel.contactInfo}"

            // Load the image using Coil
            val imageLoader = ImageLoader(inflater.context)
            val request = ImageRequest.Builder(inflater.context)
                .data(kennel.imageUrl)
                .target(
                    onStart = {
                        imageView.setImageResource(android.R.drawable.ic_menu_gallery)
                    },
                    onSuccess = { result ->
                        imageView.setImageDrawable(result)
                        // Refresh the info window
                        marker.showInfoWindow()
                    },
                    onError = {
                        imageView.setImageResource(android.R.drawable.ic_menu_report_image)
                    }
                )
                .build()
            imageLoader.enqueue(request)
        } else {
            // Handle case where kennel data is not available
            titleTextView.text = marker.title ?: ""
            snippetTextView.text = marker.snippet ?: ""
            imageView.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        return view
    }
}

