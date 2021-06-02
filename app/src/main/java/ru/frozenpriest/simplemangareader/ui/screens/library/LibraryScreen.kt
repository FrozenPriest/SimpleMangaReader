package ru.frozenpriest.simplemangareader.ui.screens.library

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.frozenpriest.simplemangareader.data.Manga
import ru.frozenpriest.simplemangareader.data.mangas
import ru.frozenpriest.simplemangareader.ui.components.MangaItem
import ru.frozenpriest.simplemangareader.ui.theme.SimpleMangaReaderTheme

@ExperimentalFoundationApi
@Preview
@Composable
private fun LPreview() {
    SimpleMangaReaderTheme(darkTheme = true) {
        Library(navController = rememberNavController(), mangas)
    }
}

@ExperimentalFoundationApi
@Composable
fun Library(
    navController: NavController,
    mangaList: List<Manga>
) {
    Surface(color = MaterialTheme.colors.background) {
        LazyVerticalGrid(
            cells = GridCells.Adaptive(100.dp),
        ) {
            items(items = mangaList) { manga ->
                MangaItem(manga = manga, { println("CLAKC")})
            }
        }
    }

}

