package ru.frozenpriest.simplemangareader.ui.screens.library

import android.os.Bundle
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.frozenpriest.simplemangareader.data.models.Manga
import ru.frozenpriest.simplemangareader.data.models.mangas
import ru.frozenpriest.simplemangareader.ui.components.MangaItem
import ru.frozenpriest.simplemangareader.ui.theme.SimpleMangaReaderTheme
import timber.log.Timber

@ExperimentalFoundationApi
@Preview
@Composable
private fun LPreview() {
    SimpleMangaReaderTheme(darkTheme = true) {
        Library(navController = rememberNavController(), mangas)
    }
}


@Composable
fun LibraryScreen(
    navController: NavController,
    viewModel: LibraryViewModel = hiltViewModel()
) {
    val mangaFeedList by remember {
        viewModel.getMangas()
        viewModel.mangaFeedList
    }

    Library(navController = navController, mangaList = mangaFeedList)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Library(
    navController: NavController,
    mangaList: List<Manga>
) {
    Surface(color = MaterialTheme.colors.background) {
        LazyVerticalGrid(
            cells = GridCells.Adaptive(100.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(items = mangaList) { manga ->
                Timber.e(manga.posterLink)
                MangaItem(manga = manga) {
                    navController.currentBackStackEntry?.arguments =
                        Bundle().apply {
                            putParcelable("manga", manga)
                        }
                    navController.navigate("manga_details")
                }
            }
        }
    }

}

