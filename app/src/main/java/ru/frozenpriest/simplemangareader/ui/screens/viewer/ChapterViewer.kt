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
import ru.frozenpriest.simplemangareader.ui.components.rememberState
import ru.frozenpriest.simplemangareader.ui.screens.viewer.ChapterViewerOrientation.Horizontal
import ru.frozenpriest.simplemangareader.ui.screens.viewer.ChapterViewerOrientation.Webtoon
import timber.log.Timber

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
                    painter = painter
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
    painter: Painter
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
                .fillMaxWidth()
                .graphicsLayer {
                    translationX = offset.x
                    translationY = offset.y

                    scaleX = scaleAnim
                    scaleY = scaleAnim
                }
                ,
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