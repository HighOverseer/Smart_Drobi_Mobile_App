package com.smartdrobi.aplikasipkm.domain.model

import com.smartdrobi.aplikasipkm.domain.helper.getCurrentDate
import java.util.Calendar
import java.util.Date

/*private var currentIncrementId = 100
    get(){
        field += 1
        return field
    }*/
data class BridgeCheck(
    val id:Int = 0,
    val bridgeId:Int,
    var firstPageAnswer: FirstPageAnswer = FirstPageAnswer(),
    var securityPageAnswer: SecurityPageAnswer = SecurityPageAnswer(),
    var safetyPageAnswer: SafetyPageAnswer = SafetyPageAnswer(),
    var conveniencePageAnswer: ConveniencePageAnswer = ConveniencePageAnswer(),
    var maintenancePageAnswer: MaintenancePageAnswer = MaintenancePageAnswer(),
    var socialPageAnswer: SocialPageAnswer = SocialPageAnswer(),
    var emergencyPageAnswer: EmergencyPageAnswer = EmergencyPageAnswer(),
    val inspectionDate: Date = getCurrentDate()
){
    fun clone():BridgeCheck{

        return this.copy(
            firstPageAnswer = firstPageAnswer.copy(),
            securityPageAnswer = securityPageAnswer.copy(
                riverFlowAnswer = securityPageAnswer.riverFlowAnswer.toMutableList(),
                riverFlowImagePaths = securityPageAnswer.riverFlowImagePaths.toMutableList(),
                foundationAnswer = securityPageAnswer.foundationAnswer.toMutableList(),
                foundationImagePaths = securityPageAnswer.foundationImagePaths.toMutableList(),
                lowerBuildingAnswer = securityPageAnswer.lowerBuildingAnswer.toMutableList(),
                lowerBuildingImagePaths = securityPageAnswer.lowerBuildingImagePaths.toMutableList(),
                upperBuildingAnswer = securityPageAnswer.upperBuildingAnswer.toMutableList(),
                upperBuildingImagePaths = securityPageAnswer.upperBuildingImagePaths.toMutableList(),
                siarMuaiAnswer = securityPageAnswer.siarMuaiAnswer.toMutableList(),
                siarMuaiImagePaths = securityPageAnswer.siarMuaiImagePaths.toMutableList(),
                placementAnswer = securityPageAnswer.placementAnswer.toMutableList(),
                placementImagePaths = securityPageAnswer.placementImagePaths.toMutableList()
            ),
            safetyPageAnswer = safetyPageAnswer.copy(
                backRestAnswer = safetyPageAnswer.backRestAnswer.toMutableList(),
                backRestImagePaths = safetyPageAnswer.backRestImagePaths.toMutableList(),
                signAnswer = safetyPageAnswer.signAnswer.toMutableList(),
                signImagePaths = safetyPageAnswer.signImagePaths.toMutableList(),
                lightningRodAnswer = safetyPageAnswer.lightningRodAnswer.toMutableList(),
                lightningRodImagePaths = safetyPageAnswer.lightningRodImagePaths.toMutableList(),
                smksAnswer = safetyPageAnswer.smksAnswer.toMutableList(),
                smksImagePaths = safetyPageAnswer.smksImagePaths.toMutableList()
            ),
            conveniencePageAnswer = conveniencePageAnswer.copy(),
            maintenancePageAnswer = maintenancePageAnswer.copy(
                routineAnswer = maintenancePageAnswer.routineAnswer.toMutableList(),
                routineImagePaths = maintenancePageAnswer.routineImagePaths.toMutableList(),
                periodicAnswer = maintenancePageAnswer.periodicAnswer.toMutableList(),
                periodicImagePaths = maintenancePageAnswer.periodicImagePaths.toMutableList(),
                rehabilitationAnswer = maintenancePageAnswer.rehabilitationAnswer.toMutableList(),
                rehabilitationImagePaths = maintenancePageAnswer.rehabilitationImagePaths.toMutableList(),
                replacementAnswer = maintenancePageAnswer.replacementAnswer.toMutableList(),
                replacementImagePaths = maintenancePageAnswer.replacementImagePaths.toMutableList()
            ),
            socialPageAnswer = socialPageAnswer.copy(),
            emergencyPageAnswer = emergencyPageAnswer.copy()
        )
    }
}


data class FirstPageAnswer(
    val inspectorName:String = "",
    val inspectionDate:String = "",
    val trafficValue:Double? = null,
    val lhr:Double? = null,
    val year:Int? = null,
    val note:String = "",
)

data class SecurityPageAnswer(
    val landfillAnswer:BridgeCheckField.BooleanQuestionAnswer = BridgeCheckField.BooleanQuestionAnswer.NONE,
    val landFillImagePaths:List<String> = emptyList(),

    val riverFlowAnswer:MutableList<BridgeCheckField.BooleanQuestionAnswer> = MutableList(4){ BridgeCheckField.BooleanQuestionAnswer.NONE },
    val riverFlowImagePaths:MutableList<List<String>> = MutableList(4){ emptyList() },

    val foundationAnswer:MutableList<BridgeCheckField.BooleanQuestionAnswer> = MutableList(4){ BridgeCheckField.BooleanQuestionAnswer.NONE },
    val foundationImagePaths:MutableList<List<String>> = MutableList(4){ emptyList() },

    val lowerBuildingAnswer:MutableList<BridgeCheckField.BooleanQuestionAnswer> = MutableList(6){ BridgeCheckField.BooleanQuestionAnswer.NONE },
    val lowerBuildingImagePaths:MutableList<List<String>> = MutableList(6){ emptyList() },

    val upperBuildingAnswer:MutableList<BridgeCheckField.BooleanQuestionAnswer> = MutableList(12){ BridgeCheckField.BooleanQuestionAnswer.NONE },
    val upperBuildingImagePaths:MutableList<List<String>> = MutableList(12){ emptyList() },

    val siarMuaiAnswer:MutableList<BridgeCheckField.BooleanQuestionAnswer> = MutableList(3){ BridgeCheckField.BooleanQuestionAnswer.NONE },
    val siarMuaiImagePaths:MutableList<List<String>> = MutableList(3){ emptyList() },

    val placementAnswer:MutableList<BridgeCheckField.BooleanQuestionAnswer> = MutableList(3){ BridgeCheckField.BooleanQuestionAnswer.NONE },
    val placementImagePaths:MutableList<List<String>> = MutableList(3){ emptyList() },
)

data class SafetyPageAnswer(
    val backRestAnswer:MutableList<BridgeCheckField.BooleanQuestionAnswer> = MutableList(3){ BridgeCheckField.BooleanQuestionAnswer.NONE },
    val backRestImagePaths:MutableList<List<String>> = MutableList(3){ emptyList() },

    val signAnswer:MutableList<BridgeCheckField.BooleanQuestionAnswer> = MutableList(2){ BridgeCheckField.BooleanQuestionAnswer.NONE },
    val signImagePaths:MutableList<List<String>> = MutableList(2){ emptyList() },

    val lightningRodAnswer:MutableList<BridgeCheckField.BooleanQuestionAnswer> = MutableList(4){ BridgeCheckField.BooleanQuestionAnswer.NONE },
    val lightningRodImagePaths:MutableList<List<String>> = MutableList(4){ emptyList() },

    val smksAnswer:MutableList<BridgeCheckField.BooleanQuestionAnswer> = MutableList(2){ BridgeCheckField.BooleanQuestionAnswer.NONE },
    val smksImagePaths:MutableList<List<String>> = MutableList(2){ emptyList() },
)

data class ConveniencePageAnswer(
    val floorSystemAnswer:BridgeCheckField.BooleanQuestionAnswer = BridgeCheckField.BooleanQuestionAnswer.NONE,
    val floorSystemImagePaths:List<String> = emptyList(),

    val upperBuildingAnswer:BridgeCheckField.BooleanQuestionAnswer = BridgeCheckField.BooleanQuestionAnswer.NONE,
    val upperBuildingImagePaths:List<String> = emptyList(),

    val shortRoadAnswer:BridgeCheckField.BooleanQuestionAnswer = BridgeCheckField.BooleanQuestionAnswer.NONE,
    val shortRoadImagePaths:List<String> = emptyList(),
)

data class MaintenancePageAnswer(
    val routineAnswer:MutableList<BridgeCheckField.BooleanQuestionAnswer> = MutableList(4){ BridgeCheckField.BooleanQuestionAnswer.NONE },
    val routineImagePaths:MutableList<List<String>> = MutableList(4){ emptyList() },

    val periodicAnswer:MutableList<BridgeCheckField.BooleanQuestionAnswer> = MutableList(14){ BridgeCheckField.BooleanQuestionAnswer.NONE },
    val periodicImagePaths:MutableList<List<String>> = MutableList(14){ emptyList() },

    val rehabilitationAnswer:MutableList<BridgeCheckField.BooleanQuestionAnswer> = MutableList(3){ BridgeCheckField.BooleanQuestionAnswer.NONE },
    val rehabilitationImagePaths:MutableList<List<String>> = MutableList(3){ emptyList() },

    val replacementAnswer:MutableList<BridgeCheckField.BooleanQuestionAnswer> = MutableList(2){ BridgeCheckField.BooleanQuestionAnswer.NONE },
    val replacementImagePaths:MutableList<List<String>> = MutableList(2){ emptyList() },

    val wideningAnswer:BridgeCheckField.BooleanQuestionAnswer = BridgeCheckField.BooleanQuestionAnswer.NONE,
    val wideningImagePaths:List<String> = emptyList(),
)

data class SocialPageAnswer(
    val uncleanlinessAnswer:BridgeCheckField.BooleanQuestionAnswer = BridgeCheckField.BooleanQuestionAnswer.NONE,
    val uncleanlinessImagePaths:List<String> = emptyList(),

    val incompatibilityAnswer:BridgeCheckField.BooleanQuestionAnswer = BridgeCheckField.BooleanQuestionAnswer.NONE,
    val incompatibilityImagePaths:List<String> = emptyList(),

    val activityAnswer:BridgeCheckField.BooleanQuestionAnswer = BridgeCheckField.BooleanQuestionAnswer.NONE,
    val activityImagePaths:List<String> = emptyList(),
)

data class EmergencyPageAnswer(
    val actAnswer:BridgeCheckField.BooleanQuestionAnswer = BridgeCheckField.BooleanQuestionAnswer.NONE,

    val elementCode:String = "",
    val elementDesc:String = "",
    val elementApb:String = "",
    val elementX:String = "",
    val elementY:String = "",
    val elementZ:String = "",
    val elementReason:String = "",

    val conditionInventoryAnswer: BridgeCheckField.BooleanQuestionAnswer = BridgeCheckField.BooleanQuestionAnswer.NONE,
    val conditionDetailAnswer:BridgeCheckField.BooleanQuestionAnswer = BridgeCheckField.BooleanQuestionAnswer.NONE
)