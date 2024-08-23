package com.example.myapptest01

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapptest01.ui.theme.MyAppTest01Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppTest01Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CounterScreen()
                }
            }
        }
    }
}

@Composable
fun CounterScreen(modifier: Modifier = Modifier) {
    var count by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()  // Fill the available space
            .height(16.dp),
        verticalArrangement = Arrangement.Center,  // Center the content vertically
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally  // Center the content horizontally
    ) {
        Text(text = "$count", fontSize = 150.sp)  // The counter

        Spacer(modifier = Modifier.height(32.dp))  // Add some space between the counter and the buttons

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = { count = if (count == 0) 0 else count - 1 }) {
                Text("Decrement")
            }
            Button(onClick = { count += 1 }) {
                Text("Increment")
            }
        }
    }
}
@Preview
@Composable
fun CounterScreenPreview() {
    CounterScreen()
}
