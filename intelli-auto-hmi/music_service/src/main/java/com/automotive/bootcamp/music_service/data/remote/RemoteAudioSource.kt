package com.automotive.bootcamp.music_service.data.remote

import com.automotive.bootcamp.music_service.data.remote.retrofit.RetrofitAudioSource
import com.automotive.bootcamp.music_service.utils.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteAudioSource {
    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val api = retrofit.create(AudioAPI::class.java)

    private val source = RetrofitAudioSource(api)

    suspend fun load() = source.retrieveRemoteMusic()
}