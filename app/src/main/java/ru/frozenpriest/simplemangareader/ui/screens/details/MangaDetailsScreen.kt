package ru.frozenpriest.simplemangareader.ui.screens.details

import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.frozenpriest.simplemangareader.data.models.Manga
import ru.frozenpriest.simplemangareader.data.models.mangas
import ru.frozenpriest.simplemangareader.ui.theme.SimpleMangaReaderTheme
import timber.log.Timber

/**
 * Shows details of manga
 * @param manga - manga to be displayed
 * @param navController - navigation controller
 */
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
        Column() {
            Text(text = "MANGA = $manga")
            Chapters(navController, viewModel, manga)
        }
    }
}


@Composable
private fun Chapters(
    navController: NavController,
    viewModel: DetailsViewModel,
    manga: Manga
) {
    LaunchedEffect(key1 = "loadChapters") {
        viewModel.loadChapters(manga.id)
    }
    Text(text = "Chapters")
    val chapters = viewModel.chapters.collectAsState()
    Timber.e(chapters.value.toString())
    LazyColumn() {
        items(chapters.value) { item ->
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        navController.currentBackStackEntry?.arguments =
                            Bundle().apply {
                                putString("chapterId", item.id)
                            }
                        navController.navigate("reader")
                    }
                ) {
                    Text(text = "title = ${item.title}, chapter = ${item.chapter}")
                }
            }
        }
    }

}

@Preview
@Composable
private fun Preview() {
    SimpleMangaReaderTheme {
        MangaDetailsScreen(navController = rememberNavController(), manga = mangas.first())
    }
}