package com.smartdrobi.aplikasipkm.domain.model

import com.smartdrobi.aplikasipkm.domain.StringRes


sealed class BridgeCheckField private constructor(
    val typeId: Int,
    open val fieldId: Int,
    inline val saveToBridgeCheck: (BridgeCheck, Field) -> Unit = { _, _ -> }
) {
    data class Header(
        val tvInfoText: StringRes
    ) : BridgeCheckField(HEADER_TYPE, HEADER_ID)

    data class RegularText(
        val id: Int,
        val tvInfoText: StringRes,
        val marginStart: Int,
        val marginTop: Int,
        val maxLength: Int,
        val inputType: EditTextInputType = EditTextInputType.TEXT,
        var lastEtText: String = "",
        inline val save: (BridgeCheck, Field) -> Unit
    ) : BridgeCheckField(TEXT_TYPE, id, save), Text {
        override fun saveNewText(newText: String) {
            lastEtText = newText
        }

        @Suppress("UNCHECKED_CAST")
        override fun <T> getValue(): T {
            return lastEtText as T
        }
    }

    data class DateText(
        val id: Int,
        val tvInfoText: StringRes,
        val marginStart: Int,
        val marginTop: Int,
        var lastEtText: String = "",
        inline val save: (BridgeCheck, Field) -> Unit
    ) : BridgeCheckField(DATE_TYPE, id, save), Text {
        override fun saveNewText(newText: String) {
            lastEtText = newText
        }

        @Suppress("UNCHECKED_CAST")
        override fun <T> getValue(): T {
            return lastEtText as T
        }
    }

    data class MultilineText(
        val id: Int,
        val tvInfoText: StringRes,
        val marginStart: Int,
        val marginTop: Int,
        var lastEtText: String = "",
        inline val save: (BridgeCheck, Field) -> Unit
    ) : BridgeCheckField(MULTILINE_TEXT_TYPE, id, save), Text {
        override fun saveNewText(newText: String) {
            lastEtText = newText
        }

        @Suppress("UNCHECKED_CAST")
        override fun <T> getValue(): T {
            return lastEtText as T
        }
    }

    data class ContainerBooleans(
        val id: Int,
        val tvInfoText: StringRes,
        val booleanQuestions: List<BooleanQuestion>,
    ) : BridgeCheckField(CONTAINER_BOOLEANS_TYPE, id)

    @Suppress("UNCHECKED_CAST")
    data class BooleanQuestion(
        val questionId: Int,
        val tvInfoText: StringRes,
        var answer: BooleanQuestionAnswer = BooleanQuestionAnswer.NONE,
        val listImagePath: MutableList<String> = mutableListOf(),
        var isImageCollectionsVisible: Boolean = false,
        inline val save: (BridgeCheck, Field) -> Unit
    ) : BridgeCheckField(BOOLEAN_QUESTION_TYPE, questionId, save), BooleanField {
        override fun saveNewAnswer(newAnswer: BooleanQuestionAnswer) {
            answer = newAnswer
        }

        override fun <T> getValue(): T {
            return BooleanQuestionContent(
                answer,
                listImagePath
            ) as T
        }
    }

    data class BooleanQuestionWithoutImages(
        val id: Int,
        val tvInfoText: StringRes,
        var answer: BooleanQuestionAnswer = BooleanQuestionAnswer.NONE,
        inline val save: (BridgeCheck, Field) -> Unit
    ) : BridgeCheckField(BOOLEAN_QUESTION_WITHOUT_IMAGES_TYPE, id, save), BooleanField {
        override fun saveNewAnswer(newAnswer: BooleanQuestionAnswer) {
            answer = newAnswer
        }

        @Suppress("UNCHECKED_CAST")
        override fun <T> getValue(): T {
            return answer as T
        }
    }

    data class BooleanQuestionContent(
        val answer: BooleanQuestionAnswer,
        val listImagePaths: List<String>
    )

    data class MultifieldText(
        val id: Int,
        var code: String = "",
        var desc: String = "",
        var apb: String = "",
        var x: String = "",
        var y: String = "",
        var z: String = "",
        var reason: String = "",
        inline val save: (BridgeCheck, Field) -> Unit
    ) : BridgeCheckField(MULTIFIELD_TEXT_TYPE, id, save), Field {

        @Suppress("UNCHECKED_CAST")
        override fun <T> getValue(): T {
            return MultifieldContent(
                code, desc, apb, x, y, z, reason
            ) as T
        }
    }

    data class MultifieldContent(
        val code: String = "",
        val desc: String = "",
        val apb: String = "",
        val x: String = "",
        val y: String = "",
        val z: String = "",
        val reason: String = "",
    )

    enum class EditTextInputType {
        TEXT,
        NUMBER_DECIMAL,
        NUMBER
    }

    enum class BooleanQuestionAnswer {
        NONE,
        YES,
        NO
    }

    interface Field {
        fun <T> getValue(): T
    }

    interface BooleanField : Field {
        fun saveNewAnswer(newAnswer: BooleanQuestionAnswer)
    }

    interface Text : Field {
        fun saveNewText(newText: String)
    }


    companion object {
        const val HEADER_ID = -1

        const val HEADER_TYPE = 1

        //const val ADD_IMAGE_TYPE = 2
        const val TEXT_TYPE = 3
        const val DATE_TYPE = 4
        const val MULTILINE_TEXT_TYPE = 5
        const val CONTAINER_BOOLEANS_TYPE = 6
        const val BOOLEAN_QUESTION_TYPE = 7
        const val BOOLEAN_QUESTION_WITHOUT_IMAGES_TYPE = 8
        const val MULTIFIELD_TEXT_TYPE = 9
    }
}