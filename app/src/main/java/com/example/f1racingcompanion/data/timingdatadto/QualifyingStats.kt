package com.example.f1racingcompanion.data.timingdatadto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QualifyingStats(
    @Json(name = "TimeDifftoPositionAhead")
    val timeDiffToNext: String?,
    @Json(name = "TimeDiffToFastest")
    val timeDiffToFastest: String?,
)
