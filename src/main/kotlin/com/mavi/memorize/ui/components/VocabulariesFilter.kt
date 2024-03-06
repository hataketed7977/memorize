package com.mavi.memorize.ui.components

import com.vaadin.flow.component.Key
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.dom.Style
import com.vaadin.flow.theme.lumo.LumoUtility


class VocabulariesFilter(private val onSearch: Runnable) : FormLayout() {
    private val word = createWordText()
    private val study = createStudySelect()

    init {
        setWidthFull()
        addClassName("filter-layout")
        addClassNames(
            LumoUtility.Padding.Horizontal.LARGE,
            LumoUtility.Padding.Vertical.MEDIUM,
            LumoUtility.BoxSizing.BORDER
        )
        val search = createSearchActions()
        add(word, study, Div(), search)
    }

    private fun createWordText(): TextField {
        val word = TextField("Word", "Type the word")
        word.addKeyPressListener {
            if (it.key == Key.ENTER) {
                onSearch.run()
            }
        }
        return word
    }

    private fun createStudySelect(): Select<String> {
        val study = Select<String>()
        study.label = "Study"
        study.setItems("Yes", "No")
        study.isEmptySelectionAllowed = true
        study.placeholder = "Has Study"
        return study
    }

    private fun createSearchActions(): Div {
        val resetBtn = Button("Reset")
        resetBtn.addClickListener {
            word.clear()
            study.clear()
            onSearch.run()
        }
        val searchBtn = Button("Search")
        searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        searchBtn.addClickListener { onSearch.run() }

        val actions = Div(resetBtn, searchBtn)
        actions.setWidthFull()
        actions.addClassName(LumoUtility.Gap.SMALL)
        actions.style.setDisplay(Style.Display.FLEX)
        actions.style.setJustifyContent(Style.JustifyContent.FLEX_END)
        actions.style.setAlignItems(Style.AlignItems.FLEX_END)
        return actions
    }

    fun wordValue(): String? {
        if (word.value.trim() == "") return null
        return word.value
    }

    fun studyValue(): Boolean? {
        return study.value?.let { it == "Yes" }
    }
}
