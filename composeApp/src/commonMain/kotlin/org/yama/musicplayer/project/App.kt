package org.yama.musicplayer.project

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch

data class Song(val title: String, val artist: String, val uri: String)

@Composable
@Preview
fun App() {
    val musicPlayer = remember { MusicPlayer(getPlatform()) }
    var songs by remember { mutableStateOf(emptyList<Song>()) }
    var currentSong by remember { mutableStateOf<Song?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            songs = loadMusic()
        }
    }

    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            SongList(
                songs = songs,
                onSongClick = { song ->
                    currentSong = song
                    musicPlayer.play(song)
                },
                modifier = Modifier.weight(1f)
            )
            PlayerControls(
                onPlayPauseClick = {
                    if (currentSong != null) {
                        musicPlayer.pause()
                    }
                },
                onStopClick = {
                    musicPlayer.stop()
                    currentSong = null
                }
            )
        }
    }
}

@Composable
fun SongList(songs: List<Song>, onSongClick: (Song) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(songs) { song ->
            Text(
                text = "${song.title} - ${song.artist}",
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { onSongClick(song) }
            )
        }
    }
}

@Composable
fun PlayerControls(onPlayPauseClick: () -> Unit, onStopClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(modifier = modifier.padding(16.dp)) {
        Button(onClick = onPlayPauseClick) {
            Text("Play/Pause")
        }
        Button(onClick = onStopClick) {
            Text("Stop")
        }
    }
}