package com.automotive.bootcamp.music_browse_service.sources.remote

import com.automotive.bootcamp.music_browse_service.sources.remote.retrofit.RetrofitAudioSource
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteAudioSource {
    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://storage.googleapis.com/automotive-media/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val api = retrofit.create(AudioAPI::class.java)

    private val source = RetrofitAudioSource(api)

    suspend fun load() = source.retrieveRemoteMusic()
}