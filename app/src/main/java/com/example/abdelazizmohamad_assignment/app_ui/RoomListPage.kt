package com.example.abdelazizmohamad_assignment.app_ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.abdelazizmohamad_assignment.data.RoomCommandDto
import com.example.abdelazizmohamad_assignment.data.RoomDto
import com.example.abdelazizmohamad_assignment.viewmodel.RoomViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomListPage(navController: NavController) {
    val viewModel: RoomViewModel = viewModel()

    // Observe the list of rooms and the loading/error states
    val rooms by viewModel.roomsState.collectAsState()
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var showCreateRoomDialog by remember { mutableStateOf(false) }

    // Fetch the rooms when the page is loaded
    LaunchedEffect(Unit) {
        viewModel.findAll()
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rooms List", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showCreateRoomDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Create Room",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                errorMessage != null -> {
                    ErrorView(
                        message = errorMessage ?: "An unknown error occurred",
                        onRetry = {
                            errorMessage = null
                            isLoading = true
                            viewModel.findAll()
                        }
                    )
                }

                rooms.isEmpty() -> {
                    EmptyView(message = "No rooms available.")
                }

                else -> {
                    RoomList(rooms = rooms, onRoomClick = { roomId ->
                        navController.navigate("roomDetails/$roomId")
                    })
                }
            }
        }

        if (showCreateRoomDialog) {
            CreateRoomDialog(
                onDismiss = { showCreateRoomDialog = false },
                onCreate = { name, currentTemperature, targetTemperature, floor, buildingId ->
                    viewModel.createRoom(
                        RoomCommandDto(
                            name = name,
                            currentTemperature = currentTemperature,
                            targetTemperature = targetTemperature,
                            floor = floor,
                            buildingId = buildingId
                        )
                    )
                    showCreateRoomDialog = false
                }
            )
        }
    }
}

@Composable
fun CreateRoomDialog(
    onDismiss: () -> Unit,
    onCreate: (String, Double?, Double?, Int, Long) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var currentTemperature by remember { mutableStateOf("") }
    var targetTemperature by remember { mutableStateOf("") }
    var floor by remember { mutableStateOf("") }
    var buildingId by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Room") },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Room Name") },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = currentTemperature,
                    onValueChange = { currentTemperature = it },
                    label = { Text("Current Temperature (Optional)") },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = targetTemperature,
                    onValueChange = { targetTemperature = it },
                    label = { Text("Target Temperature (Optional)") },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = floor,
                    onValueChange = { floor = it },
                    label = { Text("Floor (Default: 1)") },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = buildingId,
                    onValueChange = { buildingId = it },
                    label = { Text("Building ID (Default: -10)") },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val floorParsed = floor.toIntOrNull() ?: 1
                val buildingIdParsed = buildingId.toLongOrNull() ?: -10
                val currentTempParsed = currentTemperature.toDoubleOrNull()
                val targetTempParsed = targetTemperature.toDoubleOrNull()

                onCreate(name, currentTempParsed, targetTempParsed, floorParsed, buildingIdParsed)
            }) {
                Text("Create")
            }
        },
        dismissButton = {
            Button(onClick = {
                focusManager.clearFocus()
                onDismiss()
            }) {
                Text("Cancel")
            }
        }
    )
}


@Composable
fun RoomList(rooms: List<RoomDto>, onRoomClick: (Long) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(rooms) { room ->
            RoomItem(room = room, onClick = { onRoomClick(room.id) })
            Divider()
        }
    }
}


@Composable
fun RoomItem(room: RoomDto, onClick: () -> Unit) {
    Log.d("RoomItem", "Room ID: ${room.id}")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = room.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Target Temperature: ${room.targetTemperature}°C",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ErrorView(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}

@Composable
fun EmptyView(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
