package com.bradpark.extensions

import android.os.Parcel
import android.os.Parcelable

/**
 * 객체 깊은 복사 (Parcelable)
 * @param objectToClone T
 * @return T?
 */
fun <T : Parcelable> deepClone(objectToClone: T): T? {
    var parcel: Parcel? = null
    return try {
        parcel = Parcel.obtain()
        parcel.writeParcelable(objectToClone, 0)
        parcel.setDataPosition(0)
        parcel.readParcelable(objectToClone::class.java.classLoader)
    } finally {
        parcel?.recycle()
    }
}