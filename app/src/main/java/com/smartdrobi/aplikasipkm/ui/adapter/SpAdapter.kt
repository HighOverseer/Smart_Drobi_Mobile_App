package com.smartdrobi.aplikasipkm.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.res.ResourcesCompat
import com.smartdrobi.aplikasipkm.R
import com.smartdrobi.aplikasipkm.databinding.SpinnerDropdownItemBinding
import com.smartdrobi.aplikasipkm.databinding.SpinnerItemBinding

class SpAdapter(
    context: Context,
    private val listItem: List<String>,
) : ArrayAdapter<String>(context, 0, listItem) {

    private val colorGrey = ResourcesCompat.getColor(context.resources, R.color.grey_1000, null)
    private val colorBlack = ResourcesCompat.getColor(context.resources, R.color.black_100, null)

    override fun isEnabled(position: Int): Boolean {
        return position != 0
    }


    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val currItem = getItem(position)
        val textView = if (convertView != null) {
            convertView.findViewById(android.R.id.text1)
        } else {
            val binding = SpinnerDropdownItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            binding.text1
        }
        return textView.apply {
            text = currItem
            if (currItem == listItem[0]) {
                setTextColor(colorGrey)
            } else setTextColor(colorBlack)

            if (currItem == listItem.last()) {
                setBackgroundResource(R.drawable.spinner_dropdown_bg)
            }
        }

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): String {
        return listItem[position]
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val currSelectedItem = getItem(position)
        val textView = if (convertView != null) {
            convertView.findViewById(android.R.id.text1)
        } else {
            val binding =
                SpinnerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            binding.text1
        }
        textView.text = currSelectedItem
        if (position == 0) {
            textView.setTextColor(colorGrey)
        }
        return textView
    }

}