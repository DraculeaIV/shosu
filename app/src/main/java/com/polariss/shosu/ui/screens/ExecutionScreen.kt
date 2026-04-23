package com.polariss.shosu.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.polariss.shosu.R
import com.polariss.shosu.app.ExecutionButton
import com.polariss.shosu.app.SoundUtils

@Composable
fun ExecutionScreen() {
    val context = LocalContext.current
    val soundUtils = remember { SoundUtils(context) }
    
    DisposableEffect(Unit) {
        onDispose {
            soundUtils.release()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxHeight(),
            contentScale = ContentScale.FillHeight
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            ExecutionButton(
                size = 240.dp,
                soundUtils = soundUtils,
                onPressStart = {
                    // Animation logic for the removed info button is no longer needed here
                },
                onPressEnd = {
                    // Animation logic for the removed info button is no longer needed here
                }
            )
        }
    }
}
