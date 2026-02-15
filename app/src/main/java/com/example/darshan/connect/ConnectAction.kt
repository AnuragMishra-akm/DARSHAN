package com.example.darshan.connect

sealed interface ConnectAction {
    data class OnNameChange(val name: String) : ConnectAction
    data class OnRoomIDChange(val roomID: String) : ConnectAction
    data class OnModeChange(val isCreateMode: Boolean) : ConnectAction
    data object OnGenerateRoomID : ConnectAction
    data object OnConnectClick : ConnectAction
}