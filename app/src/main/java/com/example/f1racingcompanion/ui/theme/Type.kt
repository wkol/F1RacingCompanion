package com.example.f1racingcompanion.ui.theme

import androidx.compose.material.Typography
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
    body1 = TextStyle(
        fontFamily = TitilliumWeb,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    button = TextStyle(
        fontFamily = TitilliumWeb,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = TitilliumWeb,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )

)
