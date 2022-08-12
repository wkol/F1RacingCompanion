package com.example.f1racingcompanion.utils

import com.example.f1racingcompanion.R
import com.example.f1racingcompanion.model.CircuitOffset
import com.example.f1racingcompanion.timing.CircuitInfo
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

object Constants {
    val DEFAULT_DATETIME_FORMATTER: DateTimeFormatter =
        DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd'T'HH:mm:ss")
            .appendFraction(ChronoField.MILLI_OF_SECOND, 1, 7, true)
            .appendLiteral('Z')
            .toFormatter()

    const val ERGAST_API_URL = "https://ergast.com/api/f1/"

    const val HUB_DATA = "[{\"name\": \"Streaming\"}]"

    const val LIVETIMING_NEGOTIATE_URL = "https://livetiming.formula1.com/"

    const val WEBSCOKET_HOST = "livetiming.formula1.com"

    const val WEBSOCKET_SIGNALR_PATH = "signalr"

    const val WEBSOCKET_CONNECT_PATH = "connect"

    const val WEBSCOKET_TRANSPORT = "webSockets"

    const val WEBSCOKET_PROTOCOL = "1.5"

    const val MAXIMUM_THROTTLE_VALUE: Int = 104

    const val MAXIMUM_BRAKE_VALUE: Int = 104

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
        "catalunya" to CircuitInfo(
            circuitOffset = CircuitOffset(
                xOffset = -7337F,
                yOffset = 3734F,
                xAbs = 9637F,
                yAbs = 11476F
            ),
            grandPrixName = "Spanish",
            circuitMap = R.drawable.catalunya
        ),
        "monaco" to CircuitInfo(
            circuitOffset = CircuitOffset(
                xOffset = -7681F,
                yOffset = -192F,
                xAbs = 7475F,
                yAbs = 9577F
            ),
            grandPrixName = "Monaco",
            circuitMap = R.drawable.monaco
        ),
        "BAK" to CircuitInfo(
            circuitOffset = CircuitOffset(
                xOffset = -16914F,
                yOffset = 3701F,
                xAbs = 20646F,
                yAbs = 14850F
            ),
            grandPrixName = "Azerbaijan",
            circuitMap = R.drawable.bak
        ),
        "ricard" to CircuitInfo(
            circuitOffset = CircuitOffset(
                xOffset = -11083F,
                yOffset = 5285F,
                xAbs = 21095F,
                yAbs = 12215F
            ),
            grandPrixName = "French",
            circuitMap = R.drawable.ricard
        ),
        "red_bull_ring" to CircuitInfo(
            circuitOffset = CircuitOffset(
                xOffset = -8236F,
                yOffset = 5681F,
                xAbs = 12483F,
                yAbs = 7926F
            ),
            grandPrixName = "Austrian",
            circuitMap = R.drawable.red_bull_ring
        ),
        "silverstone" to CircuitInfo(
            circuitOffset = CircuitOffset(
                xOffset = -2318F,
                yOffset = 13123F,
                xAbs = 10112F,
                yAbs = 17244F
            ),
            grandPrixName = "British",
            circuitMap = R.drawable.silverstone
        ),
        "hungaroring" to CircuitInfo(
            circuitOffset = CircuitOffset(
                xOffset = -6087F,
                yOffset = 10573F,
                xAbs = 10638F,
                yAbs = 12070F
            ),
            grandPrixName = "Hungarian",
            circuitMap = R.drawable.hungaroring
        ),
        "spa" to CircuitInfo(
            circuitOffset = CircuitOffset(
                xOffset = -4341F,
                yOffset = 4558F,
                xAbs = 12658F,
                yAbs = 20338F
            ),
            grandPrixName = "Belgian",
            circuitMap = R.drawable.spa
        ),
        "zandvoort" to CircuitInfo(
            circuitOffset = CircuitOffset(
                xOffset = -1021F,
                yOffset = 6715F,
                xAbs = 9730F,
                yAbs = 8329F
            ),
            grandPrixName = "Dutch",
            circuitMap = R.drawable.zandvoort
        ),
        "monza" to CircuitInfo(
            circuitOffset = CircuitOffset(
                xOffset = -1502F,
                yOffset = 15893F,
                xAbs = 12574F,
                yAbs = 21698F
            ),
            grandPrixName = "Italian",
            circuitMap = R.drawable.monza
        ),
        "americas" to CircuitInfo(
            circuitOffset = CircuitOffset(
                xOffset = -2554F,
                yOffset = 6437F,
                xAbs = 18136F,
                yAbs = 10534F
            ),
            grandPrixName = "United States",
            circuitMap = R.drawable.americas
        ),
        "rodriguez" to CircuitInfo(
            circuitOffset = CircuitOffset(
                xOffset = -2433F,
                yOffset = 1287F,
                xAbs = 14448F,
                yAbs = 10329F
            ),
            grandPrixName = "Mexican",
            circuitMap = R.drawable.rodriguez
        ),
        "interlagos" to CircuitInfo(
            circuitOffset = CircuitOffset(
                xOffset = -4683F,
                yOffset = 4988F,
                xAbs = 6687F,
                yAbs = 10381F
            ),
            grandPrixName = "Brazilian",
            circuitMap = R.drawable.interlagos
        ),
        "yas_marina" to CircuitInfo(
            circuitOffset = CircuitOffset(
                xOffset = -2181F,
                yOffset = 11784F,
                xAbs = 7988F,
                yAbs = 17136F
            ),
            grandPrixName = "Abu Dhabi",
            circuitMap = R.drawable.yas_marina
        ),
        "marina_bay" to CircuitInfo(
            circuitOffset = CircuitOffset(
                xOffset = -13498F,
                yOffset = 4154F,
                xAbs = 14686F,
                yAbs = 12487F
            ),
            grandPrixName = "Singapore",
            circuitMap = R.drawable.marina_bay
        ),
        "suzuka" to CircuitInfo(
            circuitOffset = CircuitOffset(
                xOffset = -14149F,
                yOffset = 2832F,
                xAbs = 20199F,
                yAbs = 15852F
            ),
            grandPrixName = "Japan",
            circuitMap = R.drawable.suzuka
        ),
        "villeneuve" to CircuitInfo(
            circuitOffset = CircuitOffset(
                xOffset = -9857F,
                yOffset = 16790F,
                xAbs = 13792F,
                yAbs = 20408F
            ),
            grandPrixName = "Canadian",
            circuitMap = R.drawable.villeneuve
        ),
        "unknown" to CircuitInfo(
            circuitOffset = CircuitOffset(
                xOffset = 0F,
                yOffset = 0F,
                xAbs = 0F,
                yAbs = 0F
            ),
            grandPrixName = "Unknown",
            circuitMap = R.drawable.spa
        )
    )
    val SESSION_TYPE_SHORTCUT = mapOf(
        "First practice" to "FP1",
        "Second practice" to "FP2",
        "Third practice" to "FP3",
        "Qualifying" to "Q",
        "Sprint" to "Sprint",
        "Race" to "Race",
    )
}
