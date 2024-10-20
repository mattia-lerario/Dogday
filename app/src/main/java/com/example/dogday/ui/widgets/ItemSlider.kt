package com.example.dogday.ui.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.dogday.models.HikeData
import com.example.dogday.models.Kennel

@Composable
fun ItemSlider(visibleItems: List<Any>, modifier: Modifier = Modifier) {
    LazyRow(modifier = modifier.fillMaxWidth()) {
        items(visibleItems) { item ->
            when (item) {
                is Kennel -> KennelSliderItem(kennel = item)
                is HikeData -> HikeSliderItem(hike = item)
            }
        }
    }
}

@Composable
fun KennelSliderItem(kennel: Kennel) {
    // Customize your item layout
    Text(text = kennel.name)
}

@Composable
fun HikeSliderItem(hike: HikeData) {
    // Customize your item layout
    Text(text = hike.name)
}
