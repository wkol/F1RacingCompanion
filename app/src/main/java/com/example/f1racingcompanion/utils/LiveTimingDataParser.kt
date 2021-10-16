package com.example.f1racingcompanion.utils

import com.example.f1racingcompanion.data.cardatadto.CarDataDto
import com.example.f1racingcompanion.data.liveTimingData.LiveTimingData
import com.example.f1racingcompanion.data.positiondatadto.PositionDataDto
import com.example.f1racingcompanion.data.timingdatadto.TimingDataDto
import com.example.f1racingcompanion.data.timingstatsdto.TimingStatsDto
import com.squareup.moshi.*
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

class LiveTimingDataParser(
    private val timingStatsAdapter: JsonAdapter<TimingStatsDto>,
    private val timingDataAdapter: JsonAdapter<TimingDataDto>,
    private val carDataAdapter: JsonAdapter<CarDataDto>,
    private val positionDataAdapter: JsonAdapter<PositionDataDto>
) : JsonAdapter<LiveTimingData<*>>() {
    @FromJson
    override fun fromJson(reader: JsonReader): LiveTimingData<*>? {
        if (reader.hasNext()) {
            val token = reader.peek()
            if (token == JsonReader.Token.BEGIN_ARRAY) {
                reader.beginArray()
                val name = reader.nextString()
                val data = reader.readJsonValue()
                val date = SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                    Locale.US
                ).parse(reader.nextString())
                reader.endArray()
                return when (name) {
                    "TimingStats" -> LiveTimingData<TimingStatsDto>(
                        name,
                        timingStatsAdapter.lenient().fromJsonValue(data),
                        date
                    )
                    "TimingData" -> LiveTimingData<TimingDataDto>(
                        name,
                        timingDataAdapter.lenient().fromJsonValue(data),
                        date
                    )
                    "CarData.z" -> LiveTimingData<CarDataDto>(
                        name,
                        carDataAdapter.lenient().fromJson(LiveTimingUtils.decodeMessage(data.toString())),
                        date
                    )
                    "Position.z" -> LiveTimingData<PositionDataDto>(
                        name,
                        positionDataAdapter.lenient().fromJson(LiveTimingUtils.decodeMessage(data.toString())),
                        date
                    )
                    else -> null
                }
            }
        }
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
                val dataAdapter = moshi.adapter<TimingDataDto>(TimingDataDto::class.java)
                val statsAdapter = moshi.adapter<TimingStatsDto>(TimingStatsDto::class.java)
                val carDataAdapter = moshi.adapter<CarDataDto>(CarDataDto::class.java)
                val positionDataAdapter = moshi.adapter<PositionDataDto>(PositionDataDto::class.java)
                return LiveTimingDataParser(statsAdapter, dataAdapter, carDataAdapter, positionDataAdapter)
            }
            return null
        }
    }
}
