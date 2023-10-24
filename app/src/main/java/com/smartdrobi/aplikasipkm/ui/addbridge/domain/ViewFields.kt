package com.smartdrobi.aplikasipkm.ui.addbridge.domain

import com.smartdrobi.aplikasipkm.R
import com.smartdrobi.aplikasipkm.domain.StaticString
import com.smartdrobi.aplikasipkm.domain.model.BridgeCheck
import com.smartdrobi.aplikasipkm.domain.model.BridgeCheckField
import com.smartdrobi.aplikasipkm.ui.addbridge.fragment.form.AddBridgeCheckFragment
import com.smartdrobi.aplikasipkm.ui.addbridge.fragment.form.BridgeConvenienceFormFragment
import com.smartdrobi.aplikasipkm.ui.addbridge.fragment.form.BridgeEmergencyFormFragment
import com.smartdrobi.aplikasipkm.ui.addbridge.fragment.form.BridgeMaintenanceFormFragment
import com.smartdrobi.aplikasipkm.ui.addbridge.fragment.form.BridgeSafetyFormFragment
import com.smartdrobi.aplikasipkm.ui.addbridge.fragment.form.BridgeSecurityFormFragment
import com.smartdrobi.aplikasipkm.ui.addbridge.fragment.form.BridgeSocialFormFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun getFirstFormFields(
    bridgeCheckObj: BridgeCheck
) = withContext(Dispatchers.Default) {
    bridgeCheckObj.firstPageAnswer.run {
        mutableListOf(
            BridgeCheckField.Header(
                StaticString(
                    R.string.form_pemeriksaan_jembatan
                )
            ),
            BridgeCheckField.RegularText(
                AddBridgeCheckFragment.ViewId.INSPECTOR_NAME.id,
                StaticString(
                    R.string.name_pemeriksa
                ),
                8,
                6,
                30,
                lastEtText = inspectorName

            ){bridgeCheck, field ->
                bridgeCheck.apply {
                    firstPageAnswer = firstPageAnswer.copy(
                        inspectorName = field.getValue()
                    )
                }
            },
            BridgeCheckField.DateText(
                AddBridgeCheckFragment.ViewId.CHECK_DATE.id,
                StaticString(
                    R.string.tanggal_pemeriksaan
                ),
                29f.toInt(),
                2,
                lastEtText = inspectionDate
            ){bridgeCheck, field  ->
                bridgeCheck.apply {
                    firstPageAnswer = firstPageAnswer.copy(
                        inspectionDate = field.getValue()
                    )
                }
             },
            BridgeCheckField.RegularText(
                AddBridgeCheckFragment.ViewId.TRAFFIC_VAL.id,
                StaticString(
                    R.string.nilai_lalu_lintas
                ),
                15f.toInt(),
                10,
                4,
                BridgeCheckField.EditTextInputType.NUMBER_DECIMAL,
                lastEtText = trafficValue?.toString() ?: ""
            ){bridgeCheck , field ->
                bridgeCheck.apply {
                    val doubleVal = try {
                        field.getValue<String>().toDouble()
                    }catch (e:Exception){
                        e.printStackTrace()
                        0.0
                    }

                    firstPageAnswer = firstPageAnswer.copy(
                        trafficValue = doubleVal
                    )
                }
             },
            BridgeCheckField.RegularText(
                AddBridgeCheckFragment.ViewId.LHR.id,
                StaticString(
                    R.string.lhr
                ),
                56f.toInt(),
                10,
                4,
                BridgeCheckField.EditTextInputType.NUMBER_DECIMAL,
                lastEtText = lhr?.toString() ?: ""
            ){bridgeCheck , field ->
                bridgeCheck.apply {
                    val doubleVal = try {
                        field.getValue<String>().toDouble()
                    }catch (e:Exception){
                        e.printStackTrace()
                        0.0
                    }

                    firstPageAnswer = firstPageAnswer.copy(
                        lhr = doubleVal
                    )
                }
             },
            BridgeCheckField.RegularText(
                AddBridgeCheckFragment.ViewId.Year.id,
                StaticString(
                    R.string.year
                ),
                73f.toInt(),
                10,
                4,
                BridgeCheckField.EditTextInputType.NUMBER,
                lastEtText = year?.toString() ?: ""
            ){bridgeCheck, field  ->
                bridgeCheck.apply {
                    val intVal = try {
                        field.getValue<String>().toInt()
                    }catch (e:Exception){
                        e.printStackTrace()
                        0
                    }

                    firstPageAnswer = firstPageAnswer.copy(
                        year = intVal
                    )
                }
             },
            BridgeCheckField.MultilineText(
                AddBridgeCheckFragment.ViewId.Note.id,
                StaticString(
                    R.string.catatan
                ),
                4f.toInt(),
                15f.toInt(),
                lastEtText = note
            ){bridgeCheck, field  ->
                bridgeCheck.apply {
                    firstPageAnswer = firstPageAnswer.copy(
                        note = field.getValue()
                    )
                }
            }
        )
    }
}

suspend fun getConvenienceFormFields(
    bridgeCheckObj: BridgeCheck
) = withContext(Dispatchers.Default) {
    bridgeCheckObj.conveniencePageAnswer.run {
        mutableListOf(
            BridgeCheckField.Header(
                StaticString(
                    R.string.kenyamanan_jembatan
                )
            ),
            BridgeCheckField.ContainerBooleans(
                BridgeConvenienceFormFragment.ViewId.FLOOR_SYSTEM.id,
                StaticString(
                    R.string._1_sistem_lantai
                ),
                booleanQuestions = mutableListOf(
                    BridgeCheckField.BooleanQuestion(
                        BridgeConvenienceFormFragment.QuestionId.FLOOR_SYSTEM_A.id,
                        StaticString(
                            R.string.a_kerataan_permukaan_pada_lapis_permukaan
                        ),
                        answer = floorSystemAnswer,
                        listImagePath = floorSystemImagePaths.toMutableList(),
                        isImageCollectionsVisible = floorSystemImagePaths.isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            conveniencePageAnswer = conveniencePageAnswer.copy(
                                floorSystemAnswer = content.answer,
                                floorSystemImagePaths = content.listImagePaths
                            )
                        }
                    }
                )
            ),
            BridgeCheckField.ContainerBooleans(
                BridgeConvenienceFormFragment.ViewId.UPPER_BUILDING.id,
                StaticString(
                    R.string._2_bangunan_atas
                ),
                booleanQuestions = mutableListOf(
                    BridgeCheckField.BooleanQuestion(
                        BridgeConvenienceFormFragment.QuestionId.UPPER_BUILDING_A.id,
                        StaticString(
                            R.string.a_getaran_yang_mengganggu_kenyamanan_kendaraan_dan_pejalan_kaki
                        ),
                        answer = upperBuildingAnswer,
                        listImagePath = upperBuildingImagePaths.toMutableList(),
                        isImageCollectionsVisible = upperBuildingImagePaths.isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            conveniencePageAnswer = conveniencePageAnswer.copy(
                                upperBuildingAnswer = content.answer,
                                upperBuildingImagePaths = content.listImagePaths
                            )
                        }
                    }
                )
            ),
            BridgeCheckField.ContainerBooleans(
                BridgeConvenienceFormFragment.ViewId.SHORT_ROAD.id,
                StaticString(
                    R.string._3_jalan_pendekat
                ),
                booleanQuestions = mutableListOf(
                    BridgeCheckField.BooleanQuestion(
                        BridgeConvenienceFormFragment.QuestionId.SHORT_ROAD_A.id,
                        StaticString(
                            R.string.a_tidak_berfungsinya_drainase_jalan_pendekat
                        ),
                        answer = shortRoadAnswer,
                        listImagePath = shortRoadImagePaths.toMutableList(),
                        isImageCollectionsVisible = shortRoadImagePaths.isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            conveniencePageAnswer = conveniencePageAnswer.copy(
                                shortRoadAnswer = content.answer,
                                shortRoadImagePaths = content.listImagePaths
                            )
                        }
                    }
                )
            )
        )
    }
}

suspend fun getMaintenanceFormFields(
    bridgeCheckObj: BridgeCheck
) = withContext(Dispatchers.Default) {
    bridgeCheckObj.maintenancePageAnswer.run {
        mutableListOf(
            BridgeCheckField.Header(
                StaticString(
                    R.string.pemeliharaan_rehabilitasi_penggantian_dan_pembangunan
                )
            ),
            BridgeCheckField.ContainerBooleans(
                BridgeMaintenanceFormFragment.ViewId.ROUTINE.id,
                StaticString(
                    R.string._1_pemeliharaan_rutin
                ),
                booleanQuestions = mutableListOf(
                    BridgeCheckField.BooleanQuestion(
                        BridgeMaintenanceFormFragment.QuestionId.ROUTINE_A.id,
                        StaticString(
                            R.string.a_pembuatan_jalan_akses
                        ),
                        answer = routineAnswer[0],
                        listImagePath = routineImagePaths[0].toMutableList(),
                        isImageCollectionsVisible = routineImagePaths[0].isNotEmpty(),
                    ){ bridgeCheck, field ->
                        bridgeCheck.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            bridgeCheck.maintenancePageAnswer.apply {
                                routineAnswer[0] = content.answer
                                routineImagePaths[0] = content.listImagePaths
                            }
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeMaintenanceFormFragment.QuestionId.ROUTINE_B.id,
                        StaticString(
                            R.string.b_pembersihan_secara_umum
                        ),
                        answer = routineAnswer[1],
                        listImagePath = routineImagePaths[1].toMutableList(),
                        isImageCollectionsVisible = routineImagePaths[1].isNotEmpty(),
                    ){ bridgeCheck, field ->
                        bridgeCheck.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            bridgeCheck.maintenancePageAnswer.apply {
                                routineAnswer[1] = content.answer
                                routineImagePaths[1] = content.listImagePaths
                            }
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeMaintenanceFormFragment.QuestionId.ROUTINE_C.id,
                        StaticString(
                            R.string.c_pengecatan_sederhana
                        ),
                        answer = routineAnswer[2],
                        listImagePath = routineImagePaths[2].toMutableList(),
                        isImageCollectionsVisible = routineImagePaths[2].isNotEmpty(),
                    ){ bridgeCheck, field ->
                        bridgeCheck.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            bridgeCheck.maintenancePageAnswer.apply {
                                routineAnswer[2] = content.answer
                                routineImagePaths[2] = content.listImagePaths
                            }
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeMaintenanceFormFragment.QuestionId.ROUTINE_D.id,
                        StaticString(
                            R.string.d_penanganan_kerusakan_ringan
                        ),
                        answer = routineAnswer[3],
                        listImagePath = routineImagePaths[3].toMutableList(),
                        isImageCollectionsVisible = routineImagePaths[3].isNotEmpty(),
                    ){ bridgeCheck, field ->
                        bridgeCheck.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            bridgeCheck.maintenancePageAnswer.apply {
                                routineAnswer[3] = content.answer
                                routineImagePaths[3] = content.listImagePaths
                            }
                        }
                    }
                )
            ),
            BridgeCheckField.ContainerBooleans(
                BridgeMaintenanceFormFragment.ViewId.PERIODIC.id,
                StaticString(
                    R.string._2_pemeliharaan_berkala
                ),
                booleanQuestions = mutableListOf(
                    BridgeCheckField.BooleanQuestion(
                        BridgeMaintenanceFormFragment.QuestionId.PERIODIC_A.id,
                        StaticString(
                            R.string.a_pengecatan_ulang
                        ),
                        answer = periodicAnswer[0],
                        listImagePath = periodicImagePaths[0].toMutableList(),
                        isImageCollectionsVisible = periodicImagePaths[0].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.maintenancePageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            periodicAnswer[0] = content.answer
                            periodicImagePaths[0] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeMaintenanceFormFragment.QuestionId.PERIODIC_B.id,
                        StaticString(
                            R.string.b_penggantian_lapisan_permukaan
                        ),
                        answer = periodicAnswer[1],
                        listImagePath = periodicImagePaths[1].toMutableList(),
                        isImageCollectionsVisible = periodicImagePaths[1].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.maintenancePageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            periodicAnswer[1] = content.answer
                            periodicImagePaths[1] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeMaintenanceFormFragment.QuestionId.PERIODIC_C.id,
                        StaticString(
                            R.string.c_penggantian_lantai_kayu
                        ),
                        answer = periodicAnswer[2],
                        listImagePath = periodicImagePaths[2].toMutableList(),
                        isImageCollectionsVisible = periodicImagePaths[2].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.maintenancePageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            periodicAnswer[2] = content.answer
                            periodicImagePaths[2] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeMaintenanceFormFragment.QuestionId.PERIODIC_D.id,
                        StaticString(
                            R.string.d_penggantian_kayu_jalur_roda_kendaraan
                        ),
                        answer = periodicAnswer[3],
                        listImagePath = periodicImagePaths[3].toMutableList(),
                        isImageCollectionsVisible = periodicImagePaths[3].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.maintenancePageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            periodicAnswer[3] = content.answer
                            periodicImagePaths[3] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeMaintenanceFormFragment.QuestionId.PERIODIC_E.id,
                        StaticString(
                            R.string.e_pembersihan_keseluruhan_jembatan
                        ),
                        answer = periodicAnswer[4],
                        listImagePath = periodicImagePaths[4].toMutableList(),
                        isImageCollectionsVisible = periodicImagePaths[4].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.maintenancePageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            periodicAnswer[4] = content.answer
                            periodicImagePaths[4] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeMaintenanceFormFragment.QuestionId.PERIODIC_F.id,
                        StaticString(
                            R.string.f_pemeliharaan_peletakan_landasan
                        ),
                        answer = periodicAnswer[5],
                        listImagePath = periodicImagePaths[5].toMutableList(),
                        isImageCollectionsVisible = periodicImagePaths[5].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.maintenancePageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            periodicAnswer[5] = content.answer
                            periodicImagePaths[5] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeMaintenanceFormFragment.QuestionId.PERIODIC_G.id,
                        StaticString(
                            R.string.g_penggantian_sambungan_siar_muai
                        ),
                        answer = periodicAnswer[6],
                        listImagePath = periodicImagePaths[6].toMutableList(),
                        isImageCollectionsVisible = periodicImagePaths[6].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.maintenancePageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            periodicAnswer[6] = content.answer
                            periodicImagePaths[6] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeMaintenanceFormFragment.QuestionId.PERIODIC_H.id,
                        StaticString(
                            R.string.h_perbaikan_keretakan_pasangan_batu_bata
                        ),
                        answer = periodicAnswer[7],
                        listImagePath = periodicImagePaths[7].toMutableList(),
                        isImageCollectionsVisible = periodicImagePaths[7].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.maintenancePageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            periodicAnswer[7] = content.answer
                            periodicImagePaths[7] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeMaintenanceFormFragment.QuestionId.PERIODIC_I.id,
                        StaticString(
                            R.string.i_penggatian_elemen_elemen_kecil
                        ),
                        answer = periodicAnswer[8],
                        listImagePath = periodicImagePaths[8].toMutableList(),
                        isImageCollectionsVisible = periodicImagePaths[8].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.maintenancePageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            periodicAnswer[8] = content.answer
                            periodicImagePaths[8] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeMaintenanceFormFragment.QuestionId.PERIODIC_J.id,
                        StaticString(
                            R.string.j_perbaikan_tiang_dan_sandaran
                        ),
                        answer = periodicAnswer[9],
                        listImagePath = periodicImagePaths[9].toMutableList(),
                        isImageCollectionsVisible = periodicImagePaths[9].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.maintenancePageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            periodicAnswer[9] = content.answer
                            periodicImagePaths[9] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeMaintenanceFormFragment.QuestionId.PERIODIC_K.id,
                        StaticString(
                            R.string.k_perawatan_bagian_bagian_yang_bergerak
                        ),
                        answer = periodicAnswer[10],
                        listImagePath = periodicImagePaths[10].toMutableList(),
                        isImageCollectionsVisible = periodicImagePaths[10].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.maintenancePageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            periodicAnswer[10] = content.answer
                            periodicImagePaths[10] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeMaintenanceFormFragment.QuestionId.PERIODIC_L.id,
                        StaticString(
                            R.string.l_perkuatan_skala_elemen_struktural_jembatan
                        ),
                        answer = periodicAnswer[11],
                        listImagePath = periodicImagePaths[11].toMutableList(),
                        isImageCollectionsVisible = periodicImagePaths[11].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.maintenancePageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            periodicAnswer[11] = content.answer
                            periodicImagePaths[11] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeMaintenanceFormFragment.QuestionId.PERIODIC_M.id,
                        StaticString(
                            R.string.m_perbaikan_longsor_dan_erosi_tebing
                        ),
                        answer = periodicAnswer[12],
                        listImagePath = periodicImagePaths[12].toMutableList(),
                        isImageCollectionsVisible = periodicImagePaths[12].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.maintenancePageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            periodicAnswer[12] = content.answer
                            periodicImagePaths[12] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeMaintenanceFormFragment.QuestionId.PERIODIC_N.id,
                        StaticString(
                            R.string.n_perbaikan_sederhana_bangunan_pengaman
                        ),
                        answer = periodicAnswer[13],
                        listImagePath = periodicImagePaths[13].toMutableList(),
                        isImageCollectionsVisible = periodicImagePaths[13].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.maintenancePageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            periodicAnswer[13] = content.answer
                            periodicImagePaths[13] = content.listImagePaths
                        }
                    }
                )
            ),
            BridgeCheckField.ContainerBooleans(
                BridgeMaintenanceFormFragment.ViewId.REHABILITATION.id,
                StaticString(
                    R.string._3_rehabilitasi
                ),
                booleanQuestions = mutableListOf(
                    BridgeCheckField.BooleanQuestion(
                        BridgeMaintenanceFormFragment.QuestionId.REHABILITATION_A.id,
                        StaticString(
                            R.string.a_penggantian_skala_komponen_jembatan
                        ),
                        answer = rehabilitationAnswer[0],
                        listImagePath = rehabilitationImagePaths[0].toMutableList(),
                        isImageCollectionsVisible = rehabilitationImagePaths[0].isNotEmpty()
                    ){ bridgeCheck, field ->  
                        bridgeCheck.maintenancePageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            rehabilitationAnswer[0] = content.answer
                            rehabilitationImagePaths[0] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeMaintenanceFormFragment.QuestionId.REHABILITATION_B.id,
                        StaticString(
                            R.string.b_modifikasi_bangunan_atas_bawah_fondasi
                        ),
                        answer = rehabilitationAnswer[1],
                        listImagePath = rehabilitationImagePaths[1].toMutableList(),
                        isImageCollectionsVisible = rehabilitationImagePaths[1].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.maintenancePageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            rehabilitationAnswer[1] = content.answer
                            rehabilitationImagePaths[1] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeMaintenanceFormFragment.QuestionId.REHABILITATION_C.id,
                        StaticString(
                            R.string.c_perubahan_sistem_siar_muai_dan_perletakan
                        ),
                        answer = rehabilitationAnswer[2],
                        listImagePath = rehabilitationImagePaths[2].toMutableList(),
                        isImageCollectionsVisible = rehabilitationImagePaths[2].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.maintenancePageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            rehabilitationAnswer[2] = content.answer
                            rehabilitationImagePaths[2] = content.listImagePaths
                        }
                    }
                )
            ),
            BridgeCheckField.ContainerBooleans(
                BridgeMaintenanceFormFragment.ViewId.REPLACEMENT.id,
                StaticString(
                    R.string._4_penggantian
                ),
                booleanQuestions = mutableListOf(
                    BridgeCheckField.BooleanQuestion(
                        BridgeMaintenanceFormFragment.QuestionId.REPLACEMENT_A.id,
                        StaticString(
                            R.string.a_bangunan_atas
                        ),
                        answer = replacementAnswer[0],
                        listImagePath = replacementImagePaths[0].toMutableList(),
                        isImageCollectionsVisible = replacementImagePaths[0].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.maintenancePageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            replacementAnswer[0] = content.answer
                            replacementImagePaths[0] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeMaintenanceFormFragment.QuestionId.REPLACEMENT_B.id,
                        StaticString(
                            R.string.b_bangunan_bawah
                        ),
                        answer = replacementAnswer[1],
                        listImagePath = replacementImagePaths[1].toMutableList(),
                        isImageCollectionsVisible = replacementImagePaths[1].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.maintenancePageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            replacementAnswer[1] = content.answer
                            replacementImagePaths[1] = content.listImagePaths
                        }
                    }
                )
            ),
            BridgeCheckField.BooleanQuestion(
                BridgeMaintenanceFormFragment.ViewId.WIDENING.id,
                StaticString(
                    R.string._5_pelebaran_duplikasi_jembatan
                ),
                answer = wideningAnswer,
                listImagePath = wideningImagePaths.toMutableList(),
                isImageCollectionsVisible = wideningImagePaths.isNotEmpty()
            ){ bridgeCheck, field ->
                bridgeCheck.apply {
                    val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                    maintenancePageAnswer = maintenancePageAnswer.copy(
                        wideningAnswer = content.answer,
                        wideningImagePaths = content.listImagePaths
                    )
                }
            }
        )

    }
}

suspend fun getSafetyFormFields(
    bridgeCheckObj: BridgeCheck
) = withContext(Dispatchers.Default) {
    bridgeCheckObj.safetyPageAnswer.run {
        mutableListOf(
            BridgeCheckField.Header(
                StaticString(
                    R.string.keselamatan_jembatan
                )
            ),
            BridgeCheckField.ContainerBooleans(
                BridgeSafetyFormFragment.ViewId.BACKREST.id,
                StaticString(
                    R.string._1_sandaran
                ),
                booleanQuestions = mutableListOf(
                    BridgeCheckField.BooleanQuestion(
                        BridgeSafetyFormFragment.QuestionId.BACKREST_A.id,
                        StaticString(
                            R.string.a_ketidaklengkapan_elemen
                        ),
                        backRestAnswer[0],
                        backRestImagePaths[0].toMutableList(),
                        isImageCollectionsVisible = backRestImagePaths[0].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.safetyPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            backRestAnswer[0] = content.answer
                            backRestImagePaths[0] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeSafetyFormFragment.QuestionId.BACKREST_B.id,
                        StaticString(
                            R.string.b_longgar_hilang_sistem_sambungan
                        ),
                        backRestAnswer[1],
                        backRestImagePaths[1].toMutableList(),
                        isImageCollectionsVisible = backRestImagePaths[1].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.safetyPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            backRestAnswer[1] = content.answer
                            backRestImagePaths[1] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeSafetyFormFragment.QuestionId.BACKREST_C.id,
                        StaticString(
                            R.string.c_kerusakan_berupa_pelepasan_bahan_gompal_delaminasi_karat_atau_busuk
                        ),
                        backRestAnswer[2],
                        backRestImagePaths[2].toMutableList(),
                        isImageCollectionsVisible = backRestImagePaths[2].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.safetyPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            backRestAnswer[2] = content.answer
                            backRestImagePaths[2] = content.listImagePaths
                        }
                    }
                )
            ),
            BridgeCheckField.ContainerBooleans(
                BridgeSafetyFormFragment.ViewId.SIGN.id,
                StaticString(
                    R.string.rambu_dan_tanda
                ),
                booleanQuestions = mutableListOf(
                    BridgeCheckField.BooleanQuestion(
                        BridgeSafetyFormFragment.QuestionId.SIGN_A.id,
                        StaticString(
                            R.string.a_ketidaklengkapan_elemen
                        ),
                        signAnswer[0],
                        signImagePaths[0].toMutableList(),
                        isImageCollectionsVisible = signImagePaths[0].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.safetyPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            signAnswer[0] = content.answer
                            signImagePaths[0] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeSafetyFormFragment.QuestionId.SIGN_B.id,
                        StaticString(
                            R.string.b_longgar_hilang_sistem_sambungan
                        ),
                        signAnswer[1],
                        signImagePaths[1].toMutableList(),
                        isImageCollectionsVisible = signImagePaths[1].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.safetyPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            signAnswer[1] = content.answer
                            signImagePaths[1] = content.listImagePaths
                        }
                    }
                )
            ),
            BridgeCheckField.ContainerBooleans(
                BridgeSafetyFormFragment.ViewId.LIGHTNING_ROD.id,
                StaticString(
                    R.string._3_penangkal_petir
                ),
                booleanQuestions = mutableListOf(
                    BridgeCheckField.BooleanQuestion(
                        BridgeSafetyFormFragment.QuestionId.LIGHTNING_ROD_A.id,
                        StaticString(
                            R.string.a_ketidaklengkapan_elemen
                        ),
                        lightningRodAnswer[0],
                        lightningRodImagePaths[0].toMutableList(),
                        isImageCollectionsVisible = lightningRodImagePaths[0].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.safetyPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            lightningRodAnswer[0] = content.answer
                            lightningRodImagePaths[0] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeSafetyFormFragment.QuestionId.LIGHTNING_ROD_B.id,
                        StaticString(
                            R.string.b_tidak_berfungsinya_elemen
                        ),
                        lightningRodAnswer[1],
                        lightningRodImagePaths[1].toMutableList(),
                        isImageCollectionsVisible = lightningRodImagePaths[1].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.safetyPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            lightningRodAnswer[1] = content.answer
                            lightningRodImagePaths[1] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeSafetyFormFragment.QuestionId.LIGHTNING_ROD_C.id,
                        StaticString(
                            R.string.c_longgar_hilang_sistem_sambungan
                        ),
                        lightningRodAnswer[2],
                        lightningRodImagePaths[2].toMutableList(),
                        isImageCollectionsVisible = lightningRodImagePaths[2].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.safetyPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            lightningRodAnswer[2] = content.answer
                            lightningRodImagePaths[2] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeSafetyFormFragment.QuestionId.LIGHTNING_ROD_D.id,
                        StaticString(
                            R.string.d_kerusakan_berupa_pelepasan_bahan_gompal_delaminasi_karat_atau_busuk
                        ),
                        lightningRodAnswer[3],
                        lightningRodImagePaths[3].toMutableList(),
                        isImageCollectionsVisible = lightningRodImagePaths[3].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.safetyPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            lightningRodAnswer[3] = content.answer
                            lightningRodImagePaths[3] = content.listImagePaths
                        }
                    }
                )
            ),
            BridgeCheckField.ContainerBooleans(
                BridgeSafetyFormFragment.ViewId.SMKS.id,
                StaticString(
                    R.string._4_smks
                ),
                booleanQuestions = mutableListOf(
                    BridgeCheckField.BooleanQuestion(
                        BridgeSafetyFormFragment.QuestionId.SMKS_A.id,
                        StaticString(
                            R.string.a_ketidaklengkapan_elemen
                        ),
                        smksAnswer[0],
                        smksImagePaths[0].toMutableList(),
                        isImageCollectionsVisible = smksImagePaths[0].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.safetyPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            smksAnswer[0] = content.answer
                            smksImagePaths[0] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeSafetyFormFragment.QuestionId.SMKS_B.id,
                        StaticString(
                            R.string.b_tidak_berfungsinya_elemen
                        ),
                        smksAnswer[1],
                        smksImagePaths[1].toMutableList(),
                        isImageCollectionsVisible = smksImagePaths[1].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.safetyPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            smksAnswer[1] = content.answer
                            smksImagePaths[1] = content.listImagePaths
                        }
                    }
                )
            ),
        )
    }
}

suspend fun getSecurityFormFields(
    bridgeCheckObj: BridgeCheck
) = withContext(Dispatchers.Default) {
    bridgeCheckObj.securityPageAnswer.run {
        mutableListOf(
            BridgeCheckField.Header(
                StaticString(
                    R.string.keamanan_jembatan
                )
            ),
            BridgeCheckField.ContainerBooleans(
                BridgeSecurityFormFragment.ViewId.LANDFILL.id,
                StaticString(
                    R.string._1_tanah_timbunan
                ),
                booleanQuestions = mutableListOf(
                    BridgeCheckField.BooleanQuestion(
                        BridgeSecurityFormFragment.QuestionId.LANDFILL_A.id,
                        StaticString(
                            R.string.keruntuhan_longsor_atau_amblas
                        ),
                        landfillAnswer,
                        landFillImagePaths.toMutableList(),
                        isImageCollectionsVisible = landFillImagePaths.isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            securityPageAnswer = securityPageAnswer.copy(
                                landfillAnswer = content.answer,
                                landFillImagePaths = content.listImagePaths
                            )
                        }
                    }
                )
            ),
            BridgeCheckField.ContainerBooleans(
                BridgeSecurityFormFragment.ViewId.RIVER_FLOW.id,
                StaticString(
                    R.string.aliran_sungai
                ),
                booleanQuestions = mutableListOf(
                    BridgeCheckField.BooleanQuestion(
                        BridgeSecurityFormFragment.QuestionId.RIVER_FLOW_A.id,
                        StaticString(
                            R.string.a_gerusan_degradasi_dasar_sungai
                        ),
                        riverFlowAnswer[0],
                        riverFlowImagePaths[0].toMutableList(),
                        isImageCollectionsVisible = riverFlowImagePaths[0].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.securityPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            riverFlowAnswer[0] = content.answer
                            riverFlowImagePaths[0] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeSecurityFormFragment.QuestionId.RIVER_FLOW_B.id,
                        StaticString(
                            R.string.b_endapan_agradasi
                        ),
                        riverFlowAnswer[1],
                        riverFlowImagePaths[1].toMutableList(),
                        isImageCollectionsVisible = riverFlowImagePaths[1].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.securityPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            riverFlowAnswer[1] = content.answer
                            riverFlowImagePaths[1] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeSecurityFormFragment.QuestionId.RIVER_FLOW_C.id,
                        StaticString(
                            R.string.c_benda_hanyutan_debris_di_aliran_sungai
                        ),
                        riverFlowAnswer[2],
                        riverFlowImagePaths[2].toMutableList(),
                        isImageCollectionsVisible = riverFlowImagePaths[2].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.securityPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            riverFlowAnswer[2] = content.answer
                            riverFlowImagePaths[2] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeSecurityFormFragment.QuestionId.RIVER_FLOW_D.id,
                        StaticString(
                            R.string.d_sisa_struktur_jembatan_lama
                        ),
                        riverFlowAnswer[3],
                        riverFlowImagePaths[3].toMutableList(),
                        isImageCollectionsVisible = riverFlowImagePaths[3].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.securityPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            riverFlowAnswer[3] = content.answer
                            riverFlowImagePaths[3] = content.listImagePaths
                        }
                    }
                )
            ),
            BridgeCheckField.ContainerBooleans(
                BridgeSecurityFormFragment.ViewId.FOUNDATION.id,
                StaticString(
                    R.string._3_fondasi
                ),
                booleanQuestions = mutableListOf(
                    BridgeCheckField.BooleanQuestion(
                        BridgeSecurityFormFragment.QuestionId.FOUNDATION_A.id,
                        StaticString(
                            R.string.a_penurunan_atau_deformasi
                        ),
                        foundationAnswer[0],
                        foundationImagePaths[0].toMutableList(),
                        isImageCollectionsVisible = foundationImagePaths[0].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.securityPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            foundationAnswer[0] = content.answer
                            foundationImagePaths[0] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeSecurityFormFragment.QuestionId.FOUNDATION_B.id,
                        StaticString(
                            R.string.b_retak_pada_bagian_fondasi
                        ),
                        foundationAnswer[1],
                        foundationImagePaths[1].toMutableList(),
                        isImageCollectionsVisible = foundationImagePaths[1].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.securityPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            foundationAnswer[1] = content.answer
                            foundationImagePaths[1] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeSecurityFormFragment.QuestionId.FOUNDATION_C.id,
                        StaticString(
                            R.string.c_kerusakan_berupa_lepasnya_bahan_fondasi_gompal_delaminasi_karat_atau_busuk
                        ),
                        foundationAnswer[2],
                        foundationImagePaths[2].toMutableList(),
                        isImageCollectionsVisible = foundationImagePaths[2].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.securityPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            foundationAnswer[2] = content.answer
                            foundationImagePaths[2] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeSecurityFormFragment.QuestionId.FOUNDATION_D.id,
                        StaticString(
                            R.string.d_lepas_rusaknya_sistem_perlindungan_cat_galvanis_proteksi_katodik
                        ),
                        foundationAnswer[3],
                        foundationImagePaths[3].toMutableList(),
                        isImageCollectionsVisible = foundationImagePaths[3].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.securityPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            foundationAnswer[3] = content.answer
                            foundationImagePaths[3] = content.listImagePaths
                        }
                    }
                )
            ),
            BridgeCheckField.ContainerBooleans(
                BridgeSecurityFormFragment.ViewId.LOWER_BUILDING.id,
                StaticString(
                    R.string.bangunan_bawah
                ),
                booleanQuestions = mutableListOf(
                    BridgeCheckField.BooleanQuestion(
                        BridgeSecurityFormFragment.QuestionId.LOWER_BUILDING_A.id,
                        StaticString(
                            R.string.a_pergerakan_atau_amblasnya_kepala_jembatan_pilar
                        ),
                        lowerBuildingAnswer[0],
                        lowerBuildingImagePaths[0].toMutableList(),
                        isImageCollectionsVisible = lowerBuildingImagePaths[0].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.securityPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            lowerBuildingAnswer[0] = content.answer
                            lowerBuildingImagePaths[0] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeSecurityFormFragment.QuestionId.LOWER_BUILDING_B.id,
                        StaticString(
                            R.string.b_keretakan_bagian_tembok_sayap_kepala_jembatan_dan_pilar
                        ),
                        lowerBuildingAnswer[1],
                        lowerBuildingImagePaths[1].toMutableList(),
                        isImageCollectionsVisible = lowerBuildingImagePaths[1].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.securityPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            lowerBuildingAnswer[1] = content.answer
                            lowerBuildingImagePaths[1] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeSecurityFormFragment.QuestionId.LOWER_BUILDING_C.id,
                        StaticString(
                            R.string.c_kerusakan_berupa_pelepasan_bahan_gompal_delaminasi_karat_atau_busuk
                        ),
                        lowerBuildingAnswer[2],
                        lowerBuildingImagePaths[2].toMutableList(),
                        isImageCollectionsVisible = lowerBuildingImagePaths[2].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.securityPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            lowerBuildingAnswer[2] = content.answer
                            lowerBuildingImagePaths[2] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeSecurityFormFragment.QuestionId.LOWER_BUILDING_D.id,
                        StaticString(
                            R.string.d_rembesan_air
                        ),
                        lowerBuildingAnswer[3],
                        lowerBuildingImagePaths[3].toMutableList(),
                        isImageCollectionsVisible = lowerBuildingImagePaths[3].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.securityPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            lowerBuildingAnswer[3] = content.answer
                            lowerBuildingImagePaths[3] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeSecurityFormFragment.QuestionId.LOWER_BUILDING_E.id,
                        StaticString(
                            R.string.e_lepas_rusaknya_sistem_perlindungan_cat_galvanis_proteksi_katodik
                        ),
                        lowerBuildingAnswer[4],
                        lowerBuildingImagePaths[4].toMutableList(),
                        isImageCollectionsVisible = lowerBuildingImagePaths[4].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.securityPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            lowerBuildingAnswer[4] = content.answer
                            lowerBuildingImagePaths[4] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeSecurityFormFragment.QuestionId.LOWER_BUILDING_F.id,
                        StaticString(
                            R.string.f_tidak_berfungsinya_sistem_perkuatan
                        ),
                        lowerBuildingAnswer[5],
                        lowerBuildingImagePaths[5].toMutableList(),
                        isImageCollectionsVisible = lowerBuildingImagePaths[5].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.securityPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            lowerBuildingAnswer[5] = content.answer
                            lowerBuildingImagePaths[5] = content.listImagePaths
                        }
                    }
                ),
            ),
            BridgeCheckField.ContainerBooleans(
                BridgeSecurityFormFragment.ViewId.UPPER_BUILDING.id,
                StaticString(
                    R.string.bangunan_atas
                ),
                booleanQuestions = mutableListOf(
                    BridgeCheckField.BooleanQuestion(
                        BridgeSecurityFormFragment.QuestionId.UPPER_BUILDING_A.id,
                        StaticString(
                            R.string.a_lendutan_berlebihan_sewaktu_lalu_lintas_lewat_di_atas_jembatan
                        ),
                        upperBuildingAnswer[0],
                        upperBuildingImagePaths[0].toMutableList(),
                        isImageCollectionsVisible = upperBuildingImagePaths[0].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.securityPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            upperBuildingAnswer[0] = content.answer
                            upperBuildingImagePaths[0] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeSecurityFormFragment.QuestionId.UPPER_BUILDING_B.id,
                        StaticString(
                            R.string.b_keretakan_bahan_bangunan_atas
                        ),
                        upperBuildingAnswer[1],
                        upperBuildingImagePaths[1].toMutableList(),
                        isImageCollectionsVisible = upperBuildingImagePaths[1].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.securityPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            upperBuildingAnswer[1] = content.answer
                            upperBuildingImagePaths[1] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeSecurityFormFragment.QuestionId.UPPER_BUILDING_C.id,
                        StaticString(
                            R.string.c_kerusakan_berupa_pelepasan_bahan
                        ),
                        upperBuildingAnswer[2],
                        upperBuildingImagePaths[2].toMutableList(),
                        isImageCollectionsVisible = upperBuildingImagePaths[2].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.securityPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            upperBuildingAnswer[2] = content.answer
                            upperBuildingImagePaths[2] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeSecurityFormFragment.QuestionId.UPPER_BUILDING_D.id,
                        StaticString(
                            R.string.d_lepas_longgar_sistem_sambungan
                        ),
                        upperBuildingAnswer[3],
                        upperBuildingImagePaths[3].toMutableList(),
                        isImageCollectionsVisible = upperBuildingImagePaths[3].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.securityPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            upperBuildingAnswer[3] = content.answer
                            upperBuildingImagePaths[3] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeSecurityFormFragment.QuestionId.UPPER_BUILDING_E.id,
                        StaticString(
                            R.string.e_rusaknya_elemen_penahan_struktur_kabel
                        ),
                        upperBuildingAnswer[4],
                        upperBuildingImagePaths[4].toMutableList(),
                        isImageCollectionsVisible = upperBuildingImagePaths[4].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.securityPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            upperBuildingAnswer[4] = content.answer
                            upperBuildingImagePaths[4] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeSecurityFormFragment.QuestionId.UPPER_BUILDING_F.id,
                        StaticString(
                            R.string.f_tidak_berfungsinya_sistem_perkuatan
                        ),
                        upperBuildingAnswer[5],
                        upperBuildingImagePaths[5].toMutableList(),
                        isImageCollectionsVisible = upperBuildingImagePaths[5].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.securityPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            upperBuildingAnswer[5] = content.answer
                            upperBuildingImagePaths[5] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeSecurityFormFragment.QuestionId.UPPER_BUILDING_G.id,
                        StaticString(
                            R.string.g_pergeseran_bangunan_atas_yang_ekstrim
                        ),
                        upperBuildingAnswer[6],
                        upperBuildingImagePaths[6].toMutableList(),
                        isImageCollectionsVisible = upperBuildingImagePaths[6].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.securityPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            upperBuildingAnswer[6] = content.answer
                            upperBuildingImagePaths[6] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeSecurityFormFragment.QuestionId.UPPER_BUILDING_H.id,
                        StaticString(
                            R.string.h_lepasnya_ikatan_penahan_gempa
                        ),
                        upperBuildingAnswer[7],
                        upperBuildingImagePaths[7].toMutableList(),
                        isImageCollectionsVisible = upperBuildingImagePaths[7].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.securityPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            upperBuildingAnswer[7] = content.answer
                            upperBuildingImagePaths[7] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeSecurityFormFragment.QuestionId.UPPER_BUILDING_I.id,
                        StaticString(
                            R.string.i_sampah_pada_elemen_rangka_baja
                        ),
                        upperBuildingAnswer[8],
                        upperBuildingImagePaths[8].toMutableList(),
                        isImageCollectionsVisible = upperBuildingImagePaths[8].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.securityPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            upperBuildingAnswer[8] = content.answer
                            upperBuildingImagePaths[8] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeSecurityFormFragment.QuestionId.UPPER_BUILDING_J.id,
                        StaticString(
                            R.string.j_rembesan_air_bangunan_atas
                        ),
                        upperBuildingAnswer[9],
                        upperBuildingImagePaths[9].toMutableList(),
                        isImageCollectionsVisible = upperBuildingImagePaths[9].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.securityPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            upperBuildingAnswer[9] = content.answer
                            upperBuildingImagePaths[9] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeSecurityFormFragment.QuestionId.UPPER_BUILDING_K.id,
                        StaticString(
                            R.string.k_rembesan_rembesan_air_bagian_bawah_lantai
                        ),
                        upperBuildingAnswer[10],
                        upperBuildingImagePaths[10].toMutableList(),
                        isImageCollectionsVisible = upperBuildingImagePaths[10].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.securityPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            upperBuildingAnswer[10] = content.answer
                            upperBuildingImagePaths[10] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeSecurityFormFragment.QuestionId.UPPER_BUILDING_L.id,
                        StaticString(
                            R.string.l_tidak_berfungsinya_elemen_elemen_drainase
                        ),
                        upperBuildingAnswer[11],
                        upperBuildingImagePaths[11].toMutableList(),
                        isImageCollectionsVisible = upperBuildingImagePaths[11].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.securityPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            upperBuildingAnswer[11] = content.answer
                            upperBuildingImagePaths[11] = content.listImagePaths
                        }
                    }
                ),
            ),
            BridgeCheckField.ContainerBooleans(
                BridgeSecurityFormFragment.ViewId.SIAR_MUAI.id,
                StaticString(
                    R.string.siar_muai
                ),
                booleanQuestions = mutableListOf(
                    BridgeCheckField.BooleanQuestion(
                        BridgeSecurityFormFragment.QuestionId.SIAR_MUAI_A.id,
                        StaticString(
                            R.string.a_beda_tinggi_antara_elevansi_jalan_pendekatandengan_elevansi_lantai_jembatan
                        ),
                        siarMuaiAnswer[0],
                        siarMuaiImagePaths[0].toMutableList(),
                        isImageCollectionsVisible = siarMuaiImagePaths[0].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.securityPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            siarMuaiAnswer[0] = content.answer
                            siarMuaiImagePaths[0] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeSecurityFormFragment.QuestionId.SIAR_MUAI_B.id,
                        StaticString(
                            R.string.b_hilang_elemen_sambungan_siar_muai
                        ),
                        siarMuaiAnswer[1],
                        siarMuaiImagePaths[1].toMutableList(),
                        isImageCollectionsVisible = siarMuaiImagePaths[1].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.securityPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            siarMuaiAnswer[1] = content.answer
                            siarMuaiImagePaths[1] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeSecurityFormFragment.QuestionId.SIAR_MUAI_C.id,
                        StaticString(
                            R.string.c_tidak_berfungsinya_sambungan_siar_muai
                        ),
                        siarMuaiAnswer[2],
                        siarMuaiImagePaths[2].toMutableList(),
                        isImageCollectionsVisible = siarMuaiImagePaths[2].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.securityPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            siarMuaiAnswer[2] = content.answer
                            siarMuaiImagePaths[2] = content.listImagePaths
                        }
                    },
                )
            ),
            BridgeCheckField.ContainerBooleans(
                BridgeSecurityFormFragment.ViewId.PLACEMENT.id,
                StaticString(
                    R.string._7_perletakan
                ),
                booleanQuestions = mutableListOf(
                    BridgeCheckField.BooleanQuestion(
                        BridgeSecurityFormFragment.QuestionId.PLACEMENT_A.id,
                        StaticString(
                            R.string.a_tidak_lengkapnya_elemen_perletakan
                        ),
                        placementAnswer[0],
                        placementImagePaths[0].toMutableList(),
                        isImageCollectionsVisible = placementImagePaths[0].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.securityPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            placementAnswer[0] = content.answer
                            placementImagePaths[0] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeSecurityFormFragment.QuestionId.PLACEMENT_B.id,
                        StaticString(
                            R.string.b_pergerakan_pergeseran_landasan_ekstrim
                        ),
                        placementAnswer[1],
                        placementImagePaths[1].toMutableList(),
                        isImageCollectionsVisible = placementImagePaths[1].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.securityPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            placementAnswer[1] = content.answer
                            placementImagePaths[1] = content.listImagePaths
                        }
                    },
                    BridgeCheckField.BooleanQuestion(
                        BridgeSecurityFormFragment.QuestionId.PLACEMENT_C.id,
                        StaticString(
                            R.string.c_keutuhan_sistem_pendukung_landasan_i
                        ),
                        placementAnswer[2],
                        placementImagePaths[2].toMutableList(),
                        isImageCollectionsVisible = placementImagePaths[2].isNotEmpty()
                    ){ bridgeCheck, field ->
                        bridgeCheck.securityPageAnswer.apply {
                            val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                            placementAnswer[2] = content.answer
                            placementImagePaths[2] = content.listImagePaths
                        }
                    },
                )
            ),

            )
    }

}

suspend fun getSocialFormFields(
    bridgeCheckObj: BridgeCheck
) = withContext(Dispatchers.Default) {
    bridgeCheckObj.socialPageAnswer.run {
        mutableListOf(
            BridgeCheckField.Header(
                StaticString(
                    R.string.kondisi_sosial_kemasyarakatan
                )
            ),
            BridgeCheckField.BooleanQuestion(
                BridgeSocialFormFragment.ViewId.UNCLEANLINESS.id,
                StaticString(
                    R.string._1_ketidakbersihan_sekitar_jembatan_sampah_dan_limbah
                ),
                uncleanlinessAnswer,
                uncleanlinessImagePaths.toMutableList(),
                isImageCollectionsVisible = uncleanlinessImagePaths.isNotEmpty()
            ){ bridgeCheck, field ->
                bridgeCheck.apply {
                    val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                    socialPageAnswer = socialPageAnswer.copy(
                        uncleanlinessAnswer = content.answer,
                        uncleanlinessImagePaths = content.listImagePaths
                    )
                }
            },
            BridgeCheckField.BooleanQuestion(
                BridgeSocialFormFragment.ViewId.INCOMPATIBILITY.id,
                StaticString(
                    R.string._2_ketidaksesuaian_peruntukan_jembatan
                ),
                incompatibilityAnswer,
                incompatibilityImagePaths.toMutableList(),
                isImageCollectionsVisible = incompatibilityImagePaths.isNotEmpty()
            ){ bridgeCheck, field ->
                bridgeCheck.apply {
                    val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                    socialPageAnswer = socialPageAnswer.copy(
                        incompatibilityAnswer = content.answer,
                        incompatibilityImagePaths = content.listImagePaths
                    )
                }
            },
            BridgeCheckField.BooleanQuestion(
                BridgeSocialFormFragment.ViewId.ACTIVITY.id,
                StaticString(
                    R.string._3_aktivitas_yang_mengganggu_pelayanan
                ),
                activityAnswer,
                activityImagePaths.toMutableList(),
                isImageCollectionsVisible = activityImagePaths.isNotEmpty()
            ){ bridgeCheck, field ->
                bridgeCheck.apply {
                    val content = field.getValue<BridgeCheckField.BooleanQuestionContent>()
                    socialPageAnswer = socialPageAnswer.copy(
                        activityAnswer = content.answer,
                        activityImagePaths = content.listImagePaths
                    )
                }
            },
        )
    }
}

suspend fun getEmergencyFormFields(
    bridgeCheckObj: BridgeCheck
) = withContext(Dispatchers.Default) {
    bridgeCheckObj.emergencyPageAnswer.run {
        mutableListOf(
            BridgeCheckField.Header(
                StaticString(
                    R.string.tindakan_darurat
                )
            ),
            BridgeCheckField.BooleanQuestionWithoutImages(
                BridgeEmergencyFormFragment.ViewId.ACT.id,
                StaticString(
                    R.string.apakah_tindakan_darurat_disarankan
                ),
                actAnswer
                ){ bridgeCheck, field ->
                 bridgeCheck.apply {
                     emergencyPageAnswer = emergencyPageAnswer.copy(
                         actAnswer = field.getValue()
                     )
                 }
            },
            BridgeCheckField.MultifieldText(
                BridgeEmergencyFormFragment.ViewId.ELEMENT.id,
                code = elementCode,
                desc = elementDesc,
                apb = elementApb,
                x = elementX,
                y = elementY,
                z = elementZ,
                reason = elementReason
            ){ bridgeCheck, field ->
                bridgeCheck.apply {
                    val content = field.getValue<BridgeCheckField.MultifieldContent>()
                    emergencyPageAnswer = emergencyPageAnswer.copy(
                        elementCode = content.code,
                        elementApb = content.apb,
                        elementDesc = content.desc,
                        elementReason = content.reason,
                        elementX = content.x,
                        elementY = content.y,
                        elementZ = content.z
                    )
                }
            },
            BridgeCheckField.BooleanQuestionWithoutImages(
                BridgeEmergencyFormFragment.ViewId.CONDITION_INVENTORY.id,
                StaticString(
                    R.string.apakah_kondisi_jembatan_sesuai_dengan
                ),
                conditionInventoryAnswer
            ){ bridgeCheck, field ->
                bridgeCheck.apply {
                    emergencyPageAnswer = emergencyPageAnswer.copy(
                        conditionInventoryAnswer = field.getValue()
                    )
                }
            },
            BridgeCheckField.BooleanQuestionWithoutImages(
                BridgeEmergencyFormFragment.ViewId.CONDITION_DETAIL.id,
                StaticString(
                    R.string.apakah_kondisi_jembatan_sesuai_dengan_pemeriksaan_detail_terakhir
                ),
                conditionDetailAnswer
            ){ bridgeCheck, field ->
                bridgeCheck.apply {
                    emergencyPageAnswer = emergencyPageAnswer.copy(
                        conditionDetailAnswer = field.getValue()
                    )
                }
            },
        )
    }
}
