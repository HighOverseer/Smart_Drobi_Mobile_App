package com.smartdrobi.aplikasipkm.domain

import android.content.Context
import androidx.annotation.StringRes as StrRes

sealed class StringRes {
    fun getValue(context: Context): String {
        return when (this) {
            is DynamicString -> stringVal
            is StaticString -> {
                this.arg?.let {
                    context.getString(stringResId, it)
                } ?: context.getString(stringResId)

            }
        }
    }

}

class DynamicString(val stringVal: String) : StringRes()

class StaticString(@StrRes val stringResId: Int, val arg: String? = null) : StringRes()
