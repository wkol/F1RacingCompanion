package com.example.f1racingcompanion.utils

import android.util.Base64
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.example.f1racingcompanion.R
import com.example.f1racingcompanion.data.positiondatadto.PositionDataDto
import com.example.f1racingcompanion.data.timingappdatadto.TimingAppDataDto
import com.example.f1racingcompanion.data.timingdatadto.SectorValue
import com.example.f1racingcompanion.data.timingdatadto.TimingDataDto
import com.example.f1racingcompanion.model.*
import okhttp3.HttpUrl
import java.io.ByteArrayOutputStream
import java.util.zip.Inflater

object LiveTimingUtils {

    fun decodeMessage(text: String): String {
        if (text.length % 4 != 0 || !text.matches(Regex("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?\$"))) {
            throw IllegalArgumentException("Invalid Base64 encoded string")
        }
        val byteArray =
            Base64.decode(text, Base64.DEFAULT) // I relay that given text is correct b64 encoded
        return byteArray.zlibDecompress()
    }

    fun createWebSocketUrl(token: String): String {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host(Constants.WEBSCOKET_HOST)
            .addPathSegment(Constants.WEBSOCKET_SIGNALR_PATH)
            .addPathSegment(Constants.WEBSOCKET_CONNECT_PATH)
            .addQueryParameter("transport", Constants.WEBSCOKET_TRANSPORT)
            .addQueryParameter("connectionToken", token)
            .addQueryParameter("connectionData", Constants.HUB_DATA)
            .addQueryParameter("clientProtocol", Constants.WEBSCOKET_PROTOCOL)
            .build().toUrl()
        return url.toString().replace("https", "wss")
    }

    fun getColorFromSector(sectorValue: SectorValue?): Color = when {
        sectorValue == null -> Color.Transparent
        sectorValue.overallFastest == true -> Color(0xFF7A30A2)
        sectorValue.personalFastest == true -> Color(0xFF33B353)
        else -> Color(0xC8FFFFFF)
    }

    fun getTiresIcon(compound: Compound): Int = when(compound) {
        Compound.SOFT -> R.drawable.ic_soft_tires
        Compound.MEDIUM -> R.drawable.ic_medium_tires
        Compound.HARD -> R.drawable.ic_hard_tires
        Compound.INTER -> R.drawable.ic_inter_tires
        Compound.WET -> R.drawable.ic_wet_tires
        Compound.UNKNOWN -> R.drawable.ic_unknown_tires
    }
}

fun ByteArray.zlibDecompress(): String {
    val inflater = Inflater(true) // Given data is without any header - only raw data

    val decompressedStream = ByteArrayOutputStream()

    return decompressedStream.use {
        val buffer = ByteArray(1024)

        inflater.setInput(this)

        // Decompress data in chunks of 1024 bytes until end of the data
        var count = -1
        while (count != 0) {
            count = inflater.inflate(buffer)
            decompressedStream.write(buffer, 0, count)
        }

        inflater.end()

        decompressedStream.toString(Charsets.UTF_8.name())
    }
}


fun TimingDataDto.toListTimingData(): List<TimingData> {
    val timingAppDatas = mutableListOf<TimingData>()
    for ((key, value) in this.lines) {
        val driver = F1Driver.getDriverByNumber(key)
        val gapToLoader = value.gap
        val gapToNext = value.interval?.value
        val lastLapTime = value.lastLap?.value
        val fastestLap = value.lastLap?.overallFastest
        val sector = value.sector
        timingAppDatas.add(TimingData(driver, gapToLoader, gapToNext, lastLapTime, fastestLap, sector))
    }
    return timingAppDatas
}

fun Offset.times(operand: Offset): Offset = Offset(x * operand.x, y * operand.y)

fun TimingAppDataDto.toListTimingAppData(): List<TimingAppData> {
    val timingAppDatas = mutableListOf<TimingAppData>()
    for ((key, value) in this.lapInfo) {
        val driver = F1Driver.getDriverByNumber(key)
        val pitstopCount = value.stints.keys.first()
        val stint = value.stints[pitstopCount]!!
        val compound = Compound.valueOf(stint.compound ?: "")
        val lapTime = stint.lapTime
        val lapNumber = stint.lapNumber
        val isNew = stint.newTires
        val tiresAge = stint.tiresAge
        val currentPos = value.position
        timingAppDatas.add(TimingAppData(driver, pitstopCount, Tires(compound, isNew, tiresAge), currentPos, lapNumber, lapTime))
    }
    return timingAppDatas
}

fun PositionDataDto.toPositionDataList(): List<PositionData> {
    return entries.map { entry ->
        PositionData(
            entry.time,
            entry.cars.entries.map {
                PositionOnTrack(
                    F1Driver.getDriverByNumber(it.key), it.value.xPosition, it.value.yPosition
                )
            }
        )
    }
}