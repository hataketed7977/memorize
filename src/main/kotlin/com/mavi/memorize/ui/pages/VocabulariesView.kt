package com.mavi.memorize.ui.pages

import com.mavi.memorize.ui.model.response.VocabularyResponse
import com.mavi.memorize.ui.model.response.toRepresentation
import com.mavi.memorize.domain.model.Vocabularies
import com.mavi.memorize.ui.components.VocabulariesFilter
import com.vaadin.flow.component.dependency.Uses
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.RouteAlias
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@PageTitle("Vocabularies")
@Route(value = "vocabularies", layout = MainView::class)
@RouteAlias(value = "", layout = MainView::class)
@Uses(Icon::class)
class VocabulariesView(
    private val vocabularies: Vocabularies
) : VerticalLayout() {
    private val grid = Grid(VocabularyResponse::class.java, false)
    private val filter = VocabulariesFilter { refreshGrid() }

    companion object {
        private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            .withZone(ZoneId.of("Asia/Shanghai"))

        fun formatCreatedAt(vocabulary: VocabularyResponse): String = formatter.format(vocabulary.createdAt)
    }

    init {
        addClassName("grid-with-filters-view")
        setSizeFull()
        isPadding = false
        isSpacing = false
        add(filter, createVocabulariesGird())
    }

    private fun createVocabulariesGird(): Grid<VocabularyResponse> {
        grid.addColumn(VocabularyResponse::word).setHeader("Word").setSortable(true)
            .setSortProperty("word")
        grid.addColumn(VocabularyResponse::meaning).setHeader("Meaning").setSortable(false)
        grid.addColumn(VocabularyResponse::partOfSpeech).setHeader("Part Of Speech").setSortable(false)
        grid.addColumn(VocabularyResponse::pron).setHeader("Pron.").setSortable(false)
        grid.addColumn(VocabulariesView::formatCreatedAt).setHeader("Created At").setSortable(true)
            .setSortProperty("createdAt")
        grid.addColumn(VocabularyResponse::study).setHeader("Study").setSortable(true)
            .setSortProperty("study")

        grid.width = "100%"
        grid.setItems { query ->
            vocabularies.findByPage(
                word = filter.wordValue(),
                study = filter.studyValue(),
                pageRequest = VaadinSpringDataHelpers.toSpringPageRequest(query)
            ).map { it.toRepresentation() }.stream()
        }
        grid.addSortListener { refreshGrid() }
        return grid
    }

    private fun refreshGrid() {
        grid.dataProvider.refreshAll()
    }
}
