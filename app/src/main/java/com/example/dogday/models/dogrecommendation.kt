package com.example.dogday.models

import com.example.dogday.R

enum class DogID {
    BULLDOG, GOLDEN_RETRIEVER, BORDER_COLLIE, PUG, LABRADOR, BEAGLE, GERMAN_SHEPHERD, DALMATIAN,
    SIBERIAN_HUSKY, FRENCH_BULLDOG, COCKER_SPANIEL, SHIBA_INU, BOXER, BICHON_FRISE, GREAT_DANE,
    PEMBROKE_WELSH_CORGI, CHIHUAHUA, DACHSHUND, ROTTWEILER, MALTESE, AUSTRALIAN_SHEPHERD,
    BASENJI, DOBERMAN, SHIH_TZU, WHIPPET, WEIMARANER, AKITA
}
data class DogRecommendation(val breed: String, val description: String, val imageUrl: String)

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