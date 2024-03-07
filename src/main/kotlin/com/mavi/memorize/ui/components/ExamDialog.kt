package com.mavi.memorize.ui.components

import com.mavi.memorize.api.VocabulariesApi
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


class ExamDialog(private val api: VocabulariesApi) : Dialog() {
    private val fields = mutableListOf<TextField>()
    private var vocabularies: List<Vocabulary> = api.findExamVocabularies()
    private val confirm = createSubmitConfirmDialog()

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
        form.addClassName("px-l")
        form.setSizeFull()

        vocabularies.forEach {
            val word = TextField(it.meaning + " " + it.partOfSpeech)
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
        val exitBtn = Button("Exit", Icon(VaadinIcon.EXIT)) { close() }
        val submitBtn = Button("Submit", Icon(VaadinIcon.CHECK)) {
            val filled = fields.filter { it.value != null && it.value.trim() != "" }
                .associate { it.id.get() to it.value }
            confirm.setText("Status: [ ${filled.size}/${vocabularies.size} ] filled")
            confirm.addConfirmListener {
                api.checkExamVocabularies(filled)
                close()
            }
            confirm.open()
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
}