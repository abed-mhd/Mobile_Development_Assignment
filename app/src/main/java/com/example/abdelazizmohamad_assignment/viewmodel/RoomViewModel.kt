package com.example.abdelazizmohamad_assignment.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.abdelazizmohamad_assignment.data.RoomCommandDto
import com.example.abdelazizmohamad_assignment.data.RoomDto
import com.example.abdelazizmohamad_assignment.data.WindowCommandDto
import com.example.abdelazizmohamad_assignment.data.WindowDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import service.ApiServices

class RoomViewModel : ViewModel() {

    private val _roomsState = MutableStateFlow<List<RoomDto>>(emptyList())
    val roomsState: StateFlow<List<RoomDto>> get() = _roomsState

    private val _roomState = MutableStateFlow<RoomDto?>(null)
    val roomState: StateFlow<RoomDto?> get() = _roomState

    private val _windowsState = MutableStateFlow<List<WindowDto>>(emptyList())
    val windowsState: StateFlow<List<WindowDto>> = _windowsState

    // Function to load all rooms
    fun findAll() {
        viewModelScope.launch(context = Dispatchers.IO) {
            runCatching {
                ApiServices.roomsApiService.findAll().execute()
            }.onSuccess { response ->
                _roomsState.value = response.body() ?: emptyList()
            }.onFailure { throwable ->
                throwable.printStackTrace()
                Log.e("RoomViewModel", "Failed to load rooms: ${throwable.message}")
            }
        }
    }

    // Function to load a room by ID
    fun findById(identifier: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                ApiServices.roomsApiService.findById(identifier).execute()
            }.onSuccess { response ->
                _roomState.value = response.body()
                response.body()?.let { setWindows(it.windows) }
            }.onFailure { throwable ->
                Log.e("RoomViewModel", "Failed to find room: ${throwable.message}")
                _roomState.value = null
            }
        }
    }


    // Function to update room by ID
    fun updateRoom(id: Long, roomDto: RoomDto) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                ApiServices.roomsApiService.updateRoom(id, roomDto).execute()
            }.onSuccess { response ->
                _roomState.value = response.body()
            }.onFailure { throwable ->
                Log.e("RoomViewModel", "Failed to update room: ${throwable.message}")
            }
        }
    }

    // Function to delete room by ID
    fun deleteRoom(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                ApiServices.roomsApiService.deleteRoom(id).execute()
            }.onSuccess {
                _roomState.value = null // Clear the current room state
                findAll() // Reload the list of rooms
                Log.d("RoomViewModel", "Room deleted successfully")
            }.onFailure { throwable ->
                Log.e("RoomViewModel", "Failed to delete room: ${throwable.message}")
            }
        }
    }

    // Function to create a room
    fun createRoom(roomCommandDto: RoomCommandDto) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                ApiServices.roomsApiService.createRoom(roomCommandDto).execute()
            }.onSuccess { response ->
                response.body()?.let { responseDto ->
                    findAll() // Reload the list of rooms
                    Log.d("RoomViewModel", "Room created successfully: $responseDto")
                }
            }.onFailure { throwable ->
                Log.e("RoomViewModel", "Failed to create room: ${throwable.message}")
            }
        }
    }

    fun setWindows(windows: List<WindowDto>) {
        _windowsState.value = windows
    }

    // Function to create a window
    fun createWindow(window: WindowCommandDto) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                ApiServices.windowsApiService.createWindow(window).execute()
            }.onSuccess { response ->
                response.body()?.let { newWindow ->
                    _windowsState.value += newWindow
                    Log.d("RoomViewModel", "Window created successfully: $newWindow")
                }
            }.onFailure { throwable ->
                Log.e("RoomViewModel", "Failed to create window: ${throwable.message}")
            }
        }
    }

    // Function to update a window by ID
    fun updateWindow(window: WindowDto) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                ApiServices.windowsApiService.updateWindow(window.id, window).execute()
            }.onSuccess { response ->
                response.body()?.let { updatedWindow ->
                    _windowsState.value = _windowsState.value.map {
                        if (it.id == updatedWindow.id) updatedWindow else it
                    }
                    Log.d("RoomViewModel", "Window updated successfully: $updatedWindow")
                }
            }.onFailure { throwable ->
                Log.e("RoomViewModel", "Failed to update window: ${throwable.message}")
            }
        }
    }

    // Function to delete a window by ID
    fun deleteWindow(windowId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                ApiServices.windowsApiService.deleteWindow(windowId).execute()
            }.onSuccess {
                _windowsState.value = _windowsState.value.filter { it.id != windowId }
                Log.d("RoomViewModel", "Window deleted successfully: ID $windowId")
            }.onFailure { throwable ->
                Log.e("RoomViewModel", "Failed to delete window: ${throwable.message}")
            }
        }
    }


}
