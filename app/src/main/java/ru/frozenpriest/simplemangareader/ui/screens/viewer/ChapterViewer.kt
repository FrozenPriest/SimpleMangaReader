package ru.frozenpriest.simplemangareader.ui.screens.viewer

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.*
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import ru.frozenpriest.simplemangareader.data.models.ChapterInfo
import ru.frozenpriest.simplemangareader.data.models.Manga
import ru.frozenpriest.simplemangareader.ui.components.rememberState
import ru.frozenpriest.simplemangareader.ui.screens.viewer.ChapterViewerOrientation.Horizontal
import ru.frozenpriest.simplemangareader.ui.screens.viewer.ChapterViewerOrientation.Webtoon
import timber.log.Timber

@Composable
fun ChapterViewer(
    manga: Manga,
    chapterInfo: ChapterInfo,
    viewModel: ChapterViewerViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = "load") {
        viewModel.loadChapterImages(chapterInfo.id)
    }
    val pages = viewModel.pages.collectAsState()
    val loading = viewModel.loading.collectAsState()
    var currentPage by rememberState(value = 1)
    if (loading.value) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                Modifier.scale(0.5f)
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
            var overlayVisible by rememberState(value = false)
            var targetPage by rememberState(value = 1)

            var scrollFinished by rememberState(value = true)
            scrollFinished = scrollFinished || (currentPage == targetPage)

            if(scrollFinished) {
                targetPage = currentPage
            }

            Box {
                Reader(
                    currentPage = currentPage,
                    targetPage = targetPage,
                    onPageChange = { newPage -> currentPage = newPage },
                    openCloseOverlay = { overlayVisible = !overlayVisible },
                    orientation = orientation,
                    items = pages.value
                )
                ViewerOverlay(
                    visible = overlayVisible,
                    mangaName = manga.name ?: "",
                    chapterName = chapterInfo.title,
                    currentPage = targetPage,
                    chapterSize = pages.value.size,
                    onPageChange = { newPage ->
                        targetPage = newPage
                        scrollFinished = false
                    },
                    previousChapter = { /*TODO*/ },
                    nextChapter = { /*TODO*/ },
                    back = {/*TODO*/}
                )
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Reader(
    currentPage: Int,
    targetPage: Int,
    onPageChange: (Int) -> Unit,
    openCloseOverlay: () -> Unit,
    orientation: ChapterViewerOrientation,
    items: List<String>,
) {
    when (orientation) {
        Webtoon -> {
            val listState = rememberLazyListState(initialFirstVisibleItemIndex = 1)

            if (listState.firstVisibleItemIndex != currentPage) {
                onPageChange(listState.firstVisibleItemIndex)
            }
            LaunchedEffect(key1 = targetPage) {
                listState.animateScrollToItem(targetPage)
            }

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
                    ChapterPage(
                        chapterLink = chapterLink,
                        openCloseOverlay = { openCloseOverlay() }
                    )
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

            if (pagerState.currentPage != currentPage) {
                onPageChange(pagerState.currentPage)
            }
            LaunchedEffect(key1 = targetPage) {
                pagerState.animateScrollToPage(targetPage)
            }

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
                        ChapterPage(
                            chapterLink = chapterLink,
                            openCloseOverlay = { openCloseOverlay() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChapterPage(
    chapterLink: String,
    openCloseOverlay: () -> Unit
) {
    val painter = rememberCoilPainter(
        request = chapterLink,
        fadeIn = true
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .requiredHeightIn(min = 100.dp),
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
                ZoomableImage(
                    modifier = Modifier.fillMaxWidth(),
                    painter = painter,
                    onClick = { openCloseOverlay() }
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

@Composable
fun ZoomableImage(
    modifier: Modifier = Modifier,
    painter: Painter,
    onClick: (() -> Unit) = {}
) {
    var centerPoint by remember { mutableStateOf(Offset.Zero) }

    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }


    val scaleAnim by animateFloatAsState(
        targetValue = scale
    ) {
        if (scale == 1f) offset = Offset.Zero
    }

    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale *= zoomChange
        scale = scale.coerceIn(1f, 5f)

        offset += offsetChange
        offset = clampOffset(centerPoint, offset, scale)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RectangleShape)
            .onGloballyPositioned { coordinates ->
                val size = coordinates.size.toSize() / 2.0f
                centerPoint = Offset(size.width, size.height)
            }
            .background(Color.Gray)
            .transformable(state)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        onClick()
                    },
                    onDoubleTap = {
                        Timber.e("Double tap working")
                        when {
                            scale > 2f -> {
                                scale = 1f
                            }
                            else -> {
                                scale = 3f

                                offset = (centerPoint - it) * (scale - 1)
                                offset = clampOffset(centerPoint, offset, scale)
                            }
                        }

                    }
                )
            }
            .scrollable(
                state = rememberScrollableState { delta ->
                    offset += Offset(delta, 0f)
                    val preClamp = offset
                    offset = clampOffset(centerPoint, offset, scale)

                    delta - (preClamp.x - offset.x)
                },
                orientation = Orientation.Horizontal
            )
            .scrollable(
                state = rememberScrollableState { delta ->
                    offset += Offset(0f, delta)
                    val preClamp = offset
                    offset = clampOffset(centerPoint, offset, scale)

                    delta - (preClamp.y - offset.y)
                },
                orientation = Orientation.Vertical
            )
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationX = offset.x
                    translationY = offset.y

                    scaleX = scaleAnim
                    scaleY = scaleAnim
                },
            painter = painter,
            contentScale = ContentScale.FillWidth,
            contentDescription = "Chapter page"
        )
    }
}

fun clampOffset(centerPoint: Offset, offset: Offset, scale: Float): Offset {
    val maxPosition = centerPoint * (scale - 1)

    return offset.copy(
        x = offset.x.coerceIn(-maxPosition.x, maxPosition.x),
        y = offset.y.coerceIn(-maxPosition.y, maxPosition.y)
    )
}


enum class ChapterViewerOrientation {
    Webtoon,
    Horizontal
}