package com.mavi.memorize.ui.helper

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.grid.ColumnTextAlign
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.binder.Setter
import com.vaadin.flow.function.SerializableFunction
import com.vaadin.flow.function.ValueProvider

fun <T> Grid.Column<T>.header(labelText: String): Grid.Column<T> = setHeader(labelText).setAutoWidth(true).unsort()
fun <T> Grid.Column<T>.width(width: Int): Grid.Column<T> = setAutoWidth(false).setWidth("${width}px")
fun <T> Grid.Column<T>.sort(name: String): Grid.Column<T> = setSortable(true).setSortProperty(name)
fun <T> Grid.Column<T>.unsort(): Grid.Column<T> = setSortable(false)
fun <T> Grid.Column<T>.tooltip(tooltipGenerator: SerializableFunction<T, String>): Grid.Column<T> =
    setTooltipGenerator(tooltipGenerator)

fun <T> Grid.Column<T>.centerHeader(headerText: Component): Grid.Column<T> {
    val headerContent = VerticalLayout()
    headerContent.setWidthFull()
    headerContent.isPadding = false
    headerContent.isSpacing = false
    headerContent.isMargin = false
    headerContent.add(headerText)
    headerContent.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, headerText)
    return setHeader(headerContent).setAutoWidth(true).unsort()
}

fun <T> Grid.Column<T>.cellAlign(colTextAlign: ColumnTextAlign? = ColumnTextAlign.CENTER): Grid.Column<T> {
    textAlign = colTextAlign
    return this
}


@Suppress("kotlin:S6530")
fun <T> Grid.Column<T>.bindInlineTextEditor(
    getter: ValueProvider<T, String>,
    setter: Setter<T, String>,
    required: Boolean = true
): Grid.Column<T> {
    return setEditorComponent {
        val text = TextField()
        text.setWidthFull()
        val binder = grid.editor.binder as Binder<T>
        val bindingBuilder = binder.forField(text)
        if (required) bindingBuilder.asRequired()
        bindingBuilder.bind(getter, setter)
        text
    }
}
