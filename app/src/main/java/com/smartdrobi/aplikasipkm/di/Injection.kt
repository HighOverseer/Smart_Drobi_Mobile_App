package com.smartdrobi.aplikasipkm.di

import android.content.Context
import com.smartdrobi.aplikasipkm.data.Repository
import com.smartdrobi.aplikasipkm.data.BridgeDatabase

object Injection {
    fun provideRepository(context: Context): Repository {
        val db = BridgeDatabase.getDatabase(context.applicationContext)
        return Repository.getInstance(
            db.bridgeCheckDao(),
            db.bridgeDao()
        )
    }
}