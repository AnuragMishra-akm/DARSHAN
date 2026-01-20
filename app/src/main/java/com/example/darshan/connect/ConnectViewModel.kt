package com.example.darshan.connect

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.darshan.DarshanVideoCallingApp
import kotlinx.coroutines.flow.MutableStateFlow

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

        }
    }
    private fun connectToRoom(){
        state = state.copy(errorMessage = null)
        if(state.name?.isBlank() == true){
            state = state.copy(errorMessage = "Name cannot be empty")
            return
        }
        // initialize video client and connect to room
        (app as DarshanVideoCallingApp).initVideoClient(state.name!!)
        state = state.copy(isConnected = true)
    }

}