package com.example.darshan.video

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.call.state.LeaveCall
import kotlinx.coroutines.launch

class VideoCallViewModel(
    private val videoClient: StreamVideo,
    private val roomID: String
): ViewModel() {
    var state by mutableStateOf(VideoCallState(
        call =  videoClient.call("default", roomID)
    ))
    private set

    init {
        viewModelScope.launch {
            state.call.subscribe { event ->
                if (event is LeaveCall) {
                    onAction(VideoCallAction.OnDisConnectClick)
                }
            }
        }
    }

    fun onAction(action: VideoCallAction){
        when(action){
            VideoCallAction.JoinCallClick -> {
                joinCall()
            }
            VideoCallAction.OnDisConnectClick -> {
                state.call.leave()
                videoClient.logOut()
                state = state.copy(callState = com.example.darshan.video.CallState.ENDED)
            }
        }
    }

    private fun joinCall(){
        if(state.callState == com.example.darshan.video.CallState.ACTIVE){
            return
        }
        viewModelScope.launch {
            state = state.copy(callState = com.example.darshan.video.CallState.JOINING)

            state.call.join(create = true)
                .onSuccess {
                    state = state.copy(callState = com.example.darshan.video.CallState.ACTIVE, errorMessage = null)
                }
                .onError{
                    state = state.copy(errorMessage = it.message, callState = null)
                }
        }
    }
}