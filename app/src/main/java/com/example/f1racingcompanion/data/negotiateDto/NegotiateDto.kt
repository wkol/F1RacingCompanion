package com.example.f1racingcompanion.data.negotiateDto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NegotiateDto(
    @Json(name = "ConnectionId")
    val connectionId: String,
    @Json(name = "ConnectionTimeout")
    val connectionTimeout: Double,
    @Json(name = "ConnectionToken")
    val connectionToken: String,
    @Json(name = "DisconnectTimeout")
    val disconnectTimeout: Double,
    @Json(name = "KeepAliveTimeout")
    val keepAliveTimeout: Double,
    @Json(name = "LongPollDelay")
    val longPollDelay: Double,
    @Json(name = "ProtocolVersion")
    val protocolVersion: String,
    @Json(name = "TransportConnectTimeout")
    val transportConnectTimeout: Double,
    @Json(name = "TryWebSockets")
    val tryWebSockets: Boolean,
    @Json(name = "Url")
    val url: String
)
