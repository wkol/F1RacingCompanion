package com.example.f1racingcompanion.di.data.timingappdatadto

import com.google.gson.annotations.SerializedName

data class DriverTimingDto(
    @SerializedName("Lines")
    val driverNum: Int

)

/*
    Timing app data provides the following data channels per sample:
       - LapNumber (float or nan): Current lap number
       - Driver (str): Driver number
       - LapTime (pandas.Timedelta or None): Lap time of last lap
       - Stint (int): Counter for the number of driven stints
       - TotalLaps (float or nan): Total number of laps driven on this set of tires (includes laps driven in
         other sessions!)
       - Compound (str or None): Tire compound
       - New (bool or None): Whether the tire was new when fitted
       - TyresNotChanged (int or None): ??? Probably a flag to mark pit stops without tire changes
       - Time (pandas.Timedelta): Session time
       - LapFlags (float or nan): ??? unknown
       - LapCountTime (None or ???): ??? unknown; no data
       - StartLaps (float or nan): ??? Tire age when fitted (same as 'TotalLaps' in the same sample?!?)
       - Outlap (None or ???): ??? unknown; no data
 */