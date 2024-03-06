package com.mavi.memorize.ui.helper

import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextField


enum class Badge(val value: String) {
    DEFAULT("badge"),
    SUCCESS("badge success"),
    ERROR("badge error"),
    CONTRAST("badge contrast"),
}

fun readOnlyTextField(label: String, value: String, badge: Badge? = null): TextField {
    val field = TextField()
    field.isReadOnly = true
    field.label = label
    field.value = value
    badge?.let {
        field.getElement().themeList.add(badge.value)
    }
    return field
}

fun intField(label: String, placeholder: String, value: Int? = null, min: Int = 0, max: Int = 50): IntegerField {
    val field = IntegerField()
    field.label = label
    field.placeholder = placeholder
    field.value = value
    field.isStepButtonsVisible = true;
    field.min = min
    field.max = max
    return field
}
