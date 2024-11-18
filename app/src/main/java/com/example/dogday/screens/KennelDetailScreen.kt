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
import com.example.dogday.viewmodel.KennelDetailViewModel

@Composable
fun KennelDetailScreen(kennelId: String?) {
    val viewModel: KennelDetailViewModel = viewModel()

    // Fetch the kennel details using the kennelId
    LaunchedEffect(kennelId) {
        if (kennelId != null) {
            viewModel.fetchKennelById(kennelId)
        }
    }

    val kennel by viewModel.kennel.collectAsStateWithLifecycle()

    if (kennel != null) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = kennel?.imageUrl,
                contentDescription = kennel?.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = kennel?.name ?: "",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            DetailItem(label = "Address", value = kennel?.address)
            DetailItem(label = "Contact", value = kennel?.contactInfo)
            DetailItem(label = "Owner Name", value = kennel?.ownerName)
            DetailItem(label = "Description", value = kennel?.description)

            Spacer(modifier = Modifier.height(16.dp))
        }
    } else {
        Text(
            text = "Kennel not found",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun DetailItem(label: String, value: String?) {
    if (!value.isNullOrEmpty()) {
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            Text(
                text = "$label:",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}
