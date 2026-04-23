package com.polariss.shosu

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.navigation.compose.rememberNavController
import com.polariss.shosu.ui.navigation.ShosuNavHost
import com.polariss.shosu.ui.theme.ShosuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ShosuMainApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShosuMainApp() {
    val context = LocalContext.current
    val navController = rememberNavController()
    var showBottomSheet by remember { mutableStateOf(false) }

    // Tip logic
    val tipFromEma = stringResource(R.string.tip_from_ema)
    LaunchedEffect(Unit) {
        if (shouldShowTip(context)) {
            Toast.makeText(context, tipFromEma, Toast.LENGTH_LONG).show()
        }
    }

    ShosuTheme {
        Box {
            ShosuNavHost(
                navController = navController,
                onSettingsClick = { showBottomSheet = true }
            )

            if (showBottomSheet) {
                AboutBottomSheet(onDismiss = { showBottomSheet = false })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutBottomSheet(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    val versionName = packageInfo.versionName

    val aboutTitle = stringResource(R.string.about_title)
    val aboutDescriptionZh = stringResource(R.string.about_description_zh)
    val aboutDescriptionJa = stringResource(R.string.about_description_ja)
    val appName = stringResource(R.string.app_name)
    val versionLabel = stringResource(R.string.version_label)
    val developerInfo = stringResource(R.string.developer_info)
    val xLabel = stringResource(R.string.x_label)
    val bilibiliLabel = stringResource(R.string.bilibili_label)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .size(width = 40.dp, height = 4.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(2.dp)
                    )
            )
        },
        containerColor = Color(0xFF4B0029),
        contentColor = Color.White,
        scrimColor = Color.Black.copy(alpha = 0.6f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier
                        .height(100.dp)
                        .align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = aboutTitle,
                fontSize = 10.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                fontSize = 8.sp,
                lineHeight = 12.sp,
                text = buildAnnotatedString {
                    append(aboutDescriptionZh)
                    append("\n\n")
                    append(aboutDescriptionJa)
                    append("\n\n")
                    append(appName)
                    append("\n")
                    append("$versionLabel $versionName\n")
                    append(developerInfo)
                    append("\n")

                    withLink(
                        LinkAnnotation.Url(
                            url = "https://x.com/KooyuooK"
                        )
                    ) {
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFFFF45AB),
                                fontWeight = FontWeight.Bold,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append(xLabel)
                        }
                    }
                    append("｜")
                    withLink(
                        LinkAnnotation.Url(
                            url = "https://space.bilibili.com/294080110"
                        )
                    ) {
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFFFF45AB),
                                fontWeight = FontWeight.Bold,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append(bilibiliLabel)
                        }
                    }
                }
            )
        }
    }
}

fun shouldShowTip(context: Context): Boolean {
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val shown = prefs.getBoolean("tip_shown", false)
    if (!shown) {
        prefs.edit { putBoolean("tip_shown", true) }
        return true
    }
    return false
}
