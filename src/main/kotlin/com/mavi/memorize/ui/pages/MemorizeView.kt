package com.mavi.memorize.ui.pages

import com.mavi.memorize.api.FamiliarWordsApi
import com.mavi.memorize.api.IncorrectWordsApi
import com.mavi.memorize.api.UnfamiliarWordsApi
import com.mavi.memorize.api.VocabulariesApi
import com.mavi.memorize.data.entity.Vocabulary
import com.mavi.memorize.ui.components.ExamDialog
import com.mavi.memorize.ui.components.VocabulariesGrid
import com.mavi.memorize.ui.helper.Badge
import com.mavi.memorize.ui.helper.intField
import com.mavi.memorize.ui.helper.readOnlyTextField
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dependency.Uses
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.provider.Query
import com.vaadin.flow.router.Route
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers
import org.springframework.data.domain.Sort


@Route(value = "memorize", layout = MainView::class)
@Uses(Icon::class)
class MemorizeView(
    private val vocabulariesApi: VocabulariesApi,
    private val unfamiliarWordsApi: UnfamiliarWordsApi,
    private val familiarWordsApi: FamiliarWordsApi,
    private val incorrectWordsApi: IncorrectWordsApi
) : VerticalLayout() {
    private val grid = VocabulariesGrid(true) { fetchData(it) }
    private val taskDialog = createTaskDialog()

    private val totalCount = readOnlyTextField("Total", Badge.DEFAULT)
    private val studyCount = readOnlyTextField("Study", Badge.SUCCESS)
    private val notStudyCount = readOnlyTextField("Not Study", Badge.CONTRAST)
    private val unfamiliarCount = readOnlyTextField("Unfamiliar", Badge.DEFAULT)
    private val familiarCount = readOnlyTextField("Familiar", Badge.SUCCESS)
    private val incorrectCount = readOnlyTextField("Incorrect", Badge.ERROR)

    init {
        setSizeFull()
        add(countsLayout(), createActions(), grid)
    }

    private fun createActions(): HorizontalLayout {
        val refreshBtn = Button(VaadinIcon.REFRESH.create()) {
            refreshGrid()
        }

        val createBtn = Button("Create New Memorize") {
            taskDialog.open()
        }
        val startBtn = Button("Start Test") {
            val examDialog = ExamDialog(vocabulariesApi) { refreshGrid() }
            examDialog.open()
        }
        startBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        val container = HorizontalLayout(refreshBtn, createBtn, startBtn)
        container.addClassName("px-l")
        container.setWidthFull()
        return container
    }

    private fun countsLayout(): FormLayout {
        val form = FormLayout()
        form.addClassName("px-l")
        form.add(
            totalCount, studyCount, notStudyCount,
            unfamiliarCount, familiarCount, incorrectCount
        )
        form.setResponsiveSteps(
            ResponsiveStep("0", 1),
            ResponsiveStep("500px", 3)
        )
        refreshCounts()
        return form
    }

    private fun createTaskDialog(): Dialog {
        val dialog = Dialog()
        dialog.header.add(H2("Create New Memorize"))

        val content = FormLayout()
        val field = intField("Number of words", "The number of words your want to study", 30)
        content.add(field)
        dialog.add(content)

        val cancelBtn = Button("Cancel") { dialog.close() }
        val saveBtn = Button("Create") {
            unfamiliarWordsApi.batchCreateUnfamiliarWords(field.value)
            refreshGrid()
            dialog.close()
        }
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        dialog.footer.add(cancelBtn, saveBtn)
        return dialog
    }

    private fun fetchData(it: Query<Vocabulary, *>): List<Vocabulary> {
        val pageable = VaadinSpringDataHelpers.toSpringPageRequest(it)
        pageable.withSort(Sort.by(Sort.Order.desc("word")))
        val ids = unfamiliarWordsApi.findAll().map { it.vocabularyId }
        return vocabulariesApi.findAllByIds(ids, pageable).content
    }

    private fun refreshGrid() {
        grid.refreshGrid()
        refreshCounts()
    }

    private fun refreshCounts() {
        val counts = vocabulariesApi.count()
        totalCount.value = "${counts.first}"
        studyCount.value = "${counts.second}"
        notStudyCount.value = "${counts.third}"
        familiarCount.value = "${familiarWordsApi.count()}"
        unfamiliarCount.value = "${unfamiliarWordsApi.count()}"
        incorrectCount.value = "${incorrectWordsApi.count()}"
    }
}
