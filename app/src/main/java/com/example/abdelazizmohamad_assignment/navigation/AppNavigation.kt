package com.example.abdelazizmohamad_assignment.navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.abdelazizmohamad_assignment.app_ui.HomePage
import com.example.abdelazizmohamad_assignment.app_ui.MenuPage
import com.example.abdelazizmohamad_assignment.app_ui.RoomDetailsPage
import com.example.abdelazizmohamad_assignment.app_ui.RoomListPage

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    // Scaffold integrates the MenuPage (TopAppBar) into the overall UI
    Scaffold(
        topBar = {
            MenuPage(navController = navController)
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = "home"
            ) {
                composable("home") { HomePage(navController) }
                composable("roomList") { RoomListPage(navController) }
                composable("roomDetails/{roomId}") { backStackEntry ->
                    val roomId = backStackEntry.arguments?.getString("roomId")?.toLongOrNull()
                    if (roomId != null) {
                        RoomDetailsPage(roomId, navController)
                    } else {
                        Log.e("AppNavigation", "Room Id is null or invalid")
                    }
                }

            }
        }
    }
}
