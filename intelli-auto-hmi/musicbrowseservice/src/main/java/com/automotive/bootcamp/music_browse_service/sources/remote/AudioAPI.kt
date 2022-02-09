package com.automotive.bootcamp.music_browse_service.sources.remote

import com.automotive.bootcamp.music_browse_service.sources.remote.retrofit.responses.RemoteMusicResponse
import retrofit2.Response
import retrofit2.http.GET

interface AudioAPI {

    @GET("music.json")
    suspend fun getRemoteMusic(): Response<RemoteMusicResponse>
}