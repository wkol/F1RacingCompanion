package com.example.f1racingcompanion.utils

import com.example.f1racingcompanion.utils.LiveTimingUtils.createWebSocketUrl
import com.example.f1racingcompanion.utils.LiveTimingUtils.decodeMessage
import org.junit.Assert.*
import org.junit.Test

class LiveTimingUtilsTest {

    @Test
    fun decodeMessageFromApi_correct() {
        // Given a base64 encoded and compressed string
        val msg =
            "vZjNbtw4DMffxee0ICnxa67FvsHupcUeikWBXWCRQ7e3YN59PRYVOLAse9RML06QmDRJiT/+pZfpt+cf3//59t90+fIy/fHjr+kyERB+QPgA9jvYJeUL00ciFkX/PD1Nn75+n99+mdLt8envr8/P3/5d/gDTBZ4mWp5peebpgpCfJo6fef7FrtflHxtbRPC02CfQxYMtLzqFA1g54IYDUbfFfnl1tk8lgoa5tL4Pr/ZZVvZWElibazN81mJOmBdzjgLAxt6bnzfOxV7K53XXHqEZQAYvDgyPHGDDgbNJyT/lVf7QqB+2FnC7+I0PNyuPKUXq6keRW8OBqWuJnPQgcqJm7Z3L1kMtqefdAFKrdAhJwwGUDGhxoA37Vt+oKRZzhIMEcrt3LJcKEJe1l1YCSIuH5vYFkxzdl1bd1/TArfZzS7V9fJWD6KYEcg4dbz4MS9+1IxcqxaPgBu992FuNZzPbovbYrf3soYfILCyJH4tIyMjRKGtEtvZJk5ASa3wD3Wum6SwgMWu0Ga8BcXN2BpCQJIIn2t+jHUC6BN/EBgEZgCdL+w4w7xLSXHNwRn4xIbNAhE5DhGSshOQhQuIylG8OYrjcS0ik2yBf1a4Q0ugcIY2kAjaNEdKlElJGCRnbf5SQ85shEWoDlSLqtgbvi0hXi84DW+V+lpCak5+q/QEhFZjd8yMJ6ayRKdMQIHMAcp0obvd4u0mBo0ekP0n2AMkpBhl1tug+IGcHVUZ1+NYB5Lw+0SPGHUDSPiDZIWbpkRB7b0Ayx8JbHgIkeUU7bSI/gUepiduggIQUeMxrBehn8Si5nn1wRdc78IiRP7EO4tFjRCXgQTyyxRoIrYpojfZ7XzxaPX6EeA88bnVNG4/z8aVAYysKTrPRM8/y8aHq0YQoDqhD4jHnYu40wEYgCJWdjwTIjsiXykYbYiNhJK8dtHXZSFU8+tH2brORoLJxS5jHslFeLwYOc2+yEYR2qX6CjaSRuMOYdAQL7cdv2LjtzzYb61j2PIRGkLhdIOncq/TRqP6TaHy9oIjLoVCO2xK8NxrrWEUcQKO4y1ZPpXvQmD7OZx9OGR+KRsYYn8Hwe9GIMIxG9yATJh8io8a1KaXO1U+HjAmrahw7VkO2IKPjEBlVLQABv/pYLRmCjDpCRrV6qMXtVDsmI6T8dtvdTUZhb5w4zpJRPW7OfCsaT3Cx3peTdCR3j4uzKvhJLmKqd9b6RjLiw7noTS76SS7Wu37HHjCu1z+v/wM="

        // When decoding and uncompressing the message
        val decodedMsg = decodeMessage(msg)

        // Then the message is correctly decoded
        assertTrue(decodedMsg.startsWith("{\"Entries\":[{\"Utc\":\"2021-10-08T08:34:52.2256719Z\",\"Cars\":{\"3\":"))
    }

    @Test
    fun decodeMessageFromApi_incorrectMessage() {
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
    fun createWebSocketUrl_encodeToken() {
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
}
