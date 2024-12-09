package com.example.abdelazizmohamad_assignment.service

import com.example.abdelazizmohamad_assignment.data.RoomDto
import com.example.abdelazizmohamad_assignment.data.WindowDto
import com.example.abdelazizmohamad_assignment.data.WindowStatus

object RoomService {
    private val ROOM_KIND = listOf("Room", "Meeting", "Laboratory", "Office", "Boardroom")
    private val WINDOW_KIND = listOf("Sliding", "Bay", "Casement", "Hung", "Fixed")
    private val ROOMS = mutableListOf<RoomDto>()

    init {
        (1..50).forEach { id ->
            val roomName = "${('A'..'Z').random()}$id ${ROOM_KIND.random()}"
            val windows = (1..(1..6).random()).map {
                WindowDto(
                    id = it.toLong(),
                    name = "${WINDOW_KIND.random()} Window $it",
                    roomName = roomName,
                    roomId = id.toLong(),
                    windowStatus = WindowStatus.values().random()
                )
            }
            ROOMS.add(
                RoomDto(
                    id = id.toLong(),
                    name = roomName,
                    currentTemperature = (15..30).random().toDouble(),
                    targetTemperature = (18..25).random().toDouble(),
                    windows = windows
                )
            )
        }
    }

    fun findAll() = ROOMS.sortedBy { it.name }
    fun findById(id: Long) = ROOMS.find { it.id == id }
    fun findByName(name: String) = ROOMS.find { it.name == name }
    fun findByNameOrId(param: String?): RoomDto? {
        return param?.toLongOrNull()?.let { findById(it) } ?: findByName(param ?: "")
    }
    /**
     * Updates an existing room with the given values.
     * Returns the updated RoomDto if successful.
     * Throws IllegalArgumentException if the room is not found.
     */
    fun updateRoom(id: Long, room: RoomDto): RoomDto {
        val index = ROOMS.indexOfFirst { it.id == id }
        val updatedRoom = findById(id)?.copy(
            name = room.name,
            targetTemperature = room.targetTemperature,
            currentTemperature = room.currentTemperature
        ) ?: throw IllegalArgumentException()
        return ROOMS.set(index, updatedRoom)
    }

}