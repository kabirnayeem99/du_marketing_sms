package io.github.kabirnayeem99.dumarketingstudent.data.vo

import android.os.Parcel
import android.os.Parcelable


data class GalleryData(val imageUrl: String?) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GalleryData> {
        override fun createFromParcel(parcel: Parcel): GalleryData {
            return GalleryData(parcel)
        }

        override fun newArray(size: Int): Array<GalleryData?> {
            return arrayOfNulls(size)
        }
    }
}
