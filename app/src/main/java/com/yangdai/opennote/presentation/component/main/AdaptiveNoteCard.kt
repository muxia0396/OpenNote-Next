package com.yangdai.opennote.presentation.component.main

import android.icu.text.DateFormat
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.expandIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.yangdai.opennote.R
import com.yangdai.opennote.data.local.entity.NoteEntity
import com.yangdai.opennote.presentation.util.parseMarkdownContent

private object NoteCardDefaults {
    const val MAX_PREVIEW_CHARACTERS = 6_000
    val MIN_INTERACTION_HEIGHT = 48.dp
    val CARD_START_PADDING = 16.dp
    val ICON_SIZE = 8.dp
    val CHECKBOX_PADDING = 10.dp
    val HEADER_BOTTOM_PADDING = 8.dp
    val HEADER_TOP_PADDING = 4.dp
    val HORIZONTAL_PADDING = 10.dp
}

@Composable
private fun NoteCardHeader(
    formattedTimestamp: String,
    isStandard: Boolean,
    isEditMode: Boolean,
    isNoteSelected: Boolean,
    colorScheme: ColorScheme = MaterialTheme.colorScheme,
    typography: Typography = MaterialTheme.typography
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                bottom = NoteCardDefaults.HEADER_BOTTOM_PADDING,
                top = NoteCardDefaults.HEADER_TOP_PADDING
            )
    ) {
        Canvas(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(NoteCardDefaults.ICON_SIZE)
        ) {
            drawCircle(
                color = colorScheme.primary,
                radius = size.minDimension / 2
            )
        }

        BasicText(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = NoteCardDefaults.CARD_START_PADDING),
            text = formattedTimestamp,
            style = typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = ColorProducer { colorScheme.onSurfaceVariant }
        )

        if (isEditMode) {
            Checkbox(
                checked = isNoteSelected,
                onCheckedChange = null,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        } else {
            BasicText(
                modifier = Modifier.align(Alignment.CenterEnd),
                text = stringResource(if (isStandard) R.string.standard_mode else R.string.lite_mode),
                style = typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = ColorProducer { colorScheme.onSurfaceVariant }
            )
        }
    }
}

@Composable
private fun NoteCardContent(
    displayedNote: NoteEntity,
    contentTextOverflow: TextOverflow,
    contentMaxLines: Int,
    isRaw: Boolean,
    searchQuery: String,
    colorScheme: ColorScheme = MaterialTheme.colorScheme
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = NoteCardDefaults.HEADER_BOTTOM_PADDING,
                horizontal = NoteCardDefaults.HORIZONTAL_PADDING
            )
    ) {
        if (displayedNote.title.isNotEmpty()) {
            BasicText(
                modifier = Modifier.basicMarquee(),
                text = remember(displayedNote.title, searchQuery) {
                    highlightSearchMatches(displayedNote.title, searchQuery)
                },
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                color = ColorProducer { colorScheme.onSurface }
            )

            if (displayedNote.content.isNotEmpty()) {
                Spacer(modifier = Modifier.height(NoteCardDefaults.HEADER_BOTTOM_PADDING))
            }
        }

        val previewContent = remember(displayedNote.content, searchQuery) {
            if (searchQuery.isBlank()) {
                displayedNote.content.take(NoteCardDefaults.MAX_PREVIEW_CHARACTERS)
            } else {
                extractMatchingParagraph(displayedNote.content, searchQuery)
            }
        }
        val annotatedString = remember(previewContent, isRaw, searchQuery) {
            if (searchQuery.isNotBlank()) highlightSearchMatches(previewContent, searchQuery)
            else if (!isRaw) parseMarkdownContent(previewContent)
            else AnnotatedString(previewContent)
        }

        BasicText(
            text = annotatedString,
            style = MaterialTheme.typography.bodyMedium,
            overflow = contentTextOverflow,
            maxLines = contentMaxLines,
            color = ColorProducer { colorScheme.onSurfaceVariant }
        )
    }
}

private fun extractMatchingParagraph(content: String, query: String): String {
    val matchIndex = content.indexOf(query, ignoreCase = true)
    if (matchIndex < 0) return content.take(1_600)

    val paragraphStart = content.lastIndexOf('\n', matchIndex - 1)
        .let { if (it < 0) 0 else it + 1 }
    val paragraphEnd = content.indexOf('\n', matchIndex + query.length)
        .let { if (it < 0) content.length else it }
    val paragraph = content.substring(paragraphStart, paragraphEnd).trim()
    if (paragraph.length <= 1_200) return paragraph

    val relativeMatch = matchIndex - paragraphStart
    val start = (relativeMatch - 450).coerceIn(0, paragraph.length)
    val end = (relativeMatch + query.length + 700).coerceAtMost(paragraph.length)
    return buildString {
        if (start > 0) append('…')
        append(paragraph.substring(start, end))
        if (end < paragraph.length) append('…')
    }
}

private fun highlightSearchMatches(text: String, query: String): AnnotatedString {
    if (query.isBlank()) return AnnotatedString(text)
    return AnnotatedString.Builder(text).apply {
        var start = text.indexOf(query, ignoreCase = true)
        while (start >= 0) {
            addStyle(
                SpanStyle(
                    background = androidx.compose.ui.graphics.Color(0xFFFFFF00),
                    color = androidx.compose.ui.graphics.Color.Black
                ),
                start,
                start + query.length
            )
            start = text.indexOf(query, start + query.length, ignoreCase = true)
        }
    }.toAnnotatedString()
}

@Composable
private fun NoteCardFooter(
    formattedTimestamp: String,
    isEditMode: Boolean,
    isNoteSelected: Boolean,
    colorScheme: ColorScheme = MaterialTheme.colorScheme,
    typography: Typography = MaterialTheme.typography
) {
    Surface(
        color = colorScheme.surface,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = NoteCardDefaults.MIN_INTERACTION_HEIGHT)
                .padding(horizontal = NoteCardDefaults.HORIZONTAL_PADDING)
        ) {
            BasicText(
                modifier = Modifier.align(Alignment.CenterStart),
                text = formattedTimestamp,
                style = typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = ColorProducer { colorScheme.onSurfaceVariant }
            )
            if (isEditMode) {
                Checkbox(
                    checked = isNoteSelected,
                    onCheckedChange = null,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }
    }
}

@Composable
fun AdaptiveNoteCard(
    modifier: Modifier = Modifier,
    isListView: Boolean,
    displayedNote: NoteEntity,
    dateFormatter: DateFormat,
    displayTimestamp: Long = displayedNote.timestamp,
    searchQuery: String = "",
    contentMaxLines: Int,
    contentTextOverflow: TextOverflow,
    isRaw: Boolean,
    isEditMode: Boolean,
    isNoteSelected: Boolean,
    onSelectNote: (NoteEntity) -> Unit,
    onEditModeChange: (Boolean) -> Unit
) = Column(modifier) {
    AnimatedVisibility(
        visible = isListView,
        enter = expandIn(expandFrom = Alignment.CenterStart),
        exit = ExitTransition.None
    ) {
        NoteCardHeader(
            formattedTimestamp = dateFormatter.format(displayTimestamp),
            isStandard = displayedNote.isMarkdown,
            isEditMode = isEditMode,
            isNoteSelected = isNoteSelected
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (isListView) Modifier.padding(start = NoteCardDefaults.CARD_START_PADDING) else Modifier),
        colors = if (isNoteSelected) CardDefaults.outlinedCardColors() else CardDefaults.elevatedCardColors(),
        border = if (isNoteSelected) CardDefaults.outlinedCardBorder() else null
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = NoteCardDefaults.MIN_INTERACTION_HEIGHT)
                .combinedClickable(
                    onLongClick = { onEditModeChange(true) },
                    onClick = { onSelectNote(displayedNote) }
                )
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawWithContent {
                            drawContent()
                            if (isNoteSelected) drawRect(Color.Gray.copy(alpha = 0.24f))
                        }
                ) {
                    NoteCardContent(
                        displayedNote = displayedNote,
                        contentTextOverflow = contentTextOverflow,
                        contentMaxLines = contentMaxLines,
                        isRaw = isRaw,
                        searchQuery = searchQuery
                    )
                }
                if (!isListView) {
                    NoteCardFooter(
                        formattedTimestamp = dateFormatter.format(displayTimestamp),
                        isEditMode = isEditMode,
                        isNoteSelected = isNoteSelected
                    )
                }
            }
        }
    }
}
