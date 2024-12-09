package com.example.abdelazizmohamad_assignment.service

import com.example.abdelazizmohamad_assignment.data.RoomCommandDto
import com.example.abdelazizmohamad_assignment.data.RoomDto
import com.example.abdelazizmohamad_assignment.data.WindowDto
import retrofit2.Call
import retrofit2.http.*

interface RoomApiService {
    @GET("rooms")
    fun findAll(): Call<List<RoomDto>>

    @GET("rooms/{id}")
    fun findById(@Path("id") id: Long): Call<RoomDto>

    @GET("rooms/search") // Endpoint to search by name or ID
    fun findByNameOrId(@Query("identifier") identifier: String): Call<RoomDto>

    @PUT("rooms/{id}")
    fun updateRoom(@Path("id") id: Long, @Body room: RoomDto): Call<RoomDto>

    @DELETE("rooms/{id}")
    fun deleteRoom(@Path("id") id: Long): Call<Unit>

    @POST("rooms")
    fun createRoom(@Body room: RoomCommandDto): Call<RoomCommandDto>

}