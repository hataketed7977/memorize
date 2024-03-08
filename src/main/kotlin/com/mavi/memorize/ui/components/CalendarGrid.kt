package com.mavi.memorize.ui.components

import com.mavi.memorize.ui.helper.header
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.html.H2
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


class CalendarDate {
    var sun: Int? = null
    var mon: Int? = null
    var tue: Int? = null
    var wed: Int? = null
    var thu: Int? = null
    var fri: Int? = null
    var sat: Int? = null
}

class CalendarGrid : VerticalLayout() {
    private val grid = Grid(CalendarDate::class.java, false)
    private var currentDate: LocalDate = LocalDate.now()
    private var month = H2(currentDate.month.name)
    private var year = H2(currentDate.year.toString())

    init {
        setSizeFull()
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
        today.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        today.addClickListener {
            this.currentDate = LocalDate.now()
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
        grid.addColumn(CalendarDate::sun).header("Sunday")
        grid.addColumn(CalendarDate::mon).header("Monday")
        grid.addColumn(CalendarDate::tue).header("Tuesday")
        grid.addColumn(CalendarDate::wed).header("Wednesday")
        grid.addColumn(CalendarDate::thu).header("Thursday")
        grid.addColumn(CalendarDate::fri).header("Friday")
        grid.addColumn(CalendarDate::sat).header("Saturday")

        //TODO add exam record
        //TODO click see details
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT)
        grid.setItems {
            fetchData(it)
        }
    }

    private fun fetchData(query: Query<CalendarDate, *>): Stream<CalendarDate> {
        VaadinSpringDataHelpers.toSpringPageRequest(query)
        return calendarDates(currentDate).stream()
    }

    fun calendarDates(today: LocalDate = LocalDate.now()): List<CalendarDate> {
        val data = mutableListOf<CalendarDate>()
        val firstDay = LocalDate.of(today.year, today.monthValue, 1)
        val lastDay = LocalDate.of(today.year, today.monthValue, firstDay.lengthOfMonth())
        val lastWeek = lastDay[ChronoField.ALIGNED_WEEK_OF_MONTH]

        var curDay = firstDay
        while (data.size < lastWeek) {
            val calendarDate = CalendarDate()
            val curDayOfWeek = curDay.dayOfWeek.value
            repeat(8 - curDayOfWeek) {
                when (curDay.dayOfWeek) {
                    DayOfWeek.MONDAY -> calendarDate.mon = curDay.dayOfMonth
                    DayOfWeek.TUESDAY -> calendarDate.tue = curDay.dayOfMonth
                    DayOfWeek.WEDNESDAY -> calendarDate.wed = curDay.dayOfMonth
                    DayOfWeek.THURSDAY -> calendarDate.thu = curDay.dayOfMonth
                    DayOfWeek.FRIDAY -> calendarDate.fri = curDay.dayOfMonth
                    DayOfWeek.SATURDAY -> calendarDate.sat = curDay.dayOfMonth
                    DayOfWeek.SUNDAY -> calendarDate.sun = curDay.dayOfMonth
                    else -> Unit
                }
                curDay = curDay.plusDays(1)
            }
            data.add(calendarDate)
        }
        return data
    }

    private fun refreshGrid() {
        grid.dataProvider.refreshAll()
        month.text = currentDate.month.name
        year.text = currentDate.year.toString()
    }
}
