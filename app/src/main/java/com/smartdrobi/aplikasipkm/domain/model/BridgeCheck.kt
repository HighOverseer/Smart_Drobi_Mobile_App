package com.smartdrobi.aplikasipkm.domain.model

import com.smartdrobi.aplikasipkm.domain.helper.getCurrentDate
import java.util.Date


data class BridgeCheck(
    val id: Int = 0,
    val bridgeId: Int,
    var firstPageAnswer: FirstPageAnswer = FirstPageAnswer(),
    var securityPageAnswer: SecurityPageAnswer = SecurityPageAnswer(),
    var safetyPageAnswer: SafetyPageAnswer = SafetyPageAnswer(),
    var conveniencePageAnswer: ConveniencePageAnswer = ConveniencePageAnswer(),
    var maintenancePageAnswer: MaintenancePageAnswer = MaintenancePageAnswer(),
    var socialPageAnswer: SocialPageAnswer = SocialPageAnswer(),
    var emergencyPageAnswer: EmergencyPageAnswer = EmergencyPageAnswer(),
    val inspectionDate: Date = getCurrentDate()
)


data class FirstPageAnswer(
    val inspectorName: String = "",
    val inspectionDate: String = "",
    val trafficValue: Double? = null,
    val lhr: Double? = null,
    val year: Int? = null,
    val note: String = "",
)

data class SecurityPageAnswer(
    val landfillAnswer: BridgeCheckField.BooleanQuestionAnswer = BridgeCheckField.BooleanQuestionAnswer.NONE,
    val landFillImagePaths: List<String> = emptyList(),

    val riverFlowAnswer: MutableList<BridgeCheckField.BooleanQuestionAnswer> = MutableList(4) { BridgeCheckField.BooleanQuestionAnswer.NONE },
    val riverFlowImagePaths: MutableList<List<String>> = MutableList(4) { emptyList() },

    val foundationAnswer: MutableList<BridgeCheckField.BooleanQuestionAnswer> = MutableList(4) { BridgeCheckField.BooleanQuestionAnswer.NONE },
    val foundationImagePaths: MutableList<List<String>> = MutableList(4) { emptyList() },

    val lowerBuildingAnswer: MutableList<BridgeCheckField.BooleanQuestionAnswer> = MutableList(6) { BridgeCheckField.BooleanQuestionAnswer.NONE },
    val lowerBuildingImagePaths: MutableList<List<String>> = MutableList(6) { emptyList() },

    val upperBuildingAnswer: MutableList<BridgeCheckField.BooleanQuestionAnswer> = MutableList(12) { BridgeCheckField.BooleanQuestionAnswer.NONE },
    val upperBuildingImagePaths: MutableList<List<String>> = MutableList(12) { emptyList() },

    val siarMuaiAnswer: MutableList<BridgeCheckField.BooleanQuestionAnswer> = MutableList(3) { BridgeCheckField.BooleanQuestionAnswer.NONE },
    val siarMuaiImagePaths: MutableList<List<String>> = MutableList(3) { emptyList() },

    val placementAnswer: MutableList<BridgeCheckField.BooleanQuestionAnswer> = MutableList(3) { BridgeCheckField.BooleanQuestionAnswer.NONE },
    val placementImagePaths: MutableList<List<String>> = MutableList(3) { emptyList() },
)

data class SafetyPageAnswer(
    val backRestAnswer: MutableList<BridgeCheckField.BooleanQuestionAnswer> = MutableList(3) { BridgeCheckField.BooleanQuestionAnswer.NONE },
    val backRestImagePaths: MutableList<List<String>> = MutableList(3) { emptyList() },

    val signAnswer: MutableList<BridgeCheckField.BooleanQuestionAnswer> = MutableList(2) { BridgeCheckField.BooleanQuestionAnswer.NONE },
    val signImagePaths: MutableList<List<String>> = MutableList(2) { emptyList() },

    val lightningRodAnswer: MutableList<BridgeCheckField.BooleanQuestionAnswer> = MutableList(4) { BridgeCheckField.BooleanQuestionAnswer.NONE },
    val lightningRodImagePaths: MutableList<List<String>> = MutableList(4) { emptyList() },

    val smksAnswer: MutableList<BridgeCheckField.BooleanQuestionAnswer> = MutableList(2) { BridgeCheckField.BooleanQuestionAnswer.NONE },
    val smksImagePaths: MutableList<List<String>> = MutableList(2) { emptyList() },
)

data class ConveniencePageAnswer(
    val floorSystemAnswer: BridgeCheckField.BooleanQuestionAnswer = BridgeCheckField.BooleanQuestionAnswer.NONE,
    val floorSystemImagePaths: List<String> = emptyList(),

    val upperBuildingAnswer: BridgeCheckField.BooleanQuestionAnswer = BridgeCheckField.BooleanQuestionAnswer.NONE,
    val upperBuildingImagePaths: List<String> = emptyList(),

    val shortRoadAnswer: BridgeCheckField.BooleanQuestionAnswer = BridgeCheckField.BooleanQuestionAnswer.NONE,
    val shortRoadImagePaths: List<String> = emptyList(),
)

data class MaintenancePageAnswer(
    val routineAnswer: MutableList<BridgeCheckField.BooleanQuestionAnswer> = MutableList(4) { BridgeCheckField.BooleanQuestionAnswer.NONE },
    val routineImagePaths: MutableList<List<String>> = MutableList(4) { emptyList() },

    val periodicAnswer: MutableList<BridgeCheckField.BooleanQuestionAnswer> = MutableList(14) { BridgeCheckField.BooleanQuestionAnswer.NONE },
    val periodicImagePaths: MutableList<List<String>> = MutableList(14) { emptyList() },

    val rehabilitationAnswer: MutableList<BridgeCheckField.BooleanQuestionAnswer> = MutableList(3) { BridgeCheckField.BooleanQuestionAnswer.NONE },
    val rehabilitationImagePaths: MutableList<List<String>> = MutableList(3) { emptyList() },

    val replacementAnswer: MutableList<BridgeCheckField.BooleanQuestionAnswer> = MutableList(2) { BridgeCheckField.BooleanQuestionAnswer.NONE },
    val replacementImagePaths: MutableList<List<String>> = MutableList(2) { emptyList() },

    val wideningAnswer: BridgeCheckField.BooleanQuestionAnswer = BridgeCheckField.BooleanQuestionAnswer.NONE,
    val wideningImagePaths: List<String> = emptyList(),
)

data class SocialPageAnswer(
    val uncleanlinessAnswer: BridgeCheckField.BooleanQuestionAnswer = BridgeCheckField.BooleanQuestionAnswer.NONE,
    val uncleanlinessImagePaths: List<String> = emptyList(),

    val incompatibilityAnswer: BridgeCheckField.BooleanQuestionAnswer = BridgeCheckField.BooleanQuestionAnswer.NONE,
    val incompatibilityImagePaths: List<String> = emptyList(),

    val activityAnswer: BridgeCheckField.BooleanQuestionAnswer = BridgeCheckField.BooleanQuestionAnswer.NONE,
    val activityImagePaths: List<String> = emptyList(),
)

data class EmergencyPageAnswer(
    val actAnswer: BridgeCheckField.BooleanQuestionAnswer = BridgeCheckField.BooleanQuestionAnswer.NONE,

    val elementCode: String = "",
    val elementDesc: String = "",
    val elementApb: String = "",
    val elementX: String = "",
    val elementY: String = "",
    val elementZ: String = "",
    val elementReason: String = "",

    val conditionInventoryAnswer: BridgeCheckField.BooleanQuestionAnswer = BridgeCheckField.BooleanQuestionAnswer.NONE,
    val conditionDetailAnswer: BridgeCheckField.BooleanQuestionAnswer = BridgeCheckField.BooleanQuestionAnswer.NONE
)