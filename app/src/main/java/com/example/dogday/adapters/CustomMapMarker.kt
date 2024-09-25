package com.example.dogday.adapters

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.dogday.R
import com.example.dogday.models.Kennel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomMapMarker(private val inflater: LayoutInflater) : GoogleMap.InfoWindowAdapter {

    override fun getInfoWindow(marker: Marker): View? {
        // We don't want to change the whole window, just the content
        return null
    }

    override fun getInfoContents(marker: Marker): View {
        // Inflate the custom info window layout
        val view = inflater.inflate(R.layout.custom_info_window, null)

        // Get the title, image, and snippet from the marker
        val title = view.findViewById<TextView>(R.id.info_window_title)
        val snippet = view.findViewById<TextView>(R.id.info_window_snippet)
        val imageView = view.findViewById<ImageView>(R.id.info_window_image)

        // Get the Kennel data (using marker's tag)
        val kennel = marker?.tag as? Kennel
        title.text = kennel?.name
        snippet.text = "${kennel?.address}, Contact: ${kennel?.contactInfo}"

        // Optionally load an image from a URL (you can use a library like Coil or Glide)
        imageView.setImageResource(R.drawable.placeholder_image) // Placeholder image

        return view
    }
}
