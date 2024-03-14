package com.mavi.memorize.ui.components

import com.mavi.memorize.data.MemorizeRecord
import com.mavi.memorize.helper.memorizeRecord
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class CalendarGridTest {

    @Test
    fun `should get calendar dates`() {
        val today = LocalDate.of(2024, 3, 8)
        val dates = CalendarGrid(today) { year, month -> fetchMemorizeRecords(year, month) }
            .calendarDates(today, fetchMemorizeRecords(2024, 3).toLocalDateMap()).toList()
        assertThat(dates.size).isEqualTo(5)

        assertThat(dates[0].mon.dayOfMonth).isNull()
        assertThat(dates[0].thu.dayOfMonth).isNull()
        assertThat(dates[0].fri.dayOfMonth).isEqualTo(1)
        assertThat(dates[0].sat.dayOfMonth).isEqualTo(2)
        assertThat(dates[0].sun.dayOfMonth).isEqualTo(3)

        assertThat(dates[1].mon.dayOfMonth).isEqualTo(4)
        assertThat(dates[1].tue.dayOfMonth).isEqualTo(5)
        assertThat(dates[1].fri.dayOfMonth).isEqualTo(8)
        assertThat(dates[1].fri.isToday).isTrue()
        assertThat(dates[1].sun.dayOfMonth).isEqualTo(10)

        assertThat(dates[2].mon.dayOfMonth).isEqualTo(11)
        assertThat(dates[2].wed.dayOfMonth).isEqualTo(13)
        assertThat(dates[2].sun.dayOfMonth).isEqualTo(17)

        assertThat(dates[3].mon.dayOfMonth).isEqualTo(18)
        assertThat(dates[3].mon.memorizeRecords.map { it.word }).isEqualTo(listOf("test"))
        assertThat(dates[3].thu.dayOfMonth).isEqualTo(21)
        assertThat(dates[3].sun.dayOfMonth).isEqualTo(24)

        assertThat(dates[4].mon.dayOfMonth).isEqualTo(25)
        assertThat(dates[4].fri.dayOfMonth).isEqualTo(29)
        assertThat(dates[4].sun.dayOfMonth).isEqualTo(31)
    }

    @Test
    fun `should merge records when locale date is same`() {
        val map = listOf(
            memorizeRecord(listOf("test", "a")),
            memorizeRecord(listOf("a"))
        ).toLocalDateMap()

        assertThat(map[LocalDate.of(2024, 3, 11)]?.map { it.word })
            .isEqualTo(listOf("test", "a"))
    }

    private fun fetchMemorizeRecords(year: Int, month: Int): List<MemorizeRecord> {
        val records = mutableListOf<MemorizeRecord>()
        repeat(20) {
            records.add(memorizeRecord(year = year, month = month, day = (it + 1)))
        }
        return records
    }
}
