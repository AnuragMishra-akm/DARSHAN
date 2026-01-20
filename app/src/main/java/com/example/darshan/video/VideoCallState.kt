package com.example.darshan.video

import io.getstream.video.android.core.Call

data class VideoCallState(
    val call : Call,
    val callState : CallState?=null,
    val errorMessage : String? = null
)
enum class CallState{
    JOINING,
    ACTIVE,
    ENDED
}