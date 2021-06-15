package com.bradpark.extensions

val String.stringToByteArray: ByteArray
    get() {
        return this.toByteArray(Charsets.ISO_8859_1)
    }

val ByteArray.byteArrayToString: String
    get() {
        return String(this, Charsets.ISO_8859_1)
    }

fun String?.isDataEqual(value: String?) = this ?: "" == value ?: ""