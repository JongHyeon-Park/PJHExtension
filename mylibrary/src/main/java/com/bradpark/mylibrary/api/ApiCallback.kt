package com.bradpark.mylibrary.api

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

abstract class ApiCallback<T> : Callback<T> {

    override fun onResponse(call: Call<T>, response: Response<T>) {
        when (response.code()) {
            HttpURLConnection.HTTP_OK,
            HttpURLConnection.HTTP_CREATED,
            HttpURLConnection.HTTP_ACCEPTED,
            HttpURLConnection.HTTP_NOT_AUTHORITATIVE -> response.body()?.let { onSuccess(it) }
            else -> onFailed(Throwable("Code " + response.code() + " : " + response.message()))
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        if (call.isCanceled) {
            onFailed(null)
        } else {
            onFailed(t)
        }
    }

    abstract fun onSuccess(response: T)

    abstract fun onFailed(throwable: Throwable?)
}