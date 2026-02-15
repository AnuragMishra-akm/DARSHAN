package com.example.darshan.connect

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.darshan.DarshanVideoCallingApp
import java.util.UUID

class ConnectViewModel(
  private val app: Application
): AndroidViewModel(app) {
    var state by mutableStateOf(ConnectState())
        private set

    fun onAction(action: ConnectAction){
        when(action){
            ConnectAction.OnConnectClick -> {
                connectToRoom()
            }
            is ConnectAction.OnNameChange -> {
                state = state.copy(name = action.name)
            }
            is ConnectAction.OnRoomIDChange -> {
                state = state.copy(roomID = action.roomID)
            }
            is ConnectAction.OnModeChange -> {
                state = state.copy(
                    isCreateMode = action.isCreateMode,
                    errorMessage = null,
                    roomID = ""
                )
            }
            ConnectAction.OnGenerateRoomID -> {
                state = state.copy(
                    roomID = UUID.randomUUID().toString().substring(0, 8)
                )
            }
        }
    }

    private fun connectToRoom(){
        state = state.copy(errorMessage = null)
        if(state.name?.isBlank() == true){
            state = state.copy(errorMessage = "Name cannot be empty")
            return
        }
        if(state.roomID?.isBlank() == true){
            state = state.copy(errorMessage = "Room ID cannot be empty")
            return
        }
        
        (app as DarshanVideoCallingApp).initVideoClient(state.name!!)
        state = state.copy(isConnected = true)
    }
}