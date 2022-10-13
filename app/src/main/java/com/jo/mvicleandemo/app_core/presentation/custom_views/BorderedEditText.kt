package com.jo.mvicleandemo.app_core.presentation.custom_views

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.text.Spanned
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.ImageViewCompat
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout
import com.jo.mvicleandemo.R


class BorderedEditText : FrameLayout {

    //views
    private lateinit var ivDrawableStart: ImageView
    private lateinit var tilContent: TextInputLayout
    private lateinit var edtContent: EditText
    private lateinit var tvError: TextView
    private lateinit var clViewContainer: ConstraintLayout
    private lateinit var ivClear: ImageView

    //properties
    private var isInErrorState = false
    private var textChangeListener: ((String) -> Unit)? = null

    //attrs
    private var _inputType: Int = 0
    private var _imeOptions: Int = 0
    private var _nextFocus: Int = 0
    private var _enabled: Boolean = true
    private var _hint: CharSequence = ""
    private var _textSize = 0
    private var _maxLines = 10
    private var _maxLength = 1000
    private var _showClearButton = false
    private var _drawableStart: Int = -1
    private var _drawableStartTintSelector: Int = -1


    //To create a new View instance from Kotlin code, it needs the Activity context.
    constructor(context: Context) : this(context, null)

    //To create a new View instance from XML.
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        getAttributes(context, attrs)
        initView(context)
        observeEditTextChanges()
        applyAttributes()
        displayInActiveStatus()
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }


    private fun getAttributes(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.BorderedEditText,
            0,
            0
        )
        if (typedArray.hasValue(R.styleable.BorderedEditText_android_inputType))
            _inputType = typedArray.getInt(
                R.styleable.BorderedEditText_android_inputType,
                EditorInfo.TYPE_NULL
            )

        if (typedArray.hasValue(R.styleable.BorderedEditText_android_imeOptions))
            _imeOptions = typedArray.getInt(
                R.styleable.BorderedEditText_android_imeOptions,
                EditorInfo.TYPE_NULL
            )

        if (typedArray.hasValue(R.styleable.BorderedEditText_nextFocus))
            _nextFocus = typedArray.getResourceId(
                R.styleable.BorderedEditText_nextFocus,
                -1
            )

        if (typedArray.hasValue(R.styleable.BorderedEditText_enabled))
            _enabled =
                typedArray.getBoolean(R.styleable.BorderedEditText_enabled, true)

        if (typedArray.hasValue(R.styleable.BorderedEditText_hint))
            _hint = typedArray.getString(R.styleable.BorderedEditText_hint) ?: ""

        if (typedArray.hasValue(R.styleable.BorderedEditText_textSize))
            _textSize =
                typedArray.getDimensionPixelSize(R.styleable.BorderedEditText_textSize, _textSize)

        if (typedArray.hasValue(R.styleable.BorderedEditText_maxLines))
            _maxLines = typedArray.getInt(R.styleable.BorderedEditText_maxLines, _maxLines)

        if (typedArray.hasValue(R.styleable.BorderedEditText_maxLength))
            _maxLength = typedArray.getInt(R.styleable.BorderedEditText_maxLength, _maxLength)

        if (typedArray.hasValue(R.styleable.BorderedEditText_showClearButton))
            _showClearButton =
                typedArray.getBoolean(R.styleable.BorderedEditText_showClearButton, false)

        if (typedArray.hasValue(R.styleable.BorderedEditText_drawableStart))
            _drawableStart =
                typedArray.getResourceId(R.styleable.BorderedEditText_drawableStart, -1)

        if (typedArray.hasValue(R.styleable.BorderedEditText_drawableStartTintSelector))
            _drawableStartTintSelector =
                typedArray.getResourceId(R.styleable.BorderedEditText_drawableStartTintSelector, -1)


        typedArray.recycle()
    }

    private fun applyAttributes() {

        edtContent.apply {
            isEnabled = _enabled
            if (_inputType > 0)
                inputType = _inputType
            if (_imeOptions > 0)
                imeOptions = _imeOptions
            nextFocusForwardId = _nextFocus
            nextFocusDownId = _nextFocus
            hint = _hint
            if (_textSize > 0)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, _textSize.toFloat())
            maxLines = _maxLines
            setLines(_maxLines)
            isSingleLine = _maxLines == 1
            filters = arrayOf(InputFilter.LengthFilter(_maxLength))
        }

        if (_inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            edtContent.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            tilContent.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
            tilContent.setEndIconDrawable(R.drawable.selector_password_toggle_drawable)
        }

        setDrawableStartImageResource(_drawableStart)
    }

    private fun initView(context: Context) {
        View.inflate(context, R.layout.view_bordered_edit_text, this)
        ivDrawableStart = findViewById(R.id.ivDrawableStart)
        tilContent = findViewById(R.id.tilContent)
        edtContent = findViewById(R.id.edtContent)
        tvError = findViewById(R.id.tvError)
        clViewContainer = findViewById(R.id.clMobileInputContainer)
        ivClear = findViewById(R.id.ivClear)

        edtContent.setOnFocusChangeListener { _, hasFocus ->
            if (isInErrorState)
                return@setOnFocusChangeListener

            handleHasFocusChanges(hasFocus)
        }

        ivClear.setOnClickListener {
            edtContent.setText("")
        }
    }


    private fun handleHasFocusChanges(hasFocus: Boolean) {
        if (hasFocus)
            displayActiveStatus()
        else
            displayInActiveStatus()
    }

    private fun displayActiveStatus() {
        clearErrorStatus()
        ivDrawableStart.isSelected = true
        clViewContainer.setBackgroundResource(R.drawable.shape_rounded_white_bg_secondary_border)
    }

    private fun displayInActiveStatus() {
        clearErrorStatus()
        ivDrawableStart.isSelected = false
        clViewContainer.setBackgroundResource(R.drawable.shape_rounded_white_bg_gray_border)
    }

    private fun clearErrorStatus() {
        setDrawableStartSelectorTintColor()
        isInErrorState = false
        tvError.isVisible = false
        edtContent.setTextColor(ContextCompat.getColor(context, R.color.color_primary_text))
    }

    private fun setDrawableStartSelectorTintColor(resId: Int = -1) {
        (_drawableStart > 0).apply {
            val tintResId = when {
                resId > 0 -> resId
                _drawableStartTintSelector > 0 -> _drawableStartTintSelector
                else -> R.color.selector_primary_to_secondary
            }

            ImageViewCompat.setImageTintList(
                ivDrawableStart,
                ContextCompat.getColorStateList(
                    ivDrawableStart.context,
                    tintResId
                )
            )

            ivDrawableStart.invalidate()

        }
    }

    private fun observeEditTextChanges() {
        edtContent.addTextChangedListener {
            val text = it.toString()
            ivClear.isVisible = text.isNotEmpty() && _showClearButton && _enabled
            handleHasFocusChanges(edtContent.isFocused)
            textChangeListener?.invoke(text)
        }
    }

    private fun setDrawableStartImageResource(resId: Int) {
        ivDrawableStart.apply {
            isVisible = resId > 0
            if (resId > 0)
                setImageResource(resId)
        }
    }


    fun setError(errorMessage: Spanned) {
        tvError.text = errorMessage
        showErrorStatus()
    }

    fun setError(errorMessage: String) {
        tvError.text = errorMessage
        showErrorStatus()
    }

    private fun showErrorStatus() {
        isInErrorState = true
        tvError.isVisible = true
        edtContent.setTextColor(ContextCompat.getColor(context, R.color.error))
        setDrawableStartSelectorTintColor(R.color.error)
        clViewContainer.setBackgroundResource(R.drawable.shape_rounded_error)
    }


    fun setTextChangeListener(listener: (String) -> Unit) {
        textChangeListener = listener
    }

    fun getContent(): String {
        return edtContent.text.toString().trim()
    }

    fun setContent(content: String) {
        edtContent.setText(content)
    }

}
