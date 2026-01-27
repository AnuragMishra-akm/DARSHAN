package com.example.darshan.connect

sealed interface ConnectAction {
    data class OnNameChange(val name: String) : ConnectAction
    data class OnRoomIDChange(val roomID: String) : ConnectAction
    data object OnConnectClick : ConnectAction
}