package com.example.abdelazizmohamad_assignment.data

import com.squareup.moshi.JsonClass

enum class WindowStatus { OPENED, CLOSED }

@JsonClass(generateAdapter = true)
data class WindowDto(
    val id: Long,
    val name: String,
    val roomName: String?,
    val roomId: Long?,
    val windowStatus: WindowStatus
)

data class WindowCommandDto(
    val name: String,
    val roomName: String?,
    val roomId: Long?,
    val windowStatus: WindowStatus
)