package com.bradpark.mylibrary.data

open class BaseResponse(var code: String? = "",
                        var message: String? = "",
                        var error: Throwable? = null) {
    companion object {
        const val SUCCESS_CODE = "1"
        const val ERROR_CODE = "2"
        const val DUPLICATE_LOGIN_CODE = "3"
    }
}
