package com.bradpark.extensions

/**
 * Int null 체크
 * @receiver Int?
 * @return Boolean
 */
fun Int?.isNull(): Boolean {
    return this == null
}

/**
 * Int null 또는 0 체크
 * @receiver Int?
 * @return Boolean
 */
fun Int?.isNullOrZero(): Boolean {
    return this == null || this == 0
}

/**
 * Int null 또는 -1 체크
 * @receiver Int?
 * @return Boolean
 */
fun Int?.isNullOrMinus1(): Boolean {
    return this == null || this == -1
}