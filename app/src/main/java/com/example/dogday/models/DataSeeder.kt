package com.example.dogday.util

import com.example.dogday.models.DogID
import com.example.dogday.models.DogRecommendation
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun seedDogRecommendations(firestore: FirebaseFirestore) {
    val dogRecommendations = mapOf(
        DogID.BULLDOG to DogRecommendation("Bulldog", "A calm and relaxed dog for small spaces.", "https://example.com/bulldog.jpg"),
        DogID.PUG to DogRecommendation("Pug", "Small, affectionate dog perfect for low activity.", "https://example.com/pug.jpg"),
        DogID.FRENCH_BULLDOG to DogRecommendation("French Bulldog", "Compact and low-energy, great for small apartments.", "https://example.com/french_bulldog.jpg"),

        DogID.SHIH_TZU to DogRecommendation("Shih Tzu", "Friendly and low-maintenance; perfect for moderate spaces.", "https://example.com/shih_tzu.jpg"),
        DogID.WHIPPET to DogRecommendation("Whippet", "Quiet and relaxed, great for medium-sized homes.", "https://example.com/whippet.jpg"),
        DogID.MALTESE to DogRecommendation("Maltese", "Small, gentle, and suitable for medium spaces.", "https://example.com/maltese.jpg"),

        DogID.CHIHUAHUA to DogRecommendation("Chihuahua", "Tiny but spirited, loves large spaces.", "https://example.com/chihuahua.jpg"),
        DogID.BICHON_FRISE to DogRecommendation("Bichon Frise", "Playful and compact; enjoys more room.", "https://example.com/bichon_frise.jpg"),
        DogID.GREAT_DANE to DogRecommendation("Great Dane", "Gentle giant, ideal for larger homes.", "https://example.com/great_dane.jpg"),

        DogID.PEMBROKE_WELSH_CORGI to DogRecommendation("Pembroke Welsh Corgi", "Playful, small dog for beginners in compact spaces.", "https://example.com/corgi.jpg"),
        DogID.DACHSHUND to DogRecommendation("Dachshund", "Loyal and playful; good in small spaces.", "https://example.com/dachshund.jpg"),
        DogID.BEAGLE to DogRecommendation("Beagle", "Curious and friendly, fits well in small homes.", "https://example.com/beagle.jpg"),

        DogID.BOXER to DogRecommendation("Boxer", "Energetic and protective; great for medium spaces.", "https://example.com/boxer.jpg"),
        DogID.GOLDEN_RETRIEVER to DogRecommendation("Golden Retriever", "Friendly and adaptable; perfect for medium homes.", "https://example.com/golden_retriever.jpg"),
        DogID.BASENJI to DogRecommendation("Basenji", "Independent and curious; good in medium-sized homes.", "https://example.com/basenji.jpg"),

        DogID.LABRADOR to DogRecommendation("Labrador", "Friendly and adaptable; thrives in larger homes.", "https://example.com/labrador.jpg"),
        DogID.COCKER_SPANIEL to DogRecommendation("Cocker Spaniel", "Affectionate and energetic; enjoys larger spaces.", "https://example.com/cocker_spaniel.jpg"),
        DogID.DALMATIAN to DogRecommendation("Dalmatian", "High-energy; needs ample room for play.", "https://example.com/dalmatian.jpg"),

        DogID.SHIBA_INU to DogRecommendation("Shiba Inu", "Independent and alert; great in small spaces.", "https://example.com/shiba_inu.jpg"),
        DogID.DOBERMAN to DogRecommendation("Doberman", "Confident and protective; good for experienced owners.", "https://example.com/doberman.jpg"),
        DogID.ROTTWEILER to DogRecommendation("Rottweiler", "Loyal and strong; thrives in a small, controlled space.", "https://example.com/rottweiler.jpg"),

        DogID.WEIMARANER to DogRecommendation("Weimaraner", "Energetic and loyal; perfect for moderate spaces.", "https://example.com/weimaraner.jpg"),
        DogID.AUSTRALIAN_SHEPHERD to DogRecommendation("Australian Shepherd", "Highly active; needs a medium home with activities.", "https://example.com/australian_shepherd.jpg"),
        DogID.SIBERIAN_HUSKY to DogRecommendation("Siberian Husky", "Independent and strong-willed; loves outdoor spaces.", "https://example.com/siberian_husky.jpg"),

        DogID.AKITA to DogRecommendation("Akita", "Loyal and protective; best for large homes with space.", "https://example.com/akita.jpg"),
        DogID.GERMAN_SHEPHERD to DogRecommendation("German Shepherd", "Loyal and intelligent; needs a larger home.", "https://example.com/german_shepherd.jpg"),
        DogID.BORDER_COLLIE to DogRecommendation("Border Collie", "Highly energetic and intelligent; thrives in large homes.", "https://example.com/border_collie.jpg")
    )

    CoroutineScope(Dispatchers.IO).launch {
        for ((dogID, recommendation) in dogRecommendations) {
            val documentID = dogID.name // Document ID in uppercase

            firestore.collection("dogRecommendations").document(documentID).set(
                mapOf(
                    "id" to documentID,
                    "breed" to recommendation.breed,
                    "description" to recommendation.description,
                    "imageUrl" to recommendation.imageUrl
                )
            ).addOnSuccessListener {
                println("Successfully added $documentID")
            }.addOnFailureListener { e ->
                println("Error adding $documentID: ${e.message}")
            }
        }
    }
}
