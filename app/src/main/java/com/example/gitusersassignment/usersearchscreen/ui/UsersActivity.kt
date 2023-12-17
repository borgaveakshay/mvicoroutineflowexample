package com.example.gitusersassignment.usersearchscreen.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.gitusersassignment.ui.theme.GitUsersAssignmentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GitUsersAssignmentTheme {
                // A surface container using the 'background' color from the theme
                UserScreenComponent()
            }
        }
    }
}