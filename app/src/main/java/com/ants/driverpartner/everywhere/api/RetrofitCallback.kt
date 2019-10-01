package com.ants.driverpartner.everywhere.api

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
abstract class RetrofitCallback<M> : Callback<M> {

    abstract fun onSuccess(model: M)

    abstract fun onFailure(code: Int, msg: String)

    abstract fun onThrowable(t: Throwable)

    abstract fun onFinish()

    override fun onResponse(call: Call<M>, response: Response<M>) {
        if (response.isSuccessful()) {
            onSuccess(response.body()!!)
        } else {
            onFailure(response.code(), response.errorBody().toString())
        }
        onFinish()
    }

    override fun onFailure(call: Call<M>, t: Throwable) {
        onThrowable(t)
        onFinish()
    }

}
