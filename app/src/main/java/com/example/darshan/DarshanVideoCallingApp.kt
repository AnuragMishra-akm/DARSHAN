package com.example.darshan

import android.app.Application
import com.example.darshan.di.appModule
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.model.User
import io.getstream.video.android.model.UserType
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class DarshanVideoCallingApp: Application() {
    private var currentName: String?=null
    var client : StreamVideo?=null

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@DarshanVideoCallingApp)
            modules(appModule)
        }
    }

    fun initVideoClient(userName: String){
        if(client == null || currentName != userName){
            StreamVideo.removeClient()
            currentName = userName
            client = StreamVideoBuilder(
                context = this,
                apiKey = "qk5n36xpvdsz",
                user = User(
                    id = userName,
                    name = userName,
                    type= UserType.Guest
                ),
                token = StreamVideo.devToken(userName)
            ).build()
        }
    }
}