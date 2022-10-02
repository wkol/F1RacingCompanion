package com.example.f1racingcompanion.utils.parsers

import com.example.f1racingcompanion.data.liveTimingData.LiveTimingData
import com.example.f1racingcompanion.data.timingappdatadto.TimingAppDataDto
import com.example.f1racingcompanion.data.timingdatadto.TimingDataDto
import com.example.f1racingcompanion.data.timingstatsdto.TimingStatsDto
import com.example.f1racingcompanion.utils.Constants
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type
import java.time.LocalDateTime

// In the future refactor the method fromJson
class LiveTimingDataParser(
    private val timingStatsAdapter: JsonAdapter<TimingStatsDto>,
    private val timingDataAdapter: JsonAdapter<TimingDataDto>,
    private val timingAppDataAdapter: JsonAdapter<TimingAppDataDto>,
) : JsonAdapter<LiveTimingData>() {

    @FromJson
    override fun fromJson(reader: JsonReader): LiveTimingData? {
        if (reader.peek() != JsonReader.Token.BEGIN_OBJECT) {
            return null
        }
        reader.beginObject()
        while (reader.hasNext() && reader.nextName() != "A") {
            reader.skipValue()
        }
        if (reader.peek() == JsonReader.Token.BEGIN_ARRAY) {
            reader.beginArray()
            val name = reader.nextString()
            val data = reader.nextSource().readUtf8()
            val date = LocalDateTime.parse(
                reader.nextString(),
                Constants.DEFAULT_DATETIME_FORMATTER
            )
            reader.endArray()
            reader.endObject()
            return when (name) {
                "TimingStats" -> LiveTimingData.LiveTimingStatsDto(
                    data = timingStatsAdapter.lenient().fromJson(data),
                    date = date
                )
                "TimingData" -> LiveTimingData.LiveTimingDataDto(
                    data = timingDataAdapter.lenient().fromJson(data),
                    date = date
                )
                "TimingAppData" -> LiveTimingData.LiveTimingAppDataDto(
                    data = timingAppDataAdapter.lenient().fromJson(data),
                    date = date
                )
                "CarData.z" -> LiveTimingData.LiveTimingBulkMessage(
                    type = Constants.LiveTimingDataType.CAR_DATA,
                    data = data.substring(1, data.length - 1),
                    date = date
                )
                "Position.z" -> LiveTimingData.LiveTimingBulkMessage(
                    type = Constants.LiveTimingDataType.POSITION_DATA,
                    data = data.substring(1, data.length - 1),
                    date = date
                )
                else -> null
            }
        }
        reader.endObject()
        return null
    }

    override fun toJson(writer: JsonWriter, value: LiveTimingData?) {
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
                val timingAppDataAdapter = moshi.adapter(TimingAppDataDto::class.java)
                return LiveTimingDataParser(
                    statsAdapter,
                    dataAdapter,
                    timingAppDataAdapter,
                )
            }
            return null
        }
    }
}
