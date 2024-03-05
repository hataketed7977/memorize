package com.mavi.memorize.ui.pages

import com.mavi.memorize.api.VocabulariesApi
import com.mavi.memorize.data.entity.Vocabulary
import com.mavi.memorize.ui.components.VocabulariesFilter
import com.mavi.memorize.ui.helper.header
import com.mavi.memorize.ui.helper.sort
import com.mavi.memorize.ui.helper.tooltip
import com.mavi.memorize.ui.helper.width
import com.vaadin.flow.component.dependency.Uses
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.provider.Query
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
    private val api: VocabulariesApi
) : VerticalLayout() {
    private val grid = Grid(Vocabulary::class.java, false)
    private val filter = VocabulariesFilter(api) { refreshGrid() }

    companion object {
        private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            .withZone(ZoneId.of("Asia/Shanghai"))

        fun formatCreatedAt(vocabulary: Vocabulary): String = formatter.format(vocabulary.createdAt)
        fun formatStudy(vocabulary: Vocabulary): String = if (vocabulary.study) "Yes" else "No"
    }

    init {
        addClassName("grid-with-filters-view")
        setSizeFull()
        add(filter, createVocabulariesGird())
    }

    private fun createVocabulariesGird(): Grid<Vocabulary> {
        grid.addColumn(Vocabulary::word).header("Word").sort("word")
        grid.addColumn(Vocabulary::meaning).header("Meaning").width(300).tooltip { it.meaning }
        grid.addColumn(Vocabulary::partOfSpeech).header("Part Of Speech")
        grid.addColumn(Vocabulary::pron).header("Pron.")
        grid.addColumn(Vocabulary::sentence).header("Sentence").width(590).tooltip { it.sentence }
        grid.addColumn(VocabulariesView::formatStudy).header("Study").sort("study")
        grid.addColumn(VocabulariesView::formatCreatedAt).header("Created At").sort("createdAt")

        grid.setWidthFull()
        grid.setItems { fetchData(it) }
        grid.addSortListener { refreshGrid() }
        return grid
    }

    private fun fetchData(query: Query<Vocabulary, *>) = api.findByPage(
        word = filter.wordValue(),
        study = filter.studyValue(),
        pageRequest = VaadinSpringDataHelpers.toSpringPageRequest(query)
    ).stream()

    private fun refreshGrid() {
        grid.dataProvider.refreshAll()
    }
}
