package ru.frozenpriest.simplemangareader.ui.screens.details

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.frozenpriest.simplemangareader.data.models.Manga
import ru.frozenpriest.simplemangareader.data.models.mangas
import ru.frozenpriest.simplemangareader.ui.theme.SimpleMangaReaderTheme

/**
 * Shows details of manga
 * @param manga - manga to be displayed
 * @param navController - navigation controller
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MangaDetailsScreen(
    navController: NavController,
    manga: Manga,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "MANGA = $manga")
    }
}

@Preview
@Composable
private fun Preview() {
    SimpleMangaReaderTheme {
        MangaDetailsScreen(navController = rememberNavController(), manga = mangas.first())
    }
}