package ru.frozenpriest.simplemangareader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.frozenpriest.simplemangareader.ui.Screen
import ru.frozenpriest.simplemangareader.ui.screens.library.LibraryScreen
import ru.frozenpriest.simplemangareader.ui.theme.SimpleMangaReaderTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val items = listOf(
        Screen.Library,
        Screen.Explore,
    )


    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleMangaReaderTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()


                Scaffold(
                    bottomBar = {
                        BottomNavigation {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentRoute = navBackStackEntry?.destination?.route
                            items.forEach { screen ->
                                BottomNavigationItem(
                                    icon = {
                                        Icon(
                                            screen.icon,
                                            stringResource(id = screen.resourceId)
                                        )
                                    },
                                    label = { Text(stringResource(screen.resourceId)) },
                                    selected = currentRoute == screen.route,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.startDestinationRoute!!) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) {

                    NavHost(navController, startDestination = Screen.Library.route) {
                        composable(Screen.Library.route) { LibraryScreen(navController) }
                        composable(Screen.Explore.route) { /*FriendsList(navController) */ }
                    }
                }
            }
        }
    }
}