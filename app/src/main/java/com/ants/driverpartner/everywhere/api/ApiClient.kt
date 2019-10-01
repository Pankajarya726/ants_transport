package com.ants.driverpartner.everywhere.api

import com.ants.driverpartner.everywhere.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    var mRetrofit: Retrofit? = null

    fun retrofit(): Retrofit {
        if (mRetrofit == null) {

            val builder = OkHttpClient.Builder()
            if (BuildConfig.DEBUG) {
                //log information interceptor
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

                //Set the Debug Log mode
                builder.addInterceptor(loggingInterceptor)
            }

            builder.addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                        .header("type", "1")
                        .method(original.method(), original.body())
                        .build()

                chain.proceed(request)
            }

            val okHttpClient = builder
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .build()

            mRetrofit = Retrofit.Builder()
                   .baseUrl(BuildConfig.BaseUrl)
                //    .addConverterFactory(JSONConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .build()
        }
        return mRetrofit as Retrofit
    }


}