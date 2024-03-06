package com.mavi.memorize.ui.pages

import com.mavi.memorize.api.VocabulariesApi
import com.mavi.memorize.data.entity.Vocabulary
import com.mavi.memorize.ui.components.AddVocabularyDialog
import com.mavi.memorize.ui.components.VocabulariesFilter
import com.mavi.memorize.ui.components.VocabulariesGrid
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dependency.Uses
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.provider.Query
import com.vaadin.flow.data.provider.QuerySortOrder
import com.vaadin.flow.data.provider.SortDirection
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.RouteAlias
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers
import org.vaadin.lineawesome.LineAwesomeIcon
import java.util.stream.Stream


@Route(value = "vocabularies", layout = MainView::class)
@RouteAlias(value = "", layout = MainView::class)
@Uses(Icon::class)
class VocabulariesView(
    private val api: VocabulariesApi
) : VerticalLayout() {
    private val filter = VocabulariesFilter { refreshGrid() }
    private val grid: VocabulariesGrid = VocabulariesGrid(api, false) { fetchData(it) }
    private val addVocabularyDialog = AddVocabularyDialog(api) { refreshGrid() }

    init {
        setSizeFull()
        val btn = Div(addNewWordBtn())
        btn.addClassName("px-l")
        add(filter, btn, grid)
    }


    private fun addNewWordBtn(): Button {
        val addBtn = Button("Add New Word")
        addBtn.icon = LineAwesomeIcon.PLUS_SOLID.create()
        addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS)
        addBtn.addClickListener {
            addVocabularyDialog.open()
        }
        return addBtn
    }

    private fun refreshGrid() {
        grid.refreshGrid()
    }

    private fun fetchData(query: Query<Vocabulary, *>): Stream<Vocabulary> {
        if (query.sortOrders.isEmpty())
            query.sortOrders.add(QuerySortOrder("word", SortDirection.ASCENDING))

        return api.findByPage(
            word = filter.wordValue(),
            study = filter.studyValue(),
            pageRequest = VaadinSpringDataHelpers.toSpringPageRequest(query)
        ).stream()
    }
}
