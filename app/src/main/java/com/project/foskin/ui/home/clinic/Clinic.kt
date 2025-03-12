package com.project.foskin.ui.home.clinic

import android.os.Parcel
import android.os.Parcelable

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
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readFloat(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readParcelable(OperationalHours::class.java.classLoader) ?: OperationalHours(),
        parcel.readParcelable(DetailedAddress::class.java.classLoader) ?: DetailedAddress(),
        parcel.readDouble(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(address)
        parcel.writeInt(imageResId)
        parcel.writeFloat(rating)
        parcel.writeInt(reviews)
        parcel.writeString(slug)
        parcel.writeString(storeImage)
        parcel.writeString(description)
        parcel.writeParcelable(operationalHours, flags)
        parcel.writeParcelable(detailedAddress, flags)
        parcel.writeDouble(ratings)
        parcel.writeInt(totalReviews)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Clinic> {
        override fun createFromParcel(parcel: Parcel): Clinic {
            return Clinic(parcel)
        }

        override fun newArray(size: Int): Array<Clinic?> {
            return arrayOfNulls(size)
        }
    }
}

data class OperationalHours(
    val opening: String = "",
    val closing: String = "",
    val weekday: Map<String, String> = emptyMap(),
    val weekend: Map<String, String> = emptyMap()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readHashMap(String::class.java.classLoader) as Map<String, String>,
        parcel.readHashMap(String::class.java.classLoader) as Map<String, String>
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(opening)
        parcel.writeString(closing)
        parcel.writeMap(weekday)
        parcel.writeMap(weekend)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<OperationalHours> {
        override fun createFromParcel(parcel: Parcel): OperationalHours {
            return OperationalHours(parcel)
        }

        override fun newArray(size: Int): Array<OperationalHours?> {
            return arrayOfNulls(size)
        }
    }
}

data class DetailedAddress(
    val country: String = "",
    val city: String = "",
    val state: String = "",
    val district: String = "",
    val streetName: String = "",
    val location: Location = Location(),
    val maps: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readParcelable(Location::class.java.classLoader) ?: Location(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(country)
        parcel.writeString(city)
        parcel.writeString(state)
        parcel.writeString(district)
        parcel.writeString(streetName)
        parcel.writeParcelable(location, flags)
        parcel.writeString(maps)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<DetailedAddress> {
        override fun createFromParcel(parcel: Parcel): DetailedAddress {
            return DetailedAddress(parcel)
        }

        override fun newArray(size: Int): Array<DetailedAddress?> {
            return arrayOfNulls(size)
        }
    }
}

data class Location(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Location> {
        override fun createFromParcel(parcel: Parcel): Location {
            return Location(parcel)
        }

        override fun newArray(size: Int): Array<Location?> {
            return arrayOfNulls(size)
        }
    }
}