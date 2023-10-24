package com.smartdrobi.aplikasipkm.ui.addbridge.fragment.form

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.smartdrobi.aplikasipkm.R
import com.smartdrobi.aplikasipkm.databinding.FragmentAddBaseFormBinding
import com.smartdrobi.aplikasipkm.ui.adapter.BooleanQuestionsAdapter
import com.smartdrobi.aplikasipkm.ui.adapter.BridgeCheckFormFieldsAdapter
import com.smartdrobi.aplikasipkm.ui.adapter.ImageCollectionAdapter
import com.smartdrobi.aplikasipkm.ui.addbridge.uievent.CheckFormUiEvent
import com.smartdrobi.aplikasipkm.ui.addbridge.viewmodel.AddBridgeCheckFormViewModel

abstract class BaseFormFragment(
    private val formPage: FormPage
) : Fragment() {
    protected var binding: FragmentAddBaseFormBinding? = null

    protected val viewModel by activityViewModels<AddBridgeCheckFormViewModel>()
    protected lateinit var rvAdapter: BridgeCheckFormFieldsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBaseFormBinding.inflate(inflater, container, false)
        return binding?.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding?.apply {

            root.setOnClickListener {
                clearAllViewFocus()
            }

            //for handling click on rv empty space
            /*rvFields.setOnTouchListener { v, _ ->
                if (v == root){
                    root.performClick()
                }
                false
            }*/
        }

        viewModel.apply {
            uiState.observe(viewLifecycleOwner) { uiState ->
                binding?.apply {
                    if (uiState.currentFormPage != FormPage.NONE) {

                        progressBar.isVisible = false
                        rvFields.isVisible = true

                        rvAdapter = BridgeCheckFormFieldsAdapter(
                            uiState.currentListFields,
                            onGeneralItemCallbackAction
                        ).also { setCallbacksIfAny(it) }


                        rvFields.adapter = rvAdapter
                    } else {
                        progressBar.isVisible = true
                        rvFields.isVisible = false
                    }
                }

            }

            uiEvent.observe(viewLifecycleOwner) { event ->
                when (event) {
                    is CheckFormUiEvent.NotifyAddedImage -> {
                        event {
                            rvAdapter.notifyItemChanged(
                                event.selectedFieldPosition
                            )
                        }
                    }
                    is CheckFormUiEvent.NotifyAddedImageOnNestedRv ->{
                        event{
                            try {
                                binding?.apply {
                                    val childView = rvFields.layoutManager?.findViewByPosition(event.parentFieldPosition)
                                    val rvChild = childView?.findViewById<RecyclerView>(R.id.rv_questions)
                                    rvChild?.adapter?.notifyItemChanged(event.fieldPosition)
                                }
                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }
                    }
                    is CheckFormUiEvent.EndingSession -> {
                        event{
                            val activity = requireActivity()
                            activity.setResult(event.resultCode)
                            activity.finish()
                        }
                    }
                }
            }
        }
    }

    private fun setCallbacksIfAny(adapter:BridgeCheckFormFieldsAdapter){
        if (this@BaseFormFragment is BooleanQuestionsAdapter.OnItemCallback) {
            adapter.setBooleansQuestionItemCallback(this@BaseFormFragment)
        }

        if (this@BaseFormFragment is BridgeCheckFormFieldsAdapter.OnHeaderBooleanQuestionWithImagesCallback) {
            adapter.setHeaderBooleanQuestionWithImagesCallback(this@BaseFormFragment)
        }

        if (this@BaseFormFragment is BridgeCheckFormFieldsAdapter.OnTextDateCallback){
            adapter.setTextDateCallback(this@BaseFormFragment)
        }

        if (this@BaseFormFragment is BridgeCheckFormFieldsAdapter.OnTextCallback){
            adapter.setTextCallback(this@BaseFormFragment)
        }

        if (this@BaseFormFragment is ImageCollectionAdapter.OnImageCollectionCallback){
            adapter.setImageCollectionCallback(this)
        }

        if (this@BaseFormFragment is BridgeCheckFormFieldsAdapter.OnHeaderBooleanQuestionCallback){
            adapter.setHeaderBooleanQuestionImagesCallback(this)
        }

        if (this@BaseFormFragment is BridgeCheckFormFieldsAdapter.OnMultiFieldCallback){
            adapter.setMultiFieldCallback(this)
        }
    }


    enum class FormPage {
        NONE, //just for initialization

        FIRST,
        SECURITY,
        SAFETY,
        CONVENIENCE,
        MAINTENANCE,
        SOCIAL,
        EMERGENCY
    }


    private val onGeneralItemCallbackAction by lazy {
        object : BridgeCheckFormFieldsAdapter.OnGeneralItemCallback {
            override fun clearFocus() {
                clearAllViewFocus()
            }
        }
    }


    protected fun clearAllViewFocus() {
        binding?.rvFields?.focusedChild?.clearFocus()
        closeKeyboard()
    }

    private fun closeKeyboard() {
        val imm = requireActivity()
            .getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager

        imm.hideSoftInputFromWindow(
            view?.windowToken,
            0
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}