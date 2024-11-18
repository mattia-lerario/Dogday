package com.example.dogday.ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.example.dogday.R
import com.example.dogday.models.Breeder
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
                is Breeder -> BreederSliderItem(breeder = item, navController = navController) // Add Breeder support
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
            .height(100.dp)
            .clickable {
                // Navigate to Kennel detail page
                navController.navigate("kennel_detail/${kennel.id}")
            },
        shape = AbsoluteRoundedCornerShape(
            topLeft = 12.dp,
            bottomLeft = 12.dp,
            topRight = 12.dp,
            bottomRight = 12.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3E0) // Light orange color for the background
        )
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
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
                    .size(100.dp) // Adjusted size for consistency
                    .aspectRatio(1f)
                    .clip(AbsoluteRoundedCornerShape(
                        topLeft = 12.dp,
                        bottomLeft = 12.dp,
                        topRight = 0.dp,
                        bottomRight = 0.dp
                    ))
                    .fillMaxHeight(), // Match image height to the card height
                contentScale = ContentScale.Crop
            )
            // Text content on the right
            Column(
                modifier = Modifier
                    .padding(start = 8.dp, top = 8.dp, bottom = 8.dp) // Add padding for better spacing
                    .weight(1f)
            ) {
                Text(
                    text = kennel.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = kennel.address,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Category: Kennel",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
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
            .height(100.dp)
            .clickable {
                // Navigate to Hike detail page
                navController.navigate("hike_detail/${hike.id}")
            },
        shape = AbsoluteRoundedCornerShape(
            topLeft = 12.dp,
            bottomLeft = 12.dp,
            topRight = 12.dp,
            bottomRight = 12.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3E0) // Light orange color for the background
        )
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Image on the left
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(hike.imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .crossfade(true)
                    .scale(Scale.FIT)
                    .build(),
                contentDescription = hike.title,
                modifier = Modifier
                    .size(100.dp) // Adjusted size for consistency
                    .aspectRatio(1f)
                    .clip(AbsoluteRoundedCornerShape(
                        topLeft = 12.dp,
                        bottomLeft = 12.dp,
                        topRight = 0.dp,
                        bottomRight = 0.dp
                    ))
                    .fillMaxHeight(), // Match image height to the card height
                contentScale = ContentScale.Crop
            )
            // Text content on the right
            Column(
                modifier = Modifier
                    .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = hike.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = hike.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Category: Hike",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun BreederSliderItem(breeder: Breeder, navController: NavHostController) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(250.dp)
            .height(100.dp) // Set a fixed height for all cards
            .clickable {
                // Navigate to Breeder detail page
                navController.navigate("breeder_detail/${breeder.id}")
            },
        shape = AbsoluteRoundedCornerShape(
            topLeft = 12.dp,
            bottomLeft = 12.dp,
            topRight = 12.dp,
            bottomRight = 12.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3E0) // Light orange color for the background
        )
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Image on the left
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(breeder.imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .crossfade(true)
                    .scale(Scale.FIT)
                    .build(),
                contentDescription = breeder.name,
                modifier = Modifier
                    .size(100.dp) // Adjusted size for consistency
                    .aspectRatio(1f)
                    .clip(AbsoluteRoundedCornerShape(
                        topLeft = 12.dp,
                        bottomLeft = 12.dp,
                        topRight = 0.dp,
                        bottomRight = 0.dp
                    ))
                    .fillMaxHeight(), // Match image height to the card height
                contentScale = ContentScale.Crop
            )
            // Text content on the right
            Column(
                modifier = Modifier
                    .padding(start = 8.dp, top = 8.dp, bottom = 8.dp) // Add padding for better spacing
                    .weight(1f)
            ) {
                Text(
                    text = breeder.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = breeder.address,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Breeds: ${breeder.dogBreeds.joinToString(", ")}",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Category: Breeder",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
