package com.matrix.otpview

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.widget.EditText
import android.widget.LinearLayout
import com.matrix.otpview.interfaces.OTPCompletionHandler

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
    private var inputType = InputType.TYPE_CLASS_NUMBER
    private var onCompleteBorderColor: Int = Color.GREEN
    private var borderColorIsSet = false
    private var onOTPErrorBorderColor = Color.RED
    private var fontFamily: String? = null
    private var textStyle: String? = null
    private var otp: String = ""

    private var completionListener: OTPCompletionHandler? = null

    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    /**
     * A highly customizable OTP layout. Please refer the datasheet to learn about the features.
     */
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
                squareCount = getInt(R.styleable.OtpView_squareCount, 4).let {
                    if (it < 1) 4 else it
                }
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
                fontFamily = getString(R.styleable.OtpView_fontFamily)
                textStyle = getString(R.styleable.OtpView_textStyle)
                onCompleteBorderColor =
                    getColor(R.styleable.OtpView_onCompleteBorderColor, Color.GREEN)
                onOTPErrorBorderColor =
                    getColor(R.styleable.OtpView_onOTPErrorBorderColor, Color.RED)
                inputType =
                    if (getString(R.styleable.OtpView_inputType)?.lowercase() == "number") InputType.TYPE_CLASS_NUMBER else InputType.TYPE_CLASS_TEXT
                val shapeString = getString(R.styleable.OtpView_shape)?.lowercase() ?: "rectangle"
                editTextShape = when (shapeString) {
                    "circle" -> Shape.CIRCLE
                    else -> Shape.RECTANGLE
                }
                maxCountPerLine = getInt(R.styleable.OtpView_maxCountPerLine, 4).let {
                    if (it < 1) 4 else it
                }
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
            var rowLinearLayout: LinearLayout? = null
            for (i in start until squareCount) {
                if (i % maxCountPerLine == 0) {
                    rowLinearLayout = LinearLayout(context).apply {
                        orientation = HORIZONTAL
                        gravity = Gravity.CENTER_HORIZONTAL
                        layoutParams =
                            LayoutParams(
                                LayoutParams.MATCH_PARENT,
                                LayoutParams.WRAP_CONTENT
                            )
                    }
                    addView(rowLinearLayout)
                }
                val editText = EditText(context).apply {
                    layoutParams =
                        if (i < squareCount - squareCount % maxCountPerLine || i < maxCountPerLine) LayoutParams(
                            resolveAvailableWidth().toInt(),
                            resolveAvailableHeight().toInt(),
                            0.2f
                        ).apply {
                            setMargins(margins, margins, margins, margins)
                        } else LayoutParams(
                            squareWidth.toInt() - margins * 2 - paddingLeft / (squareCount % maxCountPerLine),
                            resolveAvailableHeight().toInt()
                        ).apply {
                            setMargins(margins, margins, margins, margins)
                        }

                    maxWidth = squareWidth.toInt()
                    gravity = Gravity.CENTER
                    background = createBackgroundDrawable()
                    inputType = this@OtpView.inputType
                    textSize = this@OtpView.textSize
                    setTextColor(textColor)
                    if (!fontFamily.isNullOrEmpty()) setTypeface(
                        Typeface.create(
                            fontFamily,
                            resolveTextStyle()
                        )
                    )
                    hint = this@OtpView.hint
                    filters = arrayOf(InputFilter.LengthFilter(1))
                    addTextChangedListener(GenericTextWatcher(this, i))
                }
                rowLinearLayout?.addView(editText)

            }
        } else {
            var currentRow: LinearLayout? = null
            val itemWidth = resolveAvailableWidth()

            for (i in start until squareCount) {
                if (i % maxCountPerLine == 0) {
                    currentRow = LinearLayout(context).apply {
                        orientation = HORIZONTAL
                        gravity = Gravity.CENTER_HORIZONTAL
                        layoutParams =
                            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                    }
                    addView(currentRow)
                }

                val editText = EditText(context).apply {
                    layoutParams =
                        LayoutParams(itemWidth.toInt(), resolveAvailableHeight().toInt()).apply {
                            setMargins(margins, margins, margins, margins)
                        }
                    gravity = Gravity.CENTER
                    background = createBackgroundDrawable()
                    inputType = this@OtpView.inputType
                    maxLines = 1
                    if (!fontFamily.isNullOrEmpty()) setTypeface(
                        Typeface.create(
                            fontFamily,
                            resolveTextStyle()
                        )
                    )
                    textSize = this@OtpView.textSize
                    setTextColor(textColor)
                    hint = this@OtpView.hint
                    filters = arrayOf(InputFilter.LengthFilter(1))
                    addTextChangedListener(GenericTextWatcher(this, i))
                }
                currentRow?.addView(editText)
            }
        }
    }

    private fun resolveTextStyle(): Int {
        if (!textStyle.isNullOrEmpty()) {
            if (textStyle == "bold")
                return Typeface.BOLD
            else if (textStyle == "italic")
                return Typeface.ITALIC
            else if (textStyle == "bold_italic")
                return Typeface.BOLD_ITALIC
        }
        return Typeface.NORMAL
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
            if (child is LinearLayout) {
                for (j in 0 until child.childCount) {
                    val editText = child.getChildAt(j) as? EditText
                    editText?.apply {
                        background = createBackgroundDrawable()
                        setCustomFont(editText)
                    }
                }
            }
        }
    }

    private fun updateEditTextsTextSize() {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child is LinearLayout) {
                for (j in 0 until child.childCount) {
                    val editText = child.getChildAt(j) as? EditText
                    editText?.textSize = this.textSize
                    setCustomFont(editText)
                }
            }
        }
    }

    private fun updateEditTextsTextColor() {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child is LinearLayout) {
                for (j in 0 until child.childCount) {
                    val editText = child.getChildAt(j) as? EditText
                    editText?.setTextColor(this.textColor)
                    setCustomFont(editText)
                }
            }
        }
    }

    private fun setCustomFont(editText: EditText?) {
        if (editText != null)
            if (!fontFamily.isNullOrEmpty()) editText.apply {
                setTypeface(
                    Typeface.create(
                        fontFamily,
                        resolveTextStyle()
                    )
                )
                inputType = this@OtpView.inputType
            }
    }

    /**
     * Set the input type of the OTP View. By default, the type is text.
     * @param inputType The input type. Use the values from input type class.
     */
    fun setInputType(inputType: Int) {
        this.inputType = inputType
        updateEditTextsTextSize()
    }

    /**
     * Set the number of squares in the OTP View.
     * @param squareCount The number of characters in OTP.
     */
    fun setSquareCount(squareCount: Int) {
        val previousCount = this.squareCount
        this.squareCount = squareCount

        if (previousCount != squareCount) {
            removeAllViews()
            addEditTexts(0)
            updateEditTextsLayout()
        }
    }

    /**
     * Set autoSizing mode. The size of the squares will be adjusted irrespective of the hardcoded values, making sure that the squares fit in the given width.
     * @param mode True to turn on autoSizing and false to turn it off.
     */
    fun toggleAutoSizing(mode: Boolean) {
        if (autoSize != mode) {
            autoSize = mode
            updateEditTextsLayout()
        }
    }

    /**
     * Set the background color of the square.
     * @param color The background color of square.
     */
    fun setSquareColor(color: Int) {
        this.squareColor = color
        updateEditTextsBackground()
    }

    /**
     * Set the size of the square.
     * It is assumed to be a square.
     * @param size The size of the square.
     */
    fun setSquareSize(size: Float) {
        if (this.squareSize != size) {
            this.squareSize = size
            updateEditTextsLayout()
        }
    }

    /**
     * Set the corner radius of the square.
     * @param radius The corner radius
     */
    fun setCornerRadius(radius: Float) {
        if (this.cornerRadius != radius) {
            this.cornerRadius = radius
            updateEditTextsBackground()
        }
    }

    /**
     * Set the text size of the OTP View.
     * @param size The size of OTP Text.
     */
    fun setTextSize(size: Float) {
        if (this.textSize != size) {
            this.textSize = size
            updateEditTextsTextSize()
        }
    }

    /**
     * Set the border color of the square.
     * @param color The border color
     */
    fun setBorderColor(color: Int) {
        if (this.borderColor != color) {
            this.borderColor = color
            updateEditTextsBackground()
        }
    }

    /**
     * Set the border color of the square when the OTP is completely entered.
     * @param color The border color
     */
    fun setOnCompleteBorderColor(color: Int) {
        if (this.onCompleteBorderColor != color) {
            this.onCompleteBorderColor = color
        }
    }

    /**
     * Set the border color of the square when the OTP is entered wrong
     * It is triggered when onOtpError is called.
     * @param color The border color
     */
    fun setOnOTPErrorBorderColor(color: Int) {
        if (this.onOTPErrorBorderColor != color) {
            this.onOTPErrorBorderColor = color
        }
    }

    private fun updateOnCompleteBorderColor(color: Int) {
        val tempColor = borderColor
        borderColor = color
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child is LinearLayout) {
                for (j in 0 until child.childCount) {
                    val editText = child.getChildAt(j) as? EditText
                    editText?.background = createBackgroundDrawable()
                }
            }
        }
        borderColor = tempColor
    }

    /**
     * Sets the text color of the OTP.
     * @param color Text color to be changed to.
     */
    fun setTextColor(color: Int) {
        if (this.textColor != color) {
            this.textColor = color
            updateEditTextsTextColor()
        }
    }

    /**
     * A listener for OTP. It is automatically executed when OTP is entered completely.
     */
    fun setOnCompleteListener(listener: OTPCompletionHandler) {
        this.completionListener = listener
    }

    private fun shakeAnimation() {
        val animation = android.view.animation.TranslateAnimation(
            0f, 20f,
            0f, 0f
        ).apply {
            duration = 50
            repeatCount = 5
            repeatMode = android.view.animation.Animation.REVERSE
        }
        this.startAnimation(animation)
    }

    /**
     * Call this function to perform a shake animation with vibration to show that the entered otp is invalid.
     * @param clearOtp Set this true to clear the OTP entered when this is called.
     */
    fun onOtpError(clearOtp: Boolean = false) {
        updateOnCompleteBorderColor(onOTPErrorBorderColor)
        vibrate()
        shakeAnimation()
        if (clearOtp) {
            clearOtpInput()
        }
        borderColorIsSet = true
    }

    private fun clearOtpInput() {
        var firstEditText: EditText? = null

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child is LinearLayout) {
                for (j in 0 until child.childCount) {
                    val editText = child.getChildAt(j) as? EditText
                    editText?.text?.clear()
                    if (firstEditText == null) {
                        firstEditText = editText
                    }
                }
            }
        }
        firstEditText?.requestFocus()
    }


    private fun vibrate() {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }

    inner class GenericTextWatcher(private val currentEditText: EditText, private val index: Int) :
        TextWatcher {

        override fun afterTextChanged(editable: Editable?) {
            val text = editable.toString()

            if (!text.isEmpty()) updateOtp(index, text[0])
            else clearOtp(index)

            if (text.length == 1 && index < squareCount - 1) {
                val nextEditText = getNextEditText(index)
                nextEditText?.requestFocus()
            } else if (text.isEmpty() && index > 0) {
                val prevEditText = getPreviousEditText(index)
                prevEditText?.requestFocus()
            }

            if (isOtpComplete()) {
                updateOnCompleteBorderColor(onCompleteBorderColor)
                otp = getOtpFromFields()
                completionListener?.onComplete(otp)
                borderColorIsSet = true
            } else if (borderColorIsSet) {
                borderColorIsSet = false
                updateOnCompleteBorderColor(borderColor)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        private fun getNextEditText(index: Int): EditText? {
            val nextIndex = index + 1
            val row = nextIndex / maxCountPerLine
            val col = nextIndex % maxCountPerLine

            val rowLayout = getChildAt(row) as? LinearLayout ?: return null
            return rowLayout.getChildAt(col) as? EditText
        }

        private fun getPreviousEditText(index: Int): EditText? {
            val prevIndex = index - 1
            val row = prevIndex / maxCountPerLine
            val col = prevIndex % maxCountPerLine

            val rowLayout = getChildAt(row) as? LinearLayout ?: return null
            return rowLayout.getChildAt(col) as? EditText
        }

        private fun updateOtp(index: Int, char: Char) {
            val otpArray = otp.toCharArray()
            if (otpArray.size > index) {
                otpArray[index] = char
            } else {
                otp = otp.padEnd(index, ' ') + char
                return
            }
            otp =
                if (inputType != InputType.TYPE_CLASS_TEXT) String(otpArray).trim()
                else String(otpArray)
        }

        private fun clearOtp(index: Int) {
            val otpArray = otp.toCharArray()
            if (otpArray.size > index) {
                otpArray[index] = ' '
            }
            otp = String(otpArray)
        }

        private fun isOtpComplete(): Boolean {
            for (i in 0 until squareCount) {
                val row = i / maxCountPerLine
                val col = i % maxCountPerLine
                val rowLayout = getChildAt(row) as? LinearLayout ?: return false
                val editText = rowLayout.getChildAt(col) as? EditText ?: return false
                if (editText.text.isEmpty()) return false
            }
            return true
        }

        private fun getOtpFromFields(): String {
            val otpBuilder = StringBuilder()
            for (i in 0 until squareCount) {
                val row = i / maxCountPerLine
                val col = i % maxCountPerLine
                val rowLayout = getChildAt(row) as? LinearLayout ?: continue
                val editText = rowLayout.getChildAt(col) as? EditText ?: continue
                otpBuilder.append(editText.text.toString())
            }
            return otpBuilder.toString()
        }

    }

    enum class Shape {
        RECTANGLE, CIRCLE
    }
}
