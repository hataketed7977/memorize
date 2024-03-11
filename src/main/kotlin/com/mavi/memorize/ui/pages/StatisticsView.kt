package com.mavi.memorize.ui.pages

import com.mavi.memorize.api.MemorizeRecordsApi
import com.mavi.memorize.data.entity.MemorizeRecord
import com.mavi.memorize.ui.components.CalendarGrid
import com.vaadin.flow.component.dependency.Uses
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.splitlayout.SplitLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.Route


@Route(value = "statistics", layout = MainView::class)
@Uses(Icon::class)
class StatisticsView(
    private val memorizeRecordsApi: MemorizeRecordsApi
) : VerticalLayout() {
    private var details = FormLayout()

    init {
        setSizeFull()
        val calendar = CalendarGrid { year, month -> fetchData(year, month) }
        calendar.addDateClickListener {
            buildMemorizeRecords(it.memorizeRecords)
        }

        details.setResponsiveSteps(
            FormLayout.ResponsiveStep("0", 1),
            FormLayout.ResponsiveStep("500px", 4)
        )
        details.style.setPadding("20px")
        details.addClassNames("px-l", "box-border")

        val splitLayout = SplitLayout(calendar, details)
        splitLayout.orientation = SplitLayout.Orientation.VERTICAL
        splitLayout.setSizeFull()
        splitLayout.splitterPosition = 55.0
        add(splitLayout)
    }

    private fun buildMemorizeRecords(memorizeRecords: List<String>) {
        details.removeAll()
        details.add(
            memorizeRecords.map {
                val field = TextField()
                field.isReadOnly = true
                field.value = it
                field
            }
        )
    }

    private fun fetchData(year: Int, month: Int): List<MemorizeRecord> {
        return memorizeRecordsApi.findByMonth(year, month)
    }
}
