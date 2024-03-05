package com.mavi.memorize.ui.components

import com.mavi.memorize.api.VocabulariesApi
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField


class AddVocabularyDialog(private val api: VocabulariesApi) : Dialog() {
    init {
        isModal = false
        isDraggable = true
        addHeader()
        addForm()
        addFooterActions()
    }

    private fun addHeader() {
        header.add(H2("New Vocabulary"))
    }

    private fun addForm() {
        val word = TextField("Word")
        val pron = TextField("Pron.")
        val meaning = TextField("Meaning")
        val partOfSpeech = TextField("Part Of Speech")
        val sentence = TextArea("Sentence")
        val form = FormLayout(word, pron, meaning, partOfSpeech, sentence)
        form.setColspan(sentence, 2)
        add(form)
    }

    private fun addFooterActions() {
        val cancelBtn = Button("Cancel") { close() }
        val saveBtn = Button("Save") {
//            api.addVocabulary()
            close()
        }
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        footer.add(cancelBtn)
        footer.add(saveBtn)
    }
}
