package com.example.netflix

import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.zIndex

@Composable
fun EnhancedVideoListScreen() {
    val context = LocalContext.current

    val categories = listOf(
        "Acción" to listOf(
            VideoItem("Ostia tio", R.raw.frankvideo),
            VideoItem("Gatito en peligro", R.raw.pamvideo),
            VideoItem("Frank Shooter", R.raw.frankjuego),
            VideoItem("Se va a matar", R.raw.bikker),
            VideoItem("App de diego", R.raw.diegoapp)
        ),
        "Comedia" to listOf(
            VideoItem("Mi perrito", R.raw.manchitas),
            VideoItem("Graduación Ing", R.raw.graduados),
            VideoItem("A bordo", R.raw.avion),
            VideoItem("Cenita", R.raw.yop),
            VideoItem("Lluvia", R.raw.mojao)
        ),
        "Drama" to listOf(
            VideoItem("Tirao", R.raw.moto),
            VideoItem("Repartiendo", R.raw.repartiendo),
            VideoItem("Rico desayuno", R.raw.desa),
            VideoItem("Mazatlan Sn", R.raw.maza),
            VideoItem("Choco", R.raw.choco)
        )
    )

    var selectedVideo by remember { mutableStateOf<VideoItem?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF2193b0), Color(0xFF0b2545))
                )
            )
    ) {
        if (selectedVideo != null) {
            VideoPlayerScreen(
                videoItem = selectedVideo!!,
                onBack = { selectedVideo = null }
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                categories.forEach { (category, videos) ->
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .align(Alignment.Start)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        CarouselRow(
                            videos = videos,
                            onClick = { selectedVideo = it }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CarouselRow(videos: List<VideoItem>, onClick: (VideoItem) -> Unit) {
    val itemCount = videos.size
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = { /* Navegación opcional */ },
            modifier = Modifier
                .padding(8.dp)
                .zIndex(2f)
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Anterior", tint = Color.White)
        }
        LazyRow(
            modifier = Modifier
                .weight(1f)
                .height(180.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            itemsIndexed(videos) { idx, video ->
                val context = LocalContext.current
                val thumbnail = remember { getVideoFrame(context, video.resId) }
                var isFocused by remember { mutableStateOf(false) }
                val elevation by animateDpAsState(targetValue = if (isFocused) 24.dp else 4.dp, label = "")

                val itemWeight = 1f / itemCount.toFloat()

                Card(
                    modifier = Modifier
                        .fillParentMaxWidth(itemWeight)
                        .height(180.dp)
                        .padding(8.dp)
                        .focusable()
                        .zIndex(if (isFocused) 1f else 0f)
                        .onFocusChanged { isFocused = it.isFocused }
                        .then(
                            if (isFocused)
                                Modifier.border(
                                    width = 4.dp,
                                    color = Color(0xFF0F22D7),
                                    shape = RoundedCornerShape(18.dp)
                                )
                            else Modifier
                        ),
                    shape = RoundedCornerShape(18.dp),
                    elevation = CardDefaults.cardElevation(elevation),
                    onClick = { onClick(video) }
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .background(if (isFocused) Color(0xFF0F2AD7) else Color(0xFF303B4A))
                            .border(
                                width = if (isFocused) 4.dp else 1.dp,
                                color = if (isFocused) Color.Blue else Color.Gray,
                            )
                    ) {
                        if (thumbnail != null) {
                            androidx.compose.foundation.Image(
                                bitmap = thumbnail.asImageBitmap(),
                                contentDescription = video.title,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp)
                            )
                        } else {
                            Text(
                                text = "Sin miniatura",
                                color = Color.White,
                                fontSize = 12.sp,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        Text(
                            text = video.title,
                            color = Color.White,
                            fontSize = 12.sp, // Más pequeño
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .background(Color(0x99000000))
                                .fillMaxWidth()
                                .padding(start = 8.dp, end = 8.dp, bottom = 4.dp, top = 2.dp)
                        )
                    }
                }
            }
        }
        IconButton(
            onClick = { /* Navegación opcional */ },
            modifier = Modifier
                .padding(8.dp)
                .zIndex(2f)
        ) {
            Icon(Icons.Filled.ArrowForward, contentDescription = "Siguiente", tint = Color.White)
        }
    }
}