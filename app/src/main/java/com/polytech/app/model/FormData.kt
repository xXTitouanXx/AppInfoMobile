package com.polytech.app.model

import android.os.Parcel
import android.os.Parcelable

class FormData(
    val productName: String? = null,
    val purchaseDate: String ? = null,
    val origin: String ? = null,
    val selectedProductType: String ? = null,
    val isFavorite: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productName)
        parcel.writeString(purchaseDate)
        parcel.writeString(origin)
        parcel.writeString(selectedProductType)
        parcel.writeByte(if (isFavorite) 1 else 0)
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
    }
}