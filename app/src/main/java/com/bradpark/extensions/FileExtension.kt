package com.bradpark.extensions

import java.io.File
import java.io.InputStream

/**
 * 파일 복사
 * @receiver File
 * @param inputStream InputStream
 */
fun File.copyInputStreamToFile(inputStream: InputStream) {
    this.outputStream().use { fileOut ->
        inputStream.copyTo(fileOut)
    }
}