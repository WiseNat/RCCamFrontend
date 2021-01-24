package com.example.rccamfrontend.views

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.widget.*
import androidx.core.widget.doOnTextChanged
import com.example.rccamfrontend.R
import com.example.rccamfrontend.utils.incrementTextView
import kotlinx.android.synthetic.main.textview_incrementable.view.*
import java.util.*

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

        if (attrGetter.getString(R.styleable.IncrementableTextView_inputType) == "1"){
            textView.inputType = InputType.TYPE_CLASS_NUMBER
            textView.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
            textView.keyListener = DigitsKeyListener(Locale.getDefault(), false, true)
        }

        val maxLength = attrGetter.getString(R.styleable.IncrementableTextView_maxLength)?.toIntOrNull()
        if (maxLength != null){ textView.filters = arrayOf(InputFilter.LengthFilter(maxLength)) }

        // Setting value limit for Text View
        val max = attrGetter.getString(R.styleable.IncrementableTextView_maxVal)?.toFloatOrNull()
        val min = attrGetter.getString(R.styleable.IncrementableTextView_minVal)?.toFloatOrNull()

        textView.doOnTextChanged { text, _, _, _ ->
            val textInt = text.toString().toFloatOrNull()
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
        val dec = attrGetter.getFloat(R.styleable.IncrementableTextView_decVal, 1F)
        button_minus.setOnClickListener {  // Decrement btnYaw
            incrementTextView(textView, dec*-1)
        }


        // Modifying Plus Button
        val inc = attrGetter.getFloat(R.styleable.IncrementableTextView_incVal, 1F)
        button_plus.setOnClickListener {  // Decrement btnYaw
            incrementTextView(textView, inc)
        }
    }
}