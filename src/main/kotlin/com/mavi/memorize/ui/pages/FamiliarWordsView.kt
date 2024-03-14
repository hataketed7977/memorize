package com.mavi.memorize.ui.pages

import com.mavi.memorize.api.FamiliarWordsApi
import com.mavi.memorize.data.entity.view.FamiliarVocabulary
import com.mavi.memorize.ui.helper.header
import com.mavi.memorize.ui.helper.sort
import com.mavi.memorize.ui.helper.tooltip
import com.mavi.memorize.ui.helper.width
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.confirmdialog.ConfirmDialog
import com.vaadin.flow.component.dependency.Uses
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.provider.Query
import com.vaadin.flow.data.provider.QuerySortOrder
import com.vaadin.flow.data.provider.SortDirection
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers

@PageTitle("Memorize Hub")
@Route(value = "familiar", layout = MainView::class)
@Uses(Icon::class)
class FamiliarWordsView(
    private val familiarWordsApi: FamiliarWordsApi
) : VerticalLayout() {
    private val grid = Grid(FamiliarVocabulary::class.java, false)
    private val restoreConfirmDialog = confirmDialog()

    init {
        setSizeFull()
        addClassNames("px-l", "box-border")
        configGridColumns()
        add(grid)
    }

    private fun configGridColumns() {
        grid.setSizeFull()
        grid.addColumn(FamiliarVocabulary::word).header("Word").width(120).sort("word").tooltip { it.word }
        grid.addColumn(FamiliarVocabulary::meaning).header("Meaning").width(200).tooltip { it.meaning }
        grid.addColumn(FamiliarVocabulary::partOfSpeech).header("Part Of Speech")
        grid.addColumn(FamiliarVocabulary::pron).header("Pron.")
        grid.addColumn(FamiliarVocabulary::displayCreatedAt).header("Created At").sort("familiarCreatedAt")
        grid.addColumn(FamiliarVocabulary::displayRound1).header("Round One")
        grid.addColumn(FamiliarVocabulary::displayRound2).header("Round Two")
        grid.addColumn(FamiliarVocabulary::displayRound3).header("Round Three")
        grid.addColumn(FamiliarVocabulary::displayRound4).header("Round Four")
        grid.addComponentColumn { createDeleteBtn(it) }.header("Operations")

        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT)
        grid.setItems { fetchData(it).stream() }
        grid.addSortListener { refreshGrid() }
    }

    private fun createDeleteBtn(vocabulary: FamiliarVocabulary): Button {
        val restoreBtn = Button("Restore")
        restoreBtn.addThemeVariants(
            ButtonVariant.LUMO_ICON,
            ButtonVariant.LUMO_SMALL,
            ButtonVariant.LUMO_ERROR,
            ButtonVariant.LUMO_TERTIARY
        )
        restoreBtn.icon = Icon(VaadinIcon.TIME_BACKWARD)
        restoreBtn.addClickListener {
            restoreConfirmDialog.setId(vocabulary.id)
            restoreConfirmDialog.setText("Are you sure you want to restore word【${vocabulary.word}】to not study?")
            restoreConfirmDialog.open()
        }
        return restoreBtn
    }

    private fun confirmDialog(): ConfirmDialog {
        val dialog = ConfirmDialog()
        dialog.setHeader("Restore operation")
        dialog.setCancelable(true)
        dialog.addConfirmListener {
            familiarWordsApi.removeByVocabularyId(dialog.id.get())
            refreshGrid()
        }
        return dialog
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
