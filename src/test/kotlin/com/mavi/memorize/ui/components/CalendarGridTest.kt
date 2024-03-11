package com.mavi.memorize.ui.components

import com.mavi.memorize.data.entity.MemorizeRecord
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class CalendarGridTest {

    @Test
    fun `should get calendar dates`() {
        val today = LocalDate.of(2024, 3, 8)
        val dates = CalendarGrid { year, month -> fetchMemorizeRecords(year, month) }
            .calendarDates(today, mapOf()).toList()
        assertThat(dates.size).isEqualTo(5)

        assertThat(dates[0].mon).isNull()
        assertThat(dates[0].thu).isNull()
        assertThat(dates[0].fri).isEqualTo(1)
        assertThat(dates[0].sat).isEqualTo(2)
        assertThat(dates[0].sun).isEqualTo(3)

        assertThat(dates[1].mon).isEqualTo(4)
        assertThat(dates[1].tue).isEqualTo(5)
        assertThat(dates[1].sun).isEqualTo(10)

        assertThat(dates[2].mon).isEqualTo(11)
        assertThat(dates[2].wed).isEqualTo(13)
        assertThat(dates[2].sun).isEqualTo(17)

        assertThat(dates[3].mon).isEqualTo(18)
        assertThat(dates[3].thu).isEqualTo(21)
        assertThat(dates[3].sun).isEqualTo(24)

        assertThat(dates[4].mon).isEqualTo(25)
        assertThat(dates[4].fri).isEqualTo(29)
        assertThat(dates[4].sun).isEqualTo(31)
    }

    private fun fetchMemorizeRecords(year: Int, month: Int): List<MemorizeRecord> {
        TODO("Not yet implemented")
    }
}
