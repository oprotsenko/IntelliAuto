package com.automotive.bootcamp.music_service.data.remote

import com.automotive.bootcamp.music_service.data.remote.retrofit.responses.RemoteMusicResponse
import retrofit2.Response
import retrofit2.http.GET

interface AudioAPI {

    @GET("music.json")
    suspend fun getRemoteMusic(): Response<RemoteMusicResponse>
}