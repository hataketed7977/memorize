package com.mavi.memorize.ui.helper

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant


fun Button.addThemeVars(vararg variants: ButtonVariant) {
    removeThemeVariants(*ButtonVariant.entries.toTypedArray())
    addThemeVariants(*variants)
}
