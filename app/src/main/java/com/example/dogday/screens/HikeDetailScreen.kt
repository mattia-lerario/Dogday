package com.example.dogday.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.dogday.viewmodel.HikeDetailViewModel

@Composable
fun HikeDetailScreen(hikeId: String?) {
    val viewModel: HikeDetailViewModel = viewModel()

    LaunchedEffect(hikeId) {
        if (hikeId != null) {
            viewModel.fetchHikeById(hikeId)
        }
    }

    val hike by viewModel.hike.collectAsStateWithLifecycle()

    if (hike != null) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = hike?.imageUrl,
                contentDescription = hike?.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = hike?.title ?: "",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            DetailItem(label = "Address", value = hike?.address)
            DetailItem(label = "Description", value = hike?.description)

            Spacer(modifier = Modifier.height(16.dp))
        }
    } else {
        Text(
            text = "Hike not found",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )
    }
}
