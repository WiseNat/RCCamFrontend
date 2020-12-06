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
    private var minusButton: Button
    private var plusButton: Button

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

        val maxLength = attrGetter.getInt(R.styleable.IncrementableTextView_maxLength, 2)
        textView.filters = arrayOf(InputFilter.LengthFilter(maxLength))

        // Setting value limit for Text View
        val max = attrGetter.getInt(R.styleable.IncrementableTextView_maxVal, 10)
        val min = attrGetter.getInt(R.styleable.IncrementableTextView_minVal, 0)

        textView.doOnTextChanged { text, _, _, _ ->
            val textInt = text.toString().toIntOrNull()
            if (textInt != null) {
                if (textInt > max) {
                    textView.setText(max.toString())
                } else if (textInt < min) {
                    textView.setText(min.toString())
                }
            }
            textView.setSelection(textView.text.length)
        }


        // Modifying Minus Button
        minusButton = button_minus

        val dec = attrGetter.getInt(R.styleable.IncrementableTextView_decVal, -1)
        minusButton.setOnClickListener {  // Decrement btnYaw
            incrementTextView(textView, dec)
        }


        // Modifying Plus Button
        plusButton = button_plus

        val inc = attrGetter.getInt(R.styleable.IncrementableTextView_incVal, 1)
        plusButton.setOnClickListener {  // Decrement btnYaw
            incrementTextView(textView, inc)
        }
    }
}