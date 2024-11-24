package com.example.dogday.ui.widgets

import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
private val API_KEY = "AIzaSyC6Krt10uCwyajM12ZMC9e8yUIdnTo6whY"
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
                // Navigate to Kennel detail page, no null check needed since id is non-null
                navController.navigate("kennel_detail/${kennel.id}")
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3E0) // Light orange color for the background
        )
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Image on the left
            AsyncImage(
                model = kennel.photoReference?.let {
                    "https://maps.googleapis.com/maps/api/place/photo?maxwidth=200&photoreference=$it&key=$API_KEY"
                },
                contentDescription = kennel.name,
                modifier = Modifier
                    .size(100.dp) // Adjusted size for consistency
                    .clip(RoundedCornerShape(
                        topStart = 12.dp,
                        bottomStart = 12.dp
                    ))
                    .fillMaxHeight(), // Match image height to the card height
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.placeholder_kennel),
                error = painterResource(id = R.drawable.placeholder_kennel)
            )
            // Text content on the right
            Column(
                modifier = Modifier
                    .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = kennel.name ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = kennel.address ?: "",
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
                kennel.rating?.let { rating ->
                    RatingStars(rating)
                }
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
            .height(150.dp)
            .clickable {
                breeder.id?.let { id ->
                    navController.navigate("breeder_detail/$id")
                } ?: run {
                    Log.e("BreederSliderItem", "Breeder ID is null")
                }
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3E0)
        )
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = breeder.photoReference?.let {
                    "https://maps.googleapis.com/maps/api/place/photo?maxwidth=200&photoreference=$it&key=$API_KEY"
                },
                contentDescription = breeder.name ?: "No name available",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.placeholder_kennel),
                error = painterResource(id = R.drawable.placeholder_kennel)
            )

            Column(
                modifier = Modifier
                    .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = breeder.name ?: "Unknown Breeder",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = breeder.address ?: "Address not available",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Breeds: ${breeder.dogBreeds?.joinToString(", ") ?: "No breeds available"}",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                breeder.rating?.let { rating ->
                    RatingStars(rating)
                }
            }
        }
    }
}

@Composable
fun RatingStars(rating: Double) {
    val filledStars = rating.toInt()
    val halfStar = rating - filledStars >= 0.5
    val maxStars = 5

    Row(modifier = Modifier.padding(top = 4.dp)) {
        repeat(filledStars) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Star",
                tint = Color(0xFFFFD700), // Gold color for filled stars
                modifier = Modifier.size(16.dp)
            )
        }
        if (halfStar) {
            Icon(
                imageVector = Icons.Filled.StarHalf,
                contentDescription = "Half Star",
                tint = Color(0xFFFFD700),
                modifier = Modifier.size(16.dp)
            )
        }
        repeat(maxStars - filledStars - if (halfStar) 1 else 0) {
            Icon(
                imageVector = Icons.Filled.StarBorder,
                contentDescription = "Empty Star",
                tint = Color(0xFFFFD700),
                modifier = Modifier.size(16.dp)
            )
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





