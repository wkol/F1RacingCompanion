package com.example.f1racingcompanion.utils

import android.util.Base64
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.example.f1racingcompanion.R
import com.example.f1racingcompanion.data.cardatadto.CarDataDto
import com.example.f1racingcompanion.data.liveTimingData.PreviousData
import com.example.f1racingcompanion.data.nextsessiondto.EventSessionDto
import com.example.f1racingcompanion.data.nextsessiondto.F1EventDto
import com.example.f1racingcompanion.data.positiondatadto.PositionDataDto
import com.example.f1racingcompanion.data.timingappdatadto.TimingAppDataDto
import com.example.f1racingcompanion.data.timingdatadto.BestLap
import com.example.f1racingcompanion.data.timingdatadto.SectorValue
import com.example.f1racingcompanion.data.timingdatadto.TimingDataDto
import com.example.f1racingcompanion.model.Compound
import com.example.f1racingcompanion.model.F1DriverListElement
import com.example.f1racingcompanion.model.NextSession
import com.example.f1racingcompanion.model.PositionData
import com.example.f1racingcompanion.model.PositionOnTrack
import com.example.f1racingcompanion.model.RaceScheduleItem
import com.example.f1racingcompanion.model.TelemetryInfo
import com.example.f1racingcompanion.model.TimingAppData
import com.example.f1racingcompanion.model.TimingData
import com.example.f1racingcompanion.model.Tires
import okhttp3.HttpUrl
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.util.zip.Inflater

object LiveTimingUtils {

    @Throws(IllegalArgumentException::class)
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
        sectorValue == null || sectorValue.value.isNullOrEmpty() -> Color.Transparent
        sectorValue.overallFastest == true -> Color(0xFF7A30A2)
        sectorValue.personalFastest == true -> Color(0xFF33B353)
        else -> Color(0x97FFFFFF)
    }

    fun getTiresIcon(compound: Compound): Int = when (compound) {
        Compound.SOFT -> R.drawable.ic_soft_tires
        Compound.MEDIUM -> R.drawable.ic_medium_tires
        Compound.HARD -> R.drawable.ic_hard_tires
        Compound.INTERMEDIATE -> R.drawable.ic_inter_tires
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

fun TimingDataDto.toListTimingData(): List<TimingData> = this.lines.filterNot { entry ->
    entry.value.sector?.values?.any { it.segments != null } == true
}.map {
    val toFirst: String?
    val toNext: String?
    if (it.value.qualyfingStats != null) {
        val key = it.value.qualyfingStats!!.toList().last().first
        toFirst = it.value.qualyfingStats!![key]!!.timeDiffToFastest
        toNext = it.value.qualyfingStats!![key]!!.timeDiffToNext
    } else {
        toFirst = it.value.gap ?: it.value.timeDiffToFastest
        toNext = it.value.interval?.value ?: it.value.timeDiffToNext
    }
    TimingData(
        driverNum = it.key,
        gapToLeader = toFirst,
        gapToNext = toNext,
        lastLapTime = it.value.lastLap?.value,
        fastestLap = it.value.bestLapTime,
        sector = it.value.sector ?: emptyMap(),
        position = it.value.position ?: it.value.linePosition,
        inPit = it.value.inPit,
        retired = it.value.retired,
        pits = it.value.pitsNum,
        overallFastest = it.value.lastLap?.overallFastest,
        knockedOut = it.value.knockedOut,
    )
}

fun TimingAppDataDto.toListTimingAppData(): List<TimingAppData> = this.lapInfo.map {
    val stint = it.value.stints.entries.firstOrNull()?.value
    TimingAppData(
        driverNum = it.key,
        currentTires = Tires(
            currentCompound = Compound.valueOf(stint?.compound ?: "UNKNOWN"),
            isNew = stint?.newTires?.toBooleanStrictOrNull(),
            tyreAge = stint?.tiresAge
        ),
        lapNumber = stint?.lapNumber,
        lapTime = stint?.lapTime,
        position = it.value.position
    )
}

fun Offset.times(operand: Offset): Offset = Offset(x * operand.x, y * operand.y)

fun PositionDataDto.toPositionDataList(lastTimestamp: Long = LocalTime.parse(entries.first().time, Constants.DEFAULT_DATETIME_FORMATTER).getLong(ChronoField.MILLI_OF_DAY)): List<PositionData> {
    var firstTime = lastTimestamp
    return entries.map { entry ->
        val time = LocalTime.parse(entry.time, Constants.DEFAULT_DATETIME_FORMATTER).getLong(ChronoField.MILLI_OF_DAY)
        val timeDiff = time - firstTime
        firstTime = time
        PositionData(
            delayInMilis = timeDiff,
            position = entry.cars.entries.map {
                PositionOnTrack(
                    it.key, it.value.xPosition, it.value.yPosition
                )
            },
            timestamp = time
        )
    }
}

fun PreviousData.toF1DriverListElementList() = this.drivers.map { driver ->
    val timing = this.timingDataDto?.lines?.get(driver.key)
    val timingAppData = this.timingAppDataDto?.lapInfo?.get(driver.key)
    val qualyfingPart = this.timingDataDto?.sessionPart
    val toFirst: String?
    val toNext: String?
    if (qualyfingPart != null) {
        toFirst = timing?.qualyfingStats?.get(qualyfingPart - 1)?.timeDiffToFastest
        toNext = timing?.qualyfingStats?.get(qualyfingPart - 1)?.timeDiffToNext
    } else {
        toFirst = timing?.gap ?: timing?.timeDiffToFastest
        toNext = timing?.interval?.value ?: timing?.timeDiffToNext
    }
    F1DriverListElement(
        lastLapTime = timing?.lastLap?.value ?: "-",
        lastSectors = timing?.sector?.withIndex()
            ?.associateBy({ it.index.toString() }, { it.value })?.toMutableMap() ?: mutableMapOf(),
        tires = timingAppData?.stints?.lastOrNull()?.let {
            Tires(
                currentCompound = Compound.valueOf(it.compound ?: "UNKNOWN"),
                isNew = it.newTires.toBoolean(),
                tyreAge = it.tiresAge
            )
        } ?: Tires(Compound.UNKNOWN, false, 0),
        position = timing?.position ?: -1,
        interval = toNext ?: "-",
        toFirst = toFirst ?: "-",
        bestLap = timing?.bestLapTime ?: BestLap("-", 0),
        inPit = timing?.inPit ?: false,
        retired = timing?.knockedOut ?: timing?.retired ?: false,
        pitstopCount = timing?.pitsNum ?: 0,
        startingGridPos = timingAppData?.gridPos?.toInt() ?: -1,
        firstName = driver.value.firstName,
        lastName = driver.value.lastName,
        carNumber = driver.value.racingNumber,
        shortcut = driver.value.tla,
        team = driver.value.teamName,
        teamColor = driver.value.teamColour.toLong(16) + 0xFF000000,
        isExpanded = false
    )
}

fun EventSessionDto.toNextSession(): NextSession {
    val currentDate = ZonedDateTime.now()
    val names = listOf(
        "First practice",
        "Second practice",
        "Third practice",
        "Qualifying",
        "Sprint",
        "Race"
    )
    val schedule = listOf(
        firstPractice, secondPractice, thirdPractice, qualifying, sprint,
        F1EventDto(
            raceDate,
            raceTime,
        )
    ).mapIndexed { idx, session ->
        session?.let {
            val dateTime = ZonedDateTime.of(
                LocalDate.parse(
                    it.date,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                ),
                LocalTime.parse(it.time, DateTimeFormatter.ofPattern("HH:mm:ss'Z'")),
                ZoneId.of("UTC")
            )
            RaceScheduleItem(currentDate.isBefore(dateTime), names[idx], dateTime)
        }
    }
    return NextSession(
        circuitId = this.circuitInfo.circuitId,
        circuitName = this.circuitInfo.circuitName,
        raceName = this.raceName,
        schedule = schedule.filterNotNull()
    )
}

fun Map<String, SectorValue>.updateSectors(sectors: Map<String, SectorValue>?): Map<String, SectorValue> {
    if (sectors == null) return this
    // Check is it a new lap sectors
    if (sectors["0"]?.value?.isNotEmpty() == true) {
        return mutableMapOf("0" to sectors["0"]!!)
    }
    val validSectors = sectors.filter { it.value.value?.isNotEmpty() == true }
    return (this + validSectors).toMutableMap()
}

fun CarDataDto.toTelemtryInfoList(driverNum: Int, driverShortcut: String, sectors: Map<String, SectorValue>): List<TelemetryInfo> {
    var firstTime = LocalTime.parse(entries.first().time, Constants.DEFAULT_DATETIME_FORMATTER).getLong(ChronoField.MILLI_OF_DAY)
    return entries.map { entry ->
        val time = LocalTime.parse(entry.time, Constants.DEFAULT_DATETIME_FORMATTER)
            .getLong(ChronoField.MILLI_OF_DAY)
        val timeDiff = time - firstTime
        firstTime = time
        TelemetryInfo(
            delayInMilis = timeDiff,
            currentSpeed = entry.cars.getValue(driverNum).telemetry.speed,
            currentRPMValue = entry.cars.getValue(driverNum).telemetry.rpmValue,
            isDRSEnabled = entry.cars.getValue(driverNum).telemetry.DRSValue in 10..14,
            currentThrottleValue = entry.cars.getValue(driverNum).telemetry.throttleValue,
            currentBrakeValue = entry.cars.getValue(driverNum).telemetry.brakeValue,
            currentGear = entry.cars.getValue(driverNum).telemetry.currentGear,
            driverStr = driverShortcut,
            sectors = sectors
        )
    }
}
