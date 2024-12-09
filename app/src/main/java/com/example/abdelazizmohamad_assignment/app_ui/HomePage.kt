package com.example.abdelazizmohamad_assignment.app_ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.abdelazizmohamad_assignment.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                },
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppLogo(Modifier.padding(top = 32.dp))
                Text(
                    text = stringResource(R.string.act_main_welcome),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 24.dp)
                        .align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )

                // Input Field for Room ID
                var roomId by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = roomId,
                    onValueChange = { roomId = it },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    label = { Text(stringResource(R.string.act_main_fill_id)) },
                    placeholder = { Text(stringResource(R.string.act_main_fill_id)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.act_main_fill_id)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (roomId.isNotBlank()) {
                                navController.navigate("roomDetails/$roomId")
                            }
                        }
                    ),
                    singleLine = true
                )

                Button(
                    onClick = {
                        if (roomId.isNotBlank()) {
                            navController.navigate("roomDetails/$roomId")
                        }
                    },
                    enabled = roomId.isNotBlank(),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(text = stringResource(R.string.act_main_open))
                }
            }
        }
    )
}

@Composable
fun AppLogo(modifier: Modifier) {
    Image(
        painter = painterResource(R.drawable.ic_logo),
        contentDescription = stringResource(R.string.app_logo_description),
        modifier = modifier
            .paddingFromBaseline(top = 40.dp)
            .height(100.dp)
    )
}
