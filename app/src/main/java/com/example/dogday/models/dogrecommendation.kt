package com.example.dogday.models

import com.example.dogday.R

enum class DogID {
    BULLDOG, GOLDEN_RETRIEVER, BORDER_COLLIE, PUG, LABRADOR, BEAGLE, GERMAN_SHEPHERD, DALMATIAN,
    SIBERIAN_HUSKY, FRENCH_BULLDOG, COCKER_SPANIEL, SHIBA_INU, BOXER, BICHON_FRISE, GREAT_DANE,
    PEMBROKE_WELSH_CORGI, CHIHUAHUA, DACHSHUND, ROTTWEILER, MALTESE, AUSTRALIAN_SHEPHERD,
    BASENJI, DOBERMAN, SHIH_TZU, WHIPPET, WEIMARANER, AKITA
}
data class DogRecommendation(val breed: String, val description: String, val imageResId: Int)

val dogRecommendations = mapOf(
    DogID.BULLDOG to DogRecommendation("Bulldog", "A calm and relaxed dog for small spaces.", R.drawable.dog_cartoon),
    DogID.GOLDEN_RETRIEVER to DogRecommendation("Golden Retriever", "Friendly and active; great for families.", R.drawable.dog_cartoon),
    DogID.BORDER_COLLIE to DogRecommendation("Border Collie", "Highly energetic and intelligent; ideal for large spaces.", R.drawable.dog_cartoon),
    DogID.PUG to DogRecommendation("Pug", "Small, affectionate dog perfect for low activity.", R.drawable.dog_cartoon),
    DogID.LABRADOR to DogRecommendation("Labrador", "Outgoing and friendly; good for moderate space.", R.drawable.dog_cartoon),
    DogID.BEAGLE to DogRecommendation("Beagle", "Moderate activity level, loves medium spaces.", R.drawable.dog_cartoon),
    DogID.GERMAN_SHEPHERD to DogRecommendation("German Shepherd", "Intelligent and energetic, suited for large spaces.", R.drawable.dog_cartoon),
    DogID.DALMATIAN to DogRecommendation("Dalmatian", "Playful and active, requires a large yard.", R.drawable.dog_cartoon),
    DogID.SIBERIAN_HUSKY to DogRecommendation("Siberian Husky", "Independent and active; loves cold climates.", R.drawable.dog_cartoon),
    DogID.FRENCH_BULLDOG to DogRecommendation("French Bulldog", "Low-energy, great for small apartments.", R.drawable.dog_cartoon),
    DogID.COCKER_SPANIEL to DogRecommendation("Cocker Spaniel", "Friendly and active; enjoys moderate spaces.", R.drawable.dog_cartoon),
    DogID.SHIBA_INU to DogRecommendation("Shiba Inu", "Independent and spirited; good for various spaces.", R.drawable.dog_cartoon),
    DogID.BOXER to DogRecommendation("Boxer", "Playful and protective; good for active families.", R.drawable.dog_cartoon),
    DogID.BICHON_FRISE to DogRecommendation("Bichon Frise", "Small and affectionate, perfect for low-energy homes.", R.drawable.dog_cartoon),
    DogID.GREAT_DANE to DogRecommendation("Great Dane", "Gentle giant, ideal for larger spaces.", R.drawable.dog_cartoon),
    DogID.PEMBROKE_WELSH_CORGI to DogRecommendation("Pembroke Welsh Corgi", "Playful, small dog with a big personality.", R.drawable.dog_cartoon),
    DogID.CHIHUAHUA to DogRecommendation("Chihuahua", "Tiny and spirited; great for small spaces.", R.drawable.dog_cartoon),
    DogID.DACHSHUND to DogRecommendation("Dachshund", "Curious and independent; small but energetic.", R.drawable.dog_cartoon),
    DogID.ROTTWEILER to DogRecommendation("Rottweiler", "Strong and protective, suited for large areas.", R.drawable.dog_cartoon),
    DogID.MALTESE to DogRecommendation("Maltese", "Small and affectionate; perfect for low-energy lifestyles.", R.drawable.dog_cartoon),
    DogID.AUSTRALIAN_SHEPHERD to DogRecommendation("Australian Shepherd", "Energetic and intelligent; requires large space.", R.drawable.dog_cartoon),
    DogID.BASENJI to DogRecommendation("Basenji", "Independent and unique; requires moderate activity.", R.drawable.dog_cartoon),
    DogID.DOBERMAN to DogRecommendation("Doberman", "Intelligent and loyal; needs experienced handling.", R.drawable.dog_cartoon),
    DogID.SHIH_TZU to DogRecommendation("Shih Tzu", "Small and friendly, perfect for beginner owners.", R.drawable.dog_cartoon),
    DogID.WHIPPET to DogRecommendation("Whippet", "Calm and gentle; suited for small spaces and moderate activity.", R.drawable.dog_cartoon),
    DogID.WEIMARANER to DogRecommendation("Weimaraner", "Strong and active; loves outdoor activities.", R.drawable.dog_cartoon),
    DogID.AKITA to DogRecommendation("Akita", "Strong-willed and loyal; needs space and experienced owners.", R.drawable.dog_cartoon)
)

val dogRecommendationMap = mapOf(
    listOf("Low", "Small", "Beginner") to DogID.BULLDOG,
    listOf("Low", "Small", "Intermediate") to DogID.PUG,
    listOf("Low", "Small", "Advanced") to DogID.FRENCH_BULLDOG,
    listOf("Low", "Medium", "Beginner") to DogID.SHIH_TZU,
    listOf("Low", "Medium", "Intermediate") to DogID.WHIPPET,
    listOf("Low", "Medium", "Advanced") to DogID.MALTESE,
    listOf("Low", "Large", "Beginner") to DogID.CHIHUAHUA,
    listOf("Low", "Large", "Intermediate") to DogID.BICHON_FRISE,
    listOf("Low", "Large", "Advanced") to DogID.GREAT_DANE,
    listOf("Moderate", "Small", "Beginner") to DogID.PEMBROKE_WELSH_CORGI,
    listOf("Moderate", "Small", "Intermediate") to DogID.DACHSHUND,
    listOf("Moderate", "Small", "Advanced") to DogID.BEAGLE,
    listOf("Moderate", "Medium", "Beginner") to DogID.BOXER,
    listOf("Moderate", "Medium", "Intermediate") to DogID.GOLDEN_RETRIEVER,
    listOf("Moderate", "Medium", "Advanced") to DogID.BASENJI,
    listOf("Moderate", "Large", "Beginner") to DogID.LABRADOR,
    listOf("Moderate", "Large", "Intermediate") to DogID.COCKER_SPANIEL,
    listOf("Moderate", "Large", "Advanced") to DogID.DALMATIAN,
    listOf("High", "Small", "Beginner") to DogID.SHIBA_INU,
    listOf("High", "Small", "Intermediate") to DogID.DOBERMAN,
    listOf("High", "Small", "Advanced") to DogID.ROTTWEILER,
    listOf("High", "Medium", "Beginner") to DogID.WEIMARANER,
    listOf("High", "Medium", "Intermediate") to DogID.AUSTRALIAN_SHEPHERD,
    listOf("High", "Medium", "Advanced") to DogID.SIBERIAN_HUSKY,
    listOf("High", "Large", "Beginner") to DogID.AKITA,
    listOf("High", "Large", "Intermediate") to DogID.GERMAN_SHEPHERD,
    listOf("High", "Large", "Advanced") to DogID.BORDER_COLLIE
)


fun getRecommendedDogID(answers: List<String>): DogID {
    return dogRecommendationMap[answers] ?: DogID.BULLDOG // Default to Bulldog if no match
}