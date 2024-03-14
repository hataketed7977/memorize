package com.mavi.memorize.ui.components

import com.mavi.memorize.api.VocabulariesApi
import com.mavi.memorize.api.request.AddVocabularyRequest
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField


class AddVocabularyDialog(private val api: VocabulariesApi, private val onSearch: Runnable) : Dialog() {
    private val word = TextField("Word")
    private val pron = TextField("Pron.")
    private val meaning = TextArea("Meaning")
    private val partOfSpeech = TextField("Part Of Speech")
    private val sentence = TextArea("Sentence")

    init {
        isModal = false
        isDraggable = true
        addHeader()
        addForm()
        addFooterActions()
    }

    override fun open() {
        clear()
        super.open()
    }

    private fun clear(){
        word.clear()
        pron.clear()
        meaning.clear()
        partOfSpeech.clear()
        sentence.clear()
    }

    private fun addHeader() {
        header.add(H2("New Vocabulary"))
    }

    private fun addForm() {
        val form = FormLayout(word, pron, partOfSpeech, meaning, sentence)
        form.setColspan(meaning, 3)
        form.setColspan(sentence, 3)
        form.setResponsiveSteps(
            FormLayout.ResponsiveStep("0", 1),
            FormLayout.ResponsiveStep("200px", 3)
        )
        add(form)
    }

    private fun addFooterActions() {
        val cancelBtn = Button("Cancel") { close() }
        val saveBtn = Button("Save") {
            api.addVocabulary(
                AddVocabularyRequest(
                    word = word.value.trim(),
                    meaning = meaning.value.trim(),
                    pron = pron.value.trim(),
                    partOfSpeech = partOfSpeech.value.trim(),
                    sentence = sentence.value.trim(),
                )
            )
            onSearch.run()
            close()
        }
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        footer.add(cancelBtn)
        footer.add(saveBtn)
    }
}
