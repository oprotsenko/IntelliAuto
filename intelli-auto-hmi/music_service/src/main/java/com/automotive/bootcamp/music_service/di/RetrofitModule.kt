package com.automotive.bootcamp.music_service.di

import com.automotive.bootcamp.music_service.data.remote.AudioAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val retrofitModule = module {
    single { provideInterceptor() }
    single { provideOkHttpClient(get()) }
    single { provideRetrofit(get()) }
    single { provideDefinitionApi(get()) }
}

fun provideInterceptor(): HttpLoggingInterceptor {
    val interceptor = HttpLoggingInterceptor()
    return interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
}

fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient =
    OkHttpClient.Builder().addInterceptor(interceptor).build()

fun provideRetrofit(client: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .baseUrl("https://storage.googleapis.com/automotive-media/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

fun provideDefinitionApi(retrofit: Retrofit): AudioAPI =
    retrofit.create(AudioAPI::class.java)