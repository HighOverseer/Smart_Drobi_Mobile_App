package com.smartdrobi.aplikasipkm.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.smartdrobi.aplikasipkm.R
import com.smartdrobi.aplikasipkm.ui.home.HomeActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        lifecycleScope.launch {
            delay(2500L)
            val intent = Intent(this@Splash, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}