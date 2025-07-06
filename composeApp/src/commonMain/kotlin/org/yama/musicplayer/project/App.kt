package org.yama.musicplayer.project

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

data class Song(val title: String, val artist: String, val uri: String)

@Composable
@Preview
fun App(hasStoragePermission: StateFlow<Boolean>) {
    val musicPlayer = remember { MusicPlayer(getPlatform()) }
    var songs by remember { mutableStateOf(emptyList<Song>()) }
    var currentSong by remember { mutableStateOf<Song?>(null) }
    val coroutineScope = rememberCoroutineScope()

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Search", "Play", "Settings")

    val permissionGranted by hasStoragePermission.collectAsState()

    LaunchedEffect(permissionGranted) {
        if (permissionGranted) {
            coroutineScope.launch {
                songs = loadMusic()
            }
        }
    }

    MaterialTheme {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    tabs.forEachIndexed { index, title ->
                        NavigationBarItem(
                            icon = {
                                when (title) {
                                    "Search" -> Icon(Icons.Filled.Search, contentDescription = title)
                                    "Play" -> Icon(Icons.Filled.PlayArrow, contentDescription = title)
                                    "Settings" -> Icon(Icons.Filled.Settings, contentDescription = title)
                                }
                            },
                            label = { Text(title) },
                            selected = selectedTab == index,
                            onClick = {
                                selectedTab = index
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                if (!permissionGranted) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("ストレージへのアクセス許可が必要です。", modifier = Modifier.padding(16.dp))
                        Text("設定から権限を許可してください。", modifier = Modifier.padding(horizontal = 16.dp))
                    }
                } else {
                    when (tabs[selectedTab]) {
                        "Search" -> SearchScreen(songs, onSongClick = { song ->
                            currentSong = song
                            musicPlayer.play(song)
                        })
                        "Play" -> PlayScreen(musicPlayer, currentSong)
                        "Settings" -> SettingsScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun SearchScreen(songs: List<Song>, onSongClick: (Song) -> Unit) {
    var searchText by remember { mutableStateOf("") }
    val dummyPlaylist = remember { // ダミーの再生リストデータ
        listOf(
            Song("Dummy Song 1", "Dummy Artist 1", ""),
            Song("Dummy Song 2", "Dummy Artist 2", ""),
            Song("Dummy Song 3", "Dummy Artist 3", "")
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search music") },
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )

        // 再生リストセクション
        Text(
            text = "再生リスト",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyColumn(
            modifier = Modifier.weight(0.5f) // 画面の半分を占めるように調整
        ) {
            items(dummyPlaylist) { song ->
                Text(
                    text = "${song.title} - ${song.artist}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* ダミーなので何もしない */ }
                        .padding(16.dp)
                )
            }
        }

        // デバイス内の音楽リストセクション
        Text(
            text = "デバイス内の音楽リスト",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        SongList(
            songs = songs,
            onSongClick = onSongClick,
            modifier = Modifier.weight(1f) // 残りのスペースを占めるように調整
        )
    }
}

@Composable
fun PlayScreen(musicPlayer: MusicPlayer, currentSong: Song?) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = currentSong?.title ?: "No song playing", style = MaterialTheme.typography.headlineMedium)
        Text(text = currentSong?.artist ?: "", style = MaterialTheme.typography.bodyMedium)
        Row(modifier = Modifier.padding(top = 16.dp)) {
            Button(onClick = { musicPlayer.pause() }) {
                Text(if (musicPlayer.isPlaying()) "Pause" else "Play")
            }
            Button(onClick = { musicPlayer.stop() }, modifier = Modifier.padding(start = 8.dp)) {
                Text("Stop")
            }
        }
    }
}

@Composable
fun SettingsScreen() {
    var appVersion by remember { mutableStateOf("Unknown") }
    val platform = getPlatform()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            appVersion = platform.getAppVersion()
        }) {
            Text("Show Version")
        }
        Text(text = "App Version: $appVersion", modifier = Modifier.padding(top = 16.dp))
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
