package br.com.dcarv.medicalerta

import android.app.Application
import android.graphics.Color
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.emoji.text.EmojiCompat
import br.com.dcarv.medicalerta.common.di.ApplicationComponent
import br.com.dcarv.medicalerta.common.di.DaggerApplicationComponent
import br.com.dcarv.medicalerta.common.di.DaggerComponentProvider
import android.R
import android.util.Log
import androidx.core.provider.FontRequest
import androidx.emoji.text.FontRequestEmojiCompatConfig

private const val TAG = "MedicalertaApplication"

class MedicalertaApplication: Application(), DaggerComponentProvider {

    override val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder()
            .applicationContext(this)
            .build()
    }

    override fun onCreate() {
        super.onCreate()

        initEmojiCompat()
    }

    private fun initEmojiCompat() {
        val config = BundledEmojiCompatConfig(this)
            .setReplaceAll(true)
            .registerInitCallback(object : EmojiCompat.InitCallback() {
                override fun onInitialized() {
                    super.onInitialized()
                    Log.d(TAG, "Initialized EmojiCompat")
                }

                override fun onFailed(throwable: Throwable?) {
                    super.onFailed(throwable)
                    Log.e(TAG, "Failed to initialize EmojiCompat", throwable)
                }
            })

        EmojiCompat.init(config)
    }
}