package com.example.f1racingcompanion.utils

import com.example.f1racingcompanion.R
import com.example.f1racingcompanion.model.CircuitOffset

object Constants {
    val CHANNELS_MAP = hashMapOf(
        "0" to "RPM",
        "2" to "Speed",
        "3" to "nGear",
        "4" to "Throttle",
        "5" to "Brake",
        "45" to "DRS"
    )

    const val HUB_NAME = "Streaming"

    const val HUB_DATA = "[{\"name\": \"Streaming\"}]"

    const val LIVETIMING_NEGOTIATE_URL = "https://livetiming.formula1.com/signalr/"

    const val WEBSCOKET_HOST = "livetiming.formula1.com"

    const val WEBSOCKET_SIGNALR_PATH = "signalr"

    const val WEBSOCKET_CONNECT_PATH = "connect"

    const val WEBSCOKET_TRANSPORT = "webSockets"

    const val WEBSCOKET_PROTOCOL = "1.5"

    val SUBSRIBED_STREAMS = listOf(
        "CarData.z", "Position.z", "DriverList",
        "TimingAppData", "TimingData"
    )

    val OFFSETMAP = mapOf(
        "russia" to CircuitOffset(xOffset = -16715F, yOffset = 1063F, xAbs = 18369F, yAbs = 11862F),
        "bahrain" to CircuitOffset(xOffset = -580F, yOffset = 8358F, xAbs = 8088F, yAbs = 11863F),
        "saudi" to CircuitOffset(xOffset = -5795F, yOffset = 21801F, xAbs = 5821F, yAbs = 27443F)
    )

    val CIRCIUTS = mapOf(
        "russia" to R.drawable.russia,
        "bahrain" to R.drawable.bahrain
    )
}
