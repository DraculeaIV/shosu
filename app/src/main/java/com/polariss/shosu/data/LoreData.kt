package com.polariss.shosu.data

import androidx.annotation.DrawableRes

enum class LoreCategory(val displayName: String) {
    EVIDENCE("证物"),
    CHARACTER("人物"),
    MAP("地图"),
    RULE("规定"),
    RECORD("记录")
}

data class LoreItem(
    val name: String,
    val category: LoreCategory,
    @param:DrawableRes val icon: Int,
    @param:DrawableRes val pic: Int,
    val text: String? = null // 如果为 null 则视为地图模式
)
