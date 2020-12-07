package com.example.rccamfrontend.views

import android.content.Context
import android.text.InputFilter
import android.util.AttributeSet
import android.widget.*
import androidx.core.widget.doOnTextChanged
import com.example.rccamfrontend.R
import com.example.rccamfrontend.utils.incrementTextView
import kotlinx.android.synthetic.main.textview_incrementable.view.*

class IncrementableTextView(ctx: Context, attr: AttributeSet) : LinearLayout(ctx, attr) {

    var textView: EditText

    init {
        inflate(context, R.layout.textview_incrementable, this)
        val attrGetter = context.theme.obtainStyledAttributes(
            attr,
            R.styleable.IncrementableTextView,
            0,
            0
        )

        // Modifying Text View
        textView = textfield
        textView.hint = attrGetter.getString(R.styleable.IncrementableTextView_text)

        val maxLength = attrGetter.getString(R.styleable.IncrementableTextView_maxLength)?.toIntOrNull()
        if (maxLength != null){ textView.filters = arrayOf(InputFilter.LengthFilter(maxLength)) }

        // Setting value limit for Text View
        val max = attrGetter.getString(R.styleable.IncrementableTextView_maxVal)?.toIntOrNull()
        val min = attrGetter.getString(R.styleable.IncrementableTextView_minVal)?.toIntOrNull()

        textView.doOnTextChanged { text, _, _, _ ->
            val textInt = text.toString().toIntOrNull()
            if (textInt != null) {
                if (max != null && textInt > max) {
                    textView.setText(max.toString())
                } else if (min != null && textInt < min) {
                    textView.setText(min.toString())
                }
            }
            textView.setSelection(textView.text.length)
        }


        // Modifying Minus Button
        val dec = attrGetter.getInt(R.styleable.IncrementableTextView_decVal, -1)
        button_minus.setOnClickListener {  // Decrement btnYaw
            incrementTextView(textView, dec)
        }


        // Modifying Plus Button
        val inc = attrGetter.getInt(R.styleable.IncrementableTextView_incVal, 1)
        button_plus.setOnClickListener {  // Decrement btnYaw
            incrementTextView(textView, inc)
        }
    }
}