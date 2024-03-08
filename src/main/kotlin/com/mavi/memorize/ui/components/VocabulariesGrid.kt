package com.mavi.memorize.ui.components

import com.mavi.memorize.api.VocabulariesApi
import com.mavi.memorize.data.entity.Vocabulary
import com.mavi.memorize.ui.helper.*
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.confirmdialog.ConfirmDialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.provider.Query


class VocabulariesGrid(
    isReadMode: Boolean,
    private val api: VocabulariesApi? = null,
    private val fetchData: (query: Query<Vocabulary, *>) -> List<Vocabulary>
) : Div() {
    private val grid = Grid(Vocabulary::class.java, false)
    private val binder: Binder<Vocabulary> = Binder(Vocabulary::class.java)
    private val deleteConfirmDialog = createDeleteConfirmDialog()

    init {
        setSizeFull()
        addClassNames("px-l", "box-border")
        configColumns(isReadMode)
        add(grid)
    }

    private fun configColumns(isReadMode: Boolean): Grid<Vocabulary> {
        grid.addColumn(Vocabulary::word).header("Word").width(120).sort("word")
            .bindInlineTextEditor({ it.word }, { it, v -> it.word = v })

        grid.addColumn(Vocabulary::meaning).header("Meaning").width(200).tooltip { it.meaning }
            .bindInlineTextEditor({ it.meaning }, { it, v -> it.meaning = v })

        grid.addColumn(Vocabulary::partOfSpeech).header("Part Of Speech")
            .bindInlineTextEditor({ it.partOfSpeech }, { it, v -> it.partOfSpeech = v })

        grid.addColumn(Vocabulary::pron).header("Pron.")
            .bindInlineTextEditor({ it.pron }, { it, v -> it.pron = v })

        grid.addColumn(Vocabulary::sentence).header("Sentence").width(400).tooltip { it.sentence }
            .bindInlineTextEditor({ it.sentence }, { it, v -> it.sentence = v }, false)

        if (!isReadMode) addOperationColumns()
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT)
        grid.setItems { fetchData(it).stream() }
        grid.addSortListener { refreshGrid() }
        return grid
    }

    private fun addOperationColumns() {
        configEditor()
        grid.addColumn(Vocabulary::displayStudy).header("Study").sort("study").setEditorComponent {
            val select = Select<String>()
            select.setItems("Yes", "No")
            select.isEmptySelectionAllowed = false
            binder.forField(select).asRequired().bind(
                { item -> item.displayStudy() },
                { item, value -> item.study = value == "Yes" }
            )
            select
        }

        grid.addColumn(Vocabulary::displayCreatedAt).header("Created At").sort("createdAt")
        grid.addComponentColumn { HorizontalLayout(createEditBtn(it), createDeleteBtn(it)) }
            .header("Operations").width(200)
            .setEditorComponent {
                val saveButton = Button(VaadinIcon.CHECK.create()) { grid.editor.save() }
                saveButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SUCCESS)
                val cancelButton = Button(VaadinIcon.CLOSE.create()) { grid.editor.cancel() }
                cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR)
                val actions = HorizontalLayout(saveButton, cancelButton)
                actions.isPadding = false
                actions
            }
    }

    private fun configEditor() {
        grid.editor.setBinder(binder)
        grid.editor.setBuffered(true)
        grid.editor.addSaveListener {
            api?.updateVocabulary(it.item as Vocabulary)
            refreshGrid()
        }
    }

    private fun createEditBtn(vocabulary: Vocabulary): Button {
        val btn = Button("Edit")
        btn.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL)
        btn.icon = Icon(VaadinIcon.EDIT)
        btn.addClickListener {
            if (grid.editor.isOpen) grid.editor.cancel()
            grid.editor.editItem(vocabulary)
        }
        return btn
    }

    private fun createDeleteBtn(vocabulary: Vocabulary): Button {
        val delBtn = Button("Delete")
        delBtn.addThemeVariants(
            ButtonVariant.LUMO_ICON,
            ButtonVariant.LUMO_SMALL,
            ButtonVariant.LUMO_ERROR,
            ButtonVariant.LUMO_TERTIARY
        )
        delBtn.icon = Icon(VaadinIcon.TRASH)
        delBtn.addClickListener {
            deleteConfirmDialog.setId(vocabulary.id)
            deleteConfirmDialog.setText("Are you sure you want to delete word 【${vocabulary.word}?】")
            deleteConfirmDialog.open()
        }
        return delBtn
    }

    private fun createDeleteConfirmDialog(): ConfirmDialog {
        val dialog = ConfirmDialog()
        dialog.setHeader("Delete operation")
        dialog.setCancelable(true)
        dialog.addConfirmListener {
            api?.removeVocabularyById(dialog.id.get())
            refreshGrid()
        }
        return dialog
    }

    fun refreshGrid() {
        grid.dataProvider.refreshAll()
    }
}
