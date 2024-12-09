package com.example.abdelazizmohamad_assignment.service

import com.example.abdelazizmohamad_assignment.data.WindowCommandDto
import com.example.abdelazizmohamad_assignment.data.WindowDto
import retrofit2.Call
import retrofit2.http.*

interface WindowApiService {

    @POST("/api/windows")
    fun createWindow(@Body window: WindowCommandDto): Call<WindowDto>

    @PUT("/api/windows/{id}")
    fun updateWindow(@Path("id") id: Long, @Body window: WindowDto): Call<WindowDto>

    @DELETE("/api/windows/{id}")
    fun deleteWindow(@Path("id") id: Long): Call<Unit>
}
