package com.example.f1racingcompanion.utils

import android.util.Base64
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.example.f1racingcompanion.R
import com.example.f1racingcompanion.data.f1driverlistelementdto.F1DriverListElementDto
import com.example.f1racingcompanion.data.positiondatadto.PositionDataDto
import com.example.f1racingcompanion.data.timingappdatadto.TimingAppDataDto
import com.example.f1racingcompanion.data.timingdatadto.SectorValue
import com.example.f1racingcompanion.data.timingdatadto.TimingDataDto
import com.example.f1racingcompanion.model.Compound
import com.example.f1racingcompanion.model.F1DriverListElement
import com.example.f1racingcompanion.model.PositionData
import com.example.f1racingcompanion.model.PositionOnTrack
import com.example.f1racingcompanion.model.TimingAppData
import com.example.f1racingcompanion.model.TimingData
import com.example.f1racingcompanion.model.Tires
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

    fun getTiresIcon(compound: Compound): Int = when (compound) {
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

fun TimingDataDto.toListTimingData(): List<TimingData> = this.lines.filter { entry ->
    entry.value.sector?.values?.any { it.segments != null } == false
}.map {
    TimingData(
        driverNum = it.key,
        gapToLeader = it.value.gap,
        gapToNext = it.value.interval?.value,
        lastLapTime = it.value.lastLap?.value,
        fastestLap = it.value.bestLapTime,
        sector = it.value.sector,
        position = it.value.position,
        inPit = it.value.inPit,
        retired = it.value.retired,
        pits = it.value.pitsNum,
        overallFastest = it.value.lastLap?.overallFastest
    )
}

fun TimingAppDataDto.toListTimingAppData(): List<TimingAppData> = this.lapInfo.map {
    val stint = it.value.stints.entries.first().value
    TimingAppData(
        driverNum = it.key,
        currentTires = Tires(
            currentCompound = Compound.valueOf(stint.compound ?: ""),
            isNew = stint.newTires.toBoolean(),
            tyreAge = stint.tiresAge
        ),
        startingGridPos = it.value.gridPos?.toInt(),
        lapNumber = stint.lapNumber,
        lapTime = stint.lapTime
    )
}

fun F1DriverListElement.toDto() = F1DriverListElementDto(
    lastLapTime,
    lastSectors,
    tires,
    position,
    interval,
    toFirst,
    bestLap,
    inPit,
    retired,
    pitstopCount,
    startingGridPos,
    firstName,
    lastName,
    carNumber,
    shortcut,
    team,
    teamColor
)

fun Offset.times(operand: Offset): Offset = Offset(x * operand.x, y * operand.y)

fun PositionDataDto.toPositionDataList(): List<PositionData> {
    return entries.map { entry ->
        PositionData(
            entry.time,
            entry.cars.entries.map {
                PositionOnTrack(
                    it.key, it.value.xPosition, it.value.yPosition
                )
            }
        )
    }
}

fun F1DriverListElementDto.toF1DriverElement(): F1DriverListElement = F1DriverListElement(
    lastLapTime,
    lastSectors.toMutableMap(),
    tires,
    position,
    interval,
    toFirst,
    bestLap,
    inPit,
    retired,
    pitstopCount,
    startingGridPos,
    firstName,
    lastName,
    carNumber,
    shortcut,
    team,
    teamColor
)
