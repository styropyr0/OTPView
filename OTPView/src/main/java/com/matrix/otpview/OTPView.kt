package com.matrix.otpview

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import com.matrix.otpview.interfaces.OTPCompletionHandler
import kotlin.math.abs

/**
 * Customizable OTP Edit Text
 * @author Saurav Sajeev
 */

class OtpView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var squareColor: Int = Color.TRANSPARENT
    private var squareSize: Float = 50f
    private var squareHeight: Float = 0f
    private var squareWidth: Float = 0f
    private var cornerRadius: Float = 10f
    private var squareCount: Int = 4
    private var textSize: Float = 18f
    private var autoProcess: Boolean = true
    private var borderWidth: Float = 1f
    private var borderColor: Int = Color.BLACK
    private var textColor: Int = Color.BLACK
    private var hint: String = ""
    private var margins: Int = 8
    private var autoSize: Boolean = false
    private var highlightColor: Int = Color.BLUE
    private var editTextShape: Shape = Shape.RECTANGLE
    private var maxCountPerLine: Int = 4
    private var inputType= InputType.TYPE_CLASS_NUMBER
    private var otp: String = ""

    public var onTypingComplete = object : OTPCompletionHandler {
        override fun onComplete() {}
    }

    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.OtpView,
            0, 0
        ).apply {
            try {
                squareColor = getColor(R.styleable.OtpView_squareColor, Color.TRANSPARENT)
                squareSize = getDimension(R.styleable.OtpView_squareSize, 50f)
                cornerRadius = getDimension(R.styleable.OtpView_cornerRadius, 10f)
                squareCount = getInt(R.styleable.OtpView_squareCount, 4)
                textSize = getFloat(R.styleable.OtpView_textSize, 18f)
                autoProcess = getBoolean(R.styleable.OtpView_autoProcess, true)
                borderWidth = getDimension(R.styleable.OtpView_borderWidth, 1f)
                borderColor = getColor(R.styleable.OtpView_borderColor, Color.BLACK)
                textColor = getColor(R.styleable.OtpView_textColor, Color.BLACK)
                hint = getString(R.styleable.OtpView_hint) ?: ""
                margins = getInt(R.styleable.OtpView_margins, 8)
                squareWidth = getDimension(R.styleable.OtpView_squareWidth, 0f)
                squareHeight = getDimension(R.styleable.OtpView_squareHeight, 0f)
                autoSize = getBoolean(R.styleable.OtpView_autoSize, false)
                highlightColor = getColor(R.styleable.OtpView_highlightColor, Color.BLUE)
                inputType = if(getString(R.styleable.OtpView_inputType)?.lowercase()=="numeric")InputType.TYPE_CLASS_NUMBER else InputType.TYPE_CLASS_NUMBER
                val shapeString = getString(R.styleable.OtpView_shape)?.lowercase() ?: "rectangle"
                editTextShape = when (shapeString) {
                    "circle" -> Shape.CIRCLE
                    else -> Shape.RECTANGLE
                }
                maxCountPerLine = getInt(R.styleable.OtpView_maxCountPerLine, 4)
            } finally {
                recycle()
            }
        }
        paint.color = squareColor
        orientation = VERTICAL
        addEditTexts(0)
    }

    private fun addEditTexts(start: Int = 0) {
        if (autoSize) {
            var count = start
            var itemWidth = 0
            var isFirstRow = true
            var totalMargins = 0
            var rowLinearLayout: LinearLayout? = null
            for (i in start until squareCount) {
                if(i % maxCountPerLine == 0) {
                    rowLinearLayout = LinearLayout(context).apply {
                        orientation = HORIZONTAL
                        gravity = Gravity.CENTER_HORIZONTAL
                        layoutParams =
                            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                    }
                    addView(rowLinearLayout)
                }
                val editText = EditText(context).apply {
                    layoutParams = if(i<maxCountPerLine)LayoutParams(resolveAvailableWidth().toInt(), resolveAvailableHeight().toInt(), 1f).apply {
                        setMargins(margins, margins, margins, margins)
                    }else LayoutParams(itemWidth, resolveAvailableHeight().toInt(), 1f).apply {
                        setMargins(margins, margins, margins, margins)
                    }
                    gravity = Gravity.CENTER
                    background = createBackgroundDrawable()
                    inputType = this@OtpView.inputType
                    textSize = this@OtpView.textSize
                    setTextColor(textColor)
                    hint = this@OtpView.hint
                    addTextChangedListener(GenericTextWatcher(this, i))
                }
                if (i==0) {
                    val rowWidth = width - paddingLeft - paddingRight
                    totalMargins = margins * (maxCountPerLine + 1)
                    val availableWidth = abs(rowWidth - totalMargins)
                    itemWidth = availableWidth / maxCountPerLine
                    isFirstRow = false
                }
                if(i==maxCountPerLine-1)
                    itemWidth = rowLinearLayout?.layoutParams?.width!!/maxCountPerLine

                rowLinearLayout?.addView(editText)

            }
        }

        else{
            var currentRow: LinearLayout? = null
            val containerWidth = width - paddingLeft - paddingRight
            val totalHorizontalMargins = margins * (maxCountPerLine + 1)

            val itemWidth = if (autoSize) {
                val availableWidth = containerWidth - totalHorizontalMargins
                availableWidth / maxCountPerLine
            } else
                resolveAvailableWidth()

            for (i in start until squareCount) {
                if (i % maxCountPerLine == 0) {
                    currentRow = LinearLayout(context).apply {
                        orientation = HORIZONTAL
                        gravity = Gravity.CENTER_HORIZONTAL
                        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                    }
                    addView(currentRow)
                }

                val editText = EditText(context).apply {
                    layoutParams = LayoutParams(itemWidth.toInt(), resolveAvailableHeight().toInt()).apply {
                        setMargins(margins, margins, margins, margins)
                    }
                    gravity = Gravity.CENTER
                    background = createBackgroundDrawable()
                    inputType = this@OtpView.inputType
                    maxLines = 1
                    textSize = this@OtpView.textSize
                    setTextColor(textColor)
                    hint = this@OtpView.hint
                    addTextChangedListener(GenericTextWatcher(this, i))
                }
                currentRow?.addView(editText)
            }
        }
    }

    private fun resolveAvailableWidth() = if (squareWidth > 0f) squareWidth else squareSize

    private fun resolveAvailableHeight() = if (squareHeight > 0f) squareHeight else squareSize


    private fun createBackgroundDrawable(): GradientDrawable {
        val drawable = GradientDrawable()
        if (editTextShape == Shape.CIRCLE) {
            drawable.shape = GradientDrawable.OVAL
            drawable.setSize(squareSize.toInt(), squareSize.toInt())
            drawable.setColor(squareColor)
            drawable.setStroke(borderWidth.toInt(), borderColor)
        } else {
            drawable.shape = GradientDrawable.RECTANGLE
            drawable.setColor(squareColor)
            drawable.cornerRadius = cornerRadius
            drawable.setStroke(borderWidth.toInt(), borderColor)
        }
        return drawable
    }

    private fun updateEditTextsLayout() {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child is EditText) {
                val params = child.layoutParams as LayoutParams
                params.width = 0
                params.height = LayoutParams.MATCH_PARENT
                params.weight = if (autoSize) 1f else 0f
                child.layoutParams = params
            }
        }
    }

    private fun updateEditTextsBackground() {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child is EditText) {
                child.background = createBackgroundDrawable()
            }
        }
    }

    private fun updateEditTextsTextSize() {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child is EditText) {
                child.textSize = this.textSize
            }
        }
    }

    private fun updateEditTextsTextColor() {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child is EditText) {
                child.setTextColor(this.textColor)
            }
        }
    }

    fun setSquareCount(squareCount: Int) {
        val previousCount = this.squareCount
        this.squareCount = squareCount

        if (previousCount != squareCount) {
            removeAllViews()
            addEditTexts(0)
            updateEditTextsLayout()
        }
    }

    fun toggleAutoSizing(mode: Boolean) {
        if (autoSize != mode) {
            autoSize = mode
            updateEditTextsLayout()
        }
    }

    fun setSquareColor(color: Int) {
        this.squareColor = color
        updateEditTextsBackground()
    }

    fun setSquareSize(size: Float) {
        if (this.squareSize != size) {
            this.squareSize = size
            updateEditTextsLayout()
        }
    }

    fun setCornerRadius(radius: Float) {
        if (this.cornerRadius != radius) {
            this.cornerRadius = radius
            updateEditTextsBackground()
        }
    }

    fun setTextSize(size: Float) {
        if (this.textSize != size) {
            this.textSize = size
            updateEditTextsTextSize()
        }
    }

    fun setBorderColor(color: Int) {
        if (this.borderColor != color) {
            this.borderColor = color
            updateEditTextsBackground()
        }
    }

    fun setTextColor(color: Int) {
        if (this.textColor != color) {
            this.textColor = color
            updateEditTextsTextColor()
        }
    }

    inner class GenericTextWatcher(private val view: View, private val index: Int) : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            val text = editable.toString()
            if (text.length == 1 && index < squareCount - 1) {
                val nextEditText = getChildAt((index / maxCountPerLine))?.let { row ->
                    (row as LinearLayout).getChildAt(index % maxCountPerLine + 1) as? EditText
                }
                nextEditText?.requestFocus()
            } else if (text.isEmpty() && index > 0) {
                val prevEditText = getChildAt((index / maxCountPerLine))?.let { row ->
                    (row as LinearLayout).getChildAt(index % maxCountPerLine - 1) as? EditText
                }
                prevEditText?.requestFocus()
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTypingComplete.onComplete()
        }
    }

    enum class Shape {
        RECTANGLE, CIRCLE
    }
}
