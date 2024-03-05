package com.mavi.memorize.ui.helper

import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.function.SerializableFunction

fun <T> Grid.Column<T>.header(labelText: String): Grid.Column<T> = setHeader(labelText).setAutoWidth(true).unsort()
fun <T> Grid.Column<T>.width(width: Int): Grid.Column<T> = setWidth("${width}px")
fun <T> Grid.Column<T>.sort(name: String): Grid.Column<T> = setSortable(true).setSortProperty(name)
fun <T> Grid.Column<T>.unsort(): Grid.Column<T> = setSortable(false)
fun <T> Grid.Column<T>.tooltip(tooltipGenerator: SerializableFunction<T, String>): Grid.Column<T> =
    setTooltipGenerator(tooltipGenerator)
