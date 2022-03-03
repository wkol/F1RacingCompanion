package com.example.f1racingcompanion.utils

import com.example.f1racingcompanion.data.liveTimingData.LiveTimingData
import com.example.f1racingcompanion.data.timingdatadto.TimingDataDto
import com.example.f1racingcompanion.data.timingstatsdto.TimingStatsDto
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okio.Buffer
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class LiveTimingDataParserTest {
    private lateinit var moshi: Moshi

    @Before
    fun setUpMoshi() {
        moshi = Moshi.Builder()
            .add(DateParser())
            .add(LiveTimingDataParser.Factory)
            .add(KotlinJsonAdapterFactory()).build()
    }

    @Test
    fun jsonToLiveTimingStats() {
        // Given json string containing LiveTimingData add proper adapter
        val jsonStr =
            "{'H':'Streaming','M':'feed', 'A':['TimingStats', {'Lines': {'7': {'BestSpeeds': {'I1': {'Position': 2}}}, '14': {'BestSpeeds': {'I1': {'Position': 3}}}}}, '2021-10-08T08:30:51.596Z']}"
        val jsonReader = JsonReader.of(Buffer().writeUtf8(jsonStr))
        val adapter = moshi.adapter<LiveTimingData<TimingStatsDto>>(
            Types.newParameterizedType(
                LiveTimingData::class.java,
                TimingStatsDto::class.java
            )
        ).lenient()

        // When parsing data using moshi custom adapter
        val liveTimingData: LiveTimingData<TimingStatsDto>? = adapter.fromJson(jsonReader)

        Assert.assertEquals(liveTimingData!!.name, "TimingStats")
        Assert.assertEquals(liveTimingData.date!!.dayOfMonth, 8)
        Assert.assertEquals(liveTimingData.date!!.second, 51)
        Assert.assertEquals(liveTimingData.date!!.minute, 30)
        Assert.assertEquals(liveTimingData.data!!.lines[7]!!.bestSpeeds!!["I1"]!!.position, 2)
    }

    @Test
    fun jsonToLiveTimingData() {
        // Given json string containing LiveTimingData add proper adapter
        val jsonStr =
            "{'H':'Streaming','M':'feed', 'A':['TimingData', {'Lines': {'3': {'Line': 15, 'Position': '15'}, '4': {'GapToLeader': '+5.804', 'Line': 10, 'Position': '10', 'Speeds': {'ST': {'Value': '321', 'PersonalFastest': True}}}, '5': {'TimeDiffToFastest': '+1.483', 'Line': 5, 'Position': '5'}, '7': {'Line': 14, 'Position': '14'}, '9': {'TimeDiffToFastest': '+6.583', 'Line': 11, 'Position': '11'}, '10': {'TimeDiffToFastest': '+10.859', 'Line': 12, 'Position': '12', 'Sectors': {'1': {'Value': '31.099', 'PersonalFastest': True}}, 'Speeds': {'I2': {'Value': '281', 'PersonalFastest': True}}}, '14': {'Line': 13, 'Position': '13'}, '16': {'TimeDiffToFastest': '+0.018', 'TimeDiffToPositionAhead': '+0.018', 'Line': 2, 'Position': '2', 'LastLapTime': {'OverallFastest': False}}, '18': {'TimeDiffToFastest': '+1.223', 'Line': 4, 'Position': '4'}, '22': {'TimeDiffToFastest': '+3.000', 'Line': 9, 'Position': '9'}, '31': {'TimeDiffToFastest': '+1.732', 'Line': 6, 'Position': '6'}, '47': {'TimeDiffToFastest': '+2.594', 'Line': 8, 'Position': '8'}, '77': {'TimeDiffToFastest': '+0.565', 'Line': 3, 'Position': '3'}, '99': {'TimeDiffToFastest': '+1.958', 'Line': 7, 'Position': '7'}}}, '2021-10-08T08:34:53.943Z']}"
        val jsonReader = JsonReader.of(Buffer().writeUtf8(jsonStr))
        val adapter = moshi.adapter<LiveTimingData<TimingDataDto>>(
            Types.newParameterizedType(
                LiveTimingData::class.java,
                TimingDataDto::class.java
            )
        ).lenient()

        // When parsing data using the moshi custom adapter
        val liveTimingData: LiveTimingData<TimingDataDto>? = adapter.fromJson(jsonReader)
        // Then parse data is correct
        Assert.assertEquals(liveTimingData!!.name, "TimingData")
        Assert.assertEquals(liveTimingData.date!!.dayOfMonth, 8)
        Assert.assertEquals(liveTimingData.date!!.second, 53)
        Assert.assertEquals(liveTimingData.date!!.minute, 34)
        Assert.assertEquals(liveTimingData.data!!.lines[4]!!.gap, "+5.804")
    }
}
