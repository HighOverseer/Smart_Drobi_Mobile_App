package com.smartdrobi.aplikasipkm.ui.customeview

import android.content.Context
import android.graphics.Canvas
import android.text.StaticLayout
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import kotlin.math.ceil


class JustifyTextView : AppCompatTextView {
    private var justify = false
    private var textAreaWidth = 0f
    private var spaceCharSize = 0f
    private var lineY = 0f

    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
        init(attrs)
    }

    /**
     * @param attrs the attributes from the xml
     * This function loads all the parameters from the xml
     */
    private fun init(attrs: AttributeSet?) {

        /*     val ta = context.obtainStyledAttributes(attrs, R.styleable.DTextView, 0, 0)
             justify = ta.getBoolean(R.styleable.DTextView_justify, false)
             ta.recycle()*/
    }

    override fun onDraw(canvas: Canvas) {
        drawText(canvas)
    }

    private fun drawText(canvas: Canvas) {
        val paint = paint
        paint.color = currentTextColor
        paint.drawableState = drawableState
        textAreaWidth = (measuredWidth - (paddingLeft + paddingRight)).toFloat()
        spaceCharSize = paint.measureText(" ")
        val text = text.toString()
        lineY = textSize
        val textLayout = layout ?: return
        val fm = paint.fontMetrics
        var textHeight = ceil((fm.descent - fm.ascent).toDouble()).toInt()
        textHeight = (textHeight * lineSpacingMultiplier + textLayout.spacingAdd).toInt()
        for (i in 0 until textLayout.lineCount) {
            val lineStart = textLayout.getLineStart(i)
            val lineEnd = textLayout.getLineEnd(i)
            val lineWidth = StaticLayout.getDesiredWidth(text, lineStart, lineEnd, paint)
            var line = text.substring(lineStart, lineEnd)
            if (line[line.length - 1] == ' ') {
                line = line.substring(0, line.length - 1)
            }
            if (justify && i < textLayout.lineCount - 1) {
                drawLineJustified(canvas, line, lineWidth)
            } else {
                canvas.drawText(line, 0f, lineY, paint)
            }
            lineY += textHeight.toFloat()
        }
    }

    private fun drawLineJustified(canvas: Canvas, line: String, lineWidth: Float) {
        val paint = paint
        val emptySpace = textAreaWidth - lineWidth
        val spaces =
            line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
        val newSpaceSize = emptySpace / spaces + spaceCharSize
        var charX = 0f
        for (i in line.indices) {
            val character = line[i].toString()
            val charWidth = StaticLayout.getDesiredWidth(character, paint)
            if (character != " ") {
                canvas.drawText(character, charX, lineY, paint)
            }
            charX += if (character == " " && i != line.length - 1) newSpaceSize else charWidth
        }
    }
}