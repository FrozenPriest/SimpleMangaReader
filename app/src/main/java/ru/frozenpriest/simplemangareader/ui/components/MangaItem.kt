package ru.frozenpriest.simplemangareader.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.rememberCoilPainter
import ru.frozenpriest.simplemangareader.R
import ru.frozenpriest.simplemangareader.data.models.Manga
import ru.frozenpriest.simplemangareader.ui.theme.SimpleMangaReaderTheme

@Preview
@Composable
private fun MPreview() {
    SimpleMangaReaderTheme {
        MangaItem(manga = Manga("Test mange", "link"), onClick = {})
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MangaItem(
    manga: Manga,
    onClick: () -> Unit
) {
    val sizeImage = remember { mutableStateOf(IntSize.Zero) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.70f)
            .padding(all = 4.dp),
        elevation = 16.dp,
        onClick = { onClick() }
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                modifier = Modifier
                    .onGloballyPositioned {
                        sizeImage.value = it.size
                    },
                painter = rememberCoilPainter(
                    request = manga.posterLink,
                    previewPlaceholder = R.drawable.placeholder,
                    fadeIn = true
                ),
                contentDescription = manga.name,
                contentScale = ContentScale.FillWidth
            )
            ShadowGradientBox(sizeImage)
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 4.dp),
                text = manga.name ?:"No name",
                color = Color.White
            )

        }
    }
}

@Composable
private fun BoxScope.ShadowGradientBox(sizeImage: MutableState<IntSize>) {
    Box(
        modifier = Modifier.Companion
            .matchParentSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black),
                    startY = sizeImage.value.height.toFloat() / 3,  // 1/3
                    endY = sizeImage.value.height.toFloat()
                )
            )
    )
}

