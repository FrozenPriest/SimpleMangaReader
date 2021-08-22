package ru.frozenpriest.simplemangareader.ui.screens.viewer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.frozenpriest.simplemangareader.R
import ru.frozenpriest.simplemangareader.ui.components.rememberState
import ru.frozenpriest.simplemangareader.ui.theme.Purple200
import ru.frozenpriest.simplemangareader.ui.theme.SimpleMangaReaderTheme
import kotlin.math.roundToInt

@Composable
fun ViewerOverlay(
    //visible: Boolean
    mangaName: String,
    chapterName: String,
    currentPage: Int,
    chapterSize: Int,
    onPageChange: (Int) -> Unit,
    previousChapter: () -> Unit,
    nextChapter: () -> Unit,
    back: () -> Unit
) {
    var visible by rememberState(value = true)


    /*
    что должно быть:
        прогресс чтения с возможностью перехода
        название главы сверху
        следующая/предыдущая глава
        настройки
     */

    Box(modifier = Modifier.fillMaxSize()) {
        //Top
        TopBar(visible, back, mangaName, chapterName)

        Button(
            onClick = { visible = !visible },
            modifier = Modifier.align(Alignment.Center)
        ) {

        }
        //Bottom
        BottomBar(
            visible,
            previousChapter,
            currentPage,
            onPageChange,
            chapterSize,
            nextChapter
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun BoxScope.TopBar(
    visible: Boolean,
    back: () -> Unit,
    mangaName: String,
    chapterName: String
) {
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(),
        exit = shrinkVertically(),
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.TopCenter)
            .background(
                color = Purple200,
                shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 4.dp)

        ) {
            IconButton(onClick = { back() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
            Column {
                Text(text = mangaName, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = chapterName, fontSize = 14.sp, fontStyle = FontStyle.Italic)
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun BoxScope.BottomBar(
    visible: Boolean,
    previousChapter: () -> Unit,
    currentPage: Int,
    onPageChange: (Int) -> Unit,
    chapterSize: Int,
    nextChapter: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(expandFrom = Alignment.Bottom),
        exit = shrinkVertically(shrinkTowards = Alignment.Top),
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .background(
                color = Purple200,
                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
            )
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            SliderRow(previousChapter, currentPage, onPageChange, chapterSize, nextChapter)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun SliderRow(
    previousChapter: () -> Unit,
    currentPage: Int,
    onPageChange: (Int) -> Unit,
    chapterSize: Int,
    nextChapter: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically

    ) {
        IconButton(onClick = { previousChapter() }) {
            Icon(
                imageVector = Icons.Filled.ArrowLeft,
                contentDescription = stringResource(R.string.back),
                modifier = Modifier.weight(1f)
            )
        }
        Text(
            text = "$currentPage",
            fontSize = 12.sp,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Slider(
            modifier = Modifier.weight(5f),
            value = currentPage.toFloat(),
            onValueChange = { onPageChange(it.roundToInt()) },
            valueRange = 1f..chapterSize.toFloat(),
        )
        Text(
            text = "$chapterSize",
            fontSize = 12.sp,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        IconButton(onClick = { nextChapter() }) {
            Icon(
                imageVector = Icons.Filled.ArrowRight,
                contentDescription = stringResource(R.string.back),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview
@Composable
fun Test() {
    SimpleMangaReaderTheme {
        var page by rememberState(value = 1)
        ViewerOverlay(
            "The reincarnation magician",
            "Ch. 57 WTF",
            page,
            20,
            { page = it },
            {}, {}, {}
        )
    }
}