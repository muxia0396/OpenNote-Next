package com.yangdai.opennote.presentation.state

import androidx.compose.runtime.Stable

@Stable
data class ContentLoadState(
    val loading: Boolean = false,
    val progress: Float = 0f
)
