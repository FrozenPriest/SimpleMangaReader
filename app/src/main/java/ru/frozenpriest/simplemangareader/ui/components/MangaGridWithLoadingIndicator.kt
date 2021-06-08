package ru.frozenpriest.simplemangareader.ui.components

import android.os.Bundle
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.frozenpriest.simplemangareader.data.models.Manga
import timber.log.Timber

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MangaGridWithLoadingIndicator(
    navController: NavController,
    mangaList: List<Manga>,
    isLoading: Boolean,
    isLoadingMore: Boolean,
    lastItemReached: () -> Unit = {}
) {
    Surface(color = MaterialTheme.colors.background) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                BoxWithConstraints(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val nColumns = maxOf((maxWidth / 100.dp).toInt(), 1)
                    val state = rememberLazyListState()
                    if (state.layoutInfo.visibleItemsInfo.isNotEmpty()) {
                        val lastRowIndex = state.layoutInfo.visibleItemsInfo.last().index
                        if (lastRowIndex >= mangaList.size / nColumns)
                            lastItemReached()
                    }

                    LazyVerticalGrid(
                        cells = GridCells.Fixed(nColumns),
                        modifier = Modifier.fillMaxSize(),
                        state = state
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
                        if (isLoadingMore)
                            item {
                                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.scale(0.5f)
                                    )
                                }
                            }
                    }
                }
            }
        }
    }
}