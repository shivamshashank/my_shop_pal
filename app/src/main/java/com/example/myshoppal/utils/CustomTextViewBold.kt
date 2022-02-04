package com.example.myshoppal.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CustomTextViewBold(context: Context, attributeSet: AttributeSet) :
    AppCompatTextView(context, attributeSet) {
    init {
        applyFont()
    }

    private fun applyFont() {
        typeface = Typeface.createFromAsset(context.assets, "Montserrat-Bold.ttf")
    }
}