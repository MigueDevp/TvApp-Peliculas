// VideoListScreen.kt
package com.example.netflix

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp



@Composable

fun VideoListScreen() {
    LocalContext.current

    // Lista de videos
    val videoList = listOf(
        VideoItem("Video 1", R.raw.avion),
        VideoItem("Video 2", R.raw.diegoapp),
        VideoItem("Video 3", R.raw.frankvideo),
        VideoItem("Video 4", R.raw.bikker),
        VideoItem("Video 5", R.raw.frankjuego),
        VideoItem("Video 6", R.raw.graduados),
        VideoItem("Video 7", R.raw.manchitas),
        VideoItem("Video 8", R.raw.bikker),
        VideoItem("Video 9", R.raw.pamvideo),
        VideoItem("Video 10", R.raw.pamvideo),
        VideoItem("Video 11", R.raw.pamvideo),
        VideoItem("Video 12", R.raw.bikker),
    )

    // los diferentes carruseles y la cantidad de videos que tienen
    val section1 = videoList.subList(0, 8)
    val section2 = videoList.subList(0, 12)
    val section3 = videoList.subList(0, 8)

    var selectedVideo by remember { mutableStateOf<VideoItem?>(null) }

    if (selectedVideo != null) {
        VideoPlayerScreen(
            videoItem = selectedVideo!!,
            onBack = { selectedVideo = null }
        )
    } else {
        androidx.compose.foundation.lazy.LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                VideoRow(section1, onClick = { selectedVideo = it })
            }
            item {
                VideoRow(section2, onClick = { selectedVideo = it })
            }
            item {
                VideoRow(section3, onClick = { selectedVideo = it })
            }
        }
    }
}

@Composable
fun VideoRow(videos: List<VideoItem>, onClick: (VideoItem) -> Unit) {
    // Carrusel horizontal con las miniaturas
    LazyRow(modifier = Modifier.padding(vertical = 12.dp)) {
        items(videos) { video ->
            ThumbnailItem(video = video, onClick = onClick)
        }
    }
}


@Composable
fun ThumbnailItem(video: VideoItem, onClick: (VideoItem) -> Unit) {
    val context = LocalContext.current
    val thumbnail = remember { getVideoFrame(context, video.resId) }

    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(8.dp)
            .size(width = 200.dp, height = 120.dp)
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .border(
                width = if (isFocused) 4.dp else 1.dp,
                color = if (isFocused) Color.Blue else Color.Gray,
                shape = RoundedCornerShape(6.dp)
            )
            .onKeyEvent { keyEvent ->
                if (
                    (keyEvent.key == Key.Enter || keyEvent.key == Key.DirectionCenter)
                    && keyEvent.type == KeyEventType.KeyUp
                ) {
                    onClick(video)
                    true
                } else false
            }
            .clickable { onClick(video) } // Soporte para control  y mouse
    ) {
        if (thumbnail != null) {
            Image(
                bitmap = thumbnail.asImageBitmap(),
                contentDescription = video.title,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
