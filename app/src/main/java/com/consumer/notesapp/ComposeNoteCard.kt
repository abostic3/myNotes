package com.consumer.notesapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.consumer.notesapp.ui.theme.NotesTheme

@Composable
fun NoteCard(note: Note, onClick: () -> Unit) {
    NotesTheme {
        Box(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()) {
            Card(
                shape = RoundedCornerShape(8.dp),
                elevation = 6.dp,
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxSize()
                    .clickable(onClick = onClick)
            ) {
                Column(modifier = Modifier
                    .background(MaterialTheme.colors.surface)
                    .padding(8.dp)) {
                    Text(
                        text = note.title ?: "(no title)",
                        style = MaterialTheme.typography.h6,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (note.isSecret) "(secret)" else (note.content ?: ""),
                        fontSize = 14.sp,
                        maxLines = 6,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}