package com.example.f1racingcompanion.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.f1racingcompanion.ui.theme.TitilliumWeb

@Composable
fun FetchingDataIndicator(modifier: Modifier = Modifier, text: String = "Loading data...") {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Spacer(Modifier.height(16.dp))
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = Color.Black
                )
            )
        }
    }
}

@Composable
fun NetworkError(modifier: Modifier, errorMsg: String) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(
            text = "An error while fetching data occured: $errorMsg",
            style = TextStyle(
                fontFamily = TitilliumWeb,
                fontSize = 14.sp,
                fontWeight = FontWeight.W500,
                color = Color.Black
            ),
            modifier = Modifier.wrapContentSize(Alignment.Center),
            textAlign = TextAlign.Center
        )
    }
}
