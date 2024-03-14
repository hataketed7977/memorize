package com.mavi.memorize.ui.components

import com.mavi.memorize.data.MemorizeRecord
import com.mavi.memorize.data.entity.view.FamiliarVocabulary
import com.mavi.memorize.ui.helper.addThemeVars
import com.mavi.memorize.ui.helper.cellAlign
import com.mavi.memorize.ui.helper.centerHeader
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.provider.Query
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoField
import java.util.stream.Stream


class CalendarWeek {
    var sun: CalendarDate = CalendarDate()
    var mon: CalendarDate = CalendarDate()
    var tue: CalendarDate = CalendarDate()
    var wed: CalendarDate = CalendarDate()
    var thu: CalendarDate = CalendarDate()
    var fri: CalendarDate = CalendarDate()
    var sat: CalendarDate = CalendarDate()
}

data class CalendarDate(
    val value: LocalDate? = null,
    val dayOfMonth: Int? = null,
    val isToday: Boolean = false,
    val memorizeRecords: List<FamiliarVocabulary> = listOf()
)

class CalendarGrid(
    private val todayDate: LocalDate = LocalDate.now(),
    private val fetchMemorizeRecords: (year: Int, month: Int) -> List<MemorizeRecord>
) : VerticalLayout() {
    private val grid = Grid(CalendarWeek::class.java, false)
    private var currentDate: LocalDate = LocalDate.now()
    private var month = H2(currentDate.month.name)
    private var year = H2(currentDate.year.toString())
    private var dateClickListener: ((date: CalendarDate) -> Unit)? = null
    private var selectedDate: LocalDate? = LocalDate.now()
    private val items: MutableMap<CalendarDate, Button> = mutableMapOf()

    init {
        setWidthFull()
        addClassNames("px-l", "box-border")
        configColumns()

        val title = HorizontalLayout(year, month)
        val actions = createActions()

        add(title, actions, grid)
        setHorizontalComponentAlignment(FlexComponent.Alignment.END, actions)
    }

    private fun createActions(): HorizontalLayout {
        val prev = Button("Previous", Icon(VaadinIcon.ARROW_LEFT))
        prev.addClickListener {
            this.currentDate = this.currentDate.minusMonths(1)
            refreshGrid()
        }

        val today = Button("Today", Icon(VaadinIcon.CALENDAR))
        today.addThemeVars(ButtonVariant.LUMO_PRIMARY)
        today.addClickListener {
            this.currentDate = todayDate
            this.selectedDate = todayDate
            refreshGrid()
        }

        val next = Button("Next", Icon(VaadinIcon.ARROW_RIGHT))
        next.isIconAfterText = true
        next.addClickListener {
            this.currentDate = this.currentDate.plusMonths(1)
            refreshGrid()
        }

        val actions = HorizontalLayout(prev, today, next)
        return actions
    }

    private fun configColumns() {
        grid.addComponentColumn { displayCalendarDate(it.mon) }.centerHeader(H3("Monday")).cellAlign()
        grid.addComponentColumn { displayCalendarDate(it.tue) }.centerHeader(H3("Tuesday")).cellAlign()
        grid.addComponentColumn { displayCalendarDate(it.wed) }.centerHeader(H3("Wednesday")).cellAlign()
        grid.addComponentColumn { displayCalendarDate(it.thu) }.centerHeader(H3("Thursday")).cellAlign()
        grid.addComponentColumn { displayCalendarDate(it.fri) }.centerHeader(H3("Friday")).cellAlign()
        grid.addComponentColumn { displayCalendarDate(it.sat) }.centerHeader(H3("Saturday")).cellAlign()
        grid.addComponentColumn { displayCalendarDate(it.sun) }.centerHeader(H3("Sunday")).cellAlign()
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT)
        grid.setItems { fetchData(it) }
        grid.setSelectionMode(Grid.SelectionMode.NONE)
    }

    private fun fetchData(query: Query<CalendarWeek, *>): Stream<CalendarWeek> {
        VaadinSpringDataHelpers.toSpringPageRequest(query)
        items.clear()
        val memorizeRecords = fetchMemorizeRecords(currentDate.year, currentDate.monthValue).toLocalDateMap()
        return calendarDates(currentDate, memorizeRecords).stream()
    }

    fun calendarDates(
        current: LocalDate = LocalDate.now(),
        memorizeRecords: Map<LocalDate, List<FamiliarVocabulary>>
    ): List<CalendarWeek> {
        val data = mutableListOf<CalendarWeek>()
        val firstDay = LocalDate.of(current.year, current.monthValue, 1)
        val lastDay = LocalDate.of(current.year, current.monthValue, firstDay.lengthOfMonth())
        val lastWeek = lastDay[ChronoField.ALIGNED_WEEK_OF_MONTH]
        var curDay = firstDay
        while (data.size < lastWeek) {
            val calendarWeek = CalendarWeek()
            val curDayOfWeek = curDay.dayOfWeek.value
            repeat(8 - curDayOfWeek) {
                when (curDay.dayOfWeek) {
                    DayOfWeek.MONDAY -> calendarWeek.mon = calendarDate(curDay, memorizeRecords)
                    DayOfWeek.TUESDAY -> calendarWeek.tue = calendarDate(curDay, memorizeRecords)
                    DayOfWeek.WEDNESDAY -> calendarWeek.wed = calendarDate(curDay, memorizeRecords)
                    DayOfWeek.THURSDAY -> calendarWeek.thu = calendarDate(curDay, memorizeRecords)
                    DayOfWeek.FRIDAY -> calendarWeek.fri = calendarDate(curDay, memorizeRecords)
                    DayOfWeek.SATURDAY -> calendarWeek.sat = calendarDate(curDay, memorizeRecords)
                    DayOfWeek.SUNDAY -> calendarWeek.sun = calendarDate(curDay, memorizeRecords)
                    else -> Unit
                }
                if (curDay == lastDay) return@repeat
                curDay = curDay.plusDays(1)
            }
            data.add(calendarWeek)
        }
        return data
    }

    private fun calendarDate(curDay: LocalDate, memorizeRecords: Map<LocalDate, List<FamiliarVocabulary>>) =
        CalendarDate(curDay, curDay.dayOfMonth, todayDate == curDay, memorizeRecords[curDay] ?: listOf())

    private fun displayCalendarDate(date: CalendarDate): Component {
        return if (date.dayOfMonth != null) {
            val btn = Button("${date.dayOfMonth}")
            btn.width = "90px"
            if (date.memorizeRecords.isNotEmpty()) {
                btn.icon = Icon(VaadinIcon.CHECK)
            }
            items[date] = btn
            renderSelection(date, btn)
            btn.addClickListener {
                selectedDate = date.value
                items.forEach { (k, v) ->
                    renderSelection(k, v)
                }
            }
            btn
        } else Span()
    }

    private fun renderSelection(date: CalendarDate, btn: Button) {
        if (selectedDate != date.value) {
            defaultBtnTheme(date, btn)
            return
        }

        if (date.isToday) {
            btn.addThemeVars(ButtonVariant.LUMO_PRIMARY)
        } else if (date.memorizeRecords.isNotEmpty()) {
            btn.addThemeVars(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS)
        } else {
            btn.addThemeVars(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST)
        }
        dateClickListener?.invoke(date)
    }

    private fun defaultBtnTheme(date: CalendarDate, btn: Button) {
        if (date.memorizeRecords.isNotEmpty()) {
            btn.addThemeVars(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SUCCESS)
        } else {
            btn.addThemeVars(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_CONTRAST)
        }

        if (date.isToday) {
            btn.addThemeVars(ButtonVariant.LUMO_TERTIARY)
        }
    }


    private fun refreshGrid() {
        grid.dataProvider.refreshAll()
        month.text = currentDate.month.name
        year.text = currentDate.year.toString()
    }

    fun addDateClickListener(click: (date: CalendarDate) -> Unit) {
        dateClickListener = click
    }
}

fun List<MemorizeRecord>.toLocalDateMap(): Map<LocalDate, List<FamiliarVocabulary>> {
    val map = mutableMapOf<LocalDate, List<FamiliarVocabulary>>()
    forEach {
        val key = LocalDate.of(it.year, it.month, it.day)
        val value = map[key]
        if (value != null) {
            map[key] = (value + it.words).distinct()
        } else {
            map[key] = it.words
        }
    }
    return map
}
