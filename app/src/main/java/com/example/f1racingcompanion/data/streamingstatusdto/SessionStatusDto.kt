package com.example.f1racingcompanion.data.streamingstatusdto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SessionStatusDto(
    @Json(name = "EndDate")
    val endDate: String?,
    @Json(name = "GmtOffset")
    val gmtOffset: String?,
    @Json(name = "Key")
    val key: Int?,
    @Json(name = "Meeting")
    val meeting: Meeting?,
    @Json(name = "Name")
    val name: String?,
    @Json(name = "Number")
    val number: Int?,
    @Json(name = "Path")
    val path: String?,
    @Json(name = "StartDate")
    val startDate: String?,
    @Json(name = "Type")
    val type: String?,
    @Json(name = "Status")
    val status: String?
)
