package com.mavi.memorize.ui.pages

import com.mavi.memorize.api.FamiliarWordsApi
import com.mavi.memorize.api.IncorrectWordsApi
import com.mavi.memorize.api.UnfamiliarWordsApi
import com.mavi.memorize.api.VocabulariesApi
import com.mavi.memorize.data.entity.Vocabulary
import com.mavi.memorize.ui.components.VocabulariesGrid
import com.vaadin.flow.component.dependency.Uses
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.provider.Query
import com.vaadin.flow.data.provider.QuerySortOrder
import com.vaadin.flow.data.provider.SortDirection
import com.vaadin.flow.router.Route
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers
import java.util.stream.Stream


@Route(value = "memorize", layout = MainView::class)
@Uses(Icon::class)
class MemorizeView(
    private val vocabulariesApi: VocabulariesApi,
    private val unfamiliarWordsApi: UnfamiliarWordsApi,
    private val familiarWordsApi: FamiliarWordsApi,
    private val incorrectWordsApi: IncorrectWordsApi
) : VerticalLayout() {
    private val grid = VocabulariesGrid(vocabulariesApi, true) { Stream.empty() }
    private val taskDialog = createTaskDialog()

    init {
        setSizeFull()


//        add(FormLayout(), div)
    }


    private fun createTaskDialog(): Dialog {
        val dialog = Dialog()
//        dialog.setHeader("Delete operation")
//        dialog.setCancelable(true)
//        dialog.addConfirmListener {
//            api.removeVocabularyById(dialog.id.get())
//            refreshGrid()
//        }
        return dialog
    }

    private fun fetchData(query: Query<Vocabulary, *>): Stream<Vocabulary> {
//        if (query.sortOrders.isEmpty())
//            query.sortOrders.add(QuerySortOrder("word", SortDirection.ASCENDING))
//        unfamiliarWordsApi
//        return vocabulariesApi.findByPage(
//            word = filter.wordValue(),
//            study = filter.studyValue(),
//            pageRequest = VaadinSpringDataHelpers.toSpringPageRequest(query)
//        ).stream()
    }
}
