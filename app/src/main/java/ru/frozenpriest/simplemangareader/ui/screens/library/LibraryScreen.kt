package ru.frozenpriest.simplemangareader.ui.screens.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ru.frozenpriest.simplemangareader.ui.components.MangaGridWithLoadingIndicator

@Composable
fun LibraryScreen(
    navController: NavController,
    viewModel: LibraryViewModel = hiltViewModel()
) {
    val mangaFeedList by remember {
        if(viewModel.mangaFeedList.value.isEmpty())
            viewModel.getMangas()
        viewModel.mangaFeedList
    }
    val isLoading = viewModel.isLoading.collectAsState()
    val isLoadingMore = viewModel.isLoadingMore.collectAsState()

    MangaGridWithLoadingIndicator(
        navController = navController,
        mangaList = mangaFeedList,
        isLoading.value,
        isLoadingMore.value,
        lastItemReached = { if (viewModel.canLoadMore()) viewModel.loadMore() }
    )
}
