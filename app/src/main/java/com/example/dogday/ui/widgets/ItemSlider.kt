package com.example.dogday.ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.example.dogday.R
import com.example.dogday.models.HikeData
import com.example.dogday.models.Kennel

@Composable
fun ItemSlider(
    visibleItems: List<Any>,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    LazyRow(modifier = modifier.fillMaxWidth()) {
        items(visibleItems) { item ->
            when (item) {
                is Kennel -> KennelSliderItem(kennel = item, navController = navController)
                is HikeData -> HikeSliderItem(hike = item, navController = navController)
            }
        }
    }
}

@Composable
fun KennelSliderItem(kennel: Kennel, navController: NavHostController) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(250.dp)
            .clickable {
                // Navigate to Kennel detail page
                navController.navigate("kennel_detail/${kennel.id}")
            }
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            // Image on the left
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(kennel.imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .crossfade(true)
                    .scale(Scale.FIT)
                    .build(),
                contentDescription = kennel.name,
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 8.dp)
            )
            // Text content on the right
            Column {
                Text(text = kennel.name, style = MaterialTheme.typography.titleMedium)
                Text(text = kennel.address, style = MaterialTheme.typography.bodySmall)
                Text(text = "Category: Kennel", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun HikeSliderItem(hike: HikeData, navController: NavHostController) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(250.dp)
            .clickable {
                // Navigate to Hike detail page
                navController.navigate("hike_detail/${hike.id}")
            }
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            // Image on the left
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(hike.imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .crossfade(true)
                    .scale(Scale.FILL)
                    .build(),
                contentDescription = hike.title,
                modifier = Modifier
                    .size(100.dp) // Increased size to cover more of the card, making it more prominent
                    .aspectRatio(1f) // Ensures the image stays square
                    .clip(RoundedCornerShape(8.dp)) // Adds a rounded clip to the image for aesthetics
                    .padding(end = 8.dp)
            )
            // Text content on the right
            Column {
                Text(text = hike.title, style = MaterialTheme.typography.titleMedium)
                Text(text = hike.description, style = MaterialTheme.typography.bodySmall, maxLines = 2)
                Text(text = "Category: Hike", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
