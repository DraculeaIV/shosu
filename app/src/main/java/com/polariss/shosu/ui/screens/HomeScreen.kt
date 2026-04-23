package com.polariss.shosu.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polariss.shosu.R
import com.polariss.shosu.ui.navigation.Screen

data class HomeButtonInfo(
    val title: String,
    val route: Screen?,
    val iconResId: Int? = null,
    val onClick: (() -> Unit)? = null
)

@Composable
fun HomeScreen(
    onNavigate: (Screen) -> Unit,
    onSettingsClick: () -> Unit
) {
    val buttons = listOf(
        HomeButtonInfo("魔女图鉴", Screen.Lore, iconResId = R.drawable.witch_catalog_icon),
        HomeButtonInfo("处刑投票", Screen.Execution, iconResId = R.drawable.splash_logo),
        HomeButtonInfo("好友", Screen.Warden, iconResId = R.drawable.friends_chat_icon), // Temporarily mapping Friends to Warden or just null
        HomeButtonInfo("设置", null, iconResId = R.drawable.setting_icon, onClick = onSettingsClick)
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp),
            contentAlignment = Alignment.TopCenter
        ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(0.dp),
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            modifier = Modifier.fillMaxWidth(1f)
        ) {
            items(buttons) { button ->
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .then(
                            if (button.iconResId == null) Modifier.background(Color.DarkGray)
                            else Modifier
                        )
                        .clickable {
                            button.onClick?.invoke()
                            button.route?.let { onNavigate(it) }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (button.iconResId != null) {
                        Image(
                            painter = painterResource(id = button.iconResId),
                            contentDescription = button.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(
                            text = button.title,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
        }
    }
}
