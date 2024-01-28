package com.smartdrobi.aplikasipkm.ui.customeview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.KeyEvent
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.smartdrobi.aplikasipkm.R

class MySearchView : SearchView {
    private val mPaint = Paint()
    private lateinit var hintText: String
    private val colorGrey = ResourcesCompat.getColor(resources, R.color.grey_10, null)

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleInt: Int) : super(
        context,
        attrs,
        defStyleInt
    ) {
        init(context)
    }

    private val editText: SearchAutoComplete = findViewById(androidx.appcompat.R.id.search_src_text)

    private fun init(context: Context) {
        setIconifiedByDefault(false)
        background = ResourcesCompat.getDrawable(resources, R.drawable.search_view_bg, null)
        hintText = context.getString(R.string.search_view_hint)


        editText.textSize = 11f
        editText.isFocusableInTouchMode = true


        val clearBtn = findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        clearBtn.layoutParams.height = 30f.toDp().toInt()
        clearBtn.layoutParams.width = 30f.toDp().toInt()
        clearBtn.layoutParams = clearBtn.layoutParams

        mPaint.apply {
            textSize = 10f.toDp()
            color = colorGrey
            typeface = ResourcesCompat.getFont(context, R.font.sans_semi_bold)
        }
    }

    fun clearEditTextFocus() {
        editText.clearFocus()
    }

    fun setEditTextFocusChangeListener(listener: OnFocusChangeListener) {
        editText.onFocusChangeListener = listener
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!hasFocus() && query.isEmpty()) {
            isActivated = false
            mPaint.color = colorGrey
            canvas.drawText(hintText, 11f.toDp(), 18.5f.toDp(), mPaint)
            canvas.drawBitmap(
                vectorToBitmap(
                    R.drawable.ic_search,
                    ResourcesCompat.getColor(resources, R.color.blue, null)
                )!!,
                measuredWidth - 26f.toDp(),
                8f.toDp(),
                mPaint
            )
        } else if (hasFocus()) {
            isActivated = true
            if (query.isEmpty()) {
                mPaint.color = colorGrey
                canvas.drawText(hintText, 26f.toDp(), 18.5f.toDp(), mPaint)
            }
        }
    }


    private fun vectorToBitmap(@DrawableRes id: Int, @ColorInt color: Int): Bitmap? {
        val vectorDrawable = ResourcesCompat.getDrawable(resources, id, null) ?: return null
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        DrawableCompat.setTint(vectorDrawable, color)
        vectorDrawable.draw(canvas)
        return bitmap
    }

    private fun Float.toDp(): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            resources.displayMetrics
        )
    }


}