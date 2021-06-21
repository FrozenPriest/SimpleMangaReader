package ru.frozenpriest.simplemangareader.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun <T>  rememberState(value: T): MutableState<T> {
    return remember {
        mutableStateOf(value)
    }
}