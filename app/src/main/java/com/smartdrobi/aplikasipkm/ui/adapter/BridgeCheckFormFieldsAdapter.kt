package com.smartdrobi.aplikasipkm.ui.adapter

import android.text.Editable
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.smartdrobi.aplikasipkm.databinding.AddContainerBooleansTypeItemLayoutBinding
import com.smartdrobi.aplikasipkm.databinding.AddDateTypeItemLayoutBinding
import com.smartdrobi.aplikasipkm.databinding.AddHeaderTypeItemLayoutBinding
import com.smartdrobi.aplikasipkm.databinding.AddMultifieldTextTypeItemLayoutBinding
import com.smartdrobi.aplikasipkm.databinding.AddMultilineTextTypeItemLayoutBinding
import com.smartdrobi.aplikasipkm.databinding.AddTextBooleanTypeItemLayoutBinding
import com.smartdrobi.aplikasipkm.databinding.AddTextBooleanWithoutImagesItemLayoutBinding
import com.smartdrobi.aplikasipkm.databinding.AddTextTypeItemLayoutBinding
import com.smartdrobi.aplikasipkm.domain.helper.countSpanImageCollection
import com.smartdrobi.aplikasipkm.domain.helper.setInputType
import com.smartdrobi.aplikasipkm.domain.helper.setMargin
import com.smartdrobi.aplikasipkm.domain.model.BridgeCheckField
import com.smartdrobi.aplikasipkm.ui.addbridge.fragment.form.BridgeEmergencyFormFragment

class BridgeCheckFormFieldsAdapter(
    private val fields:List<BridgeCheckField>,
    private val onGeneralItemCallback: OnGeneralItemCallback
):RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var booleansOnItemCallback: BooleanQuestionsAdapter.OnItemCallback?=null
    fun setBooleansQuestionItemCallback(callback:BooleanQuestionsAdapter.OnItemCallback){
        booleansOnItemCallback = callback
    }

    private var headerBooleanQuestionWithImagesCallback:OnHeaderBooleanQuestionWithImagesCallback?=null
    fun setHeaderBooleanQuestionWithImagesCallback(callback: OnHeaderBooleanQuestionWithImagesCallback){
        headerBooleanQuestionWithImagesCallback = callback
    }

    private var headerBooleanQuestionCallback:OnHeaderBooleanQuestionCallback?=null
    fun setHeaderBooleanQuestionImagesCallback(callback: OnHeaderBooleanQuestionCallback){
        headerBooleanQuestionCallback = callback
    }

    private var textDateCallback:OnTextDateCallback?=null
    fun setTextDateCallback(callback: OnTextDateCallback){
        textDateCallback = callback
    }

    private var onImageCollectionCallback:ImageCollectionAdapter.OnImageCollectionCallback?=null
    fun setImageCollectionCallback(callback: ImageCollectionAdapter.OnImageCollectionCallback){
        onImageCollectionCallback = callback
    }

    private var textCallback:OnTextCallback?=null
    fun setTextCallback(callback:OnTextCallback){
        textCallback = callback
    }

    private var multiFieldCallback:OnMultiFieldCallback?=null
    fun setMultiFieldCallback(callback:OnMultiFieldCallback){
        multiFieldCallback = callback
    }

    sealed class ItemCallbackAction private constructor(){
        data object ClearFocus:ItemCallbackAction()

        data class SaveHeaderImageCollectionVisibility(
            val fieldPosition: Int,
            val newVisibility: Boolean
        ):ItemCallbackAction()

        data class ShowDatePicker(
            val fieldPosition: Int
        ):ItemCallbackAction()

        data class SaveTextRegularText(
            val fieldPosition: Int,
            val lastText:String
        ):ItemCallbackAction()

        data class SaveBooleanAnswers(
            val fieldPosition: Int,
            val newAnswer: BridgeCheckField.BooleanQuestionAnswer
        ):ItemCallbackAction()

        data class SaveMultifieldText(
            val viewId:BridgeEmergencyFormFragment.ElementFieldViewId,
            val newText: String
        ):ItemCallbackAction()

    }

    class HeaderViewHolder(
        val binding:AddHeaderTypeItemLayoutBinding,
        inline val action:(ItemCallbackAction) -> Unit
    ):RecyclerView.ViewHolder(binding.root), BindViewHolder{
        init {
            binding.root.setOnClickListener {
                action(ItemCallbackAction.ClearFocus)
            }
        }

        override fun bind(currField: BridgeCheckField) {
            currField as BridgeCheckField.Header

            val context = itemView.context
            binding.tvInfoForm.text = currField.tvInfoText.getValue(
                context
            )
        }
    }

    class TextViewHolder(
        val binding:AddTextTypeItemLayoutBinding,
        inline val action:(ItemCallbackAction) -> Unit
    ):RecyclerView.ViewHolder(binding.root), BindViewHolder{

        init {
            binding.apply {
                root.setOnClickListener {
                    action(ItemCallbackAction.ClearFocus)
                }
                etInput.addTextChangedListener(
                    afterTextChanged = {
                        action(
                            ItemCallbackAction.SaveTextRegularText(
                                adapterPosition,
                                it.toString()
                            )
                        )
                    }
                )
            }
        }

        override fun bind(currField: BridgeCheckField) {
            currField as BridgeCheckField.RegularText

            val context = itemView.context
            binding.apply {

                tvInfo.text = currField.tvInfoText.getValue(
                    context
                )
                tvInfo.setMargin(
                    currField.marginTop
                )

                etInput.setText(currField.lastEtText)

                etInput.setMargin(
                    marginStart = currField.marginStart
                )
                etInput.setInputType(currField.inputType)

                //set et max length
                etInput.filters = arrayOf(
                    InputFilter.LengthFilter(
                        currField.maxLength
                    )
                )
            }
        }
    }

    class DateViewHolder(
        val binding:AddDateTypeItemLayoutBinding,
        inline val action:(ItemCallbackAction) -> Unit
    ):RecyclerView.ViewHolder(binding.root), BindViewHolder{
        init {
            binding.apply {
                root.setOnClickListener {
                    action(ItemCallbackAction.ClearFocus)
                }
                etDate.setOnClickListener {
                    action(
                        ItemCallbackAction.ClearFocus
                    )
                    action(
                        ItemCallbackAction.ShowDatePicker(adapterPosition)
                    )
                }

            }

        }

        override fun bind(currField: BridgeCheckField) {
            currField as BridgeCheckField.DateText

            val context = itemView.context
            binding.apply {
                tvInfo.text = currField.tvInfoText.getValue(
                    context
                )
                tvInfo.setMargin(
                    currField.marginTop
                )
                etDate.setText(currField.lastEtText)
                etDate.setMargin(
                    marginStart = currField.marginStart
                )
            }
        }
    }

    class MultilineTextViewHolder(
        val binding:AddMultilineTextTypeItemLayoutBinding,
        inline val action:(ItemCallbackAction) -> Unit
    ):RecyclerView.ViewHolder(binding.root), BindViewHolder{
        init {
            binding.apply {
                root.setOnClickListener {
                    action(ItemCallbackAction.ClearFocus)
                }
                etInput.addTextChangedListener(
                    afterTextChanged = {
                        action(
                            ItemCallbackAction.SaveTextRegularText(
                                adapterPosition,
                                it.toString()
                            )
                        )
                    }
                )
            }
        }

        override fun bind(currField: BridgeCheckField) {
            currField as BridgeCheckField.MultilineText

            val context = itemView.context
            binding.apply {
                tvInfo.text = currField.tvInfoText.getValue(
                    context
                )
                tvInfo.setMargin(
                    marginTop = currField.marginTop,
                    marginStart = currField.marginStart
                )
                etInput.setText(currField.lastEtText)
            }
        }
    }

    class ContainerBooleansViewHolder(
        val binding:AddContainerBooleansTypeItemLayoutBinding,
        inline val action:(ItemCallbackAction) -> Unit
    ):RecyclerView.ViewHolder(binding.root), BindViewHolder{

        private var booleansOnItemCallback:BooleanQuestionsAdapter.OnItemCallback?=null
        fun setBooleanOnItemCallback(callback: BooleanQuestionsAdapter.OnItemCallback){
            booleansOnItemCallback = callback
        }

        private var onImageCollectionCallback:ImageCollectionAdapter.OnImageCollectionCallback?=null
        fun setImageCollectionCallback(callback: ImageCollectionAdapter.OnImageCollectionCallback){
            onImageCollectionCallback = callback
        }

        init {
            binding.apply {
                val displayMetrics = binding.root.resources.displayMetrics
                root.setOnClickListener {
                    action(ItemCallbackAction.ClearFocus)
                }
                rvQuestions.addItemDecoration(
                    BridgePreviewsItemDecoration(
                        displayMetrics,
                        includeLastOne = true,
                        paddingBottom = 2
                    )
                )
            }
        }

        override fun bind(currField: BridgeCheckField) {
            currField as BridgeCheckField.ContainerBooleans

            val context = itemView.context
            binding.apply {
                tvHeader.text = currField.tvInfoText.getValue(
                    context
                )
                val parentFieldPosition = adapterPosition

                val isCallbacksNull = booleansOnItemCallback == null && onImageCollectionCallback == null

                if (isCallbacksNull) return

                rvQuestions.adapter = BooleanQuestionsAdapter(
                    parentFieldPosition,
                    currField.booleanQuestions,
                    booleansOnItemCallback as BooleanQuestionsAdapter.OnItemCallback
                ).also {
                    it.setImageCollectionCallback(
                        onImageCollectionCallback as ImageCollectionAdapter.OnImageCollectionCallback
                    )
                }
            }
        }
    }

    class HeaderBooleanQuestionViewHolder(
        val binding:AddTextBooleanTypeItemLayoutBinding,
        inline val action:(ItemCallbackAction) -> Unit,
    ):RecyclerView.ViewHolder(binding.root), BindViewHolder{

        private var onImageCollectionCallback: ImageCollectionAdapter.OnImageCollectionCallback?=null
        fun setImageCollectionCallback(callback: ImageCollectionAdapter.OnImageCollectionCallback){
            onImageCollectionCallback = callback
        }

        init {
            val displayMetrics = itemView.context.resources.displayMetrics
            binding.apply {
                if (adapterPosition == 1){
                    tvQuestion.setMargin(marginTop = 0)
                }
                tvQuestion.textSize = 12f

                rvImages.addItemDecoration(
                    ImageCollectionDecoration(
                        displayMetrics,
                        4
                    )
                )

                root.setOnClickListener {
                    action(ItemCallbackAction.ClearFocus)
                }

                btnYes.setOnClickListener {
                    action(
                        ItemCallbackAction.SaveBooleanAnswers(
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
                        ItemCallbackAction.SaveBooleanAnswers(
                            adapterPosition,
                            BridgeCheckField.BooleanQuestionAnswer.NO
                        )
                    )
                    setAnswer(
                        BridgeCheckField.BooleanQuestionAnswer.NO
                    )
                }

                /*btnChooseDroneCamera.setOnClickListener {
                    action(
                        ItemCallbackAction.ShowDialogImageChooser(
                            adapterPosition
                        )
                    )
                }*/

                btnImagesVisibility.setOnClickListener {
                    val newVisibility = !btnImagesVisibility.isActivated
                    action(
                        ItemCallbackAction.SaveHeaderImageCollectionVisibility(
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

        private fun setAnswer(answer: BridgeCheckField.BooleanQuestionAnswer){
            binding.apply {
                when(answer){
                    BridgeCheckField.BooleanQuestionAnswer.NONE ->{
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

        override fun bind(currField: BridgeCheckField) {
            currField as BridgeCheckField.BooleanQuestion

            val context = itemView.context
            binding.apply {
                setImageCollectionVisibility(currField.isImageCollectionsVisible)
                setAnswer(currField.answer)

                val spanCount = countSpanImageCollection(
                    currField.listImagePath.size,4
                )

                tvQuestion.text = currField.tvInfoText.getValue(
                    context
                )

                rvImages.apply {
                    onImageCollectionCallback?.let {
                        val fieldPosition = adapterPosition
                        adapter = ImageCollectionAdapter(
                            currField.listImagePath,
                            spanCount,
                            4,
                            fieldPosition,
                            callback = it
                        )
                        layoutManager = GridLayoutManager(
                            context,
                            spanCount,
                            GridLayoutManager.HORIZONTAL,
                            false
                        )
                    }


                }
            }
        }

        private fun setImageCollectionVisibility(
            isShown:Boolean
        ){
            binding.apply {
                tvInfoPhoto.isVisible = isShown
                rvImages.isVisible = isShown
                /*btnChooseDroneCamera.isVisible = isShown
                btnChooseCamera.isVisible = isShown
                btnChooseFile.isVisible = isShown*/

                btnImagesVisibility.isActivated = isShown
            }
        }
    }

    class HeaderBooleanQuestionWithoutImagesVH(
        val binding:AddTextBooleanWithoutImagesItemLayoutBinding,
        inline val action:(ItemCallbackAction) -> Unit
    ):RecyclerView.ViewHolder(binding.root), BindViewHolder{
        init {
            binding.apply {
                btnYes.setOnClickListener {
                    action(
                        ItemCallbackAction.SaveBooleanAnswers(
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
                        ItemCallbackAction.SaveBooleanAnswers(
                            adapterPosition,
                            BridgeCheckField.BooleanQuestionAnswer.NO
                        )
                    )
                    setAnswer(
                        BridgeCheckField.BooleanQuestionAnswer.NO
                    )
                }
            }
        }

        private fun setAnswer(answer: BridgeCheckField.BooleanQuestionAnswer){
            binding.apply {
                when(answer){
                    BridgeCheckField.BooleanQuestionAnswer.NONE ->{
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

        override fun bind(currField: BridgeCheckField) {
            currField as BridgeCheckField.BooleanQuestionWithoutImages

            setAnswer(currField.answer)

            val context = itemView.context
            binding.apply {
                tvQuestion.text = currField.tvInfoText.getValue(
                    context
                )

            }
        }
    }
    
    class MultifieldViewHolder(
        val binding:AddMultifieldTextTypeItemLayoutBinding,
        inline val action:(ItemCallbackAction) -> Unit
    ):RecyclerView.ViewHolder(binding.root), BindViewHolder{

        private fun produceTextChangedListener(
            viewId:BridgeEmergencyFormFragment.ElementFieldViewId
        ):(Editable?) -> Unit{
            return {
                it?.let {
                    action(
                        ItemCallbackAction.SaveMultifieldText(
                            viewId,
                            it.toString()
                        )
                    )
                }

            }
        }


        init {
            binding.apply {
                root.setOnClickListener {
                    action(
                        ItemCallbackAction.ClearFocus
                    )
                }
                etCode.addTextChangedListener(
                    afterTextChanged = produceTextChangedListener(
                        BridgeEmergencyFormFragment.ElementFieldViewId.CODE
                    )
                )
                etDesc.addTextChangedListener(
                    afterTextChanged = produceTextChangedListener(
                        BridgeEmergencyFormFragment.ElementFieldViewId.DESC
                    )
                )
                etApb.addTextChangedListener(
                    afterTextChanged = produceTextChangedListener(
                        BridgeEmergencyFormFragment.ElementFieldViewId.APB
                    )
                )
                etX.addTextChangedListener(
                    afterTextChanged = produceTextChangedListener(
                        BridgeEmergencyFormFragment.ElementFieldViewId.X
                    )
                )
                etY.addTextChangedListener(
                    afterTextChanged = produceTextChangedListener(
                        BridgeEmergencyFormFragment.ElementFieldViewId.Y
                    )
                )
                etZ.addTextChangedListener(
                    afterTextChanged = produceTextChangedListener(
                        BridgeEmergencyFormFragment.ElementFieldViewId.Z
                    )
                )
                etReason.addTextChangedListener(
                    afterTextChanged = produceTextChangedListener(
                        BridgeEmergencyFormFragment.ElementFieldViewId.REASON
                    )
                )
            }
        }

        override fun bind(currField: BridgeCheckField) {
            currField as BridgeCheckField.MultifieldText

            binding.apply {
                currField.apply {
                    etCode.setText(code)
                    etDesc.setText(desc)
                    etApb.setText(apb)
                    etX.setText(x)
                    etY.setText(y)
                    etZ.setText(z)
                    etReason.setText(reason)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when(viewType){
            BridgeCheckField.HEADER_TYPE -> {
                val binding = getBinding(
                    parent,
                    AddHeaderTypeItemLayoutBinding::class.java
                )
                return HeaderViewHolder(binding, ::onAction)
            }
            BridgeCheckField.TEXT_TYPE -> {
                val binding = getBinding(
                    parent,
                    AddTextTypeItemLayoutBinding::class.java
                )
                return TextViewHolder(binding, ::onAction)
            }
            BridgeCheckField.MULTILINE_TEXT_TYPE -> {
                val binding = getBinding(
                    parent,
                    AddMultilineTextTypeItemLayoutBinding::class.java
                )
                return MultilineTextViewHolder(binding, ::onAction)
            }
            BridgeCheckField.CONTAINER_BOOLEANS_TYPE -> {
                val binding = getBinding(
                    parent,
                    AddContainerBooleansTypeItemLayoutBinding::class.java
                )
                return ContainerBooleansViewHolder(binding, ::onAction).apply {
                    booleansOnItemCallback?.let { setBooleanOnItemCallback(it) }
                    onImageCollectionCallback?.let { setImageCollectionCallback(it) }
                }
            }
            BridgeCheckField.BOOLEAN_QUESTION_TYPE ->{
                val binding = getBinding(
                    parent,
                    AddTextBooleanTypeItemLayoutBinding::class.java
                )
                return HeaderBooleanQuestionViewHolder(binding, ::onAction).also {
                    onImageCollectionCallback?.let { callback -> it.setImageCollectionCallback(callback) }
                }
            }
            BridgeCheckField.BOOLEAN_QUESTION_WITHOUT_IMAGES_TYPE ->{
                val binding = getBinding(
                    parent,
                    AddTextBooleanWithoutImagesItemLayoutBinding::class.java
                )
                return HeaderBooleanQuestionWithoutImagesVH(binding, ::onAction)
            }
            BridgeCheckField.MULTIFIELD_TEXT_TYPE ->{
                val binding = getBinding(
                    parent,
                    AddMultifieldTextTypeItemLayoutBinding::class.java
                )
                return MultifieldViewHolder(binding, ::onAction)
            }
            else -> {
                val binding = getBinding(
                    parent,
                    AddDateTypeItemLayoutBinding::class.java
                )
                return DateViewHolder(binding, ::onAction)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currField = fields[position]

        if (holder is BindViewHolder){
            holder.bind(currField)
        }
    }





   /* private fun AppCompatEditText.setMargin(
        marginStart:Int
    ){
        val displayMetrics = resources.displayMetrics
        val params = this.layoutParams as ConstraintLayout.LayoutParams
        params.marginStart = marginStart.toDp(displayMetrics)
        this.layoutParams = params
    }*/

    override fun getItemCount() = fields.size

    override fun getItemViewType(position: Int): Int {
        return fields[position].typeId
    }

    private fun onAction(action:ItemCallbackAction){
        when(action){
            is ItemCallbackAction.SaveTextRegularText -> textCallback?.saveLastText(
                action.fieldPosition,
                action.lastText
            )
            is ItemCallbackAction.SaveMultifieldText -> multiFieldCallback?.saveNewText(
                action.viewId,
                action.newText
            )
            ItemCallbackAction.ClearFocus -> onGeneralItemCallback.clearFocus()
            is ItemCallbackAction.SaveBooleanAnswers -> {
                headerBooleanQuestionWithImagesCallback?.saveAnswer(action.fieldPosition, action.newAnswer)
                    ?:headerBooleanQuestionCallback?.saveAnswer(action.fieldPosition, action.newAnswer)
            }
            is ItemCallbackAction.SaveHeaderImageCollectionVisibility -> {
                headerBooleanQuestionWithImagesCallback?.saveCollectionsVisibility(
                    action.fieldPosition,
                    action.newVisibility
                )
            }
            is ItemCallbackAction.ShowDatePicker -> {
                textDateCallback?.showDatePicker(action.fieldPosition)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun<T:ViewBinding>getBinding(parent: ViewGroup, bindingClass: Class<T>):T{
        val inflater = LayoutInflater.from(
            parent.context
        )
        val inflateMethod = bindingClass.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )

        return inflateMethod.invoke(null, inflater, parent, false) as T
    }

    interface BindViewHolder{
        fun bind(currField:BridgeCheckField)
    }

    interface OnTextCallback{
        fun saveLastText(
            fieldPosition: Int,
            newText:String
        )
    }

    interface OnGeneralItemCallback{
        fun clearFocus()
    }

    interface OnTextDateCallback{
        fun showDatePicker(
            fieldPosition:Int
        )
    }

    interface OnHeaderBooleanQuestionCallback{
        fun saveAnswer(
            fieldPosition: Int,
            newAnswer: BridgeCheckField.BooleanQuestionAnswer
        )
    }

    interface OnHeaderBooleanQuestionWithImagesCallback:OnHeaderBooleanQuestionCallback{
        fun saveCollectionsVisibility(
            selectedFieldPosition: Int,
            newVisibility:Boolean
        )
    }

    //sus
    interface OnMultiFieldCallback{
        fun saveNewText(
            viewId: BridgeEmergencyFormFragment.ElementFieldViewId,
            newText:String
        )
    }

}