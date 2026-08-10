package com.yangdai.opennote.presentation.component.setting

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Commit
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.yangdai.opennote.R
import com.yangdai.opennote.presentation.component.ConfettiEffect
import com.yangdai.opennote.presentation.component.CurlyCornerShape
import com.yangdai.opennote.presentation.util.rememberCustomTabsIntent

@Composable
fun AboutPane() {
    val context = LocalContext.current
    val customTabsIntent = rememberCustomTabsIntent()
    val packageInfo = remember(context.packageName) {
        context.packageManager.getPackageInfo(context.packageName, 0)
    }
    var pressAmplitude by remember { mutableFloatStateOf(16f) }
    val animatedAmplitude by animateFloatAsState(
        targetValue = pressAmplitude,
        label = "aboutIconAmplitude"
    )
    var showConfetti by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 16.dp)
                .size(240.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = CurlyCornerShape(curlAmplitude = animatedAmplitude.toDouble())
                )
                .shadow(
                    elevation = 10.dp,
                    shape = CurlyCornerShape(curlAmplitude = animatedAmplitude.toDouble()),
                    ambientColor = MaterialTheme.colorScheme.primaryContainer,
                    spotColor = MaterialTheme.colorScheme.primaryContainer
                )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            haptic.performHapticFeedback(HapticFeedbackType.ContextClick)
                            pressAmplitude = 0f
                            tryAwaitRelease()
                            haptic.performHapticFeedback(HapticFeedbackType.ContextClick)
                            pressAmplitude = 16f
                        },
                        onLongPress = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            showConfetti = true
                        }
                    )
                }
        ) {
            Image(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.TopCenter)
                    .padding(top = 20.dp),
                painter = painterResource(R.drawable.opennote_logo),
                colorFilter = ColorFilter.tint(Color.Black),
                contentDescription = stringResource(R.string.app_name)
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.version) + " " + packageInfo.versionName,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.padding(top = 8.dp))

        ListItem(
            modifier = Modifier
                .fillMaxWidth()
                .clip(CircleShape)
                .clickable {
                    customTabsIntent.launchUrl(
                        context,
                        "https://github.com/YangDai2003/OpenNote-Compose".toUri()
                    )
                },
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
            ),
            leadingContent = {
                Icon(
                    imageVector = Icons.Outlined.Commit,
                    contentDescription = null
                )
            },
            headlineContent = { Text(stringResource(R.string.source_code)) }
        )

        Spacer(Modifier.padding(top = 4.dp))

        ListItem(
            modifier = Modifier
                .fillMaxWidth()
                .clip(CircleShape)
                .clickable {
                    customTabsIntent.launchUrl(
                        context,
                        "https://github.com/muxia0396/OpenNote-Next".toUri()
                    )
                },
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
            ),
            leadingContent = {
                Icon(
                    imageVector = Icons.Outlined.Commit,
                    contentDescription = null
                )
            },
            headlineContent = { Text(stringResource(R.string.opennote_next_source_code)) }
        )

        Spacer(Modifier.weight(1f))

        Text(
            text = stringResource(R.string.opennote_derivative_notice),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.navigationBarsPadding())
    }

    if (showConfetti) ConfettiEffect()
}
