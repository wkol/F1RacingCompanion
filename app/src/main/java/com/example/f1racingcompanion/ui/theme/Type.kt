package com.example.f1racingcompanion.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.f1racingcompanion.R

val TitilliumWeb = FontFamily(
    Font(R.font.titilliumweb_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.titilliumweb_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.titilliumweb_light, FontWeight.Light, FontStyle.Normal),
    Font(R.font.titilliumweb_semibold, FontWeight.SemiBold, FontStyle.Normal),
    Font(R.font.titilliumweb_bolditalic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.titilliumweb_italic, FontWeight.Normal, FontStyle.Italic),
)
// Set of Material typography styles to start with
val Typography = Typography(
    subtitle1 = TextStyle(
        fontSize = 25.sp,
        fontFamily = TitilliumWeb,
        color = Color.White,
        fontWeight = FontWeight.Bold,
    ),
    subtitle2 = TextStyle(
        fontSize = 15.sp,
        fontFamily = TitilliumWeb,
        color = Color.White,
        fontWeight = FontWeight.Bold,
    ),

    body1 = TextStyle(
        fontFamily = TitilliumWeb,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        fontSize = 20.sp
    ),
    body2 = TextStyle(
        fontFamily = TitilliumWeb,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        fontSize = 10.sp
    ),
)
