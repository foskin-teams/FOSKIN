package com.project.foskin.ui.home.clinic

data class Clinic(
    val name: String,
    val address: String,
    val imageResId: Int,
    val rating: Float,
    val reviews: Int,
    val slug: String,
    val storeImage: String,
    val description: String,
    val operationalHours: OperationalHours,
    val detailedAddress: DetailedAddress,
    val ratings: Double,
    val totalReviews: Int
)

data class OperationalHours(
    val opening: String,
    val closing: String,
    val weekday: Map<String, String>,
    val weekend: Map<String, String>
)

data class DetailedAddress(
    val country: String,
    val city: String,
    val state: String,
    val district: String,
    val streetName: String,
    val location: Location,
    val maps: String
)

data class Location(
    val latitude: Double,
    val longitude: Double
)