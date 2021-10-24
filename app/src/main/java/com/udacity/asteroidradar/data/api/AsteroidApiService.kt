package com.udacity.asteroidradar.data.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.util.Constants.BASE_URL
import com.udacity.asteroidradar.data.domain.PictureOfDay
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

class AsteroidApiService {

    interface AsteroidService {

        @GET("neo/rest/v1/feed")
        suspend fun getAsteroids(@Query("api_key") apKey: String,
                                 @Query("start_date") startDate: String,
                                 @Query("end_date") endDate: String): String

        @GET("planetary/apod")
        suspend fun getPictureOfTheDay(@Query("api_key") apKey: String): PictureOfDay

    }

    object AsteroidApi {
        private val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val retrofitService: AsteroidService by lazy { retrofit.create(AsteroidService::class.java) }
    }

}