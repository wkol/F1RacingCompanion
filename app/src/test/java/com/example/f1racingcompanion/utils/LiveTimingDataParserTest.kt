package com.example.f1racingcompanion.utils

import com.example.f1racingcompanion.data.cardatadto.CarDataDto
import com.example.f1racingcompanion.data.liveTimingData.LiveTimingData
import com.example.f1racingcompanion.data.positiondatadto.PositionDataDto
import com.example.f1racingcompanion.data.timingappdatadto.TimingAppDataDto
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
    fun jsonToLiveTimingAppData() {
        // Given json string containing LiveTimingData add proper adapter
        val jsonStr =
            "{'H':'Streaming','M':'feed', 'A':['TimingAppData', {\"Lines\":{\"3\":{\"Stints\":{\"1\":{\"TotalLaps\":14}}},\"11\":{\"Stints\":{\"1\":{\"LapFlags\":0,\"Compound\":\"MEDIUM\",\"New\":\"true\",\"TyresNotChanged\":\"0\",\"TotalLaps\":0,\"StartLaps\":0}}},\"33\":{\"Stints\":{\"1\":{\"TotalLaps\":10}}},\"55\":{\"Stints\":{\"1\":{\"TotalLaps\":22}}}}}, '2021-10-08T08:34:53.943Z']}"
        val jsonReader = JsonReader.of(Buffer().writeUtf8(jsonStr))
        val adapter = moshi.adapter<LiveTimingData<TimingAppDataDto>>(
            Types.newParameterizedType(
                LiveTimingData::class.java,
                TimingAppDataDto::class.java
            )
        ).lenient()

        // When parsing data using the moshi custom adapter
        val liveTimingData: LiveTimingData<TimingAppDataDto>? = adapter.fromJson(jsonReader)

        // Then parse data is correct
        Assert.assertEquals(liveTimingData!!.name, "TimingAppData")
        Assert.assertEquals(liveTimingData.date.toString(), "Fri Oct 08 08:34:53 CEST 2021")
        Assert.assertEquals(liveTimingData.data!!.cars[11]!!.stints[1]!!.compound, "MEDIUM")
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

    @Test
    fun jsonToLiveCarData() {
        // Given json string containing LiveTimingData add proper adapter
        val jsonStr =
            "{'H':'Streaming','M':'feed', 'A':['CarData.z', '1ZfNThwxDMffZc4Lir/iZK+ob9BeWvWAKqRWqjhQbmjfvZkZB4Ud72ZIxaEXWJaxx/Hf/tl5mT49Pj/9evgzHb+9TF+ef0zHCQPCTcg3GD8DHSEciW8pU+KUv06H6e7+qTz9MtH84+7n/ePjw+/lizCVh0XgMGH5pPEwPyKHiadjOEwy/7f85vIhnE7L1469YlrtZz/Fni7ai2ufgiz2BGmxT4s9kOMg+gHENQAkXRzE1UFYQ2jt1bEvSQoWf+6cP3vmme34Ch1zCF74gBns/NCe3wkfwHGQA/IaQG6P7wbgCxhpDQCVFwd6OQBfAIg1g6uCvDjwBITkOUCA1QFKT0FETwOyEoKATQnOep6Zk5tBIFzNiTsVTG4LzSVoGYROBtmXQFAtAdJJAHs1vGYvLKbBTNnC57YB/Q6EbOcXavSbz3L28ugTJIha/kKTv5S2/ecFDyHDWsEUtOkAtwO9FiwIiaY/twKqnAVQPFwjZiRKpRR6xMyaaX1b1CZd+4FJaulm2ZRbD5dZWKzUpKWF7KZlBnPAMELLkiPTOnYO79IyqsEq8hgtKVutAI7QMqlaqaj2ArhAy8j/QsscwBLIOAZLQoNlDI2AeQs7n5WpJgBaVs4luYeVSSGv5qiNOexnJbxOGxpjJVsFoE2bKw4+gJVExrrYyqfb7PusBGR1mj9t7S+wksHSl+IYK6GyUrg5QDrXv8PKpKSq3GUlB+t32bKyT8qyG1myqEkW7yJlQrahzLklBe8lZdEKzQGPkLJ0mnXKlpRdTlKqMyYMcvJ1qx7jpERLfZRBTqY6qOIYJxUc0Hjq+Zws63DlJI4slTlUCbBdKuJ2q+mBsnctugBKqp1uk/7/AmWpeut8DU35e2+/QMrXBEo7qNz288LPEsn0H9wqNVgFRmhJeY6fq6SU2yKkcujewxtS8ggpU13LUpMsOt+AL6CS7AqJ0A6V+Y+9qMwVlUNXcFGLHtKmV/qoZHpTJ9IsVHtAads4AQ1dv4nrPG0xj/tBqXZ7RttI3wnKMontOoFhCJQhiYkXe6DxN8qMddBdV8/nZFCTD7a3oV2cROCdg+YCJ6Eu1G84aaA/u9B9AChj3RMSjYEyYr3SwAgok1gAdc95LygBpS4r6eoJTqfvp78=', '2021-10-08T08:34:53.943Z']}"
        val jsonReader = JsonReader.of(Buffer().writeUtf8(jsonStr))
        val adapter = moshi.adapter<LiveTimingData<CarDataDto>>(
            Types.newParameterizedType(
                LiveTimingData::class.java,
                CarDataDto::class.java
            )
        ).lenient()

        // When parsing data using the moshi custom adapter
        val liveTimingData: LiveTimingData<CarDataDto>? = adapter.fromJson(jsonReader)

        // Then parse data is correct
        Assert.assertEquals(liveTimingData!!.name, "CarData.z")
        Assert.assertEquals(liveTimingData.date.toString(), "Fri Oct 08 08:34:53 CEST 2021")
        Assert.assertEquals(liveTimingData.data!!.entries[2].cars[7]!!.telemetry.rmpValue, 8887)
    }

    @Test
    fun jsonToLivePositionData() {
        // Given json string containing LiveTimingData add proper adapter
        val jsonStr =
            "{'H':'Streaming','M':'feed', 'A':['Position.z', 'lZc9bxw5DIb/y9S7Ab9FbZ/6DoiLuxxSGAcXxsFOEG8qY//7aXZEQS6i0TSGDfgFyXfIh9T78uf3t+fr8/fX5fLP+/Lw/PL0dn18+bFcFgLCM+Qz2QPSRewC9ImVM6X8dTktn1+vP5+f3pbL+8Lrjy/Xx+uv8ufyx+vDz8d//yv/8tdyOSMb0Gn5u/xKlP20fF0uRHA7LfJ7FZrYXUOWqyIVhQ7iqJhsYdi8hsFcRDZKDnPmTZWycg2lRZWGJZHjphJPqSspD1SuttV0NoAQcREhDFRGVhNkxSjLVhWOMqTmuTbPfVUNTC8qrRkyAXcW4shDglQzFOLI8F6XD1QZoq4isqpavzHROENK1cSsUdhqBw/tkLvjW2Wcusp42LolRlV5oqqitXOHLiYM79GgiyWjnkoo1Q8j0K47dNT0ybU2Irla1x02rIuyRYqQc9e/aZSiCYeJYtQFy6O2RxauORquY3MPhrfb7bSPG03sWfQQbiSnwI1O44Y3P/DDsAxxQ54DN1m7MDu4Ed9UDgdwwzW9syTOs7gxrLZrZp3GDXggQBPM40ZjmDVa/g7sHdzEoDAytMbYwU35RJXzUiZtFjfuudZFItjVtYMbsfqVE+IR3HA0R5m1edxgbQ5OxvO4IWmzPI8bBgnctEbcx415zFYmn8eNBzjKruhTHOOGgm2sa4mzuOGkgRuwNixTuEm57CI8RBtePd9oQzRLG9wyREqztCnkaLTxedrUQGdHyfO0CQSIfbgCxrTRmqC65lnaqHssWMP+CBjThkmDNoTdUI5pgym2F5j0GQ499FxnUtqmnKBNipVHCjBPm3JbV9pQOytnaNNORGCYpo1K8176s22PNlEZAE3TRpSCNq19aZc2Sq0uoHnaJGi0EZ6mDRgEbRwO0CboW145fIQ2/AmQnZIdwg154AZ99rjRvH2ydg3lHdpIjnVeaBNQw13auMdtQ+1KkV3aiARtxAIBvkebdmJrStNPqXLQtI5X6ULt0CaGkqSdr7pPm7a8AD6oxrSJugSQpmmj1nhoB24bhup8kljm9xT3aJPaDcC9i2PaSKONeHBUJp5SNRbmOAG2dh/SRj1ow/HYm6ANa1xfkA48pTRevwSYZ2mjHmcDW2Djvvh2aEMptmzm/in17fY/', '2021-10-08T08:34:53.943Z']}"
        val jsonReader = JsonReader.of(Buffer().writeUtf8(jsonStr))
        val adapter = moshi.adapter<LiveTimingData<PositionDataDto>>(
            Types.newParameterizedType(
                LiveTimingData::class.java,
                PositionDataDto::class.java
            )
        ).lenient()

        // When parsing data using the moshi custom adapter
        val liveTimingData: LiveTimingData<PositionDataDto>? = adapter.fromJson(jsonReader)

        // Then parse data is correct
        Assert.assertEquals(liveTimingData!!.name, "Position.z")
        Assert.assertEquals(liveTimingData.date.toString(), "Fri Oct 08 08:34:53 CEST 2021")
        Assert.assertEquals(liveTimingData.data!!.entries[3].cars[31]!!.xPosition, -14077F)
    }


}
