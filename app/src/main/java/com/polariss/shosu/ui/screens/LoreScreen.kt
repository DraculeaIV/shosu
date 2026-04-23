package com.polariss.shosu.ui.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polariss.shosu.R
import com.polariss.shosu.data.LoreCategory
import com.polariss.shosu.data.LoreItem

private val SecondaryNavWidth = 100.dp

@Composable
fun LoreScreen() {
    var selectedPrimaryTab by remember { mutableStateOf(LoreCategory.EVIDENCE) }
    
    // 模拟数据源
    val allLoreItems = remember {
        listOf(
            LoreItem("测试证物", LoreCategory.EVIDENCE, R.drawable.button, R.drawable.button_bg, "这是一个测试证物的描述文本。"),
            LoreItem("地图1f", LoreCategory.MAP, R.drawable.m1f_icon, R.drawable.map_1f, null),
            LoreItem("地图2f", LoreCategory.MAP, R.drawable.m2f_icon, R.drawable.map_2f, null),
            LoreItem("地图b1f", LoreCategory.MAP, R.drawable.mb1f_icon, R.drawable.map_b1f, null),
            LoreItem("测试人物", LoreCategory.CHARACTER, R.drawable.finish, R.drawable.logo, "这是角色的介绍文字。")
        )
    }

    val currentItems = allLoreItems.filter { it.category == selectedPrimaryTab }
    var selectedItem by remember(selectedPrimaryTab) { 
        mutableStateOf(currentItems.firstOrNull()) 
    }

    val primaryTabs = LoreCategory.entries

    Box(modifier = Modifier.fillMaxSize()) {
        // 全局背景
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.fillMaxSize()) {
            // 2. 整体区域：左侧内容(标题+主显示+底部导航) + 右侧二级导航全贯穿
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                // 左侧：垂直结构
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    // 1. 标题图标 (顶到最左上角)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp),
                        contentAlignment = Alignment.TopStart
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.lore_icon),
                            contentDescription = "图鉴",
                            modifier = Modifier.wrapContentSize(),
                            contentScale = ContentScale.Fit
                        )
                    }

                    // 主显示区
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        selectedItem?.let { item ->
                            if (item.text == null) {
                                // 地图模式：双指缩放
                                MapViewer(item.pic)
                            } else {
                                // 普通模式：上下分割
                                Column(modifier = Modifier.fillMaxSize()) {
                                    // 上侧：图片展示区
                                    Box(
                                        modifier = Modifier
                                            .weight(0.4f)
                                            .fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            painter = painterResource(id = item.pic),
                                            contentDescription = item.name,
                                            modifier = Modifier.fillMaxSize(0.8f),
                                            contentScale = ContentScale.Fit
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // 下侧：描述文字区
                                    Box(
                                        modifier = Modifier
                                            .weight(0.6f)
                                            .fillMaxWidth()
                                            .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                            .padding(12.dp)
                                    ) {
                                        Text(
                                            text = item.text,
                                            color = Color.White,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("暂无数据", color = Color.Gray)
                        }
                    }

                    // 3. 底部一级导航 (去掉半透明背景)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp), // 同步增加导航栏高度
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom // 靠底对齐
                    ) {
                        primaryTabs.forEach { tab ->
                            VerticalTabText(
                                text = tab.displayName,
                                isSelected = selectedPrimaryTab == tab,
                                onClick = { selectedPrimaryTab = tab }
                            )
                        }
                    }
                }

                // 右侧二级导航 (彻底上下全贯穿)
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(SecondaryNavWidth)
                ) {
                    val itemSize = SecondaryNavWidth
                    val maxItems = (maxHeight / itemSize).toInt() + 1
                    
                    val displayItems = if (currentItems.size < maxItems) {
                        currentItems.map { it as LoreItem? } + List(maxItems - currentItems.size) { null }
                    } else {
                        currentItems.map { it as LoreItem? }
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        userScrollEnabled = currentItems.size >= maxItems,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        items(displayItems) { item ->
                            FilmItem(
                                iconRes = item?.icon,
                                isSelected = selectedItem == item,
                                onClick = item?.let { { selectedItem = it } }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MapViewer(imageRes: Int) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { _, zoomChange, panChange, _ ->
        scale *= zoomChange
        offset += panChange
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .transformable(state = state),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Map",
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = maxOf(1f, minOf(scale, 5f)),
                    scaleY = maxOf(1f, minOf(scale, 5f)),
                    translationX = offset.x,
                    translationY = offset.y
                ),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun FilmItem(
    iconRes: Int?,
    isSelected: Boolean,
    onClick: (() -> Unit)?
) {
    Box(
        modifier = Modifier
            .size(SecondaryNavWidth)
            .clickable(enabled = onClick != null, onClick = { onClick?.invoke() }),
        contentAlignment = Alignment.Center
    ) {
        // 底层：胶片底片边框
        Image(
            painter = painterResource(id = R.drawable.film_frame),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        // 上层：图标 (仅在非空时显示)
        if (iconRes != null) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(0.6f) // 稍微缩小，避免超框
                    .graphicsLayer {
                        alpha = if (isSelected) 1f else 0.6f
                    },
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
fun VerticalTabText(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val offsetY by animateDpAsState(
        targetValue = if (isSelected) 0.dp else 30.dp, // 保持一定比例的下沉
        animationSpec = tween(durationMillis = 300),
        label = "TabOffset"
    )

    Box(
        modifier = Modifier
            .size(width = 48.dp, height = 120.dp) // 119:298 约等于 48:120
            .offset { IntOffset(0, offsetY.roundToPx()) }
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        // 背景图
        Image(
            painter = painterResource(id = R.drawable.tab_icon),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            alpha = if (isSelected) 1f else 0.7f
        )

        // 竖排文字逻辑
        // 核心算法：第一个字 (30sp) 水平居中，第二个字 (24sp) 左边缘与第一个字对齐。
        // 在 48dp 宽的容器中，30sp(约30dp) 的字居中，其左边缘距离容器左侧为 (48-30)/2 = 9dp。
        // 通过设置整体 Column 的 paddingStart = 9.dp，可以让所有字符的左边缘都对齐在 9dp 处。
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 9.dp, bottom = 12.dp), // 向下增加 padding 相当于内容向上移动
            horizontalAlignment = Alignment.Start, 
            verticalArrangement = Arrangement.Center 
        ) {
            text.forEachIndexed { index, char ->
                Text(
                    text = char.toString(),
                    color = if (isSelected) {
                        if (index == 0) Color(0xFFFC89A2) else Color.White
                    } else {
                        Color(0xFFD3BEBD)
                    },
                    fontSize = if (index == 0) 30.sp else 24.sp,
                    fontFamily = FontFamily.Serif,
                    lineHeight = if (index == 0) 34.sp else 24.sp
                )
            }
        }
    }
}
