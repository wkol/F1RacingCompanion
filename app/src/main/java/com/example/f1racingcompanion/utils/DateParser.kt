package com.example.f1racingcompanion.utils

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.ToJson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class DateParser {
    @FromJson
    fun fromJson(reader: JsonReader): LocalDateTime = LocalDateTime.parse(
        reader.nextString(),
        DateTimeFormatter.ofPattern(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            Locale.US
        )
    )

    @ToJson
    fun toJson(date: LocalDateTime): String = date.toString()
}
