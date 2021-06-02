package ru.frozenpriest.simplemangareader.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import ru.frozenpriest.simplemangareader.R

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Library : Screen("library", R.string.library, Icons.Filled.LibraryBooks)
    object Explore : Screen("explore", R.string.explore, Icons.Filled.Search)
}

