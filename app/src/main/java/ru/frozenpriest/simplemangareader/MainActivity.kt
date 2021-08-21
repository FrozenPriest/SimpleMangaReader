package ru.frozenpriest.simplemangareader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.frozenpriest.simplemangareader.data.models.Manga
import ru.frozenpriest.simplemangareader.ui.Screen
import ru.frozenpriest.simplemangareader.ui.screens.details.MangaDetailsScreen
import ru.frozenpriest.simplemangareader.ui.screens.explore.ExploreScreen
import ru.frozenpriest.simplemangareader.ui.screens.library.LibraryScreen
import ru.frozenpriest.simplemangareader.ui.screens.login.LoginScreen
import ru.frozenpriest.simplemangareader.ui.screens.viewer.ChapterViewer
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
                var showBottomNavigation by remember {
                    mutableStateOf(true)
                }
                Scaffold(
                    bottomBar = {
                        if (showBottomNavigation)
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
                                                popUpTo(Screen.Library.route) {
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

                    NavHost(navController, startDestination = "login") {
                        composable("login") {
                            showBottomNavigation = false
                            LoginScreen(navController = navController)
                        }
                        composable(Screen.Library.route) {
                            showBottomNavigation = true
                            LibraryScreen(navController)
                        }
                        composable(Screen.Explore.route) {
                            showBottomNavigation = true
                            ExploreScreen(navController = navController)
                        }
                        composable("manga_details") {
                            showBottomNavigation = false
                            navController.previousBackStackEntry?.arguments?.getParcelable<Manga>("manga")
                                ?.let { manga ->
                                    MangaDetailsScreen(
                                        navController,
                                        manga
                                    )
                                }
                        }
                        composable("reader") {
                            showBottomNavigation = false
                            navController.previousBackStackEntry?.arguments?.getString("chapterId")
                                ?.let { chapterId ->
                                    ChapterViewer(
                                        chapterId
                                    )
                                }
                        }
                    }
                }
            }
        }
    }
}