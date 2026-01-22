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
    private var currentName: String? = null
    var client: StreamVideo? = null

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@DarshanVideoCallingApp)
            modules(appModule)
        }
    }

    fun initVideoClient(userName: String) {
        // Sanitize userId: only letters, digits, @, _, and - are allowed in Stream IDs
        val userId = userName.filter { it.isLetterOrDigit() || it == '@' || it == '_' || it == '-' }
            .ifEmpty { "user_${System.currentTimeMillis()}" }

        if (client == null || currentName != userName) {
            StreamVideo.removeClient()
            currentName = userName
            
            client = StreamVideoBuilder(
                context = this,
                apiKey = "qk5n36xpvdsz",
                user = User(
                    id = userId,
                    name = userName,
                    type = UserType.Authenticated
                ),
                token = StreamVideo.devToken(userId)
            ).build()
        }
    }
}