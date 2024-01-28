package com.smartdrobi.aplikasipkm.domain.model

interface ListItem {
    fun getItemId(): Long

    fun equalsTo(any: Any): Boolean
}