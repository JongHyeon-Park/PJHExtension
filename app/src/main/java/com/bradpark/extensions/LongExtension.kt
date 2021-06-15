package com.bradpark.extensions

/**
 * 사이즈 Kb 변환
 * @receiver Long
 * @return Long
 */
fun Long.sizeInKb() = this/1024
/**
 * 사이즈 Mb 변환
 * @receiver Long
 * @return Long
 */
fun Long.sizeInMb() = this.sizeInKb()/1024