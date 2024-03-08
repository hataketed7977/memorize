package com.mavi.memorize.ui.pages

import com.mavi.memorize.api.IncorrectWordsApi
import com.mavi.memorize.data.entity.view.IncorrectVocabulary
import com.mavi.memorize.ui.helper.header
import com.mavi.memorize.ui.helper.sort
import com.mavi.memorize.ui.helper.tooltip
import com.mavi.memorize.ui.helper.width
import com.vaadin.flow.component.dependency.Uses
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.provider.Query
import com.vaadin.flow.data.provider.QuerySortOrder
import com.vaadin.flow.data.provider.SortDirection
import com.vaadin.flow.router.Route
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers


@Route(value = "incorrect", layout = MainView::class)
@Uses(Icon::class)
class IncorrectWordsView(
    private val incorrectWordsApi: IncorrectWordsApi
) : VerticalLayout() {
    private val grid = Grid(IncorrectVocabulary::class.java, false)

    init {
        setSizeFull()
        addClassNames("px-l", "box-border")
        configGridColumns()
        add(grid)
    }

    private fun configGridColumns() {
        grid.addColumn(IncorrectVocabulary::word).header("Word").width(120).sort("word")
        grid.addColumn(IncorrectVocabulary::meaning).header("Meaning").width(200).tooltip { it.meaning }
        grid.addColumn(IncorrectVocabulary::count).header("Error Count").sort("count")
        grid.addColumn(IncorrectVocabulary::partOfSpeech).header("Part Of Speech")
        grid.addColumn(IncorrectVocabulary::pron).header("Pron.")
        grid.addColumn(IncorrectVocabulary::displayUpdatedAt).header("Last Test Time").sort("incorrectUpdatedAt")

        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT)
        grid.setItems { fetchData(it).stream() }
        grid.addSortListener { refreshGrid() }
    }

    private fun fetchData(query: Query<IncorrectVocabulary, *>): List<IncorrectVocabulary> {
        if (query.sortOrders.isEmpty())
            query.sortOrders.add(QuerySortOrder("word", SortDirection.ASCENDING))

        return incorrectWordsApi.findIncorrectVocabularies(VaadinSpringDataHelpers.toSpringPageRequest(query))
            .content
    }

    private fun refreshGrid() {
        grid.dataProvider.refreshAll()
    }
}
