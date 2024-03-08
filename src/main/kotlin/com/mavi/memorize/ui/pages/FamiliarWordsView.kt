package com.mavi.memorize.ui.pages

import com.mavi.memorize.api.FamiliarWordsApi
import com.mavi.memorize.data.entity.view.FamiliarVocabulary
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


@Route(value = "familiar", layout = MainView::class)
@Uses(Icon::class)
class FamiliarWordsView(
    private val familiarWordsApi: FamiliarWordsApi
) : VerticalLayout() {
    private val grid = Grid(FamiliarVocabulary::class.java, false)

    init {
        setSizeFull()
        addClassNames("px-l", "box-border")
        configGridColumns()
        add(grid)
    }

    private fun configGridColumns() {
        grid.addColumn(FamiliarVocabulary::word).header("Word").width(120).sort("word")
        grid.addColumn(FamiliarVocabulary::meaning).header("Meaning").width(200).tooltip { it.meaning }
        grid.addColumn(FamiliarVocabulary::partOfSpeech).header("Part Of Speech")
        grid.addColumn(FamiliarVocabulary::pron).header("Pron.")
        grid.addColumn(FamiliarVocabulary::displayCreatedAt).header("Created At").sort("familiarCreatedAt")
        grid.addColumn(FamiliarVocabulary::displayRound1).header("Round One")
        grid.addColumn(FamiliarVocabulary::displayRound2).header("Round Two")
        grid.addColumn(FamiliarVocabulary::displayRound3).header("Round Three")
        grid.addColumn(FamiliarVocabulary::displayRound4).header("Round Four")

        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT)
        grid.setItems { fetchData(it).stream() }
        grid.addSortListener { refreshGrid() }
    }

    private fun fetchData(query: Query<FamiliarVocabulary, *>): List<FamiliarVocabulary> {
        if (query.sortOrders.isEmpty())
            query.sortOrders.add(QuerySortOrder("word", SortDirection.ASCENDING))

        return familiarWordsApi.findFamiliarVocabularies(VaadinSpringDataHelpers.toSpringPageRequest(query))
            .content
    }

    private fun refreshGrid() {
        grid.dataProvider.refreshAll()
    }
}
