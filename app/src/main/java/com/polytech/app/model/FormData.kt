package com.polytech.app.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import kotlin.random.Random

data class FormData(
    val id: String = generateUniqueId(),
    val productName: String? = null,
    val purchaseDate: String? = null,
    val origin: String? = null,
    val selectedProductType: String? = null,
    val isFavorite: Boolean = false,
    val imageUri: Uri? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: generateUniqueId(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readParcelable(Uri::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(productName)
        parcel.writeString(purchaseDate)
        parcel.writeString(origin)
        parcel.writeString(selectedProductType)
        parcel.writeByte(if (isFavorite) 1 else 0)
        parcel.writeParcelable(imageUri, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FormData> {
        override fun createFromParcel(parcel: Parcel): FormData {
            return FormData(parcel)
        }

        override fun newArray(size: Int): Array<FormData?> {
            return arrayOfNulls(size)
        }

        private fun generateUniqueId(): String {
            return Random.nextInt(0, Int.MAX_VALUE).toString()
        }
    }
}
