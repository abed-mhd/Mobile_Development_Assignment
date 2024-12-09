package com.example.abdelazizmohamad_assignment

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.abdelazizmohamad_assignment.navigation.AppNavigation
import com.example.abdelazizmohamad_assignment.ui.theme.AbdelazizMohamad_AssignmentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AbdelazizMohamad_AssignmentTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun Greeting(name: String, onClick: () -> Unit,  modifier: Modifier = Modifier) {
    Column {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
        Button(onClick = onClick) {
            Text(
                text = "My first button"
            )
        }
    }
}

