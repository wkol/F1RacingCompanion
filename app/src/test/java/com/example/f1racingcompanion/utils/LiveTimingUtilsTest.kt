package com.example.f1racingcompanion.utils

import androidx.compose.ui.graphics.Color
import com.example.f1racingcompanion.R
import com.example.f1racingcompanion.data.timingdatadto.SectorValue
import com.example.f1racingcompanion.model.Compound
import com.example.f1racingcompanion.utils.LiveTimingUtils.createWebSocketUrl
import com.example.f1racingcompanion.utils.LiveTimingUtils.decodeMessage
import com.example.f1racingcompanion.utils.LiveTimingUtils.getColorFromSector
import com.example.f1racingcompanion.utils.LiveTimingUtils.getTiresIcon
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class LiveTimingUtilsTest {

    @Test
    fun decodeMessageFromApiCorrect() {
        // Given a base64 encoded and compressed string
        val msg =
            "vZjNbtw4DMffxee0ICnxa67FvsHupcUeikWBXWCRQ7e3YN59PRYVOLAse9RML06QmDRJiT/+pZfpt+cf3//59t90+fIy/fHjr+kyERB+QPgA9jvYJeUL00ciFkX/PD1Nn75+n99+mdLt8envr8/P3/5d/gDTBZ4mWp5peebpgpCfJo6fef7FrtflHxtbRPC02CfQxYMtLzqFA1g54IYDUbfFfnl1tk8lgoa5tL4Pr/ZZVvZWElibazN81mJOmBdzjgLAxt6bnzfOxV7K53XXHqEZQAYvDgyPHGDDgbNJyT/lVf7QqB+2FnC7+I0PNyuPKUXq6keRW8OBqWuJnPQgcqJm7Z3L1kMtqefdAFKrdAhJwwGUDGhxoA37Vt+oKRZzhIMEcrt3LJcKEJe1l1YCSIuH5vYFkxzdl1bd1/TArfZzS7V9fJWD6KYEcg4dbz4MS9+1IxcqxaPgBu992FuNZzPbovbYrf3soYfILCyJH4tIyMjRKGtEtvZJk5ASa3wD3Wum6SwgMWu0Ga8BcXN2BpCQJIIn2t+jHUC6BN/EBgEZgCdL+w4w7xLSXHNwRn4xIbNAhE5DhGSshOQhQuIylG8OYrjcS0ik2yBf1a4Q0ugcIY2kAjaNEdKlElJGCRnbf5SQ85shEWoDlSLqtgbvi0hXi84DW+V+lpCak5+q/QEhFZjd8yMJ6ayRKdMQIHMAcp0obvd4u0mBo0ekP0n2AMkpBhl1tug+IGcHVUZ1+NYB5Lw+0SPGHUDSPiDZIWbpkRB7b0Ayx8JbHgIkeUU7bSI/gUepiduggIQUeMxrBehn8Si5nn1wRdc78IiRP7EO4tFjRCXgQTyyxRoIrYpojfZ7XzxaPX6EeA88bnVNG4/z8aVAYysKTrPRM8/y8aHq0YQoDqhD4jHnYu40wEYgCJWdjwTIjsiXykYbYiNhJK8dtHXZSFU8+tH2brORoLJxS5jHslFeLwYOc2+yEYR2qX6CjaSRuMOYdAQL7cdv2LjtzzYb61j2PIRGkLhdIOncq/TRqP6TaHy9oIjLoVCO2xK8NxrrWEUcQKO4y1ZPpXvQmD7OZx9OGR+KRsYYn8Hwe9GIMIxG9yATJh8io8a1KaXO1U+HjAmrahw7VkO2IKPjEBlVLQABv/pYLRmCjDpCRrV6qMXtVDsmI6T8dtvdTUZhb5w4zpJRPW7OfCsaT3Cx3peTdCR3j4uzKvhJLmKqd9b6RjLiw7noTS76SS7Wu37HHjCu1z+v/wM="

        // When decoding and uncompressing the message
        val decodedMsg = decodeMessage(msg)

        // Then the message is correctly decoded
        assertTrue(decodedMsg.startsWith("{\"Entries\":[{\"Utc\":\"2021-10-08T08:34:52.2256719Z\",\"Cars\":{\"3\":"))
    }

    @Test
    fun decodeMessageFromApiIncorrectMessage() {
        // Given incorrect Base64 encoded string
        val msg = "ajs*&&*%^$!hfsadgfukfghjfge=="

        // When running the decodeMessgae function
        // Then an IllegalArgumentException is thrown
        assertThrows(IllegalArgumentException::class.java) { decodeMessage(msg) }
    }

    @Test
    fun createWebSocketUrl() {
        // Given a sample token
        val token = "testToken"

        // When creating a WebSocketConnectionUrl
        val webSocketPath = createWebSocketUrl(token)

        // Then a correct wss path is created
        assertEquals(
            "wss://livetiming.formula1.com/signalr/connect?transport=webSockets&connectionToken=testToken&connectionData=%5B%7B%22name%22%3A%20%22Streaming%22%7D%5D&clientProtocol=1.5",
            webSocketPath
        )
    }

    @Test
    fun createWebSocketUrlEncodeToken() {
        // Given a token containg space
        val token = "test Token"

        // When creating a WebSocketConnectionUrl
        val webSocketPath = createWebSocketUrl(token)

        // Then a wss path is created with an encoded `space` char
        assertEquals(
            "wss://livetiming.formula1.com/signalr/connect?transport=webSockets&connectionToken=test%20Token&connectionData=%5B%7B%22name%22%3A%20%22Streaming%22%7D%5D&clientProtocol=1.5",
            webSocketPath
        )
    }

    @Test
    fun getCorrectColorFromSectorValue() {
        // Given three different Sector Values
        val sectors = listOf(SectorValue("23.12", null, true, false, null), SectorValue("23.12", null, false, false, null), SectorValue("23.12", null, true, true, null))

        // When converting them to corresponding colors
        val colorsConverted = sectors.map { getColorFromSector(it) }

        // Then each sector should have a different, corresponding color
        assertEquals(colorsConverted, listOf(Color(0xFF33B353), Color(0x97FFFFFF), Color(0xFF7A30A2)))
    }

    @Test
    fun getCorrectTiresIcons() {
        // Given each type of a compound
        val tires = listOf(Compound.UNKNOWN, Compound.HARD, Compound.MEDIUM, Compound.SOFT, Compound.INTER, Compound.WET)

        // When converting them to corresponding tires icons
        val tiresConverted = tires.map { getTiresIcon(it) }

        // Then each tyre should have a different corresponding icon
        assertEquals(tiresConverted, listOf(R.drawable.ic_unknown_tires, R.drawable.ic_hard_tires, R.drawable.ic_medium_tires, R.drawable.ic_soft_tires, R.drawable.ic_inter_tires, R.drawable.ic_wet_tires))
    }
}
