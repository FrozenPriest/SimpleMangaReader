package ru.frozenpriest.simplemangareader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.frozenpriest.simplemangareader.data.models.ChapterInfo
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


    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            SimpleMangaReaderTheme {
                val navController = rememberAnimatedNavController()
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

                    AnimatedNavHost(navController, startDestination = "login") {
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
                        composable(
                            route = "manga_details",
                            enterTransition = { initial, _ ->
                                if (initial.destination.route != "reader")
                                    slideIntoContainer(
                                        AnimatedContentScope.SlideDirection.Up,
                                        animationSpec = tween(700)
                                    )
                                else
                                    null
                            },
                            popEnterTransition = { initial, _ ->
                                if (initial.destination.route != "reader")
                                    slideIntoContainer(
                                        AnimatedContentScope.SlideDirection.Up,
                                        animationSpec = tween(700)
                                    )
                                else
                                    null
                            },
                            popExitTransition = { _, _ ->
                                slideOutOfContainer(
                                    AnimatedContentScope.SlideDirection.Down,
                                    animationSpec = tween(700)
                                )
                            }
                        ) {
                            showBottomNavigation = false
                            navController.previousBackStackEntry?.arguments?.getParcelable<Manga>("manga")
                                ?.let { manga ->
                                    MangaDetailsScreen(
                                        navController,
                                        manga
                                    )
                                }
                        }
                        composable("reader",
                            enterTransition = { initial, _ ->
                                slideIntoContainer(
                                    AnimatedContentScope.SlideDirection.Left,
                                    animationSpec = tween(700)
                                )
                            },
                            popExitTransition = { _, _ ->
                                slideOutOfContainer(
                                    AnimatedContentScope.SlideDirection.Right,
                                    animationSpec = tween(700)
                                )
                            },
                        ) {
                            showBottomNavigation = false

                            val bundle = navController.previousBackStackEntry?.arguments
                            val chapterInfo = bundle?.getParcelable<ChapterInfo>("chapterInfo")
                            val manga = bundle?.getParcelable<Manga>("manga")
                            chapterInfo?.let {
                                manga?.let {
                                    ChapterViewer(manga, chapterInfo)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}