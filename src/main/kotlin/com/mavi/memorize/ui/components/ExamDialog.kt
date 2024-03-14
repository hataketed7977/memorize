package com.mavi.memorize.ui.components

import com.mavi.memorize.api.FilledValue
import com.mavi.memorize.api.VocabulariesApi
import com.mavi.memorize.api.VocabularyId
import com.mavi.memorize.data.entity.Vocabulary
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.confirmdialog.ConfirmDialog
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.textfield.TextField


class ExamDialog(private val api: VocabulariesApi, private val onSearch: Runnable) : Dialog() {
    private val fields = mutableListOf<TextField>()
    private var vocabularies: List<Vocabulary> = api.findExamVocabularies()
    private val submitConfirm = createSubmitConfirmDialog()
    private val exitConfirm = createExitConfirmDialog()

    init {
        isModal = false
        isCloseOnEsc = false
        isCloseOnOutsideClick = false
        setSizeFull()
        addHeader()
        addForm()
        addFooterActions()
    }

    private fun addHeader() {
        header.add(H2("Testing"))
    }

    private fun addForm() {
        val form = FormLayout()
        form.setSizeFull()

        vocabularies.forEach {
            val word = TextField(it.meaning + " (${it.partOfSpeech})")
            word.setId(it.id)
            form.add(word)
            fields.add(word)
        }
        form.setResponsiveSteps(
            FormLayout.ResponsiveStep("0", 1),
            FormLayout.ResponsiveStep("500px", 3)
        )
        add(form)
    }

    private fun addFooterActions() {
        val exitBtn = Button("Exit", Icon(VaadinIcon.EXIT)) {
            exitConfirm.addConfirmListener {
                close()
            }
            exitConfirm.open()
        }

        val submitBtn = Button("Submit", Icon(VaadinIcon.CHECK)) {
            val filled: Map<VocabularyId, FilledValue> = fields.associate { it.id.get() to it.value.trim().lowercase() }
            submitConfirm.setText("Status: [ ${filled.size}/${vocabularies.size} ] filled")
            submitConfirm.addConfirmListener {
                api.checkExamVocabularies(filled)
                onSearch.run()
                close()
            }
            submitConfirm.open()
        }
        exitBtn.addThemeVariants(ButtonVariant.LUMO_ERROR)
        submitBtn.addThemeVariants(ButtonVariant.LUMO_SUCCESS)
        footer.add(exitBtn, submitBtn)
    }

    private fun createSubmitConfirmDialog(): ConfirmDialog {
        val dialog = ConfirmDialog()
        dialog.setHeader("Are you sure to submit?")
        dialog.setCancelable(true)
        return dialog
    }

    private fun createExitConfirmDialog(): ConfirmDialog {
        val dialog = ConfirmDialog()
        dialog.setText("All filled word will be lost.")
        dialog.setHeader("Are you sure to exit?")
        dialog.setCancelable(true)
        return dialog
    }
}
