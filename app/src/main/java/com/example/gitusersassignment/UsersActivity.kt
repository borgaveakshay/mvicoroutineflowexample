package com.example.gitusersassignment

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.NavType.ParcelableType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gitusersassignment.ui.theme.GitUsersAssignmentTheme
import com.example.gitusersassignment.usersearchscreen.datamodels.UserDetailViewModel
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
                NavHost(navController = navController, startDestination = "user_screen") {
                    composable("user_screen") {
                        UserScreenComponent(navController = navController)
                    }
                    composable(
                        "user_details",
                        arguments = listOf(navArgument("userName") {
                            type = NavType.StringType
                            this.nullable = true
                            this.defaultValue = null
                        },
                            navArgument("userDetailsModel") {
                                type = ParcelableType(UserDetailViewModel::class.java)
                                this.nullable = true
                                this.defaultValue = null
                            }
                        )
                    ) { navEntry ->
                        val userDetailViewModel =
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                navEntry.arguments?.getParcelable(
                                    "userDetailsModel",
                                    UserDetailViewModel::class.java
                                )
                            } else navEntry.arguments?.getParcelable("userDetailsModel")
                        UserDetailsScreen(
                            searchQuery = navEntry.arguments?.getString("userName"),
                            userDetailViewModel = userDetailViewModel
                        )
                    }
                }
            }
        }
    }
}