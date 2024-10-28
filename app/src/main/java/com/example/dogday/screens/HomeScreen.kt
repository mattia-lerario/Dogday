package com.example.dogday.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dogday.R
import com.example.dogday.models.Dog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun HomeScreen(navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {

        //DogsList(navController = navController)
        DogCard(navController = navController)
        HikeCard(navController = navController)
        CalendarHome()
        //DogListCard(navController = navController, dog = dogs)


    }
}

@Composable
fun DogListCard(navController: NavHostController, dog: Dog){
    Card(elevation = CardDefaults.cardElevation(
        defaultElevation = 6.dp),
        onClick = { navController.navigate("DogDetailScreen/${dog.dogId}")},
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)) {
        Row(modifier = Modifier.padding(10.dp)) {
            Column(modifier = Modifier
                .weight(1f)
                .padding(5.dp)) {
                Text(text = "${dog.name}", style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(0.dp))
                //Text(text = "${dog.breed}")
            }

            Image(
                painter = painterResource(id = R.drawable.dog_cartoon),
                contentDescription = "Dog",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .padding(0.dp)
            )


        }

    }


}

@Composable
fun HikeCard(navController: NavHostController){
    Card(elevation = CardDefaults.cardElevation(
        defaultElevation = 6.dp),
        onClick = { navController.navigate(DogScreen.Map.name)},
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)) {
        Row(modifier = Modifier.padding(10.dp)) {

            Image(
                painter = painterResource(id = R.drawable.dog_cartoon),
                contentDescription = "Dog",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .padding(0.dp)
            )

            Column(modifier = Modifier
                .weight(1f)
                .padding(10.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(text = "Ut på tur?", style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(0.dp))
                Text(text = "Se spennende turer her!")
            }


        }

    }
}


@Composable
fun DogCard(navController: NavHostController){
    Card(elevation = CardDefaults.cardElevation(
        defaultElevation = 6.dp),
        onClick = { navController.navigate(DogScreen.UserDogScreen.name)},
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)) {
        Row(modifier = Modifier.padding(10.dp)) {
            Column(modifier = Modifier
                .weight(1f)
                .padding(5.dp)) {
                Text(text = "Dine hunder", style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(0.dp))
                Text(text = "Klikk her for å komme til din oversikt!")
            }

            Image(
                painter = painterResource(id = R.drawable.dog_cartoon),
                contentDescription = "Dog",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .padding(0.dp)
            )


        }

    }
}




//Denne har jeg fått hjelp av AI til.
fun getDaysOfMonth(year: Int, month: Int): List<String> {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month)
        set(Calendar.DAY_OF_MONTH, 1)
        firstDayOfWeek = Calendar.MONDAY
    }

    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val days = mutableListOf<String>()


    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY


    repeat(firstDayOfWeek) {
        days.add("")
    }


    for (day in 1..daysInMonth) {
        days.add(day.toString())
    }

    return days
}


@Composable
fun CalendarHome() {
    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH)


    val daysOfMonth = getDaysOfMonth(currentYear, currentMonth)


    val monthFormatter = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    val formattedMonth = monthFormatter.format(calendar.time)


    val weekDays = listOf("Man", "Tir", "Ons", "Tor", "Fre", "Lør", "Søn")


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        //colors = CardDefaults.cardColors(containerColor = Color(0xFFD95A3C))
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = formattedMonth.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                weekDays.forEach { day ->
                    Text(
                        text = day,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier.height(200.dp),
                contentPadding = PaddingValues(4.dp)
            ) {
                items(daysOfMonth) { day ->
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .padding(4.dp)
                            .background(
                                if (day.isNotEmpty()) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (day.isNotEmpty()) MaterialTheme.colorScheme.onPrimary else Color.Transparent
                        )
                    }
                }
            }
        }
    }
}





@Preview
@Composable
fun PreCalender(){
    CalendarHome()
}