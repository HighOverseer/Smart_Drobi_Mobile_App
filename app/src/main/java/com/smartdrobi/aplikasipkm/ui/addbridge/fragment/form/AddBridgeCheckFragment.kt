package com.smartdrobi.aplikasipkm.ui.addbridge.fragment.form

import android.app.DatePickerDialog
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.smartdrobi.aplikasipkm.R
import com.smartdrobi.aplikasipkm.ui.adapter.BridgeCheckFormFieldsAdapter
import com.smartdrobi.aplikasipkm.ui.addbridge.uiaction.CheckFormUiAction
import java.util.Calendar


class AddBridgeCheckFragment
    : BaseFormFragment(FormPage.FIRST),
    BridgeCheckFormFieldsAdapter.OnTextDateCallback,
    BridgeCheckFormFieldsAdapter.OnTextCallback{

    override fun showDatePicker(fieldPosition: Int) {
        binding?.apply {
            try {
                val view = rvFields.getChildAt(fieldPosition)
                val etDate = view.findViewById<AppCompatEditText>(R.id.et_date)
                showDatePicker(fieldPosition, etDate)
            }catch (e:Exception){
                e.printStackTrace()
            }

        }
    }

    override fun saveLastText(fieldPosition: Int, newText: String) {
        viewModel.sendAction(
            CheckFormUiAction.SaveLastText(
                fieldPosition,
                newText
            )
        )
    }

    private fun showDatePicker(fieldPosition: Int, clickedView: View){
        val editText = clickedView as AppCompatEditText
        val currSelectedDate = editText.text.toString()

        val year:Int
        val month:Int
        val day:Int

        if (currSelectedDate.isBlank()){
            val c = Calendar.getInstance()
            year = c.get(Calendar.YEAR)
            month = c.get(Calendar.MONTH)
            day = c.get(Calendar.DAY_OF_MONTH)
        }else{
            val splittedDate = currSelectedDate.split("/")
            day = splittedDate[0].toInt()
            month = splittedDate[1].toInt() - 1
            year = splittedDate[2].toInt()
        }

        DatePickerDialog(requireActivity(), { _, yearr,monthOfTheYear, dayOfTheMonth ->
            val newDate = getString(
                R.string.date,
                dayOfTheMonth.toString(),
                monthOfTheYear.plus(1).toString(),
                yearr.toString()
            )
            editText.setText(
                newDate
            )
            viewModel.sendAction(
                CheckFormUiAction.SaveLastText(
                    fieldPosition,
                    newDate
                )
            )
        }, year, month, day).show()
    }


    enum class ViewId(val id:Int){
        INSPECTOR_NAME(0),
        CHECK_DATE(1),
        TRAFFIC_VAL(2),
        LHR(3),
        Year(4),
        Note(5);
    }


}