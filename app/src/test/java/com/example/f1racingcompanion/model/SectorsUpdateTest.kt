package com.example.f1racingcompanion.model

import com.example.f1racingcompanion.data.timingdatadto.SectorValue
import com.example.f1racingcompanion.utils.updateSectors
import org.junit.Assert.assertEquals
import org.junit.Test

class SectorsUpdateTest {

    @Test
    fun updateSectors_nullSectors_returnsWithNoChange() {
        val oldSectors = mutableMapOf("0" to SectorValue("12.921", "", false, false, null), "1" to SectorValue("12.921", "", false, false, null))
        val sectors = null

        val newSectors = oldSectors.updateSectors(sectors)

        assertEquals(oldSectors, newSectors)
    }

    @Test
    fun updateSectors_firstSector_returnsOnlyFirstSector() {
        val oldSectors = mapOf("0" to SectorValue("12.921", "", false, false, null), "1" to SectorValue("12.921", "", false, false, null))
        val sectors = mapOf("0" to SectorValue("12.921", "", false, false, null))

        val newSectors = oldSectors.updateSectors(sectors)

        assertEquals(sectors, newSectors)
    }

    @Test
    fun updateSectors_firstSectorBlankValue_returnsOldSectors() {
        val oldSectors = mapOf(
            "0" to SectorValue("12.921", "", false, false, null),
            "1" to SectorValue("12.921", "", false, false, null)
        )
        val sectors = mapOf("0" to SectorValue("", "+231t6", false, false, null))

        val newSectors = oldSectors.updateSectors(sectors)

        assertEquals(oldSectors, newSectors)
    }
    @Test
    fun updateSectors_secondSectorBlankValue_returnsOldSectors() {
        val oldSectors = mapOf(
            "0" to SectorValue("12.921", "", false, false, null),
            "1" to SectorValue("12.921", "", false, false, null)
        )
        val sectors = mapOf("2" to SectorValue("", "+231t6", false, false, null))

        val newSectors = oldSectors.updateSectors(sectors)

        assertEquals(oldSectors, newSectors)
    }

    @Test
    fun updateSectors_addNewSector_returnsMergedSectors() {
        val oldSectors = mapOf(
            "0" to SectorValue("12.921", "", false, false, null),
            "1" to SectorValue("12.921", "", false, false, null)
        )
        val sectors = mapOf("2" to SectorValue("12.921", "", false, false, null))

        val newSectors = oldSectors.updateSectors(sectors)

        assertEquals(sectors + oldSectors, newSectors)
    }
}
