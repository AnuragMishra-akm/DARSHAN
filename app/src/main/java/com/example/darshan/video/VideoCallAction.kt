package com.example.darshan.video

sealed interface VideoCallAction {
    data object OnDisConnectClick: VideoCallAction
    data object JoinCallClick: VideoCallAction
   // data object LeaveCallClick: VideoCallAction
}