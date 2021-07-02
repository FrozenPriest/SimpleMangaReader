package ru.frozenpriest.simplemangareader.ui.screens.explore

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.frozenpriest.simplemangareader.data.models.mangas
import ru.frozenpriest.simplemangareader.ui.components.MangaGridWithLoadingIndicator
import ru.frozenpriest.simplemangareader.ui.theme.SimpleMangaReaderTheme

@ExperimentalFoundationApi
@Preview
@Composable
private fun LPreview() {
    SimpleMangaReaderTheme(darkTheme = true) {
        MangaGridWithLoadingIndicator(navController = rememberNavController(), mangas, false, false)
    }
}


@Composable
fun ExploreScreen(
    navController: NavController,
    viewModel: ExploreViewModel = hiltViewModel()
) {
    val mangaList by remember {
        if (viewModel.mangaFeedList.value.isEmpty())
            viewModel.getMangas()
        viewModel.mangaFeedList
    }
    val isLoading = viewModel.isLoading.collectAsState()
    val isLoadingMore = viewModel.isLoadingMore.collectAsState()

    MangaGridWithLoadingIndicator(navController = navController,
        mangas = mangaList,
        isLoading.value,
        isLoadingMore.value,
        lastItemReached = { if (viewModel.canLoadMore()) viewModel.loadMore() }
    )
}



