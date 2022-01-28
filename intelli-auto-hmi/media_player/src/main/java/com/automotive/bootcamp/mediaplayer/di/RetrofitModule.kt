package com.automotive.bootcamp.mediaplayer.di

import com.automotive.bootcamp.mediaplayer.data.remote.AudioAPI
import com.automotive.bootcamp.mediaplayer.utils.BASE_URL
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
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

fun provideDefinitionApi(retrofit: Retrofit): AudioAPI =
    retrofit.create(AudioAPI::class.java)