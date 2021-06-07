package ru.frozenpriest.simplemangareader.ui.screens.details

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import ru.frozenpriest.simplemangareader.data.models.Manga

/**
 * Shows details of manga
 * @param manga - manga to be displayed
 * @param navController - navigation controller
 */
@Composable
fun MangaDetailsScreen(
    navController: NavController,
    manga: Manga
) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "MANGA = $manga")
    }
}