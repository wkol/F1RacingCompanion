package com.example.f1racingcompanion.utils.parsers

import com.example.f1racingcompanion.data.liveTimingData.LiveTimingData
import com.example.f1racingcompanion.data.liveTimingData.PreviousData
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type

class LiveTimingListParser(
    private val liveTimingDataAdapter: JsonAdapter<LiveTimingData>,
    private val previousDataAdapter: JsonAdapter<PreviousData>
) : JsonAdapter<List<LiveTimingData?>>() {
    override fun fromJson(reader: JsonReader): List<LiveTimingData?> {
        val elements = mutableListOf<LiveTimingData?>()
        reader.beginObject()
        if (reader.peek() != JsonReader.Token.NAME) {
            reader.endObject()
            return emptyList()
        }
        var name: String = reader.nextName()
        while (reader.hasNext() && name != "M") {
            if (name == "R") {
                val previousData = previousDataAdapter.fromJson(reader.nextSource())
                reader.skipName()
                reader.skipValue()
                reader.endObject()
                return listOf(previousData)
            }
            reader.skipValue()
            name = reader.nextName()
        }
        reader.beginArray()
        while (reader.peek() != JsonReader.Token.END_ARRAY) {
            elements.add(liveTimingDataAdapter.fromJson(reader))
        }
        reader.endArray()
        reader.endObject()
        return elements
    }

    override fun toJson(writer: JsonWriter, value: List<LiveTimingData?>?) {
        throw NotImplementedError("toJson is not implemented")
    }

    object Factory : JsonAdapter.Factory {
        override fun create(
            type: Type,
            annotations: MutableSet<out Annotation>,
            moshi: Moshi
        ): JsonAdapter<*>? {
            if (annotations.isNotEmpty()) return null
            if (type == Types.newParameterizedType(List::class.java, LiveTimingData::class.java)) {
                val liveTimingDataAdapter = moshi.adapter(LiveTimingData::class.java)
                val previousDataAdapter = moshi.adapter(PreviousData::class.java)
                return LiveTimingListParser(
                    liveTimingDataAdapter,
                    previousDataAdapter
                )
            }
            return null
        }
    }
}
