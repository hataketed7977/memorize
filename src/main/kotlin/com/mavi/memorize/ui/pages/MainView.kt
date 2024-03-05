package com.mavi.memorize.ui.pages

import com.vaadin.flow.component.Key
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.Route


@Route("")
class MainView : VerticalLayout() {
    init {
        val todosList = VerticalLayout()
        val taskField = TextField()
        val addButton = Button("Add")
        addButton.addClickListener {
            val checkbox = Checkbox(taskField.value)
            todosList.add(checkbox)
        }
        addButton.addClickShortcut(Key.ENTER)

        add(
            H1("Vocabulary Hub"),
            todosList,
            HorizontalLayout(
                taskField,
                addButton
            )
        )
    }
}
