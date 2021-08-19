package ru.frozenpriest.simplemangareader.ui.screens.viewer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import ru.frozenpriest.simplemangareader.ui.components.rememberState
import ru.frozenpriest.simplemangareader.ui.screens.viewer.ChapterViewerOrientation.Horizontal
import ru.frozenpriest.simplemangareader.ui.screens.viewer.ChapterViewerOrientation.Webtoon

@Composable
fun ChapterViewer(
    chapterId: String,
    viewModel: ChapterViewerViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = "load") {
        viewModel.loadChapterImages(chapterId)
    }
    val chapters = viewModel.chapters.collectAsState()
    val loading = viewModel.loading.collectAsState()
    if (loading.value) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                Modifier
                    .scale(0.5f)
            )
        }
    } else {
        var orientation by rememberState(value = Webtoon)
        Scaffold(
            floatingActionButton = {
                IconButton(
                    onClick = {
                        orientation = if (orientation == Webtoon) Horizontal else Webtoon
                    }
                ) {
                    Icon(imageVector = Icons.Default.Landscape, contentDescription = "Toggle type")
                }
            }
        ) {
            Reader(orientation = orientation, chapters.value)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Reader(
    orientation: ChapterViewerOrientation,
    items: List<String>,
) {
    when (orientation) {
        Webtoon -> {
            val listState = rememberLazyListState(initialFirstVisibleItemIndex = 1)
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .requiredHeight(100.dp),
                        contentAlignment = Alignment.Center
                    )
                    {
                        Text(text = "To prev chapter")
                    }
                }
                items(items) { chapterLink ->
                    ChapterPage(chapterLink)
                }
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .requiredHeight(100.dp),
                        contentAlignment = Alignment.Center
                    )
                    {
                        Text(text = "To next chapter")
                    }
                }
            }
        }
        Horizontal -> {
            val pagerState = rememberPagerState(
                pageCount = items.size + 2,
                initialPage = 1,
                initialOffscreenLimit = 3
            )
            HorizontalPager(state = pagerState) { page ->
                when (page) {
                    0 -> {
                        Text(text = "To prev chapter")
                    }
                    pagerState.pageCount - 1 -> {
                        Text(text = "To next chapter")
                    }
                    else -> {
                        val chapterLink = items[page - 1]
                        ChapterPage(chapterLink)
                    }
                }
            }
        }
    }
}

@Composable
private fun ChapterPage(chapterLink: String) {
    val painter = rememberCoilPainter(
        request = chapterLink,
        fadeIn = true
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .requiredHeightIn(min = 400.dp),
        contentAlignment = Alignment.Center
    ) {
        when (painter.loadState) {
            is ImageLoadState.Empty,
            is ImageLoadState.Loading -> {
                CircularProgressIndicator(
                    Modifier
                        .scale(0.5f)
                )
            }
            is ImageLoadState.Success -> {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = painter,
                    contentScale = ContentScale.FillWidth,
                    contentDescription = "Chapter page"
                )
            }
            is ImageLoadState.Error -> {
                TextButton(onClick = { /*TODO*/ }) {
                    Text(text = "Retry")
                }
            }
        }
    }
}

enum class ChapterViewerOrientation {
    Webtoon,
    Horizontal
}