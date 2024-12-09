package com.example.abdelazizmohamad_assignment.app_ui

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.abdelazizmohamad_assignment.data.RoomDto
import com.example.abdelazizmohamad_assignment.data.WindowCommandDto
import com.example.abdelazizmohamad_assignment.data.WindowDto
import com.example.abdelazizmohamad_assignment.data.WindowStatus
import com.example.abdelazizmohamad_assignment.viewmodel.RoomViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomDetailsPage(roomId: Long, navController: NavController) {
    val viewModel: RoomViewModel = viewModel()
    val context = LocalContext.current

    var isLoading by remember { mutableStateOf(true) }
    var showCreateWindowDialog by remember { mutableStateOf(false) }

    // Fetch the room by ID
    LaunchedEffect(Unit) {
        viewModel.findById(roomId)
        isLoading = false
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Room Details", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { innerPadding ->
        val room by viewModel.roomState.collectAsState()
        val windows by viewModel.windowsState.collectAsState()

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                room != null -> {
                    Column(modifier = Modifier.padding(innerPadding)) {
                        RoomDetail(
                            room = room,
                            onSave = { updatedRoom ->
                                viewModel.updateRoom(room!!.id, updatedRoom)
                                Toast.makeText(context, "Room ${updatedRoom.name} updated", Toast.LENGTH_LONG).show()
                            },
                            onDelete = {
                                room?.id?.let { id ->
                                    viewModel.deleteRoom(id)
                                    Toast.makeText(context, "Room deleted", Toast.LENGTH_SHORT).show()
                                    navController.popBackStack()
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Windows", style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
                            Button(onClick = { showCreateWindowDialog = true }) {
                                Text("Add Window")
                            }
                        }

                        LazyColumn(modifier = Modifier.padding(16.dp)) {
                            items(windows) { window ->
                                WindowCard(
                                    window = window,
                                    onUpdate = { updatedWindow ->
                                        val newStatus = if (window.windowStatus == WindowStatus.CLOSED) {
                                            WindowStatus.OPENED
                                        } else {
                                            WindowStatus.CLOSED
                                        }
                                        viewModel.updateWindow(updatedWindow.copy(windowStatus = newStatus))
                                        Toast.makeText(context, "Window status updated", Toast.LENGTH_SHORT).show()
                                    },
                                    onDelete = {
                                        viewModel.deleteWindow(window.id)
                                        Toast.makeText(context, "Window deleted", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }
                    }
                }

                else -> {
                    NoRoom(modifier = Modifier.align(Alignment.Center))
                }
            }
        }

        if (showCreateWindowDialog) {
            CreateWindowDialog(
                onDismiss = { showCreateWindowDialog = false },
                onCreate = { name, status ->
                    viewModel.createWindow(
                        WindowCommandDto(
                            name = name,
                            windowStatus = status,
                            roomName = room?.name,
                            roomId = room?.id
                        )
                    )
                    showCreateWindowDialog = false
                },
            )
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun RoomDetail(room: RoomDto?, modifier: Modifier = Modifier, onSave: (RoomDto) -> Unit, onDelete: () -> Unit) {
    // Maintain mutable state for live editing
    var roomName by remember { mutableStateOf(room?.name) }
    var targetTemperature by remember { mutableStateOf(room?.targetTemperature ?: 0.0) }
    var currentTemperature by remember { mutableStateOf(room?.currentTemperature) }

    Column(modifier = modifier.padding(16.dp)) {
        roomName?.let {
            OutlinedTextField(
                value = it,
                onValueChange = { roomName = it },
                label = { Text("Room Name") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text("Current Temperature:", modifier = Modifier.weight(1f))
            Text("${currentTemperature}°C", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Target Temperature: ${targetTemperature}°C", style = MaterialTheme.typography.bodyLarge)

        Slider(
            value = targetTemperature.toFloat(),
            onValueChange = { newValue ->
                targetTemperature = String.format("%.2f", newValue).toDouble()
            },
            valueRange = -20f..40f, // Example range in °C
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Save and Delete Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    val updatedRoom = room?.let {
                        roomName?.let { it1 ->
                            room.copy(
                                id = it.id,
                                name = it1,
                                targetTemperature = targetTemperature,
                                currentTemperature = currentTemperature
                            )
                        }
                    }
                    if (updatedRoom != null) {
                        onSave(updatedRoom)
                    }
                }
            ) {
                Text("Save")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(onClick = onDelete, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                Text("Delete")
            }
        }
    }
}


@Composable
fun CreateWindowDialog(
    onDismiss: () -> Unit,
    onCreate: (String, WindowStatus) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(WindowStatus.CLOSED) }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Window") },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Window Name") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box {
                    OutlinedTextField(
                        value = status.name,
                        onValueChange = {},
                        label = { Text("Window Status") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        }
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        WindowStatus.entries.forEach { enumValue ->
                            DropdownMenuItem(
                                text = { Text(enumValue.name) },
                                onClick = {
                                    status = enumValue
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onCreate(name, status) }) {
                Text("Create")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


@Composable
fun WindowCard(
    window: WindowDto,
    onUpdate: (WindowDto) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Window Name: ${window.name}", style = MaterialTheme.typography.bodyLarge)
            Text("Status: ${window.windowStatus}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { onUpdate(window) }) {
                Text(if (window.windowStatus == WindowStatus.CLOSED) "Open Window" else "Close Window")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = onDelete, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                Text("Delete")
            }
        }
    }
}



@Composable
fun NoRoom(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("No room found")
    }
}
