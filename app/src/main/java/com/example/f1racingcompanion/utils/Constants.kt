package com.example.f1racingcompanion.utils

import com.example.f1racingcompanion.R
import com.example.f1racingcompanion.model.CircuitOffset
import com.example.f1racingcompanion.timing.CircuitInfo

object Constants {
    val CHANNELS_MAP = hashMapOf(
        "0" to "RPM",
        "2" to "Speed",
        "3" to "nGear",
        "4" to "Throttle",
        "5" to "Brake",
        "45" to "DRS"
    )

    const val ERGAST_API_URL = "https://ergast.com/api/f1/"

    const val HUB_DATA = "[{\"name\": \"Streaming\"}]"

    const val LIVETIMING_NEGOTIATE_URL = "https://livetiming.formula1.com/"

    const val WEBSCOKET_HOST = "livetiming.formula1.com"

    const val WEBSOCKET_SIGNALR_PATH = "signalr"

    const val WEBSOCKET_CONNECT_PATH = "connect"

    const val WEBSCOKET_TRANSPORT = "webSockets"

    const val WEBSCOKET_PROTOCOL = "1.5"

    val SUBSRIBED_STREAMS = listOf(
        "Position.z", "DriverList",
        "TimingAppData", "TimingData"
    )

    val CIRCUITS = mapOf(
        "bahrain" to CircuitInfo(
            circuitOffset = CircuitOffset(
                xOffset = -580F,
                yOffset = 8358F,
                xAbs = 8088F,
                yAbs = 11863F
            ),
            grandPrixName = "Bahrain",
            circuitMap = R.drawable.bahrain
        ),
        "imola" to CircuitInfo(
            circuitOffset = CircuitOffset(
                xOffset = -14901F,
                yOffset = -323F,
                xAbs = 18112F,
                yAbs = 10102F
            ),
            grandPrixName = "Emilia Romagna",
            circuitMap = R.drawable.imola
        ),
        "miami" to CircuitInfo(
            circuitOffset = CircuitOffset(
                xOffset = -4370F,
                yOffset = 1081F,
                xAbs = 14925F,
                yAbs = 5875F
            ),
            grandPrixName = "Miami",
            circuitMap = R.drawable.miami
        ),
    )
}
