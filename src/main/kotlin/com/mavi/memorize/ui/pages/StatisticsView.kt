package com.mavi.memorize.ui.pages

import com.mavi.memorize.api.MemorizeRecordsApi
import com.mavi.memorize.data.entity.MemorizeRecord
import com.mavi.memorize.ui.components.CalendarGrid
import com.vaadin.flow.component.dependency.Uses
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route


@Route(value = "statistics", layout = MainView::class)
@Uses(Icon::class)
class StatisticsView(
    private val memorizeRecordsApi: MemorizeRecordsApi
) : VerticalLayout() {

    init {
        setSizeFull()
        add(CalendarGrid { year, month -> fetchData(year, month) })
    }

    private fun fetchData(year: Int, month: Int): List<MemorizeRecord> {
        return memorizeRecordsApi.findByMonth(year, month)
    }
}
