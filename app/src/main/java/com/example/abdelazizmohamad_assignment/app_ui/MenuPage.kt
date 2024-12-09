package com.example.abdelazizmohamad_assignment.app_ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.abdelazizmohamad_assignment.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuPage(navController: NavController) {
    val context = LocalContext.current
    var isMenuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
        },
        navigationIcon = {
            IconButton(onClick = { isMenuExpanded = !isMenuExpanded }) {
                Icon(
                    imageVector = if (isMenuExpanded) Icons.Default.Close else Icons.Default.Menu,
                    contentDescription = if (isMenuExpanded) "Close Menu" else "Open Menu"
                )
            }
        },
        actions = {
            if (isMenuExpanded) {
                // Home Icon
                IconButton(onClick = { navController.navigate("home") }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_action_home),
                        contentDescription = stringResource(R.string.app_go_home_description)
                    )
                }
                // Room Icon
                IconButton(onClick = { navController.navigate("roomList") }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_action_rooms),
                        contentDescription = stringResource(R.string.app_go_room_description)
                    )
                }
                // Email Icon
                IconButton(onClick = {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:aboudeh.mhd11@hotmail.com")
                        putExtra(Intent.EXTRA_SUBJECT, "Room Inquiry")
                        putExtra(Intent.EXTRA_TEXT, "I'd like more information about your rooms.")
                    }
                    context.startActivity(intent)
                }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_action_mail),
                        contentDescription = stringResource(R.string.app_go_mail_description)
                    )
                }
                // GitHub Icon
                IconButton(onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/abdelazizmohamad11"))
                    context.startActivity(intent)
                }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_action_github),
                        contentDescription = stringResource(R.string.app_go_github_description)
                    )
                }
            }
        },
        modifier = Modifier.height(if (isMenuExpanded) 70.dp else 56.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFB0BEC5),
            titleContentColor = Color.Black,
            actionIconContentColor = Color.Black
        )
    )
}
