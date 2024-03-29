package com.mavi.memorize.ui.pages

import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.html.Footer
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.Scroller
import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.component.sidenav.SideNav
import com.vaadin.flow.component.sidenav.SideNavItem
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.server.PWA
import com.vaadin.flow.theme.lumo.LumoUtility
import org.vaadin.lineawesome.LineAwesomeIcon

@PageTitle("Memorize Hub")
@PWA(name = "Memorize Hub", shortName = "Memorize")
class MainView : AppLayout(), AppShellConfigurator {
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
        nav.addItem(SideNavItem("Memorize", MemorizeView::class.java, LineAwesomeIcon.TASKS_SOLID.create()))
        nav.addItem(SideNavItem("Statistics", StatisticsView::class.java, LineAwesomeIcon.CHART_BAR.create()))
        nav.addItem(SideNavItem("Incorrect Words", IncorrectWordsView::class.java, LineAwesomeIcon.BUG_SOLID.create()))
        nav.addItem(
            SideNavItem(
                "Familiar Words",
                FamiliarWordsView::class.java,
                LineAwesomeIcon.CHECK_SQUARE_SOLID.create()
            )
        )
        return nav
    }

    private fun createFooter(): Footer {
        val layout = Footer()
        return layout
    }
}
