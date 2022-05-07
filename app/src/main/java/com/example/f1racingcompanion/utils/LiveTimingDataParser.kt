package com.example.f1racingcompanion.utils

import com.example.f1racingcompanion.data.cardatadto.CarDataDto
import com.example.f1racingcompanion.data.liveTimingData.LiveTimingData
import com.example.f1racingcompanion.data.positiondatadto.PositionDataDto
import com.example.f1racingcompanion.data.timingappdatadto.TimingAppDataDto
import com.example.f1racingcompanion.data.timingdatadto.TimingDataDto
import com.example.f1racingcompanion.data.timingstatsdto.TimingStatsDto
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder

// In the future refactor the method fromJson
class LiveTimingDataParser(
    private val timingStatsAdapter: JsonAdapter<TimingStatsDto>,
    private val timingDataAdapter: JsonAdapter<TimingDataDto>,
    private val timingAppDataAdapter: JsonAdapter<TimingAppDataDto>,
    private val carDataAdapter: JsonAdapter<CarDataDto>,
    private val positionDataAdapter: JsonAdapter<PositionDataDto>,
) : JsonAdapter<LiveTimingData<*>>() {

    private val dateTimeFormatter =
        DateTimeFormatterBuilder().appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
            .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'"))
            .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS"))
            .toFormatter()

    @FromJson
    override fun fromJson(reader: JsonReader): LiveTimingData<*>? {
        reader.beginObject()
        var el: String
        while (reader.hasNext()) {
            try {
                el = reader.nextName()
            } catch (e: Exception) {
                reader.skipValue()
                continue
            }
            if (el == "A") {
                if (reader.hasNext()) {
                    val token = reader.peek()
                    if (token == JsonReader.Token.BEGIN_ARRAY) {
                        reader.beginArray()
                        val name = reader.nextString()
                        val data = reader.readJsonValue()
                        val date = LocalDateTime.parse(
                            reader.nextString(),
                            dateTimeFormatter
                        )
                        reader.endArray()
                        reader.endObject()
                        return when (name) {
                            "TimingStats" -> LiveTimingData.LiveTimingStatsDto(
                                timingStatsAdapter.lenient().fromJsonValue(data),
                                date
                            )
                            "TimingData" -> LiveTimingData.LiveTimingDataDto(
                                timingDataAdapter.lenient().fromJsonValue(data),
                                date
                            )
                            "TimingAppData" -> LiveTimingData.LiveTimingAppDataDto(
                                timingAppDataAdapter.lenient().fromJsonValue(data),
                                date
                            )
                            "CarData.z" -> LiveTimingData.LiveCarDataDto(
                                carDataAdapter.lenient()
                                    .fromJson(LiveTimingUtils.decodeMessage(data.toString())),
                                date
                            )
                            "Position.z" -> LiveTimingData.LivePositionDataDto(
                                positionDataAdapter.lenient()
                                    .fromJson(LiveTimingUtils.decodeMessage(data.toString())),
                                date
                            )
                            else -> null
                        }
                    }
                }
            }
        }
        reader.endObject()
        return null
    }

    override fun toJson(writer: JsonWriter, value: LiveTimingData<*>?) {
        return
    }

    object Factory : JsonAdapter.Factory {
        override fun create(
            type: Type,
            annotations: MutableSet<out Annotation>,
            moshi: Moshi
        ): JsonAdapter<*>? {
            if (annotations.isNotEmpty()) return null
            if (Types.getRawType(type) == LiveTimingData::class.java) {
                val dataAdapter = moshi.adapter(TimingDataDto::class.java)
                val statsAdapter = moshi.adapter(TimingStatsDto::class.java)
                val carDataAdapter = moshi.adapter(CarDataDto::class.java)
                val timingAppDataAdapter = moshi.adapter(TimingAppDataDto::class.java)
                val positionDataAdapter =
                    moshi.adapter(PositionDataDto::class.java)
                return LiveTimingDataParser(
                    statsAdapter,
                    dataAdapter,
                    timingAppDataAdapter,
                    carDataAdapter,
                    positionDataAdapter
                )
            }
            return null
        }
    }
}
