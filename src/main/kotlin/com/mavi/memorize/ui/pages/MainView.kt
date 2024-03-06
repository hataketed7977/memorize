package com.mavi.memorize.ui.pages

import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.html.Footer
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.Scroller
import com.vaadin.flow.component.sidenav.SideNav
import com.vaadin.flow.component.sidenav.SideNavItem
import com.vaadin.flow.theme.lumo.LumoUtility
import org.vaadin.lineawesome.LineAwesomeIcon

class MainView : AppLayout() {
    companion object {
        private const val APP_NAME = "Memorize Hub"
    }

    init {
        primarySection = Section.NAVBAR
        addDrawerContent()
        addHeaderContent()
    }

    private fun addHeaderContent() {
        val toggle = DrawerToggle()
        val title = H1(APP_NAME)
        title.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE)
        addToNavbar(toggle, title)
    }

    private fun addDrawerContent() {
        val scroller = Scroller(createNavigation())
        addToDrawer(scroller, createFooter())
    }

    private fun createNavigation(): SideNav {
        val nav = SideNav()
        nav.addItem(SideNavItem("Vocabularies", VocabulariesView::class.java, LineAwesomeIcon.BOOK_SOLID.create()))
        nav.addItem(SideNavItem("Memorize", VocabulariesView::class.java, LineAwesomeIcon.BOOK_SOLID.create()))
        return nav
    }

    private fun createFooter(): Footer {
        val layout = Footer()
        return layout
    }
}
