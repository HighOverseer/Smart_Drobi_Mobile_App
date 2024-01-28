package com.smartdrobi.aplikasipkm.domain.mapper

import com.smartdrobi.aplikasipkm.data.entity.BridgeCheckEntity
import com.smartdrobi.aplikasipkm.data.entity.ConveniencePageAnswerEntity
import com.smartdrobi.aplikasipkm.data.entity.EmergencyPageAnswerEntity
import com.smartdrobi.aplikasipkm.data.entity.FirstPageAnswerEntity
import com.smartdrobi.aplikasipkm.data.entity.MaintenancePageAnswerEntity
import com.smartdrobi.aplikasipkm.data.entity.SafetyPageAnswerEntity
import com.smartdrobi.aplikasipkm.data.entity.SecurityPageAnswerEntity
import com.smartdrobi.aplikasipkm.data.entity.SocialPageAnswerEntity
import com.smartdrobi.aplikasipkm.data.relation.BridgeCheckAndAnswers
import com.smartdrobi.aplikasipkm.data.relation.BridgeCheckBridgeAndFirstPage
import com.smartdrobi.aplikasipkm.domain.helper.DATE_FORMAT_PATTERN
import com.smartdrobi.aplikasipkm.domain.helper.toDate
import com.smartdrobi.aplikasipkm.domain.helper.toString
import com.smartdrobi.aplikasipkm.domain.model.BridgeCheck
import com.smartdrobi.aplikasipkm.domain.model.BridgeCheckPreview
import com.smartdrobi.aplikasipkm.domain.model.ConveniencePageAnswer
import com.smartdrobi.aplikasipkm.domain.model.EmergencyPageAnswer
import com.smartdrobi.aplikasipkm.domain.model.FirstPageAnswer
import com.smartdrobi.aplikasipkm.domain.model.MaintenancePageAnswer
import com.smartdrobi.aplikasipkm.domain.model.SafetyPageAnswer
import com.smartdrobi.aplikasipkm.domain.model.SecurityPageAnswer
import com.smartdrobi.aplikasipkm.domain.model.SocialPageAnswer

object BridgeCheckMapper {

    fun toBridgeCheckPreview(
        bridgeCheckBridgeAndFirstPage: BridgeCheckBridgeAndFirstPage
    ): BridgeCheckPreview {
        return bridgeCheckBridgeAndFirstPage.run {
            val dateString = firstPageAnswerEntity.inspectionDate.toString(DATE_FORMAT_PATTERN)
            BridgeCheckPreview(
                bridgeCheckEntity.id.toInt(),
                bridgeEntity.imagePath,
                bridgeEntity.name,
                dateString
            )
        }
    }

    fun toBridgeCheckEntity(
        bridgeId: Int,
        firstPageAnswerId: Long,
        safetyPageAnswerId: Long,
        securityPageAnswerId: Long,
        maintenancePageAnswerId: Long,
        conveniencePageAnswerId: Long,
        socialPageAnswerId: Long,
        emergencyPageAnswerId: Long
    ): BridgeCheckEntity {
        return BridgeCheckEntity(
            bridgeId = bridgeId,
            firstPageAnswerId = firstPageAnswerId,
            safetyPageAnswerId = safetyPageAnswerId,
            securityPageAnswerId = securityPageAnswerId,
            maintenancePageAnswerId = maintenancePageAnswerId,
            conveniencePageAnswerId = conveniencePageAnswerId,
            socialPageAnswerId = socialPageAnswerId,
            emergencyPageAnswerId = emergencyPageAnswerId
        )
    }

    fun toBridgeCheck(
        bridgeCheckAndAnswers: BridgeCheckAndAnswers
    ): BridgeCheck {
        return bridgeCheckAndAnswers.run {
            BridgeCheck(
                id = bridgeCheckEntity.id.toInt(),
                bridgeId = bridgeCheckEntity.bridgeId,
                firstPageAnswer = toFirstPageAnswer(firstPageAnswerEntity),
                securityPageAnswer = toSecurityPageAnswer(securityPageAnswerEntity),
                safetyPageAnswer = toSafetyPageAnswer(safetyPageAnswerEntity),
                conveniencePageAnswer = toConveniencePageAnswer(conveniencePageAnswerEntity),
                maintenancePageAnswer = toMaintenancePageAnswer(maintenancePageAnswerEntity),
                socialPageAnswer = toSocialPageAnswer(socialPageAnswerEntity),
                emergencyPageAnswer = toEmergencyPageAnswer(emergencyPageAnswerEntity),
                inspectionDate = firstPageAnswerEntity.inspectionDate
            )
        }
    }

    private fun toFirstPageAnswer(firstPageAnswerEntity: FirstPageAnswerEntity): FirstPageAnswer {

        return firstPageAnswerEntity.run {
            val fixTrafficValue = if (trafficValue.isBlank()) null else trafficValue.toDouble()
            val fixLhr = if (lhr.isBlank()) null else lhr.toDouble()
            val fixYear = if (year.isBlank()) null else year.toInt()

            FirstPageAnswer(
                inspectorName,
                inspectionDate.toString(DATE_FORMAT_PATTERN),
                fixTrafficValue,
                fixLhr,
                fixYear,
                note
            )
        }
    }

    fun toFirstPageAnswerEntity(
        firstPageAnswerId: Long = 0,
        firstPageAnswer: FirstPageAnswer
    ): FirstPageAnswerEntity {
        return firstPageAnswer.run {
            val fixTrafficValue = trafficValue?.toString() ?: ""
            val fixLhr = lhr?.toString() ?: ""
            val fixYear = year?.toString() ?: ""
            FirstPageAnswerEntity(
                id = firstPageAnswerId,
                inspectorName = inspectorName,
                inspectionDate = inspectionDate.toDate(DATE_FORMAT_PATTERN),
                trafficValue = fixTrafficValue,
                lhr = fixLhr,
                year = fixYear
            )
        }
    }


    private fun toSecurityPageAnswer(
        securityPageAnswerEntity: SecurityPageAnswerEntity
    ): SecurityPageAnswer {
        return securityPageAnswerEntity.run {
            SecurityPageAnswer(
                landfillAnswer,
                landFillImagePaths,
                riverFlowAnswer.toMutableList(),
                riverFlowImagePaths.toMutableList(),
                foundationAnswer.toMutableList(),
                foundationImagePaths.toMutableList(),
                lowerBuildingAnswer.toMutableList(),
                lowerBuildingImagePaths.toMutableList(),
                upperBuildingAnswer.toMutableList(),
                upperBuildingImagePaths.toMutableList(),
                siarMuaiAnswer.toMutableList(),
                siarMuaiImagePaths.toMutableList(),
                placementAnswer.toMutableList(),
                placementImagePaths.toMutableList()
            )
        }
    }

    fun toSecurityPageAnswerEntity(
        securityPageAnswerId: Long = 0,
        securityPageAnswer: SecurityPageAnswer
    ): SecurityPageAnswerEntity {
        return securityPageAnswer.run {
            SecurityPageAnswerEntity(
                id = securityPageAnswerId,
                landfillAnswer,
                landFillImagePaths,
                riverFlowAnswer,
                riverFlowImagePaths,
                foundationAnswer,
                foundationImagePaths,
                lowerBuildingAnswer,
                lowerBuildingImagePaths,
                upperBuildingAnswer,
                upperBuildingImagePaths,
                siarMuaiAnswer,
                siarMuaiImagePaths,
                placementAnswer,
                placementImagePaths
            )
        }
    }


    private fun toSafetyPageAnswer(
        safetyPageAnswerEntity: SafetyPageAnswerEntity
    ): SafetyPageAnswer {
        return safetyPageAnswerEntity.run {
            SafetyPageAnswer(
                backRestAnswer.toMutableList(),
                backRestImagePaths.toMutableList(),
                signAnswer.toMutableList(),
                signImagePaths.toMutableList(),
                lightningRodAnswer.toMutableList(),
                lightningRodImagePaths.toMutableList(),
                smksAnswer.toMutableList(),
                smksImagePaths.toMutableList()
            )
        }
    }


    fun toSafetyPageAnswerEntity(
        safetyPageAnswerId: Long = 0,
        safetyPageAnswer: SafetyPageAnswer
    ): SafetyPageAnswerEntity {
        return safetyPageAnswer.run {
            SafetyPageAnswerEntity(
                id = safetyPageAnswerId,
                backRestAnswer,
                backRestImagePaths,
                signAnswer,
                signImagePaths,
                lightningRodAnswer,
                lightningRodImagePaths,
                smksAnswer,
                smksImagePaths
            )
        }
    }


    private fun toConveniencePageAnswer(
        conveniencePageAnswerEntity: ConveniencePageAnswerEntity
    ): ConveniencePageAnswer {
        return conveniencePageAnswerEntity.run {
            ConveniencePageAnswer(
                floorSystemAnswer,
                floorSystemImagePaths,
                upperBuildingAnswer,
                upperBuildingImagePaths,
                shortRoadAnswer,
                shortRoadImagePaths
            )
        }
    }


    fun toConveniencePageAnswerEntity(
        conveniencePageAnswerId: Long = 0,
        conveniencePageAnswer: ConveniencePageAnswer
    ): ConveniencePageAnswerEntity {
        return conveniencePageAnswer.run {
            ConveniencePageAnswerEntity(
                conveniencePageAnswerId,
                floorSystemAnswer,
                floorSystemImagePaths,
                upperBuildingAnswer,
                upperBuildingImagePaths,
                shortRoadAnswer,
                shortRoadImagePaths
            )
        }
    }


    private fun toMaintenancePageAnswer(
        maintenancePageAnswerEntity: MaintenancePageAnswerEntity
    ): MaintenancePageAnswer {
        return maintenancePageAnswerEntity.run {
            MaintenancePageAnswer(
                routineAnswer.toMutableList(),
                routineImagePaths.toMutableList(),
                periodicAnswer.toMutableList(),
                periodicImagePaths.toMutableList(),
                rehabilitationAnswer.toMutableList(),
                rehabilitationImagePaths.toMutableList(),
                replacementAnswer.toMutableList(),
                replacementImagePaths.toMutableList(),
                wideningAnswer,
                wideningImagePaths
            )
        }
    }


    fun toMaintenancePageAnswerEntity(
        maintenancePageAnswerId: Long = 0,
        maintenancePageAnswer: MaintenancePageAnswer
    ): MaintenancePageAnswerEntity {
        return maintenancePageAnswer.run {
            MaintenancePageAnswerEntity(
                maintenancePageAnswerId,
                routineAnswer,
                routineImagePaths,
                periodicAnswer,
                periodicImagePaths,
                rehabilitationAnswer,
                rehabilitationImagePaths,
                replacementAnswer,
                replacementImagePaths,
                wideningAnswer,
                wideningImagePaths
            )
        }
    }


    private fun toSocialPageAnswer(
        socialPageAnswerEntity: SocialPageAnswerEntity
    ): SocialPageAnswer {
        return socialPageAnswerEntity.run {
            SocialPageAnswer(
                uncleanlinessAnswer,
                uncleanlinessImagePaths,
                incompatibilityAnswer,
                incompatibilityImagePaths,
                activityAnswer
            )
        }
    }


    fun toSocialPageAnswerEntity(
        socialPageAnswerId: Long = 0,
        socialPageAnswer: SocialPageAnswer
    ): SocialPageAnswerEntity {
        return socialPageAnswer.run {
            SocialPageAnswerEntity(
                socialPageAnswerId,
                uncleanlinessAnswer,
                uncleanlinessImagePaths,
                incompatibilityAnswer,
                incompatibilityImagePaths,
                activityAnswer,
                activityImagePaths
            )
        }
    }

    private fun toEmergencyPageAnswer(
        emergencyPageAnswerEntity: EmergencyPageAnswerEntity
    ): EmergencyPageAnswer {
        return emergencyPageAnswerEntity.run {
            EmergencyPageAnswer(
                actAnswer,
                elementCode,
                elementDesc,
                elementApb,
                elementX,
                elementY,
                elementZ,
                elementReason,
                conditionInventoryAnswer,
                conditionDetailAnswer
            )
        }
    }

    fun toEmergencyPageAnswerEntity(
        emergencyPageAnswerId: Long = 0,
        emergencyPageAnswer: EmergencyPageAnswer
    ): EmergencyPageAnswerEntity {
        return emergencyPageAnswer.run {
            EmergencyPageAnswerEntity(
                emergencyPageAnswerId,
                actAnswer,
                elementCode,
                elementDesc,
                elementApb,
                elementX,
                elementY,
                elementZ,
                elementReason,
                conditionInventoryAnswer,
                conditionDetailAnswer
            )
        }
    }

}