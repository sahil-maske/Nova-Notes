package com.sahil.novanotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sahil.novanotes.data.Note
import com.sahil.novanotes.data.NoteDatabase
import com.sahil.novanotes.repository.NoteRepository
import com.sahil.novanotes.ui.theme.NovaNotesTheme
import com.sahil.novanotes.uiNotes.EditNoteScreen
import com.sahil.novanotes.uiNotes.HomeScreen
import com.sahil.novanotes.viewmodel.NoteViewModel
import com.sahil.novanotes.viewmodel.NoteViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NovaNotesTheme {
                val navController = rememberNavController()
                val context = LocalContext.current
                val db = NoteDatabase.getDatabase(context)
                val repository = NoteRepository(db.noteDao())
                val viewModel: NoteViewModel = viewModel(
                    factory = NoteViewModelFactory(repository)
                )

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        val notes by viewModel.allNotes.collectAsState(initial = emptyList())
                        HomeScreen(
                            notes = notes,
                            onNoteClick = { note ->
                                navController.navigate("edit/${note.id}")
                            },
                            onAddClick = {
                                navController.navigate("edit/-1")
                            },
                            onDeleteNote = { note ->
                                viewModel.deleteNote(note)
                            }
                        )
                    }
                    composable("edit/{noteId}") { backStackEntry ->
                        val noteId = backStackEntry.arguments?.getString("noteId")?.toInt() ?: -1
                        val note by produceState<Note?>(initialValue = null, key1 = noteId) {
                            value = if (noteId == -1) null else viewModel.getNoteById(noteId)
                        }

                        if (noteId != -1 && note == null) {
                            // Show loading while fetching the note
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                                androidx.compose.material3.CircularProgressIndicator()
                            }
                        } else {
                            EditNoteScreen(
                                existingNote = note,
                                onSave = { viewModel.insertNote(it); navController.popBackStack() },
                                onDelete = { viewModel.deleteNote(it); navController.popBackStack() },
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NovaNotesTheme {
        HomeScreen(
            notes = listOf(
                Note(id = 1, title = "Note 1", content = "Content 1"),
                Note(id = 2, title = "Note 2", content = "Content 2")
            ),
            onNoteClick = {},
            onAddClick = {},
            onDeleteNote = {}
        )
    }
}

