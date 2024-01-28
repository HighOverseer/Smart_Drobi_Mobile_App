package com.smartdrobi.aplikasipkm.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smartdrobi.aplikasipkm.databinding.AddTextBooleanTypeItemLayoutBinding
import com.smartdrobi.aplikasipkm.domain.model.BridgeCheckField

class BooleanQuestionsAdapter(
    private val parentFieldPosition: Int,
    private val booleanQuestions: List<BridgeCheckField.BooleanQuestion>,
    private val onItemCallback: OnItemCallback
) : RecyclerView.Adapter<BooleanQuestionsAdapter.BooleanQuestionsViewHolder>() {

    private var onImageCollectionCallback: ImageCollectionAdapter.OnImageCollectionCallback? = null
    fun setImageCollectionCallback(callback: ImageCollectionAdapter.OnImageCollectionCallback) {
        onImageCollectionCallback = callback
    }

    sealed class OnItemCallbackAction private constructor() {
        data class SaveImageCollectionsVisibility(
            val fieldPosition: Int,
            val newVisibility: Boolean,
        ) : OnItemCallbackAction()

        data class SaveAnswer(
            val fieldPosition: Int,
            val newAnswer: BridgeCheckField.BooleanQuestionAnswer
        ) : OnItemCallbackAction()
    }

    class BooleanQuestionsViewHolder(
        val binding: AddTextBooleanTypeItemLayoutBinding,
        inline val action: (OnItemCallbackAction) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            val displayMetrics = itemView.context.resources.displayMetrics
            binding.apply {
                rvImages.addItemDecoration(
                    ImageCollectionDecoration(
                        displayMetrics,
                        4
                    )
                )

                btnYes.setOnClickListener {
                    action(
                        OnItemCallbackAction.SaveAnswer(
                            adapterPosition,
                            BridgeCheckField.BooleanQuestionAnswer.YES
                        )
                    )
                    setAnswer(
                        BridgeCheckField.BooleanQuestionAnswer.YES
                    )
                }

                btnNo.setOnClickListener {
                    action(
                        OnItemCallbackAction.SaveAnswer(
                            adapterPosition,
                            BridgeCheckField.BooleanQuestionAnswer.NO
                        )
                    )
                    setAnswer(
                        BridgeCheckField.BooleanQuestionAnswer.NO
                    )
                }


                btnImagesVisibility.setOnClickListener {
                    val newVisibility = !btnImagesVisibility.isActivated
                    action(
                        OnItemCallbackAction.SaveImageCollectionsVisibility(
                            adapterPosition,
                            newVisibility
                        )
                    )
                    setImageCollectionVisibility(
                        newVisibility
                    )
                }
            }

        }

        fun setAnswer(answer: BridgeCheckField.BooleanQuestionAnswer) {
            binding.apply {
                when (answer) {
                    BridgeCheckField.BooleanQuestionAnswer.NONE -> {
                        btnYes.alpha = 0.3f
                        btnNo.alpha = 0.3f
                    }

                    BridgeCheckField.BooleanQuestionAnswer.YES -> {
                        btnYes.alpha = 1f
                        btnNo.alpha = 0.3f
                    }

                    BridgeCheckField.BooleanQuestionAnswer.NO -> {
                        btnNo.alpha = 1f
                        btnYes.alpha = 0.3f
                    }
                }
            }
        }

        fun setImageCollectionVisibility(
            isShown: Boolean
        ) {
            binding.apply {
                tvInfoPhoto.isVisible = isShown
                rvImages.isVisible = isShown
                btnImagesVisibility.isActivated = isShown
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooleanQuestionsViewHolder {
        val binding = AddTextBooleanTypeItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BooleanQuestionsViewHolder(binding, ::action)
    }

    override fun onBindViewHolder(holder: BooleanQuestionsViewHolder, position: Int) {
        val currQuestion = booleanQuestions[position]
        val context = holder.itemView.context
        holder.apply {
            binding.apply {
                tvQuestion.text = currQuestion.tvInfoText.getValue(
                    context
                )
                setImageCollectionVisibility(
                    currQuestion.isImageCollectionsVisible
                )
                setAnswer(currQuestion.answer)


                onImageCollectionCallback?.let {
                    val fieldPosition = holder.adapterPosition
                    rvImages.apply {
                        adapter = ImageCollectionAdapter(
                            currQuestion.listImagePath,
                            fieldPosition,
                            parentFieldPosition,
                            it
                        )

                        layoutManager = GridLayoutManager(
                            context,
                            4,
                            GridLayoutManager.VERTICAL,
                            false
                        )
                    }
                }
            }
        }
    }

    private fun action(onItemCallbackAction: OnItemCallbackAction) {
        when (onItemCallbackAction) {
            is OnItemCallbackAction.SaveImageCollectionsVisibility -> {
                onItemCallback.saveImageCollectionsVisibility(
                    parentFieldPosition,
                    onItemCallbackAction.fieldPosition,
                    onItemCallbackAction.newVisibility
                )
            }

            is OnItemCallbackAction.SaveAnswer -> {
                onItemCallback.saveAnswerInChild(
                    parentFieldPosition,
                    onItemCallbackAction.fieldPosition,
                    onItemCallbackAction.newAnswer
                )
            }
        }
    }

    override fun getItemCount() = booleanQuestions.size

    interface OnItemCallback {

        fun saveImageCollectionsVisibility(
            parentFieldPosition: Int,
            fieldPosition: Int,
            newVisibility: Boolean
        )

        fun saveAnswerInChild(
            parentFieldPosition: Int,
            fieldPosition: Int,
            newAnswer: BridgeCheckField.BooleanQuestionAnswer
        )
    }
}