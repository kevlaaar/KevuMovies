package com.kevlaaar.kevumovies.core.ui.mvi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest


@Composable
fun<T: UiEffect> CollectEffects(
    effects: Flow<T>,
    onEffect: suspend (T) -> Unit
) {
    LaunchedEffect(effects) {
        effects.collectLatest { effects ->
            onEffect(effects)
        }
    }
}