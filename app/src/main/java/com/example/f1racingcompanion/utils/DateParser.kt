package com.example.f1racingcompanion.utils

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateParser {
    @FromJson
    fun fromJson(reader: JsonReader): Date? = SimpleDateFormat(
        "\"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        Locale.US
    ).parse(reader.nextString())

    @ToJson
    fun toJson(date: Date): String = date.toString()
}
