package com.dev.olutoba.stytchandroidappsdktest

import android.app.Application
import com.stytch.sdk.consumer.StytchClient

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        StytchClient.configure(
            context = this,
            publicToken = BuildConfig.STYTCH_PUBLIC_TOKEN
        )
    }
}
