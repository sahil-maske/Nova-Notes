package com.sahil.novanotes.uiNotes



import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sahil.novanotes.data.Note
import com.sahil.novanotes.ui.theme.NovaNotesTheme
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen(
    existingNote: Note? = null,
    onSave: (Note) -> Unit,
    onDelete: (Note) -> Unit,
    onBack: () -> Unit
) {
    var title by remember(existingNote) { mutableStateOf(existingNote?.title ?: "") }
    var heading by remember(existingNote) { mutableStateOf(existingNote?.heading ?: "") }
    var content by remember(existingNote) { mutableStateOf(existingNote?.content ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (existingNote == null) "New Note" else "  Edit Note",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack,
                    modifier= Modifier.size(82.dp)
                    ) {
                       Icon(
                           Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                           contentDescription = "Back",
                           modifier = Modifier.size(32.dp)
                       )
                    }
                },
                actions = {
                    if (existingNote != null) {
                        TextButton(onClick = { onDelete(existingNote) }) {
                            Text(
                                "Delete",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                            )
                        }
                    }
                    TextButton(onClick = {
                        val date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
                        val note = Note(
                            id = existingNote?.id ?: 0,
                            title = title,
                            heading = heading,
                            content = content,
                            date = date
                        )
                        onSave(note)
                    }) {
                        Text(
                            "Save",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title (optional)") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = heading,
                onValueChange = { heading = it },
                label = { Text("Heading (optional)") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.SansSerif
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Note") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = FontFamily.SansSerif
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditNoteScreenPreviewNew() {
    NovaNotesTheme {
        EditNoteScreen(
            existingNote = null,
            onSave = {},
            onDelete = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditNoteScreenPreviewEdit() {
    NovaNotesTheme {
        EditNoteScreen(
            existingNote = Note(
                id = 1,
                title = "Project Idea",
                heading = "Nova Notes App",
                content = "Build a notes app with Jetpack Compose and Room.",
                date = "28 Oct 2024"
            ),
            onSave = {},
            onDelete = {},
            onBack = {}
        )
    }
}
