package com.example.gitusersassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gitusersassignment.ui.theme.GitUsersAssignmentTheme
import com.example.gitusersassignment.usersearchscreen.ui.UserDetailsScreen
import com.example.gitusersassignment.usersearchscreen.ui.UserScreenComponent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GitUsersAssignmentTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "Users") {
                    composable("Users") {
                        UserScreenComponent(navController = navController)
                    }
                    composable(
                        "Users/{userName}",
                        arguments = listOf(navArgument("userName") {
                            type = NavType.StringType
                        })
                    ) { navEntry ->
                        navEntry.arguments?.let {
                            UserDetailsScreen(
                                searchQuery = it.getString("userName", ""),
                            )
                        }
                    }
                }
            }
        }
    }
}