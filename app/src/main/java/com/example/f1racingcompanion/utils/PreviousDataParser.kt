package com.example.f1racingcompanion.utils

import com.example.f1racingcompanion.data.previousdata.DriverInfoDto
import com.example.f1racingcompanion.data.previousdata.PreviousData
import com.example.f1racingcompanion.data.previousdata.PreviousTimingAppDataDto
import com.example.f1racingcompanion.data.previousdata.PreviousTimingDataDto
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type

class PreviousDataParser(
    private val timingAppDataAdapter: JsonAdapter<PreviousTimingAppDataDto>,
    private val timingDataAdapter: JsonAdapter<PreviousTimingDataDto>
) :
    JsonAdapter<PreviousData>() {
    @FromJson
    override fun fromJson(reader: JsonReader): PreviousData {
        reader.beginObject()
        val driverInfoMap = mutableMapOf<Int, DriverInfoDto>()
        var timinAppData: PreviousTimingAppDataDto? = null
        var timingData: PreviousTimingDataDto? = null
        while (reader.hasNext()) {
            try {
                var el = reader.nextName()
                if (el == "DriverList") {
                    reader.beginObject()
                    while (reader.hasNext()) {

                        el = reader.nextName()
                        if (el == "_kf") {
                            reader.skipValue()
                        } else {
                            val json = reader.readJsonValue() as Map<String, String>
                            driverInfoMap[json["RacingNumber"]!!.toInt()] = DriverInfoDto(
                                json["BroadcastName"].toString(),
                                json["CountryCode"]!!,
                                json["FirstName"]!!,
                                json["FullName"]!!,
                                json["LastName"]!!,
                                json["RacingNumber"]!!.toInt(),
                                json["Reference"]!!,
                                json["TeamColour"]!!,
                                json["TeamName"]!!,
                                json["Tla"]!!
                            )
                        }
                        if (reader.peek() == JsonReader.Token.END_OBJECT) {
                            reader.endObject()
                            break
                        }
                    }
                } else if (el == "TimingAppData") {
                    timinAppData = timingAppDataAdapter.fromJson(reader)!!
                } else if (el == "TimingData") {
                    timingData = timingDataAdapter.fromJson(reader)!!
                } else {
                    reader.skipValue()
                }
            } catch (e: Exception) {
                reader.skipValue()
            }
        }
        reader.endObject()
        return PreviousData(timinAppData, timingData, driverInfoMap.toMap())
    }

    override fun toJson(writer: JsonWriter, value: PreviousData?) {
        return
    }

    object Factory : JsonAdapter.Factory {
        override fun create(
            type: Type,
            annotations: MutableSet<out Annotation>,
            moshi: Moshi
        ): JsonAdapter<*>? {
            if (annotations.isNotEmpty()) return null
            if (Types.getRawType(type) == PreviousData::class.java) {
                val dataAdapter = moshi.adapter(PreviousTimingDataDto::class.java)
                val timingAppDataAdapter = moshi.adapter(PreviousTimingAppDataDto::class.java)
                return PreviousDataParser(
                    timingAppDataAdapter,
                    dataAdapter
                )
            }
            return null
        }
    }
}
