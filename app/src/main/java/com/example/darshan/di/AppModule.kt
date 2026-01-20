package com.example.darshan.di

import com.example.darshan.DarshanVideoCallingApp
import com.example.darshan.connect.ConnectViewModel
import com.example.darshan.video.VideoCallViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    factory {
        val app = androidContext().applicationContext as DarshanVideoCallingApp
        app.client
    }

    viewModelOf(::ConnectViewModel)
    viewModelOf(::VideoCallViewModel)

}