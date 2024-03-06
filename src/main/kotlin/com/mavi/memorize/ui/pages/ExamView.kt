package com.mavi.memorize.ui.pages

import com.vaadin.flow.component.dependency.Uses
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route


@Route(value = "memorize/exam", layout = MainView::class)
@Uses(Icon::class)
class ExamView() : VerticalLayout() {
    init {
        setSizeFull()
    }
}
